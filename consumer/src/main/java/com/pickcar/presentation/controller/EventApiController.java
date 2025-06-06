package com.pickcar.presentation.controller;

import com.pickcar.application.EventInfoService;
import com.pickcar.presentation.dto.request.EventInfoRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/event")
@AllArgsConstructor
public class EventApiController {

    private final EventInfoService eventInfoService;

    @PostMapping("/engine/on")
    public void emulatorEngineOn(@RequestBody EventInfoRequest request) {
        log.info("POST /api/v1/engine/on - EventInfoRequest: {}", request);
        eventInfoService.on(request);
    }

    @PostMapping("/engine/off")
    public void emulatorEngineOff(@RequestBody EventInfoRequest request) {
        log.info("POST /api/v1/engine/off - EventInfoRequest: {}", request);
        eventInfoService.off(request);
    }

}
