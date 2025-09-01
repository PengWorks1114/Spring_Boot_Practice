package com.example.url_shortener.service;

import com.google.common.hash.Hashing;
import com.example.url_shortener.util.Base62;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class CodeGenerator {

    private static final int SHORT_CODE_LENGTH = 7;

    public String generate(String url, int salt) {
        // 使用 MurmurHash3_32 雜湊演算法
        long hash = Hashing.murmur3_32_fixed()
                .hashString(url + salt, StandardCharsets.UTF_8)
                .padToLong();

        // 將雜湊值轉換為 Base62 字串
        String base62Encoded = Base62.encode(hash);

        // 確保短碼長度為 7
        return base62Encoded.substring(0, Math.min(base62Encoded.length(), SHORT_CODE_LENGTH));
    }
}

//程式碼解說：
//
//@Component: 這是 Spring Framework 的註解，表示這個類別是一個 Spring 元件，讓 Spring 能夠自動建立和管理這個類別的實例。
//
//generate(String url, int salt): 這是核心方法。它接收兩個參數：url 和一個 salt（鹽值）。
//
//url + salt: 我們將原始 URL 和 salt 結合在一起。當發生碰撞時，我們會增加 salt 的值，讓同一個 URL 產生不同的雜湊值，進而生成不同的短碼，避免衝突。
//
//Hashing.murmur3_32_fixed(): 這是 Google Guava 函式庫提供的 MurmurHash3 雜湊函式。
//
//Base62.encode(hash): 將雜湊值轉換為 Base62 字串。
//
//substring(...): 最後，我們截取 Base62 字串的前 7 個字元作為最終的短碼。