package com.cony.roomy.core.reservation.domain;

import com.cony.roomy.core.accommodation.domain.Room;
import com.cony.roomy.core.common.domain.BaseTimeEntity;
import com.cony.roomy.core.common.exception.ErrorType;
import com.cony.roomy.core.common.exception.RoomyException;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

@Slf4j
@Entity
@Getter
@Builder
@NoArgsConstructor
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
    @OnDelete(action = OnDeleteAction.CASCADE)
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

    /* 편의 메소드 */
    public void decreaseQuantity(int count) {
        if (this.quantity < count) {
            throw new RoomyException(ErrorType.STOCK_NOT_ENOUGH , Map.of("quantity", quantity, "decrease", count), log::error);
        }
        this.quantity -= count;
    }

}
