package com.example.echo_time_service.dto;
//DTO（Data Transfer Object）是用來在不同層之間傳輸資料的簡單 Java 物件。
// 它們通常只包含欄位、Getter 和 Setter。我們使用 Lombok 依賴來簡化這一步驟。
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Lombok;
import lombok.Setter;

@Getter
@Setter
public class EchoRequest {
    @NotNull(message = "text must not be null")
    @Size(max = 2000, message = "text too long")
    private String text;

    @AssertTrue(message = "text must not be blank")
    public boolean isNotBlankAfterTrim() {
        return text != null && !text.trim().isEmpty();
    }
}

//說明：
//
//@Getter 和 @Setter 是 Lombok 提供的註解，它們會自動幫我們生成 getText() 和 setText() 方法。
//
//@NotNull 確保 text 欄位不是 null。
//
//@Size(max = 2000) 限制 text 的長度上限。
//
//@AssertTrue 是自定義驗證，用來確保 text 在去除前後空白後不是空字串。
//
