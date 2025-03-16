package com.cony.roomy.core.accommodation.domain;

import com.cony.roomy.core.common.domain.BaseTimeEntity;
import com.cony.roomy.core.image.domain.Image;
import com.cony.roomy.core.reservation.domain.Stock;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Room extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private BigDecimal overNightPrice;

    private BigDecimal shortStayPrice;

    private int maxGuestCnt;

    private LocalTime checkIn;

    private LocalTime checkOut;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "accommodation_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Accommodation accommodation;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    @Builder.Default
    private Set<Image> images = new HashSet<>();

    @BatchSize(size = 1000)
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    @Builder.Default
    private Set<Stock> stocks = new HashSet<>();

    /* 연관관계 편의 메소드 */
    public void setAccommodation(Accommodation accommodation) {
        this.accommodation = accommodation;
    }

    public void saveImages(Set<Image> images) {
        this.images = images;
        images.forEach(image -> image.setRoom(this));
    }
}
