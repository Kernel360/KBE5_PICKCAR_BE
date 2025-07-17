package com.pickcar.emulator.presentation;

import com.pickcar.emulator.application.CycleInfoService;
import com.pickcar.emulator.application.EventInfoService;
import com.pickcar.emulator.dto.CyclePayload;
import com.pickcar.emulator.dto.EventPayload;
import com.pickcar.security.principal.JwtUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
public class ConsumerController {

    private final EventInfoService eventInfoService;
    private final CycleInfoService cycleInfoService;

    @PostMapping("/event/engine/on")
    public void emulatorEngineOn(@RequestBody EventPayload eventPayload) {
        eventInfoService.on(eventPayload);
    }

    @PostMapping("/cycle")
    public void cycle(@RequestBody CyclePayload cyclePayload) {
        cycleInfoService.cycle(cyclePayload);
    }

    @PostMapping("/event/engine/off")
    public void emulatorEngineOff(@RequestBody EventPayload eventPayload,
                                  @AuthenticationPrincipal JwtUserDetails userDetails) {
        eventInfoService.off(eventPayload, userDetails.getId());
    }

    @PostMapping("/event/returned")
    public void emulatorEngineReturned(@RequestBody EventPayload eventPayload,
                                       @AuthenticationPrincipal JwtUserDetails userDetails) {
        eventInfoService.returned(eventPayload, userDetails.getId());
    }

}
