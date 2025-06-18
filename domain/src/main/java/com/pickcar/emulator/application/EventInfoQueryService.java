package com.pickcar.emulator.application;

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
}
