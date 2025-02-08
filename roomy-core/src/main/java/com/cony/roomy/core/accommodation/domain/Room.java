package com.cony.roomy.core.accommodation.domain;

import com.cony.roomy.core.common.domain.BaseTimeEntity;
import com.cony.roomy.core.image.domain.Image;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

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

    private int price;

    private int maxGuestCnt;

    private LocalTime checkIn;

    private LocalTime checkOut;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "accommodation_id")
    private Accommodation accommodation;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Image> images = new ArrayList<>();

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Stock> stocks = new ArrayList<>();

    /* 연관관계 편의 메소드 */
    public void setAccommodation(Accommodation accommodation) {
        this.accommodation = accommodation;
    }

    public void saveImages(List<Image> images) {
        this.images = images;
        images.forEach(image -> image.saveRoom(this));
    }
}
