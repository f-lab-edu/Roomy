package com.cony.roomy.core.accommodation.service;

import com.cony.roomy.core.accommodation.domain.Accommodation;
import com.cony.roomy.core.accommodation.domain.AccommodationRepository;
import com.cony.roomy.core.accommodation.domain.Room;
import com.cony.roomy.core.accommodation.dto.mapper.AccommodationMapper;
import com.cony.roomy.core.accommodation.dto.mapper.RoomMapper;
import com.cony.roomy.core.accommodation.dto.request.AddAccommodationRequest;
import com.cony.roomy.core.accommodation.dto.response.AccommodationResponse;
import com.cony.roomy.core.accommodation.querydsl.AccommodationSearchRepository;
import com.cony.roomy.core.common.exception.ErrorType;
import com.cony.roomy.core.common.exception.RoomyException;
import com.cony.roomy.core.image.domain.Image;
import com.cony.roomy.core.image.domain.ImageType;
import com.cony.roomy.core.image.dto.ImageMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class AccommodationService {

    private final AccommodationRepository accommodationRepository;
    private final AccommodationSearchRepository accommodationSearchRepository;
    private final AccommodationMapper accommodationMapper;
    private final RoomMapper roomMapper;
    private final ImageMapper imageMapper;

    // 숙소 생성: 어드민 전용
    public void createAccommodation(AddAccommodationRequest addAccommodationRequest) {

        // 숙소 정보
        Accommodation accommodation = accommodationMapper.toEntity(addAccommodationRequest);
        // 숙소 이미지 정보
        Set<Image> accommodationImages = addAccommodationRequest.getImages().stream()
                .map(imageMapper::toEntity)
                .collect(Collectors.toSet());
        // 룸 정보
        Set<Room> rooms = addAccommodationRequest.getRooms().stream()
                .map(addRoomRequest -> {
                    Room room = roomMapper.toEntity(addRoomRequest);
                    // 룸 이미지 정보
                    Set<Image> roomImages = addRoomRequest.getImages().stream()
                            .map(imageMapper::toEntity)
                            .collect(Collectors.toSet());
                    room.saveImages(roomImages);
                    return room;
                })
                .collect(Collectors.toSet());
        // 숙소에 객실(룸) 및 이미지 정보 추가
        accommodation.saveRoomsAndImages(rooms, accommodationImages);
        // 저장
        accommodationRepository.save(accommodation);
    }

    public List<AccommodationResponse> getAccommodations(String keyword, LocalDate startDate, LocalDate endDate, int personal) {
//        List<Accommodation> accommodations = accommodationRepository.findAccommodationsByKeyword(keyword, startDate, endDate, personal);

        // 전문 검색 인덱스 NGram + QueryDsl 적용한 검색 Repository
        List<Accommodation> accommodations = accommodationSearchRepository.searchAccommodations(keyword, startDate, endDate, personal);

        List<AccommodationResponse> accommodationResponses = accommodations.stream()
                .map(accommodation -> {
                    AccommodationResponse accommodationResponse = AccommodationResponse.from(accommodation);
                    // 1. 평균 평점 (rating) -> @Formula 를 통해 자동으로 처리되도록 적용 - 현재 리뷰가 없어 주석처리
                    // 2. 객실 최저가 (price) -> @Formula 를 통해 자동으로 처리되도록 적용
                    // 3. 썸네일 이미지
                    String thumbnailUrl = accommodation.getImages().stream()
                            .filter(image -> image.getType().equals(ImageType.ACCOMMODATION_THUMBNAIL))
                            .findFirst()
                            .orElseThrow(() -> new RoomyException(ErrorType.ACCOMMODATION_NOT_FOUND, Map.of("thumbnailUrl", "null"), log::warn))
                            .getUrl();
                    accommodationResponse.setThumbnailUrl(thumbnailUrl);

                    // 4. 리뷰 갯수 (todo)

                    return accommodationResponse;
                })
                .toList();

        return accommodationResponses;
    }
}
