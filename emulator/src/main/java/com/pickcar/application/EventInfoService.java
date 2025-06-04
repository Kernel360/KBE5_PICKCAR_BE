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
                .vehicleId(request.getCarId())
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
                .vehicleId(request.getCarId())
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

//    //가장 최근의 on 내용이 내가 고르려는 그 내용인지에 대한 보장
//    public void getLatestOnEventInfoByVehicleId(Long vehicleId) {
//
//        eventInfoRepository.findByVehicleIdAndStatus(vehicleId, )
//
//    }

    //가장 최근의 off 내용이 내가 고르려는 그 내용인지에 대한 보장
    public EventInfo getLatestOffEventInfoByVehicleId(Long vehicleId) {
        return eventInfoRepository.findTopByVehicleIdOrderByEngineOffTimeDesc(vehicleId)
                .orElseThrow(
                        () -> new IllegalArgumentException("[ERROR] EventInfo Not Found By Vehicle Id : " + vehicleId));
    }
}
