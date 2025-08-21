package com.example.health_info_service.model;
// 這個類別是用來定義 /info API 回應的資料格式。使用 Java Record 讓程式碼更簡潔。

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "版本資訊回應")
public record InfoResponse(
        @Schema(description = "應用程式名稱") String name,
        @Schema(description = "版本號") String version,
        @Schema(description = "建置時間") String buildTime) {
}