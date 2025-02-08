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

    // 약관 생성
    @PostMapping("/terms")
    public ApiResponse<Void> createTerm(@Valid @RequestBody AddTermsRequest addTermsRequest) {
        termsService.createTerm(addTermsRequest);
        return ApiResponse.created("약관 생성 완료");
    }

}
