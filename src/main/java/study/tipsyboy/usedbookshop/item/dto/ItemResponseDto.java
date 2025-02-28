package study.tipsyboy.usedbookshop.item.dto;

import lombok.Getter;
import study.tipsyboy.usedbookshop.files.dto.ItemFileResponseDto;
import study.tipsyboy.usedbookshop.item.domain.Item;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ItemResponseDto {

    private final Long itemId;
    // TODO: 카테고리
    private final String itemName;
    private final String status;
    private final String seller;
    private final Integer price;
    private final Integer stock;
    private final String description;
    private final LocalDateTime createdDate;

    private final List<ItemFileResponseDto> itemImages;

    public ItemResponseDto(Item item) {
        this.itemId = item.getId();
        this.itemName = item.getItemName();
        this.status = item.getStatus().getValue();
        this.seller = item.getMember().getNickname();
        this.price = item.getPrice();
        this.stock = item.getStock();
        this.description = item.getDescription();
        this.createdDate = item.getCreateDate();

        this.itemImages = item.getItemImages().stream()
                .map(ItemFileResponseDto::new)
                .collect(Collectors.toList());
    }
}