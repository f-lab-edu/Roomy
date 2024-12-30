package com.cony.roomy.core.user.dto.request;

import com.cony.roomy.core.user.domain.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AddUserAgreementRequest {
    private String subject;
    private String version;

    public TermId toTermId() {
        return TermId.builder()
                .subject(subject)
                .version(version)
                .build();
    }

    public UserAgreementId toUserAgreementId(User user, Term term) {
        return UserAgreementId.builder()
                .user(user)
                .term(term)
                .build();
    }
}
