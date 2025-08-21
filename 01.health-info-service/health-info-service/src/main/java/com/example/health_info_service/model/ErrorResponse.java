package com.example.health_info_service.model;
// 這是統一錯誤回應的資料格式。

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Map;

@Schema(description = "標準錯誤回應")
public record ErrorResponse(
        @Schema(description = "是否成功，固定為 false") boolean success,
        @Schema(description = "錯誤物件") Map<String, Object> error) {
}