package com.cony.roomy.core.accommodation.domain;

import com.cony.roomy.core.common.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_room_date_usage_time",
                        columnNames = {"room_id", "date", "usage_type", "start_time"}
                )
        }
)

public class Stock extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    @Column(name= "date")
    private LocalDate date;

    // 객실 유형 (숙박 / 대실)
    @Enumerated(EnumType.STRING)
    private UsageType usageType;

    // 대실 시작 시간
    private LocalTime startTime;

    // 대실 종료 시간
    private LocalTime endTime;

    // 예약 가능한 객실 수량
    private int quantity;

}
