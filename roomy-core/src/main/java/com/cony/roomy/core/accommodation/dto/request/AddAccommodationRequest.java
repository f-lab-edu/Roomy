package com.cony.roomy.core.accommodation.dto.request;

import com.cony.roomy.core.accommodation.domain.AccommodationType;
import com.cony.roomy.core.accommodation.domain.Address;
import com.cony.roomy.core.image.dto.ImageDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddAccommodationRequest {

    @NotBlank(message = "숙소 이름을 입력해주세요.")
    @Length(max = 20, message = "숙소 이름은 20자 이내로 입력해주세요.")
    private String name;

    @NotNull(message = "숙소 타입을 선택해주세요.")
    private AccommodationType type;

    @NotNull(message = "주소를 입력해주세요.")
    private Address address;

    @NotNull(message = "숙소 사진 정보가 없습니다.")
    @Valid
    private List<ImageDto> images;

    @NotNull(message = "룸 정보가 없습니다.")
    @Valid
    private List<AddRoomRequest> rooms;

    private String description;

    private Integer maxShortStayHours;
}
