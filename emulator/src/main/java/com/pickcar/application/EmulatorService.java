package com.pickcar.application;

import com.pickcar.domain.Terminal;
import com.pickcar.infrastructure.EmulatorRepository;
import com.pickcar.presentation.dto.EmulatorRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmulatorService {

    private final EmulatorRepository emulatorRepository;

    public void on(EmulatorRequest request) {
        Terminal terminal = Terminal.builder()
                .mdn(request.getMdn())
                .carId(request.getCarId())
                .terminalId(request.getTerminalId())
                .manufactureId(request.getManufactureId())
                .packetVersion(request.getPacketVersion())
                .deviceId(request.getDeviceId())
                .build();
        emulatorRepository.save(terminal);
    }

    public void off(EmulatorRequest request) {
        Terminal terminal = Terminal.builder()
                .mdn(request.getMdn())
                .carId(request.getCarId())
                .terminalId(request.getTerminalId())
                .manufactureId(request.getManufactureId())
                .packetVersion(request.getPacketVersion())
                .deviceId(request.getDeviceId())
                .build();
        emulatorRepository.save(terminal);
    }
}
