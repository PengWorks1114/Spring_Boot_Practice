//全域錯誤處理 是一個集中的機制，可以捕捉和處理應用程式中拋出的特定類型錯誤。這樣做的好處是，我們不需要在每個控制器方法中都寫一堆 try-catch 區塊，程式碼會更簡潔、易於維護，同時還能確保所有錯誤都以一致的格式回傳。
//
//依照設計書，這個類別會處理三種常見錯誤：
//
//MethodArgumentNotValidException: 當資料驗證（例如在 DTO 中使用 @Valid）失敗時拋出，回傳 400 Bad Request。
//
//IllegalArgumentException: 當業務邏輯檢查到不合法的參數（例如找不到待辦事項）時拋出，根據訊息回傳 400 Bad Request 或 404 Not Found。
//
//Exception: 捕捉其他未知的伺服器錯誤，回傳 500 Internal Server Error。

package com.example.todolist.exception;

import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        String msg = ex.getBindingResult().getFieldErrors().stream()
                .findFirst().map(f -> f.getField() + " " + f.getDefaultMessage()).orElse("Validation error");
        return ResponseEntity.badRequest().body(Map.of("error", msg));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArg(IllegalArgumentException ex) {
        String msg = ex.getMessage();
        HttpStatus status = "Todo not found".equals(msg) ? HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(Map.of("error", msg));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Internal server error"));
    }
}

//程式碼說明：
//
//@RestControllerAdvice: 這個註解告訴 Spring 這個類別是一個全域控制器建議（Global Controller Advice）。它會捕捉應用程式中所有控制器拋出的特定例外。
//
//@ExceptionHandler(MethodArgumentNotValidException.class): 這個註解會捕捉所有控制器方法中拋出的 MethodArgumentNotValidException 錯誤，並執行 handleValidation 方法。它會從錯誤中提取第一個錯誤訊息，並回傳 400 Bad Request 狀態碼和錯誤訊息。
//
//@ExceptionHandler(IllegalArgumentException.class): 這個方法會處理我們在 Service 層中拋出的 IllegalArgumentException。它會根據錯誤訊息判斷是「找不到資料」 (Todo not found) 還是其他非法參數，並分別回傳 404 Not Found 或 400 Bad Request。
//
//@ExceptionHandler(Exception.class): 這是一個廣泛捕捉的方法，它會處理所有其他未被前面方法捕捉到的錯誤，並統一回傳 500 Internal Server Error。這是一種保護機制，可以避免將內部錯誤的細節暴露給前端。