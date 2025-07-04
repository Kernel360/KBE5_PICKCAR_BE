package com.pickcar.gpx.presentation;

import com.pickcar.gpx.application.SseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/sse")
public class SseController {
    private final SseService sseService;

    // TODO : PostMapping 고려해야하려나
    @GetMapping("/connect")
    public SseEmitter handleConnect(@RequestParam Long vehicleId) {
        log.info("SSE 연결 요청: vehicleId = {}", vehicleId);
        return sseService.createSseEmitter(vehicleId);
    }

    @DeleteMapping("/disconnect")
    public void handleDisconnectAll(@RequestParam Long vehicleId) {
        log.info("SSE 연결 닫기: vehicleId = {}", vehicleId);
        sseService.deleteSseEmitter(vehicleId);
    }

    @GetMapping("/count")
    public int getSseConnectionCount() {
        return sseService.processSseConnectionCount();
    }
}
