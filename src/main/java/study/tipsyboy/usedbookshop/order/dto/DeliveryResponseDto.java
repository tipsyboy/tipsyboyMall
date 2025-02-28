package study.tipsyboy.usedbookshop.order.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.tipsyboy.usedbookshop.order.domain.Delivery;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class DeliveryResponseDto {

    private String orderer;

    private String receiver;
    private String phoneNumber;
    private String zipcode;
    private String roadAddress;
    private String jibunAddress;
    private String detailAddress;

    public static DeliveryResponseDto of(Delivery entity) {
        DeliveryResponseDto dto = new DeliveryResponseDto();
        dto.orderer = entity.getOrdererName();
        dto.receiver = entity.getReceiver();
        dto.phoneNumber = entity.getPhoneNumber();
        dto.zipcode = entity.getZipcode();
        dto.roadAddress = entity.getRoadAddress();
        dto.jibunAddress = entity.getJibunAddress();
        dto.detailAddress = entity.getDetailAddress();

        return dto;
    }
}
