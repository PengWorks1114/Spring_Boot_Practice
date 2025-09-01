// ShortenService 是這個專案的核心業務邏輯層。
// 它將串接 CodeGenerator 來生成短碼，並使用 InMemoryStore 來儲存和查詢資料。
// 此外，它也會處理設計書中提到的冪等性、TTL (Time-to-Live) 和碰撞處理。

package com.example.url_shortener.service;

import com.example.url_shortener.model.Entry;
import com.example.url_shortener.store.InMemoryStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class ShortenService {

    private final CodeGenerator codeGenerator;
    private final InMemoryStore store;

    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    @Value("${app.default-ttl-seconds:2592000}")
    private long defaultTtlSeconds;

    public ShortenService(CodeGenerator codeGenerator, InMemoryStore store) {
        this.codeGenerator = codeGenerator;
        this.store = store;
    }

    public Optional<String> shorten(String url, Long ttlSeconds) {
        // 檢查是否已存在，處理冪等性
        String existingCode = store.getCode(url);
        if (existingCode != null) {
            return Optional.of(existingCode);
        }

        // 處理碰撞，自動重新生成
        int salt = 0;
        String code;
        do {
            code = codeGenerator.generate(url, salt);
            // 嘗試將新的 url 和 code 存入 store
            Entry existingEntry = store.getEntry(code);
            if (existingEntry == null) {
                // 成功存入，無碰撞
                break;
            } else if (existingEntry.getUrl().equals(url)) {
                // 發生極低機率的碰撞，但與現有 URL 相同，直接使用現有 code
                return Optional.of(code);
            } else {
                // 發生碰撞，但 URL 不同，增加 salt 重新生成
                salt++;
            }
        } while (true);

        // 計算到期時間
        Instant now = Instant.now();
        Instant expiresAt = null;
        if (ttlSeconds != null) {
            expiresAt = now.plusSeconds(ttlSeconds);
        } else {
            expiresAt = now.plusSeconds(defaultTtlSeconds);
        }

        // 建立 Entry 並儲存
        Entry newEntry = new Entry(code, url, now, expiresAt);
        store.save(url, code, newEntry);

        return Optional.of(code);
    }

    public Optional<Entry> resolve(String code) {
        Entry entry = store.getEntry(code);

        if (entry == null) {
            return Optional.empty(); // 找不到短碼
        }

        // 檢查是否過期
        if (entry.getExpiresAt() != null && entry.getExpiresAt().isBefore(Instant.now())) {
            // 已過期，從 store 移除並回傳 empty
            store.remove(entry.getUrl(), entry.getCode());
            return Optional.empty();
        }

        return Optional.of(entry);
    }
}

//程式碼解說：
//
//@Service: 這是一個特殊的 @Component，用於標記業務邏輯層。
//
//依賴注入 (Dependency Injection): ShortenService 的建構子 (Constructor) 接收 CodeGenerator 和 InMemoryStore 作為參數。Spring 容器會自動建立這些實例並傳遞給 ShortenService，這是一種最佳實踐，稱為依賴注入。
//
//@Value: 我們使用 @Value 註解從 application.properties 或環境變數中讀取設定值，這使得我們的程式碼更具彈性。例如：${app.base-url:http://localhost:8080} 表示如果找不到 app.base-url 這個設定，就使用預設值 http://localhost:8080。
//
//shorten(String url, Long ttlSeconds) 方法:
//
//冪等性處理: 在開始生成短碼前，我們首先檢查 urlToCode 中是否已存在這個 URL。如果存在，直接回傳對應的短碼，而不做任何操作。這確保了同一個 URL 每次縮短都會得到相同的短碼。
//
//碰撞處理: 我們使用 do-while 迴圈來處理短碼碰撞。
//
//呼叫 codeGenerator.generate(url, salt) 生成初始短碼。
//
//檢查 codeToEntry 中是否已存在這個短碼。
//
//如果短碼不存在，代表沒有碰撞，我們跳出迴圈。
//
//如果短碼存在，但對應的原始 URL 和當前 URL 相同，這表示我們在極低機率下再次生成了相同的短碼，這時依然是冪等操作，我們直接回傳這個短碼。
//
//如果短碼存在，且對應的原始 URL 不同，表示發生碰撞。我們將 salt 遞增，重新生成新的短碼，並繼續迴圈。
//
//resolve(String code) 方法:
//
//這個方法負責根據短碼查詢原始網址。
//
//它首先從 codeToEntry 中查找 Entry 物件。
//
//如果找不到，回傳 Optional.empty()。
//
//如果找到了，它會檢查 expiresAt 是否過期。如果已過期，它會從兩個 Map 中移除這個項目，並回傳 Optional.empty()，符合設計書中**「逾期後短碼作廢」**的需求。