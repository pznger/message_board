package com.example.demo.service;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
public class WeatherService {

    private static final String APP_KEY = "204764212";
    private static final String APP_SECRET = "Uj2oOy3zQ18KUYJZMlSvvobBLrwYTSH8";

    public Mono<String> weather(String city) {
        WebClient webClient = WebClient.create();

        // 生成签名
        String sign = generateSign(APP_KEY, APP_SECRET, city);

        return webClient.get()
                .uri("https://route.showapi.com/9-2", uriBuilder -> uriBuilder
                        .queryParam("showapi_appid", APP_KEY)
                        .queryParam("showapi_sign", sign)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class);
    }

    private String generateSign(String appKey, String appSecret, String area) {
        String signStr = "showapi_appid=" + appKey + "&showapi_sign=" + appSecret + "&area=" + area;
        try {
            return Base64.getEncoder().encodeToString(signStr.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new RuntimeException("生成签名失败", e);
        }
    }
}