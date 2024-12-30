package com.cony.roomy.core.user.dto;

import com.cony.roomy.core.common.util.BooleanToYNConverter;
import jakarta.persistence.Convert;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TermIdDto {
    private String subject;
    private String version;
    @Convert(converter = BooleanToYNConverter.class)
    private Boolean isMandatory;

}
