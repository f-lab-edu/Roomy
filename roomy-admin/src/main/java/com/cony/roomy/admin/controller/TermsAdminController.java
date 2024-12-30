package com.cony.roomy.admin.controller;

import com.cony.roomy.core.common.dto.response.ApiResponse;
import com.cony.roomy.core.user.dto.request.AddTermsRequest;
import com.cony.roomy.core.user.service.TermsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin")
public class TermsAdminController {

    private final TermsService termsService;

    // 약관 저장 (admin)
    @PostMapping("/terms")
    public ApiResponse<Void> saveTerms(@Valid @RequestBody AddTermsRequest addTermsRequest) {
        termsService.saveTerms(addTermsRequest);
        return ApiResponse.created("약관 생성 완료");
    }

}
