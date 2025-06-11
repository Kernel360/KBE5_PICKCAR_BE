package com.pickcar.emulator.application;

import com.pickcar.emulator.domain.EventInfo;
import com.pickcar.emulator.infrastructure.EventInfoQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventInfoQueryService {

    private final EventInfoQueryRepository eventInfoRepository;

    public EventInfo getById(Long eventInfoId) {
        return eventInfoRepository.findById(eventInfoId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid eventInfoId: " + eventInfoId));
    }
}
