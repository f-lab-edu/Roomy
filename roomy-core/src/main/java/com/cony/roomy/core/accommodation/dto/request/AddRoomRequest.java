package com.cony.roomy.core.accommodation.dto.request;

import com.cony.roomy.core.image.dto.ImageDto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddRoomRequest {

    @NotBlank(message = "룸 이름을 입력해주세요.")
    @Length(max = 50, message = "룸 이름은 50자 이내로 입력해주세요.")
    private String name;

    @NotNull(message = "룸의 가격을 입력해주세요.")
    private int price;

    @NotNull(message = "룸 최대 인원을 입력해주세요.")
    @Min(1)
    private int maxGuestCnt;

    @NotNull(message = "체크인 시간을 선택해주세요.")
    private LocalTime checkIn;

    @NotNull(message = "체크아웃 시간을 선택해주세요.")
    private LocalTime checkOut;

    @NotNull(message = "룸 사진 정보가 없습니다.")
    @Builder.Default
    private List<ImageDto> images = new ArrayList<>();
}
