package com.cony.roomy.api.reservation;

import com.cony.roomy.core.common.dto.response.ApiResponse;
import com.cony.roomy.core.common.filter.Authentication;
import com.cony.roomy.core.reservation.domain.UsageType;
import com.cony.roomy.core.reservation.dto.request.ReservationRequest;
import com.cony.roomy.core.reservation.dto.response.ReservationResponse;
import com.cony.roomy.core.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    public ApiResponse<ReservationResponse> reserveOvernight(@RequestBody ReservationRequest request) {
        ReservationResponse reservationResponse = reservationService.reserveRoom(request);
        return ApiResponse.created("예약이 확정되었습니다.", reservationResponse);
    }

    @Authentication
    @GetMapping("/{reservationNo}")
    public ApiResponse<ReservationResponse> getReservationByNo(@PathVariable String reservationNo) {
        return ApiResponse.ok(reservationService.getReservationByNo(reservationNo));
    }

    @PutMapping("/cancel/{reservationNo}")
    public ApiResponse<Void> cancelReservation(@PathVariable String reservationNo) {
        reservationService.cancel(reservationNo);
        return ApiResponse.ok("예약이 취소되었습니다.");
    }
}