package com.pickcar.presentation.controller;

import com.pickcar.application.EmulatorService;
import com.pickcar.presentation.dto.EmulatorRequest;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class EmulatorApiController {

    private final EmulatorService emulatorService;

    @PostMapping("/engine/on")
    public void emulatorEngineOn(@RequestBody EmulatorRequest request) {
        log.info("POST /api/v1/engine/on - EmulatorRequest: {}", request);
        emulatorService.on(request);
    }

    @PostMapping("/engine/off")
    public void emulatorEngineOff(@RequestBody EmulatorRequest request) {
        log.info("POST /api/v1/engine/off - EmulatorRequest: {}", request);
        emulatorService.off(request);

    }
}
