package com.example.url_shortener.service;

import com.example.url_shortener.model.Entry;
import com.example.url_shortener.store.InMemoryStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Iterator;
import java.util.Map;

@Component
public class TTLSweeper {

    private static final Logger logger = LoggerFactory.getLogger(TTLSweeper.class);
    private final InMemoryStore store;

    @Value("${app.sweep-interval-seconds:60}")
    private long sweepIntervalSeconds;

    public TTLSweeper(InMemoryStore store) {
        this.store = store;
    }

    @Scheduled(fixedRateString = "${app.sweep-interval-seconds:60}000")
    public void cleanupExpiredEntries() {
        logger.info("Starting TTL sweep to remove expired entries.");
        Instant now = Instant.now();
        int removedCount = 0;

        // 迭代 codeToEntry 並刪除過期項目
        Iterator<Map.Entry<String, Entry>> iterator = store.getCodeToEntry().entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Entry> mapEntry = iterator.next();
            Entry entry = mapEntry.getValue();

            // 檢查是否已過期
            if (entry.getExpiresAt() != null && entry.getExpiresAt().isBefore(now)) {
                // 找到過期項目，從 codeToEntry 和 urlToCode 兩個 Map 中移除
                store.remove(entry.getUrl(), entry.getCode());
                removedCount++;
            }
        }
        logger.info("TTL sweep completed. Removed {} expired entries.", removedCount);
    }
}

//程式碼解說：
//
//@Component: 將這個類別標記為 Spring 元件，讓 Spring 容器可以管理它。
//
//@Scheduled(fixedRateString = "${app.sweep-interval-seconds:60}000"):
//
//@Scheduled 註解表示這是一個排程方法。
//
//fixedRateString: 這告訴 Spring 任務會以固定的時間間隔（毫秒）重複執行。
//
//${app.sweep-interval-seconds:60}000: 我們從設定檔中讀取清理的間隔秒數，然後乘以 1000 轉換為毫秒。如果設定檔中沒有這個值，則使用預設值 60 秒。
//
//cleanupExpiredEntries() 方法:
//
//Instant.now(): 取得當前 UTC 時間，用於比對是否過期。
//
//Iterator: 我們使用 Iterator 來安全地遍歷 ConcurrentHashMap，並在遍歷過程中移除元素，這比傳統的 for 迴圈更安全，能避免 ConcurrentModificationException 錯誤。
//
//entry.getExpiresAt().isBefore(now): 檢查短碼的到期時間是否早於當前時間。
//
//store.remove(...): 如果過期，呼叫 InMemoryStore 的 remove 方法來清理資料。我們傳入 url 和 code，確保兩個 Map 中的資料都能被同步刪除。
//
//logger.info(...): 使用日誌記錄清理任務的開始和結束，這有助於我們觀察應用程式的行為。