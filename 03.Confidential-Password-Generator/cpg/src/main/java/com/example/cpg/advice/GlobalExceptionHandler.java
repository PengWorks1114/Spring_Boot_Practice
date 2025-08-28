// 這個類別將集中處理所有控制器中未被捕捉的例外，例如驗證失敗的錯誤或是業務邏輯的例外。

package com.example.cpg.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleBadRequest(IllegalArgumentException ex, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                "timestamp", Instant.now().toString(),
                "path", req.getRequestURI(),
                "error", "VALIDATION_ERROR",
                "message", ex.getMessage()
        ));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, Object>> handleUnprocessable(IllegalStateException ex, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(Map.of(
                "timestamp", Instant.now().toString(),
                "path", req.getRequestURI(),
                "error", "UNPROCESSABLE",
                "message", ex.getMessage()
        ));
    }
}

// 程式碼解說
//
//@RestControllerAdvice: 這個註解讓這個類別能夠處理所有 @RestController 類別中拋出的例外。它就像一個全域的守衛，可以集中處理整個應用程式的錯誤。
//
//@ExceptionHandler(...): 這個註解標記了一個方法，用來捕捉和處理特定的例外類型。
//
//handleBadRequest 方法: 它會捕捉 IllegalArgumentException。我們在 PasswordGeneratorServiceImpl 中，當密碼長度不符合要求時，就是拋出這個例外。這個方法會將 HTTP 狀態碼設為 400 BAD_REQUEST，並回傳一個包含時間戳、路徑、錯誤類型和錯誤訊息的 JSON 物件，這完全符合設計書的要求。
//
//handleUnprocessable 方法: 它會捕捉 IllegalStateException。我們在服務層中，當字元池因移除易混淆字而導致無可用字元時，就是拋出這個例外。這個方法會將 HTTP 狀態碼設為 422 UNPROCESSABLE_ENTITY，回傳一個標準化的錯誤訊息。