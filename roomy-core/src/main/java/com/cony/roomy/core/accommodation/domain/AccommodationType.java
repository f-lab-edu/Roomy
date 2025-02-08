package com.cony.roomy.core.accommodation.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AccommodationType {
    MOTEL("모텔"),
    HOTEL("호텔"),
    RESORT("리조트"),
    PENSION("펜션"),
    PREMIUM_BLACK("프리미엄 블랙"),
    CAMPING("캠핑.글램핑"),
    HOME_VILLA("홈&빌라"),
    GUEST_HOUSE("게스트하우스");

    private final String label;
}
