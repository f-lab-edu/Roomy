package com.cony.roomy.core.accommodation.domain;

import com.cony.roomy.core.common.domain.BaseTimeEntity;
import com.cony.roomy.core.image.domain.Image;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Formula;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

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

//    @Formula("(select coalesce(average(r.rating), 0) from review r where r.accommodation_id = id)")
    private double rating;

    @Formula("(SELECT COALESCE(MIN(r.price), 0) FROM room r WHERE r.accommodation_id = id)")
    @Column(columnDefinition = "decimal(15,2)")
    private BigDecimal price;

    @Lob
    private String description;

    @BatchSize(size = 1000)
    @OneToMany(mappedBy = "accommodation", cascade = CascadeType.ALL)
    @Builder.Default
    private Set<Image> images = new HashSet<>();

    @BatchSize(size = 1000)
    @OneToMany(mappedBy = "accommodation", cascade = CascadeType.ALL)
    @Builder.Default
    private Set<Room> rooms = new HashSet<>();

    /* 연관관계 편의 메소드 */
    public void saveImages(Set<Image> images) {
        this.images = images;
        images.forEach(image -> image.setAccommodation(this));
    }

    public void saveRoomsAndImages(Set<Room> rooms, Set<Image> images) {
        saveImages(images);
        this.rooms = rooms;
        rooms.forEach(room -> room.setAccommodation(this));
    }

}
