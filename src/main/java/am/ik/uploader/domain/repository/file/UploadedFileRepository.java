package am.ik.uploader.domain.repository.file;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import am.ik.uploader.domain.model.UploadedFile;

public interface UploadedFileRepository extends
                                       JpaRepository<UploadedFile, String> {

    @Query(value = "SELECT x FROM UploadedFile x ORDER BY x.uploadedDate DESC", countQuery = "SELECT COUNT(x) FROM UploadedFile x")
    Page<UploadedFile> findAllOrderByUploadedDate(Pageable pageable);
}
