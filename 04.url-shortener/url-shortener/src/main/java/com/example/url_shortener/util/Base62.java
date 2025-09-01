// 實作 CodeGenerator。這個工具負責將一個長網址（URL）轉換成唯一的短碼（Code）。
// 設計書中提到，我們需要使用 Base62 編碼，並且在發生碰撞時能夠自動生成新的短碼。
//
//我們會使用 MurmurHash3 來作為雜湊演算法，因為它速度快且雜湊分佈均勻，很適合用來生成初始短碼。

package com.example.url_shortener.util;

public class Base62 {
    private static final String BASE62_CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public static String encode(long value) {
        StringBuilder sb = new StringBuilder();
        if (value == 0) {
            return "0";
        }
        while (value > 0) {
            sb.append(BASE62_CHARS.charAt((int) (value % 62)));
            value /= 62;
        }
        return sb.reverse().toString();
    }

    public static long decode(String str) {
        long value = 0;
        long power = 1;
        for (int i = str.length() - 1; i >= 0; i--) {
            int digit = BASE62_CHARS.indexOf(str.charAt(i));
            value += digit * power;
            power *= 62;
        }
        return value;
    }
}

//程式碼解說：
//
//BASE62_CHARS: 包含了所有 Base62 編碼會用到的 62 個字元。
//
//encode(long value): 將一個 long 型別的數值轉換為 Base62 字串。
//
//decode(String str): 將一個 Base62 字串轉換回 long 型別的數值。


//1) 功能目標（What & Why）
//
//任務：將任意長網址（URL 字串）透過雜湊與編碼，穩定（同一 URL 產生同一組候選碼）、短小（預設長度 7）、且均勻分布地轉成短碼（code）。
//
//要求：
//
//Base62：只用 [0-9A-Za-z]，適合出現在網址路徑（避免 +、/ 等保留字）。
//
//碰撞處理：不同 URL 偶爾可能得到相同候選碼 → 以 salt 疊代方式重算，直到無衝突。
//
//冪等：同一 URL 必須穩定回傳相同候選碼（在儲存層 urlToCode 層面保證重複縮短回同一 code）。

//2) 核心原理（How）
//
//雜湊：以 MurmurHash3 對 url（或 url + "#" + salt）計算 128-bit/64-bit 雜湊。
//
//轉無號數值：取得 64-bit 值並轉為非負（例如 hash & Long.MAX_VALUE）。
//
//Base62 編碼：用 Base62.encode(long) 將數值轉為由 [0-9A-Za-z] 組成的字串。
//
//長度正規化：為了固定長度（預設 7），對編碼結果 左側補零或取右側截斷。
//
//碰撞重試：若該短碼已被其他 URL 佔用，將 salt 加 1 後重算，直到產生新碼。
//
//你的 Base62.encode/decode 正是第 3 步的工具；但要注意它僅接受非負整數，所以在把雜湊結果丟進去之前，務必轉為非負值。

//3) 為何選 MurmurHash3
//
//分布均勻、速度快：非常適合在高流量情境快速生成候選碼。
//
//非密碼學：短碼不是安全憑證，採用快速非加密雜湊較合適。
//
//穩定性：相同輸入 → 相同輸出，支援冪等需求。
//
//Java 標準庫沒有內建 MurmurHash3，實務常用 Guava 的 Hashing.murmur3_128()；若不想引入依賴，可用 SHA-256 取前 8 bytes 當 64-bit 值（較慢，但無需外部套件）。