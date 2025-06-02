package com.pickcar.presentation.controller;

import com.pickcar.application.EmulatorService;
import com.pickcar.presentation.dto.request.EmulatorRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class EmulatorApiController {

    EmulatorService emulatorService;

    @PostMapping("/terminal")
    public void emulator(@RequestBody EmulatorRequest request) {
        log.info("POST /api/v1/engine - EmulatorRequest: {}", request);
        emulatorService.terminal(request);
    }

}
