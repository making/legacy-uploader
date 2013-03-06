package am.ik.uploader.app.files.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

import lombok.Data;

import org.springframework.web.multipart.MultipartFile;

import am.ik.uploader.app.files.validation.MultipartFileNotEmpty;

@Data
public class FilesForm {
    public static interface UploadGroup {
    }

    public static interface DeleteGroup {
    }

    public static interface DownloadGroup {
    }

    @Null(groups = { UploadGroup.class })
    @NotNull(groups = { DeleteGroup.class, DownloadGroup.class })
    @Size(min = 36, max = 36)
    private String id;

    @Null(groups = { DeleteGroup.class, DownloadGroup.class })
    @NotNull(groups = { UploadGroup.class })
    @MultipartFileNotEmpty
    private MultipartFile file;

    @NotNull(groups = { UploadGroup.class })
    @Size(min = 1, max = 64)
    private String description;

    private String downloadKey;

    @NotNull(groups = { DeleteGroup.class })
    private String deleteKey;
}
