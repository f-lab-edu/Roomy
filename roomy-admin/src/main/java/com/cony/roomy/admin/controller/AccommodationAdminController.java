package com.cony.roomy.admin.controller;

import com.cony.roomy.core.accommodation.dto.request.AddAccommodationRequest;
import com.cony.roomy.core.accommodation.service.AccommodationService;
import com.cony.roomy.core.common.dto.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin")
public class AccommodationAdminController {

    private final AccommodationService accommodationService;

    // 숙소 저장
    @PostMapping("/accommodations")
    public ApiResponse<Void> createAccommodation
        (@Valid @RequestBody AddAccommodationRequest addAccommodationRequest) {
        accommodationService.createAccommodation(addAccommodationRequest);
        return ApiResponse.created("숙소 생성 완료");
    }

}
