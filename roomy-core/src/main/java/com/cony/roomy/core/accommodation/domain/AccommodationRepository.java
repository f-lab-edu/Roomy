package com.cony.roomy.core.accommodation.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AccommodationRepository extends JpaRepository<Accommodation, Long> {

    @Query("""
            select a from Accommodation a
                        left join fetch a.rooms r
                        left join fetch r.stocks s
            where a.address.city like :keyword%
            and r.maxGuestCnt >= :guestCnt
            and s.date >= :startDate and s.date < :endDate and s.quantity > 0
            and :startDate >= CURRENT_DATE
            and :endDate > CURRENT_DATE
            """)
    List<Accommodation> findAccommodationsByKeyword(@Param("keyword") String keyword,
                                                    @Param("startDate") LocalDate startDate,
                                                    @Param("endDate") LocalDate endDate,
                                                    @Param("guestCnt") int guestCnt);
}
