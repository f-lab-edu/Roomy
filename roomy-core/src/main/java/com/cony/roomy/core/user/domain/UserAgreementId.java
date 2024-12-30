package com.cony.roomy.core.user.domain;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@Getter
public class UserAgreementId implements Serializable {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "subject", referencedColumnName = "subject"),
            @JoinColumn(name = "version", referencedColumnName = "version")
    })
    private Term term;

    @Builder
    public UserAgreementId(User user, Term term) {
        this.user = user;
        this.term = term;
    }
}
