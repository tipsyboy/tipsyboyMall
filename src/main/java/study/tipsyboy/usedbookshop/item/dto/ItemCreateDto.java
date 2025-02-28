package study.tipsyboy.usedbookshop.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.tipsyboy.usedbookshop.item.domain.Item;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ItemCreateDto {

    @NotBlank(message = "상품 이름을 등록하세요.")
    private String itemName;

    @NotNull(message = "가격을 입력해주세요.")
    private Integer price;

    @NotNull(message = "재고를 입력해주세요.")
    private Integer stock; // 재고

    private String description; // 상품

    @Builder
    public ItemCreateDto(String itemName, Integer price, Integer stock, String description) {
        this.itemName = itemName;
        this.price = price;
        this.stock = stock;
        this.description = description;
    }

    // 생성 메서드
    public ItemCreateDto(Item item) {
        this.itemName = item.getItemName();
        this.price = item.getPrice();
        this.stock = item.getStock();
        this.description = item.getDescription();
    }
}
