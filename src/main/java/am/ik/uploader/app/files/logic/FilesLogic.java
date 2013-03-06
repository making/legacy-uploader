package am.ik.uploader.app.files.logic;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import am.ik.uploader.app.files.model.FilesForm;
import am.ik.uploader.domain.exception.SystemException;
import am.ik.uploader.domain.model.UploadedFile;

@Component
public class FilesLogic {
    private static final Logger logger = LoggerFactory
            .getLogger(FilesLogic.class);

    public UploadedFile convertToUploadedFile(FilesForm form) {
        UploadedFile file = new UploadedFile();
        file.setDescription(form.getDescription());
        MultipartFile multipartFile = form.getFile();
        logger.debug("prepare to persist file ({})", multipartFile);
        file.setContentType(multipartFile.getContentType());
        file.setOriginalFileName(multipartFile.getOriginalFilename());
        file.setFileSize(multipartFile.getSize());
        try {
            byte[] content = IOUtils
                    .toByteArray(multipartFile.getInputStream());
            file.setContent(content);
        } catch (IOException e) {
            throw new SystemException("upload failed.", e);
        }
        return file;
    }
}
