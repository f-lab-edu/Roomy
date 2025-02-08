package com.cony.roomy.core.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@Getter
public class TermId implements Serializable {
    // 복합키를 위한 엔티티를 만들 때에는 반드시 Serializable 상속받아야 함.
    @Column(name = "subject", nullable = false, updatable = false)
    private String subject;

    @Column(name = "version", nullable = false, updatable = false)
    private String version;

    @Builder
    public TermId(String subject, String version) {
        this.subject = subject;
        this.version = version;
    }
}
