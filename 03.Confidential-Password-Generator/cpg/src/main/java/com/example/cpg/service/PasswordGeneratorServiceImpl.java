// (實作)
//這個類別是 PasswordGeneratorService 介面的具體實現，包含了密碼產生的所有邏輯。這部分程式碼比較長，但設計書已經提供了詳細的演算法，我們只需要將其轉換為 Java 程式碼即可。

package com.example.cpg.service;

import com.example.cpg.domain.PasswordPolicy;
import org.springframework.stereotype.Service;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PasswordGeneratorServiceImpl implements PasswordGeneratorService {
    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";
    private static final String SYMBOLS = "!@#$%^&*()-_=+[]{};:,.?";
    private static final String AMBIGUOUS = "0Oo1lI5S2Z8B|/\\'\"`~<>";

    private final SecureRandom rnd = new SecureRandom();

    @Override
    public String generate(PasswordPolicy policy) {
        if (policy.length < 6 || policy.length > 64) {
            throw new IllegalArgumentException("length must be between 6 and 64");
        }

        List<Character> charPool = new ArrayList<>();
        charPool.addAll(LOWER.chars().mapToObj(c -> (char) c).collect(Collectors.toList()));
        charPool.addAll(UPPER.chars().mapToObj(c -> (char) c).collect(Collectors.toList()));

        if (policy.includeDigits) {
            charPool.addAll(DIGITS.chars().mapToObj(c -> (char) c).collect(Collectors.toList()));
        }
        if (policy.includeSymbols) {
            charPool.addAll(SYMBOLS.chars().mapToObj(c -> (char) c).collect(Collectors.toList()));
        }

        if (policy.excludeAmbiguous) {
            List<Character> ambiguousChars = AMBIGUOUS.chars().mapToObj(c -> (char) c).collect(Collectors.toList());
            charPool.removeIf(ambiguousChars::contains);
        }

        boolean hasLetter = charPool.stream().anyMatch(Character::isLetter);
        if (!hasLetter) {
            throw new IllegalStateException("character pool must contain letters");
        }

        List<Character> result = new ArrayList<>(policy.length);

        if (policy.includeDigits) {
            result.add(pickFrom(DIGITS, policy.excludeAmbiguous));
        }
        if (policy.includeSymbols) {
            result.add(pickFrom(SYMBOLS, policy.excludeAmbiguous));
        }

        while (result.size() < policy.length) {
            result.add(charPool.get(rnd.nextInt(charPool.size())));
        }

        Collections.shuffle(result, rnd);

        return result.stream().map(String::valueOf).collect(Collectors.joining());
    }

    private char pickFrom(String charSet, boolean excludeAmbig) {
        List<Character> filteredSet = charSet.chars().mapToObj(c -> (char) c).collect(Collectors.toList());
        if (excludeAmbig) {
            List<Character> ambiguousChars = AMBIGUOUS.chars().mapToObj(c -> (char) c).collect(Collectors.toList());
            filteredSet.removeIf(ambiguousChars::contains);
            if (filteredSet.isEmpty()) {
                throw new IllegalStateException("No valid chars available for required categories after excluding ambiguous");
            }
        }
        return filteredSet.get(rnd.nextInt(filteredSet.size()));
    }
}

// 程式碼解說
//
//@Service: 這個 Spring 註解標記這個類別為一個服務元件。Spring Boot 會自動偵測到它，並將它註冊為一個 Spring Bean，這樣我們就可以在其他類別（例如 Controller）中輕鬆地使用它。
//
//private static final String ...: 我們將設計書中定義的字元集定義為常數，這讓程式碼更易讀且易於維護。
//
//private final SecureRandom rnd = new SecureRandom();: 這裡使用了 SecureRandom，而非 Math.random()。SecureRandom 提供加密級別的強隨機性，對於密碼產生這類安全敏感的應用程式來說是必不可少的。
//
//generate(PasswordPolicy policy) 方法：
//
//驗證長度：它會再次檢查長度範圍。雖然前端和 PasswordRequest 已經做了初步驗證，但在服務層再次檢查是個好習慣，可以確保業務邏輯的完整性。
//
//建立字元池 (charPool)：根據 PasswordPolicy 的設定，動態地將字母、數字和符號加入一個列表中。
//
//移除易混淆字：如果 excludeAmbiguous 為真，它會將字元池中所有易混淆字元移除。
//
//保證類別：這是演算法的關鍵部分。如果勾選了「包含數字」或「包含符號」，它會先從對應的字元集中隨機選取一個字元，並將其加入結果列表，以確保密碼中至少包含一個數字或符號。
//
//補足長度：在保證了必要的字元後，它會從混合的字元池中隨機抽取字元，直到達到所需的密碼長度。
//
//洗牌：使用 Collections.shuffle(..., rnd) 進行洗牌。這是一種高效且安全的 Fisher–Yates 演算法實現，可以打亂密碼中字元的順序，讓它更難被猜測。
//
//pickFrom 方法：這是一個私有輔助方法，專門用來從特定的字元集中選取字元。它同樣會考慮是否要排除易混淆字元。