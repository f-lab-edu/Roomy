package com.cony.roomy.core.accommodation.service;

import com.cony.roomy.core.accommodation.domain.Accommodation;
import com.cony.roomy.core.accommodation.domain.AccommodationRepository;
import com.cony.roomy.core.accommodation.domain.Room;
import com.cony.roomy.core.accommodation.dto.mapper.AccommodationMapper;
import com.cony.roomy.core.accommodation.dto.mapper.RoomMapper;
import com.cony.roomy.core.accommodation.dto.request.AddAccommodationRequest;
import com.cony.roomy.core.image.domain.Image;
import com.cony.roomy.core.image.dto.ImageMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class AccommodationService {

    private final AccommodationRepository accommodationRepository;
    private final AccommodationMapper accommodationMapper;
    private final RoomMapper roomMapper;
    private final ImageMapper imageMapper;

    // 숙소 생성: 어드민 전용
    public void createAccommodation(AddAccommodationRequest addAccommodationRequest) {

        // 숙소 정보
        Accommodation accommodation = accommodationMapper.toEntity(addAccommodationRequest);
        // 숙소 이미지 정보
        List<Image> accommodationImages = addAccommodationRequest.getImages().stream()
                .map(imageMapper::toEntity)
                .toList();
        // 룸 정보
        List<Room> rooms = addAccommodationRequest.getRooms().stream()
                .map(addRoomRequest -> {
                    Room room = roomMapper.toEntity(addRoomRequest);
                    // 룸 이미지 정보
                    List<Image> roomImages = addRoomRequest.getImages().stream()
                            .map(imageMapper::toEntity)
                            .toList();
                    room.saveImages(roomImages);
                    return room;
                })
                .toList();
        // 숙소에 객실(룸) 및 이미지 정보 추가
        accommodation.saveRoomsAndImages(rooms, accommodationImages);
        // 저장
        accommodationRepository.save(accommodation);
    }
}
