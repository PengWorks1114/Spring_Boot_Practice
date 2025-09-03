//建立 Controller
//在 Spring Boot 應用程式中，Controller 扮演著「指揮中心」的角色，它負責接收客戶端發來的 HTTP 請求，並將它們導向正確的服務（NoteService）去處理。
//
//NoteController 的主要職責是：
//
//定義 API 端點，例如 /notes。
//
//處理 不同類型的 HTTP 請求（POST、GET）。
//
//驗證 請求中的資料。
//
//處理 服務層拋出的例外，並將其轉換為適當的 HTTP 回應碼和格式。

package com.example.notes.web;

import com.example.notes.dto.CreateNoteRequest;
import com.example.notes.dto.DecryptNoteResponse;
import com.example.notes.dto.NoteCreatedResponse;
import com.example.notes.service.NoteService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import com.example.notes.exception.InvalidPassException;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

@RestController
@RequestMapping("/notes")
public class NoteController {
    private final NoteService service;

    public NoteController(NoteService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<NoteCreatedResponse> create(@Valid @RequestBody CreateNoteRequest req) {
        NoteCreatedResponse res = service.create(req.title(), req.plainText(), req.pass());
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    // 題目指定：GET /notes/{id}?pass=...
    @GetMapping("/{id}")
    public ResponseEntity<DecryptNoteResponse> read(@PathVariable UUID id, @RequestParam("pass") String pass) {
        DecryptNoteResponse res = service.decrypt(id, pass);
        return ResponseEntity.ok(res);
    }

    // 簡易全域錯誤映射（亦可用 @ControllerAdvice）
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Map<String,Object>> handleNotFound(HttpServletRequest req, NoSuchElementException ex) {
        return problem(req, 404, "Not Found", ex.getMessage());
    }

    @ExceptionHandler({InvalidPassException.class})
    public ResponseEntity<Map<String,Object>> handleForbidden(HttpServletRequest req, Exception ex) {
        return problem(req, 403, "Forbidden", ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,Object>> handleBadRequest(HttpServletRequest req, MethodArgumentNotValidException ex) {
        return problem(req, 400, "Bad Request", "Invalid payload");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String,Object>> handleServer(HttpServletRequest req, Exception ex) {
        return problem(req, 500, "Internal Server Error", "Unexpected error");
    }

    private ResponseEntity<Map<String,Object>> problem(HttpServletRequest req, int status, String error, String message) {
        Map<String,Object> body = new LinkedHashMap<>();
        body.put("timestamp", Instant.now().toString());
        body.put("status", status);
        body.put("error", error);
        body.put("message", message);
        body.put("path", req.getRequestURI());
        return ResponseEntity.status(status).body(body);
    }
}

//程式碼說明
//@RestController: 這個註解結合了 @Controller 和 @ResponseBody，表示這個類別將處理 HTTP 請求並將回傳值自動轉換為 JSON/XML 格式。
//
//@RequestMapping("/notes"): 這個註解設定了所有方法的基本路徑。因此，create 方法的路徑是 /notes，read 方法的路徑是 /notes/{id}。
//
//create 方法：
//
//@PostMapping: 指定這是一個處理 POST 請求的方法。
//
//@Valid: 告訴 Spring 驗證 CreateNoteRequest 中的屬性，這會啟用我們在 DTO 中定義的 @NotBlank 和 @Size 註解。
//
//@RequestBody: 將 HTTP 請求的 JSON 主體轉換為 CreateNoteRequest 物件。
//
//ResponseEntity.status(HttpStatus.CREATED).body(res): 返回 HTTP 201 Created 狀態碼，並將 NoteCreatedResponse 物件作為回應主體。
//
//read 方法：
//
//@GetMapping("/{id}"): 指定這是一個處理 GET 請求的方法，且路徑中包含一個名為 id 的變數。
//
//@PathVariable UUID id: 從路徑變數中獲取 id 並自動轉換為 UUID 物件。
//
//@RequestParam("pass") String pass: 從 URL 查詢參數（?pass=...）中獲取 pass。
//
//錯誤處理 (@ExceptionHandler)：
//
//這些方法用來處理服務層拋出的例外。當 NoteService 拋出 NoSuchElementException 時，handleNotFound 會被呼叫，並返回 HTTP 404 Not Found 錯誤。
//
//類似地，AccessControlException 會返回 HTTP 403 Forbidden，而驗證失敗則會返回 HTTP 400 Bad Request。這確保了錯誤回應的格式統一，符合設計書的要求。
//
//