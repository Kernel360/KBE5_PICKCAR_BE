package com.pickcar.presentation.controller;

import com.pickcar.application.EventMessagePublisher;
import com.pickcar.dto.EventPayload;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Void> emulatorEngineOn(@RequestBody EventPayload eventPayload) {
        log.info("POST /api/v1/engine/on - EventPayload: {}", eventPayload);
        // HACK: 상태코드 체크는 임시 방편. 상태 enum(ON, OFF, RETURNED) 생기면 교체 필요
        if (!eventPayload.getStatus()) {
            log.error("POST /api/v1/engine/on - EventPayload status false: {}", eventPayload);
            return ResponseEntity.badRequest().build();
        }
        eventMessagePublisher.publish(eventPayload);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/engine/off")
    public ResponseEntity<Void> emulatorEngineOff(@RequestBody EventPayload eventPayload) {
        log.info("POST /api/v1/engine/off - EventPayload: {}", eventPayload);
        // HACK: 상태코드 체크는 임시 방편. 상태 enum(ON, OFF, RETURNED) 생기면 교체 필요
        if (eventPayload.getStatus()) {
            log.error("POST /api/v1/engine/off - EventPayload status true: {}", eventPayload);
            return ResponseEntity.badRequest().build();
        }
        eventMessagePublisher.publish(eventPayload);
        return ResponseEntity.ok().build();
    }
    // TODO: 동일한 기능(on/off) 메서드 처리 고려
}
