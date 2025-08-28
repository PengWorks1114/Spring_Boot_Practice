package com.example.cpg.dto;

public record PasswordResponse(String password) {}

// 程式碼解說
//
//public record PasswordResponse(...): 我們再次使用了 record 語法。
//
//String password: 這個 record 只有一個欄位，用來存放我們產生的密碼字串。當後端成功產生密碼後，會將密碼封裝到這個物件中回傳。