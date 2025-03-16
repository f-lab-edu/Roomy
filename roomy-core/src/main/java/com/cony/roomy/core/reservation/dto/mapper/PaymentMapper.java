package com.cony.roomy.core.reservation.dto.mapper;

import com.cony.roomy.core.reservation.domain.Payment;
import com.cony.roomy.core.reservation.dto.request.PaymentRequest;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PaymentMapper {

    Payment toEntity(PaymentRequest paymentRequest);
}
