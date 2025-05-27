package com.pickcar.presentation;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/emulator")
public class EmulatorController {

    @GetMapping
    public String emulator() {
        return "index";
    }
}
