package com.pickcar.application;


import com.pickcar.emulator.domain.Cycle;
import com.pickcar.infrastructure.CycleRepository;
import com.pickcar.presentation.dto.request.CycleStoreRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CycleService {

    private final CycleRepository cycleRepository;

    public void store(CycleStoreRequest request) {
        Cycle cycle = new Cycle(request.getVehicleId(), request.getOccurredAt(),
                request.getCycleCnt(), request.getCycleInfos());

        cycleRepository.save(cycle);
    }
}
