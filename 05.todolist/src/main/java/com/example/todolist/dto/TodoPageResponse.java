//TodoPageResponse.java: 用於回傳分頁後的待辦事項列表。
//資料傳輸物件 (DTO) 是一種簡單的 Java 類別，專門用來在應用程式的不同層之間傳輸資料。例如，從前端接收資料的請求物件，以及回傳給前端的回應物件。

package com.example.todolist.dto;

import java.util.List;
import java.time.LocalDateTime;

public record TodoPageResponse(
        List<Item> content, int page, int size, long totalElements, int totalPages) {
    public record Item(Long id, String title, boolean done, LocalDateTime createdAt) {}
}

//程式碼說明： 這是一個 Java 14 新增的 record 類型。record 是一種更精簡的類別，
// 它會自動生成所有欄位的 getters、equals、hashCode 和 toString 方法。
// 這個 DTO 用於回傳分頁查詢的結果，包含內容列表、頁碼、大小等資訊。
// Item 則是回傳列表中每個待辦事項的精簡版資料。
