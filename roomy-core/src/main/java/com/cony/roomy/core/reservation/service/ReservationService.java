package com.cony.roomy.core.reservation.service;

import com.cony.roomy.core.accommodation.domain.Room;
import com.cony.roomy.core.accommodation.domain.RoomRepository;
import com.cony.roomy.core.common.exception.ErrorType;
import com.cony.roomy.core.common.exception.RoomyException;
import com.cony.roomy.core.reservation.domain.*;
import com.cony.roomy.core.reservation.dto.request.ReservationRequest;
import com.cony.roomy.core.reservation.dto.request.RollbackRequest;
import com.cony.roomy.core.reservation.dto.request.StockRequest;
import com.cony.roomy.core.reservation.dto.response.ReservationResponse;
import com.cony.roomy.core.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService {

    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;
    private final PaymentRepository paymentRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @KafkaListener(topics = "PAYMENT_PROCESS_EVENT", groupId = "payment-group", containerFactory = "kafkaListenerContainerFactory")
    public ReservationResponse reserveRoom(ReservationRequest reservationRequest) {

        userRepository.findById(reservationRequest.getUserId())
                .orElseThrow(() -> new RoomyException(ErrorType.USER_NOT_FOUND, Map.of("userId", reservationRequest.getUserId()), log::error));

        Room room = roomRepository.findById(reservationRequest.getRoomId())
                .orElseThrow(() -> new RoomyException(ErrorType.ROOM_NOT_FOUND, Map.of("roomId", reservationRequest.getRoomId()), log::error));

        Reservation reservation = Reservation.of(reservationRequest, room);
        reservationRepository.save(reservation);

        // 재고 감소 이벤트 발행
        StockRequest stockRequest = StockRequest.of(reservationRequest, UsageType.OVERNIGHT);
        stockRequest.setRollbackIds(reservationRequest.getPaymentId(), reservation.getId());
        kafkaTemplate.send("STOCK_DECREASE_EVENT", stockRequest);

        // response 생성
        return ReservationResponse.from(reservation);
    }

    public ReservationResponse getReservationByNo(String reservationNo) {
        Reservation reservation = reservationRepository.findByReservationNo(reservationNo)
                .orElseThrow(() -> new RoomyException(ErrorType.RESERVATION_NOT_FOUND, Map.of("reservationNo", reservationNo), log::error));
        return ReservationResponse.from(reservation);
    }

    public void cancel(String reservationNo) {
        Reservation reservation = reservationRepository.findByReservationNo(reservationNo)
                .orElseThrow(() -> new RoomyException(ErrorType.RESERVATION_NOT_FOUND, Map.of("reservationNo", reservationNo), log::error));

        Payment payment = paymentRepository.findPaymentByReservationId(reservation.getId())
                .orElseThrow(() -> new RoomyException(ErrorType.PAYMENT_NOT_FOUND, Map.of("reservationId", reservation.getId()), log::error));

        rollbackReservationAndPayment(RollbackRequest.of(reservation.getId(), payment.getId()));
    }

    @KafkaListener(topics = "ROLLBACK_EVENT", groupId = "rollback-group")
    public void rollbackReservationAndPayment(RollbackRequest rollbackRequest) {
        // 1. 예약 취소
        Reservation reservation = reservationRepository.findById(rollbackRequest.getReservationId())
                .orElseThrow(() -> new RoomyException(ErrorType.RESERVATION_NOT_FOUND, Map.of("reservationId", rollbackRequest.getReservationId()), log::error));
        reservation.cancel();
        reservationRepository.save(reservation);
        log.info("예약 취소 완료: Reservation Id={}", rollbackRequest.getReservationId());

        // 2. 결제 취소
        Payment payment = paymentRepository.findById(rollbackRequest.getPaymentId())
                .orElseThrow(() -> new RoomyException(ErrorType.PAYMENT_NOT_FOUND, Map.of("paymentId", rollbackRequest.getPaymentId()), log::error));
        payment.failPayment();
        paymentRepository.save(payment);
        // 3. PG사 결제 취소 요청 전송

        log.info("결제 실패 처리 완료: Payment Id={}", rollbackRequest.getPaymentId());
    }
}