//我們已經有了處理核心邏輯的 ShortenService，現在需要建立一個 API 介面，
// 讓外部可以透過 HTTP 請求來使用這個服務。
// ShortenController 將負責處理所有的 HTTP 請求，並呼叫 ShortenService 來執行實際的業務邏輯。

package com.example.url_shortener.api;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Range;

public class ShortenRequest {

    @NotNull(message = "URL cannot be null")
    @Pattern(regexp = "^(http|https)://.*", message = "URL must start with http or https")
    private String url;

    @Range(min = 1, max = 7776000, message = "TTL must be between 1 and 7,776,000 seconds")
    private Long ttlSeconds;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getTtlSeconds() {
        return ttlSeconds;
    }

    public void setTtlSeconds(Long ttlSeconds) {
        this.ttlSeconds = ttlSeconds;
    }
}


// 程式碼解說：
//
//@NotNull: 確保 url 欄位不能是空的。
//
//@Pattern: 這是 Java jakarta.validation 提供的功能，我們使用正規表達式來驗證 URL 必須以 http:// 或 https:// 開頭，符合設計書的安全要求。
//
//@Range: 限制 ttlSeconds 的數值必須在 1 到 7,776,000 秒之間，符合設計書中**「TTL 非正整數或超過上限」**的驗證需求。