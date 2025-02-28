package study.tipsyboy.usedbookshop.order.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY)
    private Order order;

    // -> address
    private String receiver; // 수령인
    private String phoneNumber; // 수령인 번호
    private String zipcode;
    private String roadAddress; // 도로명
    private String jibunAddress; // 지번
    private String detailAddress; // 상세주소

    @Builder
    public Delivery(String receiver, String phoneNumber, String zipcode, String roadAddress, String jibunAddress, String detailAddress) {
        this.receiver = receiver;
        this.phoneNumber = phoneNumber;
        this.zipcode = zipcode;
        this.roadAddress = roadAddress;
        this.jibunAddress = jibunAddress;
        this.detailAddress = detailAddress;
    }

    public String getOrdererName() {
        return this.order.getMember().getNickname();
    }
}
