//建立 CryptoService
//根據設計書，CryptoService 是一個獨立的服務類別，它的職責是封裝所有加解密的邏輯。這樣做的好處是，我們可以將複雜的密碼學操作與商業邏輯（例如儲存和讀取記事）分開，使程式碼更清晰、易於維護。
//
//這個服務會包含：
//
//加密 (encrypt)：將純文字和密碼轉換成密文，並生成必要的參數（鹽、IV 等）。
//
//解密 (decrypt)：使用密碼和儲存的參數，將密文還原成純文字。
//
//輔助方法 (deriveKey, randomBytes)：這些方法會用來生成加密金鑰和隨機字節

package com.example.notes.service;

import org.springframework.stereotype.Service;
import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Map;

@Service
public class CryptoService {

    // 註解：避免魔數，允許從設定檔注入
    private static final String KDF_ALGO = "PBKDF2WithHmacSHA256";
    private static final String ENC_ALGO = "AES/GCM/NoPadding";
    private static final int    KDF_ITER = 100_000;
    private static final int    SALT_LEN = 16;  // bytes
    private static final int    IV_LEN   = 12;  // GCM 推薦 12 bytes
    private static final int    KEY_LEN  = 256; // bits
    private static final int    TAG_LEN  = 128; // bits

    private final SecureRandom random = new SecureRandom();

    // 註解：加密傳回 {cipherB64, saltB64, ivB64}
    public Map<String,String> encrypt(String plainText, String pass) {
        byte[] salt = randomBytes(SALT_LEN);
        byte[] iv   = randomBytes(IV_LEN);
        SecretKey key = deriveKey(pass, salt, KDF_ITER, KEY_LEN);

        try {
            Cipher cipher = Cipher.getInstance(ENC_ALGO);
            GCMParameterSpec spec = new GCMParameterSpec(TAG_LEN, iv);
            cipher.init(Cipher.ENCRYPT_MODE, key, spec);
            byte[] cipherBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            return Map.of(
                    "cipherB64", Base64.getEncoder().encodeToString(cipherBytes),
                    "saltB64",   Base64.getEncoder().encodeToString(salt),
                    "ivB64",     Base64.getEncoder().encodeToString(iv)
            );
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("Encryption failed", e);
        }
    }

    // 註解：失敗丟 GeneralSecurityException 交由上層轉 403
    public String decrypt(String cipherB64, String pass, String saltB64, String ivB64, int iterations) throws GeneralSecurityException {
        byte[] salt = Base64.getDecoder().decode(saltB64);
        byte[] iv   = Base64.getDecoder().decode(ivB64);
        byte[] cipherBytes = Base64.getDecoder().decode(cipherB64);

        SecretKey key = deriveKey(pass, salt, iterations, KEY_LEN);
        Cipher cipher = Cipher.getInstance(ENC_ALGO);
        GCMParameterSpec spec = new GCMParameterSpec(TAG_LEN, iv);
        cipher.init(Cipher.DECRYPT_MODE, key, spec);
        byte[] plainBytes = cipher.doFinal(cipherBytes);
        return new String(plainBytes, StandardCharsets.UTF_8);
    }

    private SecretKey deriveKey(String pass, byte[] salt, int iterations, int keyLenBits) {
        try {
            PBEKeySpec spec = new PBEKeySpec(pass.toCharArray(), salt, iterations, keyLenBits);
            SecretKeyFactory skf = SecretKeyFactory.getInstance(KDF_ALGO);
            byte[] keyBytes = skf.generateSecret(spec).getEncoded();
            return new SecretKeySpec(keyBytes, "AES");
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("KDF failed", e);
        }
    }

    private byte[] randomBytes(int len) {
        byte[] out = new byte[len];
        random.nextBytes(out);
        return out;
    }

    public int kdfIterations() { return KDF_ITER; }
    public String kdfAlgo() { return KDF_ALGO; }
    public String encAlgo() { return ENC_ALGO; }
}

// 程式碼說明
//@Service: 這個註解告訴 Spring Boot 這是一個服務層的元件（Component），Spring 會自動管理它的生命週期，並將它註冊為一個 Bean，方便我們在其他類別中注入使用。
//
//常數（Constants）: 我們將所有關鍵的加解密參數（例如演算法名稱、金鑰長度、迭代次數等）定義為常數。這樣做的好處是，如果未來需要修改，只需要改動這裡即可。
//
//encrypt 方法：
//
//它會先生成一個 隨機的鹽（salt） 和 隨機的 IV。
//
//然後呼叫 deriveKey 方法，用密碼和鹽生成一個加密金鑰。
//
//最後，使用 AES/GCM/NoPadding 演算法對純文字進行加密，並將結果以 Base64 編碼後傳回。
//
//decrypt 方法：
//
//它會接收密文、密碼和所有必要的加密參數。
//
//使用 Base64 解碼這些參數，並用密碼重新生成金鑰。
//
//如果解密成功，則傳回純文字。如果密碼錯誤或密文被篡改，doFinal 方法會拋出 GeneralSecurityException，我們將在後續的服務層處理這個錯誤。
//
//deriveKey 方法：
//
//這是核心的金鑰衍生函數。它使用 PBKDF2WithHmacSHA256 演算法，將使用者的密碼和一個隨機的鹽，透過高達 10 萬次的迭代（KDF_ITER），安全地衍生出一個加密用的金鑰。這可以有效防止暴力破解。