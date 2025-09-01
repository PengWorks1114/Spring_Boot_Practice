//我們需要用兩個 ConcurrentHashMap 來儲存資料，以實現高效且執行緒安全的讀寫操作。這兩個 Map 分別是：
//
//codeToEntry: 儲存短碼與 Entry 物件的對應關係。
//
//urlToCode: 儲存原始網址與其對應短碼的關係，這對於處理冪等（Idempotency）非常重要

package com.example.url_shortener.store;

import com.example.url_shortener.model.Entry;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class InMemoryStore {

    // 儲存短碼 -> 完整 Entry 物件
    private final ConcurrentMap<String, Entry> codeToEntry = new ConcurrentHashMap<>();

    // 儲存原始 URL -> 短碼 (用於快速查找與處理冪等性)
    private final ConcurrentMap<String, String> urlToCode = new ConcurrentHashMap<>();

    public void save(String url, String code, Entry entry) {
        urlToCode.put(url, code);
        codeToEntry.put(code, entry);
    }

    public String getCode(String url) {
        return urlToCode.get(url);
    }

    public Entry getEntry(String code) {
        return codeToEntry.get(code);
    }

    public void remove(String url, String code) {
        urlToCode.remove(url, code);
        codeToEntry.remove(code);
    }

    public ConcurrentMap<String, Entry> getCodeToEntry() {
        return codeToEntry;
    }
}

//程式碼解說：
//
//@Component: 和 CodeGenerator 一樣，這標示它是一個 Spring 元件，讓 Spring 容器可以管理它。
//
//ConcurrentHashMap: 這是一種執行緒安全的 Map 實作。它允許在高併發的環境下，
// 同時有多個執行緒進行讀寫操作，而不需要額外的鎖定，這對於我們的應用程式效能至關重要。
//
//codeToEntry 和 urlToCode: 兩個 final 的 ConcurrentMap 物件，它們將在 InMemoryStore 實例化時被建立。
//
//save(url, code, entry): 這個方法將新的短網址資料同時存入兩個 Map 中，確保資料的一致性。
//
//getCode(url): 透過原始網址快速查詢對應的短碼。這對於處理冪等性（相同網址重複縮短時，回傳相同短碼）非常有效率。
//
//getEntry(code): 透過短碼查詢完整的 Entry 物件，用於轉址時獲取原始網址和檢查有效期限。
//
//remove(url, code): 刪除過期或無效的資料