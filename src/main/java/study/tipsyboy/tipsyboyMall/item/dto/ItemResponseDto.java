package study.tipsyboy.tipsyboyMall.item.dto;

import lombok.Builder;
import lombok.Getter;
import study.tipsyboy.tipsyboyMall.item.domain.Item;

@Getter
public class ItemResponseDto {

    private final String itemName;
    private final Integer price;
    private final Integer stock;
    private final String description;

    @Builder
    public ItemResponseDto(String itemName, Integer price, Integer stock, String description) {
        this.itemName = itemName;
        this.price = price;
        this.stock = stock;
        this.description = description;
    }

    public ItemResponseDto(Item item) {
        this.itemName = item.getItemName();
        this.price = item.getPrice();
        this.stock = item.getStock();
        this.description = item.getDescription();
    }
}