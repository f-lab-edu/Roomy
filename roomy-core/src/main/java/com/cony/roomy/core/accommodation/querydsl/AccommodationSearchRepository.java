package com.cony.roomy.core.accommodation.querydsl;

import com.cony.roomy.core.accommodation.domain.Accommodation;
import com.cony.roomy.core.accommodation.domain.QAccommodation;
import com.cony.roomy.core.accommodation.domain.QRoom;
import com.cony.roomy.core.accommodation.domain.QStock;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class AccommodationSearchRepository {

    private final JPAQueryFactory queryFactory;

    public List<Accommodation> searchAccommodations(String keyword, LocalDate startDate, LocalDate endDate, int guestCnt) {
        QAccommodation accommodation = QAccommodation.accommodation;
        QRoom room = QRoom.room;
        QStock stock = QStock.stock;

        /**
         *     @Query("""
         *             select a from Accommodation a
         *                         left join fetch a.rooms r
         *                         left join fetch r.stocks s
         *             where a.address.city like :keyword% -- match against 로 변경
         *             and r.maxGuestCnt >= :guestCnt
         *             and s.date >= :startDate and s.date < :endDate and s.quantity > 0
         *             and :startDate >= CURRENT_DATE
         *             and :endDate > CURRENT_DATE
         *             """)
         */

        return queryFactory
                .selectFrom(accommodation)
                .leftJoin(accommodation.rooms, room).fetchJoin()
                .leftJoin(room.stocks, stock).fetchJoin()
                .where(
                        searchKeyword(keyword, accommodation),
                        room.maxGuestCnt.goe(guestCnt), // 최대 인원 조건
                        stock.date.goe(startDate).and(stock.date.lt(endDate)) // 숙박 기간 조건
                                .and(stock.quantity.gt(0)) // 예약 가능한 재고만 조회
//                        startDate (LocalDate.now()), // todo: 날짜 비교
//                        endDate.gt(LocalDate.now())
                )
                .distinct()
                .fetch();

        // todo : pagination -> 스크롤
    }

    private BooleanExpression searchKeyword(String keyword, QAccommodation accommodation) {
        if(StringUtils.isBlank(keyword)) {
            return null;
        }

        return Expressions.numberTemplate(Double.class,
                "function('match_against', {0}, {1}, {2})",
                accommodation.name, accommodation.address.city, keyword).gt(0);
    }


}
