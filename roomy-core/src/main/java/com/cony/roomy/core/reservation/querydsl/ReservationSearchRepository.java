package com.cony.roomy.core.reservation.querydsl;

import com.cony.roomy.core.reservation.domain.QReservation;
import com.cony.roomy.core.reservation.domain.Reservation;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ReservationSearchRepository {

    private final JPAQueryFactory queryFactory;

    public Optional<Reservation> findByReservationNo(String reservationNo) {
        QReservation reservation = QReservation.reservation;

        return Optional.ofNullable(
                queryFactory
                        .select(reservation)
                        .from(reservation)
                        .where(reservation.reservationNo.eq(reservationNo))
                        .fetchOne()
        );
    }
}
