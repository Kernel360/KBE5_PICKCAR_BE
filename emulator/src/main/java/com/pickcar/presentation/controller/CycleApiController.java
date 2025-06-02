package com.pickcar.presentation.controller;

import com.pickcar.application.CycleInfoService;
import com.pickcar.presentation.dto.request.CycleInfoRequest;
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

    private final CycleInfoService cycleInfoService;

    @PostMapping
    public void emulatorCycle(@RequestBody CycleInfoRequest request) {
        log.info("POST /api/v1/engine/cycle - CycleInfoRequest: {}", request);
        cycleInfoService.cycle(request);
    }
}
