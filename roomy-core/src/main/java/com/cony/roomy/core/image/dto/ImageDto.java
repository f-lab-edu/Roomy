package com.cony.roomy.core.image.dto;

import com.cony.roomy.core.image.domain.ImageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImageDto {

    private Long id;
    private String name;
    private long size;
    private ImageType type;
    private String url;
}
