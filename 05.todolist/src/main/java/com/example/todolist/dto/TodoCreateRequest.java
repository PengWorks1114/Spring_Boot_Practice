//TodoCreateRequest.java: 用於接收建立待辦事項的請求。
//資料傳輸物件 (DTO) 是一種簡單的 Java 類別，專門用來在應用程式的不同層之間傳輸資料。例如，從前端接收資料的請求物件，以及回傳給前端的回應物件。

package com.example.todolist.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter @Setter
public class TodoCreateRequest {
    // 標題必填，限制 1..100
    @NotBlank @Size(min = 1, max = 100)
    private String title;
}

//程式碼說明： 這個類別只包含一個 title 欄位。
//
// 上面的 @NotBlank 和 @Size 是 Validation 框架的註解，它們會自動幫我們驗證傳入的資料。
//
//@NotBlank: 確保 title 不能為 null 或空白字串。
//
//@Size: 確保 title 的長度介於 1 到 100 個字元之間。