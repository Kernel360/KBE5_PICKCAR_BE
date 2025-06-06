package com.pickcar.presentation.controller;

import com.pickcar.application.CycleService;
import com.pickcar.presentation.dto.request.CycleStoreRequest;
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
    //Sub (or Consumer) => MQ <-> DB(혹은 애뮬레이터 도메인) 간의 소통

    private final CycleService cycleService;

    @PostMapping("/store")
    public void emulatorCycle(@RequestBody CycleStoreRequest request) {
        log.info("POST /api/v1/engine/cycle - CycleStoreRequest: {}", request);
        cycleService.store(request);
    }
}
