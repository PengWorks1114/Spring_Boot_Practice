//TodoUpdateRequest.java: 用於接收更新待辦事項的請求。
//資料傳輸物件 (DTO) 是一種簡單的 Java 類別，專門用來在應用程式的不同層之間傳輸資料。例如，從前端接收資料的請求物件，以及回傳給前端的回應物件。

package com.example.todolist.dto;

import jakarta.validation.constraints.Size;
import lombok.*;

@Getter @Setter
public class TodoUpdateRequest {
    // 可選更新，若提供則套用；未提供則保留
    @Size(min = 1, max = 100)
    private String title;
    private Boolean done;
}

//程式碼說明： 這個類別用於更新請求。title 和 done 都是可選的，
// 也就是說我們可以只更新其中一個欄位。如果 title 存在，@Size 註解會自動驗證它的長度。