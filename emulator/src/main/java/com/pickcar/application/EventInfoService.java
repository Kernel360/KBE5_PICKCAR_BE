package com.pickcar.application;

import com.pickcar.domain.EventInfo;
import com.pickcar.infrastructure.EventInfoRepository;
import com.pickcar.presentation.dto.request.EventInfoRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventInfoService {

    private final EventInfoRepository eventInfoRepository;

    public void on(EventInfoRequest request) {
        EventInfo eventInfo = EventInfo.builder()
                .carId(request.getCarId())
                .status(request.getStatus())
                .engineOnTime(request.getEngineOnTime())
                .engineOffTime(request.getEngineOffTime())
                .gpsStatus(request.getGpsStatus())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .angle(request.getAngle())
                .speed(request.getSpeed())
                .total_distance(request.getTotal_distance())
                .build();
        eventInfoRepository.save(eventInfo);
    }

    public void off(EventInfoRequest request) {
        EventInfo eventInfo = EventInfo.builder()
                .carId(request.getCarId())
                .status(request.getStatus())
                .engineOnTime(request.getEngineOnTime())
                .engineOffTime(request.getEngineOffTime())
                .gpsStatus(request.getGpsStatus())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .angle(request.getAngle())
                .speed(request.getSpeed())
                .total_distance(request.getTotal_distance())
                .build();
        eventInfoRepository.save(eventInfo);
    }
}
