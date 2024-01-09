package study.tipsyboy.tipsyboyMall.files.dto;

import lombok.Getter;
import study.tipsyboy.tipsyboyMall.files.ItemFile;

@Getter
public class ItemFileResponseDto {

    private final String storedName;

    public ItemFileResponseDto(ItemFile itemFile) {
        this.storedName = itemFile.getStoredName();
    }
}
