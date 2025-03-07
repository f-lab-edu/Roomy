package com.cony.roomy.core.reservation.domain;

import com.cony.roomy.core.accommodation.domain.Room;
import com.cony.roomy.core.common.domain.BaseTimeEntity;
import com.cony.roomy.core.reservation.dto.request.ReservationRequest;
import com.cony.roomy.core.reservation.generator.NoGenerator;
import com.cony.roomy.core.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Reservation {
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

    public static Reservation create(Long userId, Room room, ReservationRequest request, NoGenerator noGenerator) {
        return Reservation.builder()
                .reservationNo(noGenerator.generate())
                .userId(userId)
                .room(room)
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .guestCount(request.getGuestCount())
                .guestName(request.getGuestName())
                .guestPhone(request.getGuestPhone())
                .guestComment(request.getGuestComment())
                .status(ReservationStatus.CONFIRMED)
                .build();
    }
}