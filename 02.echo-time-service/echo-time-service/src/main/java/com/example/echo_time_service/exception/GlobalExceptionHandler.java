package com.example.echo_time_service.exception;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
// 這個類別是整個錯誤處理機制的核心。
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        List<ApiError.FieldError> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> new ApiError.FieldError(fe.getField(), fe.getDefaultMessage()))
                .toList();
        return ApiError.badRequest("Validation failed for request body", fieldErrors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleConstraintViolation(ConstraintViolationException ex) {
        List<ApiError.FieldError> errors = ex.getConstraintViolations().stream()
                .map(v -> new ApiError.FieldError(v.getPropertyPath().toString(), v.getMessage()))
                .toList();
        return ApiError.badRequest("Constraint violation on query parameter", errors);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleIllegalArgument(IllegalArgumentException ex) {
        return ApiError.badRequest(ex.getMessage(), List.of());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleOthers(Exception ex) {
        return ApiError.internalServerError("An unexpected error occurred");
    }
}

//        說明：
//
//
//
//        @RestControllerAdvice 是一個非常強大的註解，它讓這個類別能夠處理所有 @RestController 類別中拋出的例外。
//
//        @ExceptionHandler 註解告訴 Spring 這個方法應該處理哪種特定類型的例外。
//
//        @ResponseStatus 註解設定 HTTP 回應的狀態碼。
//
//        MethodArgumentNotValidException: 這是當 @Valid 註解驗證 @RequestBody 失敗時會拋出的例外。我們捕捉它，並將所有欄位錯誤 (fieldErrors) 收集起來，放入我們的 ApiError 物件中。
//
//        ConstraintViolationException: 這是當 @RequestParam 或 @PathVariable 上的驗證失敗時會拋出的例外。我們在 TimeController 類別上加上 @Validated 就是為了能讓這個例外被拋出並被這裡捕捉到。
//
//        IllegalArgumentException: 這是我們在 TimeService 中針對無效時區拋出的例外。我們捕捉它並返回 400 Bad Request。
//
//        Exception.class: 這是一個通用的處理器，它會捕捉所有未被其他 @ExceptionHandler 處理的例外。這確保了即使發生我們沒有預料到的錯誤，API 也能返回一個統一的 500 Internal Server Error 回應，並且不會暴露敏感的內部錯誤訊息。