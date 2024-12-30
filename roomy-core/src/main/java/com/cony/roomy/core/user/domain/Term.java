package com.cony.roomy.core.user.domain;


import com.cony.roomy.core.common.domain.BaseTimeEntity;
import com.cony.roomy.core.common.util.BooleanToYNConverter;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Term extends BaseTimeEntity {

    // 제목, 버전 복합키 처리
    @EmbeddedId
    private TermId termId;

    @Column(name = "content", nullable = false)
    private String content;

    @Convert(converter = BooleanToYNConverter.class)
    private Boolean isMandatory;

    @Convert(converter = BooleanToYNConverter.class)
    private Boolean isVisibleOnMain;

    @Builder
    public Term(TermId termId, String content, boolean isMandatory, boolean isVisibleOnMain) {
        this.termId = termId;
        this.content = content;
        this.isMandatory = isMandatory;
        this.isVisibleOnMain = isVisibleOnMain;
    }
}
