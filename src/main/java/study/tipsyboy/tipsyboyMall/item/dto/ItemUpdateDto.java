package study.tipsyboy.tipsyboyMall.item.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ItemUpdateDto {

    private final String itemName;
    private final Integer price;
    private final Integer stock;
    private final String description;

    @Builder
    public ItemUpdateDto(String itemName, Integer price, Integer stock, String description) {
        this.itemName = itemName;
        this.price = price;
        this.stock = stock;
        this.description = description;
    }
}