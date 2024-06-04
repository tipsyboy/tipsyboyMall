package study.tipsyboy.tipsyboyMall.files;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartFile;
import study.tipsyboy.tipsyboyMall.item.domain.Item;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
@Configuration
public class FileStore {

    private static final String FILE_PATH = "src/main/resources/static/files/";


    public void createFileForItem(Item item, MultipartFile multipartFile) throws IOException {
        String originalFileName = multipartFile.getOriginalFilename();
        String storedFileName = createStoredFileName(originalFileName);

        String itemFilePath = getItemFilePath(storedFileName);
        Path absolutePath = Paths.get(itemFilePath).toAbsolutePath();

        File dir = absolutePath.getParent().toFile();
        if (!dir.exists()) {
            dir.mkdirs();
        }

        multipartFile.transferTo(absolutePath.toFile());
        ItemFile.builder()
                .item(item)
                .uploadName(originalFileName)
                .storedName(storedFileName)
                .build();
    }

    public String getItemFilePath(String storedFileName) {
        String directoryPath = FILE_PATH + "item/";
        return directoryPath + storedFileName;
    }

    private String createStoredFileName(String originalFilename) {
        String ext = extractExt(originalFilename);
        UUID uuid = UUID.randomUUID();
        return uuid + "." + ext;
    }

    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }
}
