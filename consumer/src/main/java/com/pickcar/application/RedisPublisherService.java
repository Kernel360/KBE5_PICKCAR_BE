package com.pickcar.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisPublisherService {

    private final RedisTemplate<String, Object> redisTemplate;

    public void publish(String channel, Object message) {
        log.info("게시 채널 : {}", channel);
        redisTemplate.convertAndSend(channel, message);
    }

}
