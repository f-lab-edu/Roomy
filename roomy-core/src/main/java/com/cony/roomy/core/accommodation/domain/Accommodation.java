package com.cony.roomy.core.accommodation.domain;

import com.cony.roomy.core.common.domain.BaseTimeEntity;
import com.cony.roomy.core.image.domain.Image;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Accommodation extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private AccommodationType type;

    @Embedded
    private Address address;

    private double rating;

    @Lob
    private String description;

    @BatchSize(size = 1000)
    @OneToMany(mappedBy = "accommodation", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Image> images = new ArrayList<>();

    @BatchSize(size = 1000)
    @OneToMany(mappedBy = "accommodation", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Room> rooms = new ArrayList<>();

    /* 연관관계 편의 메소드 */
    public void saveImages(List<Image> images) {
        this.images = images;
        images.forEach(image -> image.saveAccommodation(this));
    }

    public void saveRoomsAndImages(List<Room> rooms, List<Image> images) {
        saveImages(images);
        this.rooms = rooms;
        rooms.forEach(room -> room.setAccommodation(this));
    }

}
