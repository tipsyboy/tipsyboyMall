package study.tipsyboy.usedbookshop.item.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class ItemUpdateDto {

    private final String itemName;
    private final Integer price;
    private final Integer stock;
    private final String description;
    private final List<String> storedImages;

    @Builder
    public ItemUpdateDto(String itemName, Integer price, Integer stock, String description, List<String> storedImages) {
        this.itemName = itemName;
        this.price = price;
        this.stock = stock;
        this.description = description;
        this.storedImages = storedImages;
    }
}