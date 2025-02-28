package study.tipsyboy.usedbookshop.files.dto;

import lombok.Getter;
import study.tipsyboy.usedbookshop.files.ItemFile;

@Getter
public class ItemFileResponseDto {

    private final String storedName;

    public ItemFileResponseDto(ItemFile itemFile) {
        this.storedName = itemFile.getStoredName();
    }
}
