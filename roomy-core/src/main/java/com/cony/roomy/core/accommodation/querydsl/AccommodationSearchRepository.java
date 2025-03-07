package com.cony.roomy.core.accommodation.querydsl;

import com.cony.roomy.core.accommodation.domain.Accommodation;
import com.cony.roomy.core.accommodation.domain.QAccommodation;
import com.cony.roomy.core.accommodation.domain.QRoom;
import com.cony.roomy.core.accommodation.domain.QStock;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class AccommodationSearchRepository {

    private final JPAQueryFactory queryFactory;

    public Page<Accommodation> searchAccommodations(String keyword, LocalDate startDate, LocalDate endDate, int guestCnt, Pageable pageable) {
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

        // HHH90003004: firstResult/maxResults specified with collection fetch; applying in memory
        // 페이징과 fetchJoin()을 함께 사용할 경우 컬렉션을 포함한 fetchJoin은 메모리에서 처리된다.
        // 즉, DB에서 페이징이 적용되지 않고, 먼저 모든 데이터를 가져온 후 메모리에서 페이징을 수행하는 문제가 있어 메모리가 터질 위험이 있음.
        // 해결: id 리스트를 먼저 가져와 해당 id를 기준으로 관계 데이터 처리

        // 1. ID 리스트 조회 (페이징 적용)
        List<Long> accommodationIds = queryFactory
                .select(accommodation.id)
                .from(accommodation)
                .leftJoin(accommodation.rooms, room)
                .leftJoin(room.stocks, stock)
                .where(
                        searchKeyword(keyword, accommodation),
                        room.maxGuestCnt.goe(guestCnt),
                        stock.date.goe(startDate).and(stock.date.lt(endDate))
                                .and(stock.quantity.gt(0))
                )
                .distinct()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 2. ID 리스트를 기반으로 전체 데이터 조회 (fetchJoin 사용 가능)
        List<Accommodation> content = queryFactory
                .selectFrom(accommodation)
                .leftJoin(accommodation.rooms, room).fetchJoin()
                .leftJoin(room.stocks, stock).fetchJoin()
                .where(accommodation.id.in(accommodationIds))
                .distinct()
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(accommodation.count())
                .from(accommodation)
                .leftJoin(accommodation.rooms, room)
                .leftJoin(room.stocks, stock)
                .where(
                        searchKeyword(keyword, accommodation),
                        room.maxGuestCnt.goe(guestCnt), // 최대 인원 조건
                        stock.date.goe(startDate).and(stock.date.lt(endDate)) // 숙박 기간 조건
                                .and(stock.quantity.gt(0)) // 예약 가능한 재고만 조회
                )
                .distinct();

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private BooleanExpression searchKeyword(String keyword, QAccommodation accommodation) {
        if (StringUtils.isBlank(keyword)) {
            return null;
        }

        return Expressions.numberTemplate(Double.class,
                "function('match_against', {0}, {1}, {2})",
                accommodation.name, accommodation.address.city, keyword).gt(0);
    }


}
