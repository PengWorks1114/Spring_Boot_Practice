// 這個類別代表 POST /shorten 請求成功後，回傳給客戶端的 JSON 格式。
package com.example.url_shortener.api;

public class ShortenResponse {

    private String code;
    private String shortUrl;
    private String expiresAt;

    public ShortenResponse(String code, String shortUrl, String expiresAt) {
        this.code = code;
        this.shortUrl = shortUrl;
        this.expiresAt = expiresAt;
    }

    public String getCode() {
        return code;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public String getExpiresAt() {
        return expiresAt;
    }
}