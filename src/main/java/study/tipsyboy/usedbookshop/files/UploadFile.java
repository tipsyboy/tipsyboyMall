package study.tipsyboy.usedbookshop.files;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public abstract class UploadFile {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "uploadFile_id")
    private Long id;

    private String uploadName;
    private String storedName;

    public UploadFile(String uploadName, String storedName) {
        this.uploadName = uploadName;
        this.storedName = storedName;
    }
}
