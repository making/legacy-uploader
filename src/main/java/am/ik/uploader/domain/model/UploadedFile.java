package am.ik.uploader.domain.model;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@Entity
@Table(name = "uploaded_file")
@Data
public class UploadedFile {
    @Id
    @Column(name = "id", length = 36)
    private String id;

    @Column(name = "content_type", nullable = false)
    private String contentType;

    @Column(name = "file_size", nullable = false)
    private long fileSize;

    @Column(name = "uploaded_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date uploadedDate;

    @Column(name = "content", nullable = false)
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] content;

    @Column(name = "download_key", nullable = true, length = 128)
    private String downloadKey;

    @Column(name = "delete_key", nullable = true, length = 128)
    private String deleteKey;

    @Column(name = "description", nullable = true, length = 128)
    private String description;

    @Column(name = "download_count", nullable = false)
    private int downloadCount;

    @Column(name = "original_file_name", nullable = false, length = 128)
    private String originalFileName;
}
