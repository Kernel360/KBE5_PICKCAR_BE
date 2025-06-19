package com.pickcar.presentation.controller;

import com.pickcar.application.EventMessagePublisher;
import com.pickcar.dto.EventPayload;
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

    private final EventMessagePublisher eventMessagePublisher;

    @PostMapping("/engine/on")
    public void emulatorEngineOn(@RequestBody EventPayload eventPayload) {
        log.info("POST /api/v1/engine/on - EventPayload: {}", eventPayload);
        eventMessagePublisher.publish(eventPayload);
    }

    @PostMapping("/engine/off")
    public void emulatorEngineOff(@RequestBody EventPayload eventPayload) {
        log.info("POST /api/v1/engine/off - EventPayload: {}", eventPayload);
        eventMessagePublisher.publish(eventPayload);
    }

}
