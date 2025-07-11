package com.pickcar.emulator.application;

import com.pickcar.drivehistory.exception.DriveHistoryErrorCode;
import com.pickcar.drivehistory.exception.DriveHistoryException;
import com.pickcar.dto.EventStatus;
import com.pickcar.emulator.domain.EventInfo;
import com.pickcar.emulator.exception.EventInfoQueryErrorCode;
import com.pickcar.emulator.exception.EventInfoQueryException;
import com.pickcar.emulator.infrastructure.EventInfoQueryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventInfoQueryService {

    private final EventInfoQueryRepository eventInfoRepository;

    public EventInfo getById(Long eventInfoId) {
        return eventInfoRepository.findById(eventInfoId)
                .orElseThrow(() -> new EventInfoQueryException(EventInfoQueryErrorCode.NOT_FOUND_BY_ID));
    }

    public EventInfo getOffEventById(Long offEventInfoId) {
        EventInfo eventInfo = getById(offEventInfoId);

        //FIXME: 로깅 및 예외처리를 위한 임시 테스트
        if(eventInfo.getEngineOnTime().isAfter(eventInfo.getEngineOffTime())) {
            log.info("on / off : {} / - / {}", eventInfo.getEngineOnTime(), eventInfo.getEngineOffTime());
            throw new DriveHistoryException(DriveHistoryErrorCode.END_TIME_BEFORE_START_TIME);
        }

        if(EventStatus.ON.equals(eventInfo.getEventStatus())) {
            throw new EventInfoQueryException(EventInfoQueryErrorCode.EVENT_STATUS_NOT_OFF);
        }

        return eventInfo;
    }
}
