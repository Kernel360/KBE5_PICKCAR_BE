package com.pickcar.application;

import com.pickcar.drivehistory.application.command.WriteDriveHistoryCommand;
import com.pickcar.emulator.domain.EventInfo;
import com.pickcar.infrastructure.EventInfoRepository;
import com.pickcar.presentation.dto.request.EventInfoRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventInfoService {

    private final WriteDriveHistoryCommand writeDriveHistoryCommand;
    private final EventInfoRepository eventInfoRepository;

    public void on(EventInfoRequest request) {
        EventInfo eventInfo = EventInfo.builder()
                .vehicleId(request.getVehicleId())
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
        EventInfo offEventInfo = EventInfo.builder()
                .vehicleId(request.getVehicleId())
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

        eventInfoRepository.save(offEventInfo);

        writeDriveHistoryCommand.execute(request.getVehicleId(), offEventInfo);
    }

    //FIXME: 가장 최근의 off 내용이 내가 고르려는 그 내용인지에 대한 보장
    public EventInfo getLatestOffEventInfoByVehicleId(Long vehicleId) {
        return eventInfoRepository.findTopByVehicleIdOrderByEngineOffTimeDesc(vehicleId)
                .orElseThrow(
                        () -> new IllegalArgumentException("[ERROR] EventInfo Not Found By Vehicle Id : " + vehicleId));
    }

    public EventInfo getById(Long id) {
        //FIXME: fix to custom exception
        return eventInfoRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("[ERROR] EventInfo Not Found By Id : " + id));
    }
}
