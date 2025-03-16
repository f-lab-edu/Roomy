package com.cony.roomy.api.reservation;

import com.cony.roomy.core.common.dto.response.ApiResponse;
import com.cony.roomy.core.reservation.dto.request.PaymentRequest;
import com.cony.roomy.core.reservation.dto.response.PaymentResponse;
import com.cony.roomy.core.reservation.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ApiResponse<PaymentResponse> paymentRequest(@RequestBody PaymentRequest paymentRequest) {
        PaymentResponse paymentResponse = paymentService.payment(paymentRequest);
        return ApiResponse.created("결제가 완료되었습니다. 예약이 확정될 떄까지 잠시만 기다려주세요.", paymentResponse);
    }
}
