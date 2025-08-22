package com.example.echo_time_service.controller;

import com.example.echo_time_service.dto.EchoRequest;
import com.example.echo_time_service.dto.EchoResponse;
import com.example.echo_time_service.service.EchoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EchoController {

    private final EchoService echoService;

    @PostMapping("/echo")
    public ResponseEntity<EchoResponse> echo(@Valid @RequestBody EchoRequest req) {
        return ResponseEntity.ok(echoService.echo(req));
    }
}

//說明：
//
//
//
//@RestController 結合了 @Controller 和 @ResponseBody 的功能，表示這個類別會處理 HTTP 請求，
// 並將方法的返回值直接轉換為 JSON 回應。
//
//@RequiredArgsConstructor 是 Lombok 提供的註解，它會自動為 final 欄位生成一個帶有參數的建構子，
// 並注入 EchoService。這是一種比 @Autowired 更好的依賴注入方式。
//
//@PostMapping("/echo") 將此方法對應到 /echo 這個端點，並只處理 POST 請求。
//
//@RequestBody 告訴 Spring 將 HTTP 請求的 JSON 主體 (body) 轉換為 EchoRequest 物件。
//
//@Valid 是非常重要的一個註解！它會觸發我們在 EchoRequest 上定義的所有驗證規則。