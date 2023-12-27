package study.tipsyboy.tipsyboyMall.item.domain;

import lombok.Getter;

@Getter
public enum ItemStatus {
    ON_SALE("판매중"),
    SOLD_OUT("판매완료");

    private final String value;

    ItemStatus(String value) {
        this.value = value;
    }
}
