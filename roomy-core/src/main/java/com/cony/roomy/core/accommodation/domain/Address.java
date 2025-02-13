package com.cony.roomy.core.accommodation.domain;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Address {

    private String city;
    private String street;
    private String zipcode;

    /* 편의 메소드 */
    public String getFullAddress() {
        return city + " " + street + " " + zipcode;
    }
}
