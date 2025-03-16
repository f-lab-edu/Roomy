package com.cony.roomy.core.reservation.domain;

import com.cony.roomy.core.common.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Payment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long reservationId; // 예약 번호
    private Long userId; // 결제한 사용자
    private BigDecimal amount; // 결제 금액

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private PaymentStatus status = PaymentStatus.PENDING; // PENDING, COMPLETED, FAILED

    public void completePayment() {
        this.status = PaymentStatus.COMPLETED;
    }

    public void failPayment() {
        this.status = PaymentStatus.FAILED;
    }
}
