package study.tipsyboy.usedbookshop.order.dto;

import lombok.Getter;

@Getter
public class DeliveryRequestDto {

    private final String receiver;
    private final String phoneNumber;
    private final String zipcode;
    private final String roadAddress;
    private final String jibunAddress;
    private final String detailAddress;

    public DeliveryRequestDto(String receiver, String phoneNumber, String zipcode, String roadAddress, String jibunAddress, String detailAddress) {
        this.receiver = receiver;
        this.phoneNumber = phoneNumber;
        this.zipcode = zipcode;
        this.roadAddress = roadAddress;
        this.jibunAddress = jibunAddress;
        this.detailAddress = detailAddress;
    }
}
