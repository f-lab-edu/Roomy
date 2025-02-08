package com.cony.roomy.core.accommodation.dto.mapper;

import com.cony.roomy.core.accommodation.domain.Accommodation;
import com.cony.roomy.core.accommodation.dto.request.AddAccommodationRequest;
import com.cony.roomy.core.image.dto.ImageMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = ImageMapper.class)
public interface AccommodationMapper {

    Accommodation toEntity(AddAccommodationRequest addAccommodationRequest);
}
