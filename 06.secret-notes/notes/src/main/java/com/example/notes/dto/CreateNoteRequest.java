//建立 DTO (Data Transfer Object)
//在軟體開發中，DTO（Data Transfer Object） 是用來在不同層之間傳遞資料的物件。在這個專案中，我們將使用它來定義 API 的請求 (Request) 和回應 (Response) 格式。
//
//使用 DTO 的好處是，我們可以將 API 的資料格式與資料庫的實體 (Note.java) 分開。這樣即使資料庫欄位有變動，也不會直接影響到 API 的使用者，讓系統更穩定。

package com.example.notes.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// 註解：建立記事請求
public record CreateNoteRequest(
        @NotBlank @Size(min=1,max=200) String title,
        @NotBlank String plainText,
        @NotBlank @Size(min=8,max=200) String pass
) {}

// 程式碼說明
//
//record: 從 Java 14 開始，record 是一種簡潔的類別，它會自動生成建構子、Getter、equals()、hashCode() 和 toString() 方法，非常適合用來做 DTO。
//
//@NotBlank: 這個註解來自 jakarta.validation，它確保接收到的 title、plainText 和 pass 欄位不能是空的或只包含空格。
//
//@Size: 這個註解用來限制字串的長度，確保 title 和 pass 符合設計書的要求。