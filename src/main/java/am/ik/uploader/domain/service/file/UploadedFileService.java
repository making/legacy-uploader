package am.ik.uploader.domain.service.file;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import am.ik.uploader.domain.model.UploadedFile;

public interface UploadedFileService {
    Page<UploadedFile> listFiles(Pageable pageable);

    UploadedFile download(String id, String downloadKey);

    void delete(String id, String deleteKey);

    void upload(UploadedFile file, String downloadKey, String deleteKey);
}
