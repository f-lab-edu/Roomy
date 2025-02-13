package com.cony.roomy.api.user;

import com.cony.roomy.core.common.dto.response.ApiResponse;
import com.cony.roomy.core.user.dto.response.TermDetailResponse;
import com.cony.roomy.core.user.service.TermsService;
import com.cony.roomy.core.user.dto.response.MainTermResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class TermsController {
    private final TermsService termsService;

    // 메인 약관 조회
    @GetMapping("/terms")
    public ApiResponse<MainTermResponse> getMainTerms() {
        MainTermResponse mainTermResponse = termsService.getMainTerms();
        return ApiResponse.ok(mainTermResponse);
    }

    // 약관 내용 조회
    @GetMapping("/terms/detail")
    public ApiResponse<TermDetailResponse> getTermDetails(
            @RequestParam(name = "subject") String subject,
            @RequestParam(name = "version") String version
    ) {
        TermDetailResponse termDetailResponse = termsService.getTermsDetail(subject, version);
        return ApiResponse.ok(termDetailResponse);
    }

}
