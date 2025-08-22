package com.example.echo_time_service.service;



import com.example.echo_time_service.dto.EchoRequest;
import com.example.echo_time_service.dto.EchoResponse;
import org.springframework.stereotype.Service;

import javax.swing.*;

@Service
public class EchoService {

    public EchoResponse echo(EchoRequest req) {
        final String original = req.getText();
        final int length = original.codePointCount(0, original.length());
        return new EchoResponse(original, length);
    }
}

//說明：
//
//@Service 註解告訴 Spring 這是一個服務元件，
//Spring 會自動管理它的生命週期。
//
//echo 方法接收一個 EchoRequest 物件，並返回一個 EchoResponse 物件。
//
//original.codePointCount(0, original.length()) 是用來計算字串長度的關鍵方法。
// 它能正確處理 Unicode 字符，例如 emoji 👨‍👩‍👧‍👦，確保長度計算不會出錯。