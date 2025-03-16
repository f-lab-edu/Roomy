package com.cony.roomy.core.reservation.domain;

import com.cony.roomy.core.accommodation.domain.Room;
import com.cony.roomy.core.common.domain.BaseTimeEntity;
import com.cony.roomy.core.reservation.dto.request.ReservationRequest;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Reservation extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 16)
    private String reservationNo;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @Column(nullable = false, length = 20)
    private String guestName;

    @Column(nullable = false, length = 15)
    private String guestPhone;

    @Column(length = 500)
    private String guestComment;

    private int guestCount;

    private LocalDate startDate;
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    public static Reservation of(ReservationRequest reservationRequest, Room room) {
        return Reservation.builder()
                .reservationNo(generateReservationNo())
                .userId(reservationRequest.getUserId())
                .room(room)
                .startDate(reservationRequest.getStartDate())
                .endDate(reservationRequest.getEndDate())
                .guestCount(reservationRequest.getGuestCount())
                .guestName(reservationRequest.getGuestName())
                .guestPhone(reservationRequest.getGuestPhone())
                .guestComment(reservationRequest.getGuestComment())
                .status(ReservationStatus.CONFIRMED)
                .build();
    }

    private static String generateReservationNo() {
        String random = UUID.randomUUID().toString();
        random = random.replaceAll("-", "");
        random = random.substring(0, 16);

        return random;
    }

    public void cancel() {
        this.status = ReservationStatus.CANCELLED;
    }
}