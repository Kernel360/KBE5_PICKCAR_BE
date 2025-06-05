package com.pickcar.presentation.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pickcar.application.CycleInfoService;
import com.pickcar.presentation.dto.request.CycleInfoRequest;
import java.io.IOException;
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
    public void emulatorCycle(@RequestBody CycleInfoRequest request) throws IOException {
        log.info("POST /api/v1/engine/cycle - CycleInfoRequest: {}", request);
        cycleInfoService.cycle(request);
    }
}
