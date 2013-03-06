package am.ik.uploader.app.files.controller;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.groups.Default;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefaults;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import am.ik.uploader.app.files.logic.FilesLogic;
import am.ik.uploader.app.files.model.FilesForm;
import am.ik.uploader.app.files.model.FilesForm.DeleteGroup;
import am.ik.uploader.app.files.model.FilesForm.DownloadGroup;
import am.ik.uploader.app.files.model.FilesForm.UploadGroup;
import am.ik.uploader.domain.exception.BussinessException;
import am.ik.uploader.domain.exception.EntityNotFoundException;
import am.ik.uploader.domain.model.UploadedFile;
import am.ik.uploader.domain.service.file.UploadedFileService;

@Controller
@RequestMapping("files")
public class FilesController {
    private static final Logger logger = LoggerFactory
            .getLogger(FilesController.class);

    @Inject
    protected UploadedFileService uploadedFileService;

    @Inject
    protected FilesLogic filesLogic;

    @Value("${maxUploadSize}")
    protected int maxUploadSize;

    @ModelAttribute
    public FilesForm setUpFilesForm() {
        return new FilesForm();
    }

    @ModelAttribute("maxUploadSize")
    public int getMaxUloadSize() {
        return maxUploadSize;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String list(@PageableDefaults Pageable pageable, Model model) {
        Page<UploadedFile> files = uploadedFileService.listFiles(pageable);
        model.addAttribute("page", files);
        return "files/index";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String upload(
            @Validated({ Default.class, UploadGroup.class }) FilesForm form,
            BindingResult result, Model model, RedirectAttributes attributes) {
        logger.debug("upload");

        if (result.hasErrors()) {
            logger.warn("input error -> {}", result);
            return list(new PageRequest(0, 10), model);
        }
        UploadedFile file = filesLogic.convertToUploadedFile(form);
        String downloadKey = form.getDownloadKey();
        String deleteKey = form.getDeleteKey();
        uploadedFileService.upload(file, downloadKey, deleteKey);
        attributes
                .addFlashAttribute("successMessage", "Uploaded successfully.");
        return "redirect:/files";
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public String delete(
            @Validated({ Default.class, DeleteGroup.class }) FilesForm form,
            BindingResult result, Model model, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            logger.warn("input error -> {}", result);
            return list(new PageRequest(0, 10), model);
        }

        String id = form.getId();
        logger.debug("delete {}", id);
        String deleteKey = form.getDeleteKey();
        uploadedFileService.delete(id, deleteKey);
        attributes.addFlashAttribute("successMessage", "Deleted successfully.");
        return "redirect:/files";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> download(@Validated({ Default.class,
            DownloadGroup.class }) FilesForm form, BindingResult result,
            Model model, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            logger.warn("input error -> {}", result);
            throw new BussinessException("input error");
        }
        String id = form.getId();
        String downloadKey = form.getDownloadKey();
        logger.debug("download {}", id);
        UploadedFile file = uploadedFileService.download(id, downloadKey);
        byte[] body = file.getContent();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(file.getContentType()));
        headers.setContentDispositionFormData("attachment", file
                .getOriginalFileName());
        ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(body, headers, HttpStatus.OK);
        return response;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, params = "private")
    public String downloadForm(FilesForm form, Model model) {
        return "files/downloadForm";
    }

    @ExceptionHandler({ BussinessException.class })
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public String handleBussinessException(Exception e,
            HttpServletRequest request) {
        logger.error("execption occurred.", e);
        RequestContextUtils.getOutputFlashMap(request).put("errorMessage",
                e.getMessage());
        return "redirect:/files";
    }

    @ExceptionHandler({ EntityNotFoundException.class })
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public String handleNotFoundException(Exception e,
            HttpServletRequest request) {
        RequestContextUtils.getOutputFlashMap(request).put("errorMessage",
                e.getMessage());
        return "redirect:/files";
    }
}
