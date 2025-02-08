package com.cony.roomy.core.accommodation.dto.mapper;

import com.cony.roomy.core.accommodation.domain.Room;
import com.cony.roomy.core.accommodation.dto.request.AddRoomRequest;
import com.cony.roomy.core.image.dto.ImageMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = ImageMapper.class)
public interface RoomMapper {

    Room toEntity(AddRoomRequest addRoomRequest);
}
