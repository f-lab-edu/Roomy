package com.cony.roomy.api.accommodation;

import com.cony.roomy.core.accommodation.dto.response.AccommodationResponse;
import com.cony.roomy.core.accommodation.dto.response.GetAccommodationResponse;
import com.cony.roomy.core.accommodation.service.AccommodationService;
import com.cony.roomy.core.common.dto.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/accommodations")
public class AccommodationController {

    private final AccommodationService accommodationService;

    @GetMapping()
    public ApiResponse<GetAccommodationResponse> getAccommodations(
            @RequestParam String keyword,
            @RequestParam(required = false, defaultValue = "#{T(java.time.LocalDate).now()}") LocalDate startDate,
            @RequestParam(required = false, defaultValue = "#{T(java.time.LocalDate).now().plusDays(1)}") LocalDate endDate,
            @RequestParam(required = false, defaultValue = "2") int personal) {

        List<AccommodationResponse> accommodations = accommodationService.getAccommodations(keyword, startDate, endDate, personal);
        return ApiResponse.ok(GetAccommodationResponse.of(accommodations));

    }
}
