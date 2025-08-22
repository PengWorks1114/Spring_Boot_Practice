package com.example.echo_time_service.controller;

import com.example.echo_time_service.dto.NowResponse;
import com.example.echo_time_service.service.TimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated // 啟用方法級別的驗證
public class TimeController {

    private final TimeService timeService;

    @GetMapping("/now")
    public ResponseEntity<NowResponse> now(@RequestParam(required = false) String tz) {
        return ResponseEntity.ok(timeService.now(tz));
    }
}

//說明：
//
//
//
//@GetMapping("/now") 將此方法對應到 /now 這個端點，並只處理 GET 請求。
//
//@RequestParam(required = false) 表示 tz 參數來自 URL 查詢字串，並且是可選的。
//
//@Validated 註解我們會在後續的錯誤處理中解釋，它與 ConstraintViolationException 相關。