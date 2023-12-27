package study.tipsyboy.tipsyboyMall.item.dto;

import lombok.Builder;
import lombok.Getter;
import study.tipsyboy.tipsyboyMall.item.domain.Item;

import java.time.LocalDateTime;

@Getter
public class ItemResponseDto {

    private final Long itemId;
    // TODO: 카테고리
    private final String itemName;
    private final String status;
    private final String seller;
    private final Integer price; // TODO: 삭제?
    private final Integer stock; // TODO: 삭제?
    private final String description; // TODO: 삭제?
    private final LocalDateTime createdDate;

    public ItemResponseDto(Item item) {
        this.itemId = item.getId();
        this.itemName = item.getItemName();
        this.status = item.getStatus().getValue();
        this.seller = item.getMember().getNickname();
        this.price = item.getPrice();
        this.stock = item.getStock();
        this.description = item.getDescription();
        this.createdDate = item.getCreateDate();
    }

//    @Builder
//    public ItemResponseDto(Long itemId, String itemName,
//                           String status, Integer price,
//                           Integer stock, String description,
//                           LocalDateTime createdDate) {
//        this.itemId = itemId;
//        this.itemName = itemName;
//        this.status = status;
//        this.price = price;
//        this.stock = stock;
//        this.description = description;
//        this.createdDate = createdDate;
//    }
}