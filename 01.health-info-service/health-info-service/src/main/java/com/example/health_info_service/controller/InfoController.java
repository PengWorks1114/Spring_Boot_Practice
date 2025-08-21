package com.example.health_info_service.controller;
// 這是 /info API 的實作。它會從 application.yml
// 讀取 name、version 和 buildTime 的值，並回傳。如果任何一個值不存在，它會拋出一個例外。

import com.example.health_info_service.model.InfoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "版本資訊", description = "應用程式版本資訊查詢")
@RestController
@RequestMapping("/info")
@RequiredArgsConstructor
public class InfoController {

    @Value("${app.name}")
    private String name;
    @Value("${app.version}")
    private String version;
    @Value("${app.buildTime}")
    private String buildTime;

    @Operation(summary = "版本資訊", description = "返回應用程式名稱、版本與建置時間")
    @GetMapping
    public InfoResponse info() {
        if (name == null || version == null || buildTime == null) {
            throw new IllegalStateException("Config missing");
        }
        return new InfoResponse(name, version, buildTime);
    }
}