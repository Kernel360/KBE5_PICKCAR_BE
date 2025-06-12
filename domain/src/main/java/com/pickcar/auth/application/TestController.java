package com.pickcar.auth.application;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test")
@RequiredArgsConstructor
public class TestController {

    private final JwtProvider jwtProvider;

    @GetMapping
    public void test() {
        String secret = jwtProvider.getSecret();
        System.out.println("secret = " + secret);
    }
}
