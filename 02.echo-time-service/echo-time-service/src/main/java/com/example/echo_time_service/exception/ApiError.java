package com.example.echo_time_service.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
//根據設計書，我們的目標是將所有錯誤都轉換為一種稱為「Problem Details」的標準 JSON 格式。
//這個類別將定義我們錯誤回應的格式。
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiError {
    private String type;
    private String title;
    private int status;
    private String detail;
    private String instance;
    private Instant timestamp;
    private UUID traceId;
    private List<FieldError> errors;

    public static ApiError badRequest(String detail, List<FieldError> errors) {
        return ApiError.builder()
                .type("about:blank")
                .title("Bad Request")
                .status(HttpStatus.BAD_REQUEST.value())
                .detail(detail)
                .timestamp(Instant.now())
                .traceId(UUID.randomUUID())
                .errors(errors)
                .build();
    }

    public static ApiError internalServerError(String detail) {
        return ApiError.builder()
                .type("about:blank")
                .title("Internal Server Error")
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .detail(detail)
                .timestamp(Instant.now())
                .traceId(UUID.randomUUID())
                .build();
    }

    @Getter
    @Builder
    public static class FieldError {
        private final String field;
        private final String message;
    }
}

//        說明：
//
//
//
//        @Builder 讓建立 ApiError 物件變得更簡單，我們可以像蓋積木一樣組裝它。
//
//        @JsonInclude(JsonInclude.Include.NON_NULL) 確保 JSON 回應中不會包含 null 的欄位，使輸出更精簡。
//
//        badRequest() 和 internalServerError() 是兩個靜態工廠方法，幫助我們快速建立兩種常見的錯誤回應物件。
//
//        FieldError 內部類別用於儲存驗證錯誤的詳細資訊，例如是哪個欄位 (field) 出了問題，以及錯誤訊息 (message) 是什麼。
//
//        UUID.randomUUID() 是一個很實用的方法，用於為每次錯誤產生一個唯一的 traceId，方便我們在日誌中追蹤特定的請求。