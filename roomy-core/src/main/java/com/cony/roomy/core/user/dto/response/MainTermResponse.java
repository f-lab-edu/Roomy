package com.cony.roomy.core.user.dto.response;

import com.cony.roomy.core.user.dto.TermIdDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
@Builder
public class MainTermResponse {
    private List<TermIdDto> termIdDtos;
}
