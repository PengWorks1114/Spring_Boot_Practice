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