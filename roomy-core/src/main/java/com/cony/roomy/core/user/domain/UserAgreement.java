package com.cony.roomy.core.user.domain;

import com.cony.roomy.core.common.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class UserAgreement extends BaseTimeEntity {

    @EmbeddedId
    private UserAgreementId userAgreementId;

    @Builder
    public UserAgreement(UserAgreementId userAgreementId) {
        this.userAgreementId = userAgreementId;
    }
}
