package com.cony.roomy.core.image.dto;

import com.cony.roomy.core.image.domain.Image;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface ImageMapper {

    Image toEntity(ImageDto imageDto);
}
