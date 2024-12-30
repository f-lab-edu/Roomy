package com.cony.roomy.core.user.domain;

import com.cony.roomy.core.user.dto.TermIdDto;
import com.cony.roomy.core.user.dto.TermVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TermRepository extends JpaRepository<Term, TermId> {

    // 약관 이름 조회
    @Query("select  new com.cony.roomy.core.user.dto.TermIdDto (t.termId.subject, t.termId.version, t.isMandatory) " +
            "from   Term t " +
            "where  t.isVisibleOnMain = true " +
            "and    (t.termId.subject, t.termId.version) IN ( " +
            "       select t2.termId.subject, max(t2.termId.version) " +
            "       from   Term t2 " +
            "       group by t2.termId.subject)")
    List<TermIdDto> findTermIdByIsVisibleOnMainTrue();

    @Query(value = "SELECT version AS version FROM term WHERE subject = :subject", nativeQuery = true)
    List<String> findVersionBySubject(String subject);
}
