package com.example.notes.exception;

// 註解：自訂例外類別，用來表示密碼錯誤或驗證失敗
public class InvalidPassException extends RuntimeException {
    public InvalidPassException(String message) {
        super(message);
    }
}

//程式碼說明：這個類別很簡單，它繼承了 RuntimeException，這表示它是一個「非受檢例外」（unchecked exception）。這樣做的好處是，我們不需要在每個方法簽名中都加上 throws InvalidPassException，讓程式碼更簡潔。