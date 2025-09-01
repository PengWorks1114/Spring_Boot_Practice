package com.example.url_shortener.api;

import com.example.url_shortener.model.Entry;
import com.example.url_shortener.service.ShortenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@RestController
public class ShortenController {

    private final ShortenService shortenService;

    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    public ShortenController(ShortenService shortenService) {
        this.shortenService = shortenService;
    }

    @PostMapping("/shorten")
    public ResponseEntity<ShortenResponse> shortenUrl(@Valid @RequestBody ShortenRequest request) {
        Optional<String> codeOpt = shortenService.shorten(request.getUrl(), request.getTtlSeconds());

        if (codeOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "E5001_INTERNAL_ERROR");
        }

        String code = codeOpt.get();
        Optional<Entry> entryOpt = shortenService.resolve(code);

        if (entryOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "E5001_INTERNAL_ERROR: Unable to retrieve newly created entry");
        }
        Entry entry = entryOpt.get();
        String shortUrl = baseUrl + "/r/" + code;
        String expiresAt = entry.getExpiresAt() != null ? entry.getExpiresAt().toString() : null;

        HttpStatus status = entry.getCreatedAt().equals(entry.getExpiresAt()) ? HttpStatus.CREATED : HttpStatus.OK;

        return new ResponseEntity<>(new ShortenResponse(code, shortUrl, expiresAt), status);
    }

    @GetMapping("/r/{code}")
    public ResponseEntity<Void> redirect(@PathVariable String code) {
        Optional<Entry> entryOpt = shortenService.resolve(code);

        if (entryOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "E4041_CODE_NOT_FOUND or E4101_CODE_EXPIRED");
        }

        Entry entry = entryOpt.get();

        // 建立 302 轉址回應
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(entry.getUrl()));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }
}

// 程式碼解說：
//
//@RestController: 這是 @Controller 和 @ResponseBody 的組合註解，表示這個類別處理 HTTP 請求並直接回傳 JSON 格式的資料。
//
//@PostMapping("/shorten"): 處理 POST 請求到 /shorten 路徑。
//
//@Valid @RequestBody ShortenRequest request:
//
//@RequestBody 告訴 Spring 框架將 HTTP 請求的 JSON 主體轉換為 ShortenRequest 物件。
//
//@Valid 則會觸發前面在 ShortenRequest 類別中定義的驗證規則（@NotNull, @Pattern, @Range）。如果驗證失敗，Spring 會自動回傳 400 Bad Request 狀態碼。
//
//shortenService.shorten(...): 呼叫我們在步驟 5 中定義的服務方法來生成短碼。
//
//回應狀態碼: 如果短碼已經存在（冪等性），我們回傳 200 OK，這與設計書中的描述一致。如果是新建立的，則回傳 201 Created。
//
//@GetMapping("/r/{code}"): 處理 GET 請求到 /r/{code} 路徑。{code} 是一個路徑變數。
//
//@PathVariable String code: 告訴 Spring 從路徑變數中提取短碼。
//
//ResponseEntity<Void>: 表示我們不需要回傳任何內容（Void），只需要設定回應標頭（Header）。
//
//HttpStatus.FOUND: 這是 HTTP 302 Found 狀態碼，用於轉址。
//
//HttpHeaders.setLocation(...): 設定 Location 標頭，指示瀏覽器要轉址到哪個 URL。