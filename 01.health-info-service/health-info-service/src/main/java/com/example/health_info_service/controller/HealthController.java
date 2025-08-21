package com.example.health_info_service.controller;
// 這是 /health API 的實作。它只會回傳一個包含 "status": "UP" 的 JSON 物件。


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@Tag(name = "健康檢查", description = "應用程式健康狀態檢查")
@RestController
@RequestMapping("/health")
public class HealthController {

    @Operation(summary = "服務健康狀態", description = "返回固定的服務狀態")
    @GetMapping
    public Map<String, String> health() {
        return Map.of("status", "UP");
    }
}