package am.ik.uploader.domain.service.file;

import java.util.Date;
import java.util.UUID;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import am.ik.uploader.domain.exception.BussinessException;
import am.ik.uploader.domain.exception.EntityNotFoundException;
import am.ik.uploader.domain.model.UploadedFile;
import am.ik.uploader.domain.repository.file.UploadedFileRepository;

@Service
public class UploadedFileServiceImpl implements UploadedFileService {
    @Inject
    protected UploadedFileRepository uploadedFileRepository;

    @Inject
    protected PasswordEncoder passwordEncoder;

    @Value("${maxUploadSize}")
    protected int maxUploadSize;

    @Override
    public Page<UploadedFile> listFiles(Pageable pageable) {
        return uploadedFileRepository.findAllOrderByUploadedDate(pageable);
    }

    private UploadedFile getFile(String id) {
        UploadedFile file = uploadedFileRepository.findOne(id);
        if (file == null) {
            throw new EntityNotFoundException("file (id=" + id
                    + ") is not found.");
        }
        return file;
    }

    @Override
    @Transactional
    public UploadedFile download(String id, String downloadKey) {
        UploadedFile file = getFile(id);
        String key = file.getDownloadKey();
        if (StringUtils.hasText(key)) {
            // validate downloadKey
            String salt = id;
            if (!passwordEncoder.isPasswordValid(key, downloadKey, salt)) {
                throw new BussinessException("downloadKey is not correct for file(id="
                        + id + ")");
            }
        }
        int count = file.getDownloadCount();
        file.setDownloadCount(++count);
        uploadedFileRepository.save(file);
        return file;
    }

    @Override
    @Transactional
    public void delete(String id, String deleteKey) {
        UploadedFile file = getFile(id);
        String key = file.getDeleteKey();
        if (StringUtils.hasText(key)) {
            // validate deleteKey
            String salt = id;
            if (!passwordEncoder.isPasswordValid(key, deleteKey, salt)) {
                throw new BussinessException("deleteKey is not correct for file(id="
                        + id + ")");
            }
        } else {
            throw new BussinessException("file(id=" + id + ") is not deletable");
        }
        uploadedFileRepository.delete(id);
    }

    @Override
    @Transactional
    public void upload(UploadedFile file, String downloadKey, String deleteKey) {
        int size = file.getContent().length;
        if (size > maxUploadSize) {
            throw new BussinessException("file size is too large (" + size
                    + " > " + maxUploadSize + ").");
        }
        String id = createId();
        file.setId(id);
        file.setUploadedDate(new Date());
        file.setDownloadCount(0);
        String salt = id;
        if (StringUtils.hasText(downloadKey)) {
            // encode download key
            String encodedKey = passwordEncoder.encodePassword(downloadKey,
                    salt);
            file.setDownloadKey(encodedKey);
        }
        if (StringUtils.hasText(deleteKey)) {
            // encode delete key
            String encodedKey = passwordEncoder.encodePassword(deleteKey, salt);
            file.setDeleteKey(encodedKey);
        }
        uploadedFileRepository.save(file);
    }

    private String createId() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

}
