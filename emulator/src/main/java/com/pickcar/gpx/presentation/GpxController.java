package com.pickcar.gpx.presentation;

import com.pickcar.gpx.application.GpxService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class GpxController {

    private final GpxService gpxService;

    @GetMapping("/trackingcars")
    public ResponseEntity<?> startGpxStreaming() {
        try {
            String message = gpxService.startStreaming();
            return ResponseEntity.ok(Map.of("message", message));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("message", "스트리밍 시작 중 오류 발생: " + e.getMessage()));
        }
    }
}
