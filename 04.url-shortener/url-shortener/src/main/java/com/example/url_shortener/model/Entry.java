// 建立 Entry 資料模型
//這個類別將用於儲存短碼與原始網址的對應關係。
// 它包含了設計書中提到的 code、url、createdAt 和 expiresAt。

package com.example.url_shortener.model;


import java.time.Instant;

public class Entry {
    private String code;
    private String url;
    private Instant createdAt;
    private Instant expiresAt;

    public Entry(String code, String url, Instant createdAt, Instant expiresAt) {
        this.code = code;
        this.url = url;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
    }

    public String getCode() {
        return code;
    }

    public String getUrl() {
        return url;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }
}


//程式碼解說：
//
//package com.example.urlshortener.model;: 宣告這個類別屬於 com.example.urlshortener.model 套件。
//
//private String code;: 短網址的短碼。
//
//private String url;: 原始的長網址。
//
//private Instant createdAt;: 建立這個短碼的時間。
// 我們使用 Instant 來處理時間，這是一個精確、無時區的 UTC 時間點，非常適合記錄事件發生時刻。
//
//private Instant expiresAt;: 短碼的到期時間。如果為 null，表示永不過期。
//
//建構子 (Constructor): 負責在建立 Entry 物件時初始化這些屬性。
//
//get 方法 (Getter): 讓我們能夠從外部安全地讀取這些屬性。