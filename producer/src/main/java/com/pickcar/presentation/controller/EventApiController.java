package com.pickcar.presentation.controller;

import com.pickcar.application.EventMessagePublisher;
import com.pickcar.dto.EventPayload;
import com.pickcar.dto.EventStatus;
import com.pickcar.security.principal.JwtUserDetails;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity<Void> emulatorEngineOn(@AuthenticationPrincipal JwtUserDetails userDetails, @RequestBody EventPayload eventPayload) {
        if (!EventStatus.ON.equals(eventPayload.getEventStatus())) {
            log.error("POST /api/v1/engine/on - EventPayload status NOT ON: {}", eventPayload);
            return ResponseEntity.badRequest().build();
        }
        eventMessagePublisher.publish(eventPayload, userDetails.getId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/engine/off")
    public ResponseEntity<Void> emulatorEngineOff(@AuthenticationPrincipal JwtUserDetails userDetails, @RequestBody EventPayload eventPayload) {
        if (!EventStatus.OFF.equals(eventPayload.getEventStatus())) {
            log.error("POST /api/v1/engine/off - EventPayload status NOT OFF: {}", eventPayload);
            return ResponseEntity.badRequest().build();
        }
        eventMessagePublisher.publish(eventPayload, userDetails.getId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/returned")
    public ResponseEntity<Void> emulatorEngineReturned(@AuthenticationPrincipal JwtUserDetails userDetails, @RequestBody EventPayload eventPayload) {
        if (!EventStatus.RETURNED.equals(eventPayload.getEventStatus())) {
            log.error("POST /api/v1/returned - EventPayload status NOT RETURNED : {}", eventPayload);
            return ResponseEntity.badRequest().build();
        }
        eventMessagePublisher.publish(eventPayload, userDetails.getId());
        return ResponseEntity.ok().build();
    }
    // TODO: 동일한 기능(on/off) 메서드 처리 고려
}
