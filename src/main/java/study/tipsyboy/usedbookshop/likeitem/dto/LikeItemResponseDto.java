package study.tipsyboy.usedbookshop.likeitem.dto;

import lombok.Getter;
import study.tipsyboy.usedbookshop.likeitem.domain.LikeItem;


@Getter
public class LikeItemResponseDto {

    private final Long likeItemId;

    private final Long itemId;

    private final String itemName;

    public LikeItemResponseDto(LikeItem entity) {
        this.likeItemId = entity.getId();
        this.itemId = entity.getItem().getId();
        this.itemName = entity.getItem().getItemName();
    }
}
