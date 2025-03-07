package com.cony.roomy.core.reservation.service;

import com.cony.roomy.core.accommodation.domain.Room;
import com.cony.roomy.core.accommodation.domain.RoomRepository;
import com.cony.roomy.core.common.exception.ErrorType;
import com.cony.roomy.core.common.exception.RoomyException;
import com.cony.roomy.core.reservation.domain.*;
import com.cony.roomy.core.reservation.dto.request.ReservationRequest;
import com.cony.roomy.core.reservation.dto.response.ReservationResponse;
import com.cony.roomy.core.reservation.facade.StockLockFacade;
import com.cony.roomy.core.reservation.generator.NoGenerator;
import com.cony.roomy.core.reservation.domain.StockPessimisticRepository;
import com.cony.roomy.core.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService {

    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final StockLockFacade stockLockFacade;
    private final ReservationRepository reservationRepository;
    private final StockPessimisticRepository stockPessimisticRepository;
    private final NoGenerator noGenerator;

    // @Transactional
    public ReservationResponse reserveRoom(ReservationRequest request, UsageType usageType) {

        userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RoomyException(ErrorType.USER_NOT_FOUND, Map.of("userId", request.getUserId()), log::error));

        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new RoomyException(ErrorType.ROOM_NOT_FOUND, Map.of("roomId", request.getRoomId()), log::error));

        // 밖에서
        for(LocalDate date = request.getStartDate(); date.isBefore(request.getEndDate()); date = date.plusDays(1)) {
            stockLockFacade.decreaseStockWithLock(room.getId(), date, usageType);
        }

        Reservation reservation = Reservation.create(request.getUserId(), room, request, noGenerator);
        reservationRepository.save(reservation);

        // response 생성
        return ReservationResponse.from(reservation);
    }

    public ReservationResponse getReservationByNo(String reservationNo) {
        Reservation reservation = reservationRepository.findByReservationNo(reservationNo)
                .orElseThrow(() -> new RoomyException(ErrorType.RESERVATION_NOT_FOUND, Map.of("reservationNo", reservationNo), log::error));
        return ReservationResponse.from(reservation);
    }
}