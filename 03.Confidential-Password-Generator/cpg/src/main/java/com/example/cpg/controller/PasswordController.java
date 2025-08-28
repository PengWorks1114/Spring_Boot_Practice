// 這個類別將定義我們的 REST API 端點，也就是設計書中提到的 POST /passwords。

package com.example.cpg.controller;

import com.example.cpg.dto.PasswordRequest;
import com.example.cpg.dto.PasswordResponse;
import com.example.cpg.domain.PasswordPolicy;
import com.example.cpg.service.PasswordGeneratorService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/passwords")
public class PasswordController {

    private final PasswordGeneratorService service;

    public PasswordController(PasswordGeneratorService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<PasswordResponse> generate(@Valid @RequestBody PasswordRequest req) {
        int length = (req.length() == null) ? 12 : req.length();
        boolean digits = (req.digits() == null) ? true : req.digits();
        boolean symbols = (req.symbols() == null) ? true : req.symbols();
        boolean excludeAmbiguous = (req.excludeAmbiguous() == null) ? false : req.excludeAmbiguous();

        PasswordPolicy policy = new PasswordPolicy(length, digits, symbols, excludeAmbiguous);
        String pwd = service.generate(policy);
        return ResponseEntity.ok(new PasswordResponse(pwd));
    }
}

// 程式碼解說
//
//@RestController: 這個 Spring 註解是 @Controller 和 @ResponseBody 的組合。它告訴 Spring 這個類別是一個控制器，並且它的所有方法回傳的物件都應該直接序列化成 JSON 或 XML，然後作為 HTTP 響應的內容。
//
//@RequestMapping("/passwords"): 這將所有在這個類別中定義的 API 端點都前綴為 /passwords。因此，我們的 generate 方法將對應到 /passwords。
//
//private final PasswordGeneratorService service;: 我們在這裡注入了 PasswordGeneratorService。Spring Boot 會自動處理這個依賴注入 (Dependency Injection)，為我們提供一個 PasswordGeneratorServiceImpl 的實例。這就是為什麼我們要在 PasswordGeneratorServiceImpl 上使用 @Service 註解。
//
//@PostMapping: 這個註解是 @RequestMapping(method = RequestMethod.POST) 的簡寫，它將 generate 方法映射到 HTTP POST 請求。
//
//@Valid @RequestBody PasswordRequest req:
//
//@RequestBody: 告訴 Spring 將 HTTP 請求的 JSON 主體解析成一個 PasswordRequest 物件。
//
//@Valid: 這是一個非常重要的註解。它會觸發我們在 PasswordRequest 類別中定義的驗證規則（例如 length 的 @Min 和 @Max）。如果驗證失敗，Spring 會自動拋出例外，這使得我們的控制器程式碼非常乾淨，不需要手動檢查參數。
//
//預設值處理：設計書提到 digits、symbols 和 excludeAmbiguous 都是選填的，有預設值。這段程式碼檢查這些欄位是否為 null，如果為 null 就賦予預設值，確保 PasswordPolicy 總是有完整的參數。
//
//業務流程：
//
//將 PasswordRequest 的參數處理後，建立一個 PasswordPolicy 物件。
//
//呼叫 service.generate(policy) 方法，取得產生的密碼。
//
//建立一個 PasswordResponse 物件，並將密碼放入其中。
//
//使用 ResponseEntity.ok(...) 建立一個 HTTP 200 OK 的響應，並將 PasswordResponse 物件作為響應內容。