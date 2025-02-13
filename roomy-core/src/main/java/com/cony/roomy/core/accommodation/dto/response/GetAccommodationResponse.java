package com.cony.roomy.core.accommodation.dto.response;

import java.util.List;

public record GetAccommodationResponse(List<AccommodationResponse> accommodationResponses) {

    public static GetAccommodationResponse of(List<AccommodationResponse> accommodationResponses) {
        return new GetAccommodationResponse(accommodationResponses);
    }

}
