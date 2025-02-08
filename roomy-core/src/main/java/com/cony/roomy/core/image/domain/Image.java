package com.cony.roomy.core.image.domain;

import com.cony.roomy.core.accommodation.domain.Accommodation;
import com.cony.roomy.core.accommodation.domain.Room;
import com.cony.roomy.core.common.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Image extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private long size;

    @Enumerated(EnumType.STRING)
    private ImageType type;

    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accommodation_id")
    private Accommodation accommodation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    /* 연관관계 편의 메소드 */
    public void saveAccommodation(Accommodation accommodation) {
        this.accommodation = accommodation;
    }

    public void saveRoom(Room room) {
        this.room = room;
    }
}
