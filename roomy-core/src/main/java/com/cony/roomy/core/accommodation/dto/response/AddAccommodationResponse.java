package com.cony.roomy.core.accommodation.dto.response;

import com.cony.roomy.core.accommodation.domain.AccommodationType;
import com.cony.roomy.core.accommodation.domain.Address;
import com.cony.roomy.core.image.dto.ImageDto;
import lombok.*;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AddAccommodationResponse {

    private Long id;
    private String name;
    private AccommodationType type;
    private Address address;
    private double rating;
    private List<AddRoomResponse> rooms;
    private List<ImageDto> images;
    private String description;
}
