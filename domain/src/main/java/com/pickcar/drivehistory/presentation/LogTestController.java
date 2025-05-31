package com.pickcar.drivehistory.presentation;

import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/test")
public class LogTestController {

    @GetMapping("/log")
    public ResponseEntity<String> testLog() {
        log.trace("test log - {}", "trace");
        log.debug("test log - {}", "debug");
        log.info("test log : {}", "info");
        log.warn("test log - {}", "warn");
        log.error("test log - {}", "error");

        return ResponseEntity.ok("test log");
    }
}
