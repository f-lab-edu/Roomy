package com.cony.roomy.core.reservation.service;

import com.cony.roomy.core.accommodation.domain.Room;
import com.cony.roomy.core.accommodation.domain.RoomRepository;
import com.cony.roomy.core.common.exception.ErrorType;
import com.cony.roomy.core.common.exception.RoomyException;
import com.cony.roomy.core.reservation.domain.Payment;
import com.cony.roomy.core.reservation.domain.PaymentRepository;
import com.cony.roomy.core.reservation.dto.mapper.PaymentMapper;
import com.cony.roomy.core.reservation.dto.request.PaymentRequest;
import com.cony.roomy.core.reservation.dto.request.ReservationRequest;
import com.cony.roomy.core.reservation.dto.response.PaymentResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final PaymentRepository paymentRepository;
    private final RoomRepository roomRepository;
    private final PaymentMapper paymentMapper;

    public PaymentResponse payment(PaymentRequest paymentRequest) {

        // 1. 결제 정보 생성
        Payment payment = paymentMapper.toEntity(paymentRequest);
        paymentRepository.save(payment);

        // 2. PG 결제 진행
        boolean isSuccess = pgPayment();

        // 3. 결제 상태에 따른 이벤트 발생
        if (isSuccess) {
            // 결제 성공 이벤트 발행
            ReservationRequest reservationRequest = ReservationRequest.of(paymentRequest);
            reservationRequest.setPaymentId(payment.getId());
            kafkaTemplate.send("PAYMENT_PROCESS_EVENT", reservationRequest);
            payment.completePayment();
        } else {
            payment.failPayment();
        }
        paymentRepository.save(payment);

        return PaymentResponse.from(payment);
    }

    private boolean pgPayment() {
        log.debug("Mockup pg payment request");
        return true;
    }
}
