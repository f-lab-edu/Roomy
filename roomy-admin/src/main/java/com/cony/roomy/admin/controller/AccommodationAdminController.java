package com.cony.roomy.admin.controller;

import com.cony.roomy.core.accommodation.dto.request.AddAccommodationRequest;
import com.cony.roomy.core.accommodation.service.AccommodationService;
import com.cony.roomy.core.reservation.service.StockService;
import com.cony.roomy.core.common.dto.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin")
public class AccommodationAdminController {

    private final AccommodationService accommodationService;
    private final StockService stockService;

    // 숙소 저장
    @PostMapping("/accommodations")
    public ApiResponse<Void> createAccommodation(@Valid @RequestBody AddAccommodationRequest addAccommodationRequest) {
        accommodationService.createAccommodation(addAccommodationRequest);
        return ApiResponse.created("숙소 생성 완료");
    }

    // 객실(룸) 재고 추가
    @PostMapping("/rooms/{roomId}")
    public ApiResponse<Void> createRoomStock(@PathVariable("roomId") Long roomId,
                                             @RequestParam(required = false, defaultValue = "#{T(java.time.LocalDate).now()}") LocalDate date,
                                             @RequestParam(required = false, defaultValue = "10") int quantity) {
        stockService.createStock(roomId, date, quantity);
        return ApiResponse.created("룸 재고 생성 완료");
    }

}
