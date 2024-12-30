package com.cony.roomy.core.user.service;

import com.cony.roomy.core.common.exception.ErrorType;
import com.cony.roomy.core.common.exception.RoomyException;
import com.cony.roomy.core.user.domain.Term;
import com.cony.roomy.core.user.domain.TermId;
import com.cony.roomy.core.user.dto.TermIdDto;
import com.cony.roomy.core.user.dto.TermVersion;
import com.cony.roomy.core.user.dto.request.AddTermsRequest;
import com.cony.roomy.core.user.dto.response.MainTermResponse;
import com.cony.roomy.core.user.domain.TermRepository;
import com.cony.roomy.core.user.dto.response.TermDetailResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class TermsService {

    private final TermRepository termRepository;

    // 약관 삽입 (테스트 용)
    public void saveTerms(AddTermsRequest addTermsRequest) {
        TermId termId = addTermsRequest.toTermId();
        Term term = addTermsRequest.toEntity(termId);

        termRepository.save(term);
    }

    // 약관 이름 조회
    public MainTermResponse getMainTerms() {
        List<TermIdDto> termIdDtos = termRepository.findTermIdByIsVisibleOnMainTrue();
        return MainTermResponse.builder().termIdDtos(termIdDtos).build();
    }

    // 세부 약관 조회
    public TermDetailResponse getTermsDetail(String subject, String version) {
        TermId termId = TermId.builder()
                .subject(subject).version(version)
                .build();
        Term term = termRepository.findById(termId)
                .orElseThrow(() -> new RoomyException(ErrorType.TERM_NOT_FOUND, Map.of("term subject", subject), log::warn));
        // 버전 리스트 출력
        List<String> versions = termRepository.findVersionBySubject(subject);

        return TermDetailResponse.builder()
                .subject(subject)
                .content(term.getContent())
                .createdDate(term.getCreatedDate())
                .modifiedDate(term.getModifiedDate())
                .versions(versions)
                .build();
    }
}
