package com.cony.roomy.core.user.dto.request;

import com.cony.roomy.core.user.domain.Term;
import com.cony.roomy.core.user.domain.TermId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AddTermsRequest {

    private String subject;
    private String version;
    private String content;
    private Boolean isMandatory;
    private Boolean isVisibleOnMain;

    public TermId toTermId() {
        return TermId.builder()
                .subject(subject)
                .version(version)
                .build();
    }

    public Term toEntity(TermId termId) {
        return Term.builder()
                .termId(termId)
                .content(content)
                .isMandatory(isMandatory)
                .isVisibleOnMain(isVisibleOnMain)
                .build();
    }
}
