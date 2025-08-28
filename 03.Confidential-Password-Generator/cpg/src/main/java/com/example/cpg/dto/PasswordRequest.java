package com.example.cpg.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record PasswordRequest(
        @NotNull(message = "length is required")
        @Min(value = 6, message = "length must be between 6 and 64")
        @Max(value = 64, message = "length must be between 6 and 64")
        Integer length,
        Boolean digits,
        Boolean symbols,
        Boolean excludeAmbiguous
) {}

// 程式碼解說
//
//package com.example.cpg.dto;: 這一行定義了這個檔案所屬的 package，確保它在專案中的位置正確。
//
//public record PasswordRequest(...): 這裡使用了 Java 17 的 record 語法。record 是一種簡潔的類別，專門用來存放不可變的資料。它會自動生成建構式、Getter 方法、equals()、hashCode() 和 toString()，大大減少了程式碼的長度，非常適合用在像這樣單純的資料傳輸物件。
//
//@NotNull, @Min, @Max: 這些是 Spring Boot 的 驗證（Validation） 註解。它們會自動檢查前端傳入的 length 參數。
//
//@NotNull: 確保 length 欄位不能是空的。
//
//@Min(value = 6, ...): 確保 length 的值至少為 6。
//
//@Max(value = 64, ...): 確保 length 的值最多為 64。
//
//如果驗證失敗，Spring 會自動回傳 400 Bad Request 錯誤，並帶上我們在 message 屬性中定義的文字，這完美符合設計書的需求。