package com.pickcar.presentation.controller;

import com.pickcar.application.CycleMessagePublisher;
import com.pickcar.mq.dto.CyclePayload;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/cycle")
@AllArgsConstructor
public class CycleApiController {

    private CycleMessagePublisher cycleMessagePublisher;

    @PostMapping
    public void emulatorCycle(@RequestBody CyclePayload cyclePayload) {
        log.info("POST /api/v1/cycle - CyclePayload: {}", cyclePayload);
        cycleMessagePublisher.publish(cyclePayload);
    }
}