package com.example.health_info_service.advice;
//這個類別用於集中處理所有例外。當應用程式發生未預期的錯誤時，
//它會捕捉並回傳一個標準的錯誤格式。

import com.example.health_info_service.model.ErrorResponse;
import io.swagger.v3.oas.annotations.Hidden;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Hidden // 在 Swagger 文件中隱藏這個類別
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handle(Exception ex) {
        String traceId = MDC.get("traceId");
        Map<String, Object> err = new HashMap<>();
        err.put("code", "INTERNAL_ERROR");
        err.put("message", Optional.ofNullable(ex.getMessage()).orElse("Unexpected error"));
        err.put("traceId", traceId);

        // 記錄錯誤日誌，包含 traceId 便於追蹤
        System.err.println("Unexpected error with traceId: " + traceId + " - " + ex.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(false, err));
    }
}
