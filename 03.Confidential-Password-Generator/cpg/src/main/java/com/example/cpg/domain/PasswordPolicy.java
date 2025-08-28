// 這個檔案用來封裝密碼產生的策略。它會將前端傳來的請求參數，轉化為一個簡單、易於服務層使用的策略物件。
// 這樣可以讓我們的程式碼更乾淨，將業務邏輯與資料傳輸分開。

package com.example.cpg.domain;

public final class PasswordPolicy {
    public final int length;
    public final boolean includeDigits;
    public final boolean includeSymbols;
    public final boolean excludeAmbiguous;

    public PasswordPolicy(int length, boolean includeDigits, boolean includeSymbols, boolean excludeAmbiguous) {
        this.length = length;
        this.includeDigits = includeDigits;
        this.includeSymbols = includeSymbols;
        this.excludeAmbiguous = excludeAmbiguous;
    }
}

// 程式碼解說
//
//public final class PasswordPolicy: 這個類別被宣告為 final，表示它不能被繼承。這是一個良好的設計習慣，可以確保這個策略物件的行為不會被意外修改。
//
//public final int length; 等...: 這些欄位都被宣告為 final，代表它們在物件建立後就不能被改變。這使得 PasswordPolicy 成為一個不可變（immutable）的物件，更安全、更易於在多個地方傳遞和使用。
//
//建構式：這個類別只有一個公開的建構式，用來接收 length、includeDigits、includeSymbols 和 excludeAmbiguous 等參數，並將它們賦值給對應的 final 欄位。