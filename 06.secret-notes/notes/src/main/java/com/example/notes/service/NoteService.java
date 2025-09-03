//建立 NoteService
//現在我們有了 Note 資料實體和 CryptoService 加解密服務，接下來需要一個服務來將它們串連起來，處理所有商業邏輯。這個服務就是 NoteService。
//
//NoteService 的主要職責是：
//
//整合 NoteRepository 和 CryptoService。
//
//處理 API 請求中的商業邏輯，例如：
//
//接收明文記事，呼叫 CryptoService 進行加密。
//
//將加密後的資料和參數儲存到資料庫。
//
//接收讀取請求，先進行密碼預檢，再呼叫 CryptoService 進行解密。
//
//處理各種錯誤，並拋出適當的例外。

package com.example.notes.service;

import com.example.notes.dto.DecryptNoteResponse;
import com.example.notes.dto.NoteCreatedResponse;
import com.example.notes.entity.Note;
import com.example.notes.repo.NoteRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.notes.exception.InvalidPassException;

import java.security.GeneralSecurityException;
import java.time.Instant;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@Transactional
public class NoteService {
    private final NoteRepository repo;
    private final CryptoService crypto;
    private final PasswordEncoder bcrypt; // 可替代為 Optional

    public NoteService(NoteRepository repo, CryptoService crypto) {
        this.repo = repo;
        this.crypto = crypto;
        // 註解：延伸需求：BCrypt+隨機 salt（Spring 提供的 BCryptPasswordEncoder）
        this.bcrypt = new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();
    }

    public NoteCreatedResponse create(String title, String plainText, String pass) {
        Map<String,String> enc = crypto.encrypt(plainText, pass);

        Note n = new Note();
        n.setId(UUID.randomUUID());
        n.setTitle(title);
        n.setCipherTextBase64(enc.get("cipherB64"));
        n.setSaltBase64(enc.get("saltB64"));
        n.setIvBase64(enc.get("ivB64"));
        n.setKdfIterations(crypto.kdfIterations());
        n.setKdfAlgorithm(crypto.kdfAlgo());
        n.setEncAlgorithm(crypto.encAlgo());

        // 註解：延伸：存 pass 的 BCrypt 雜湊以快速預檢（可選）
        n.setPassBcryptHash(bcrypt.encode(pass));

        n.setCreatedAt(Instant.now());
        n.setUpdatedAt(Instant.now());
        repo.save(n);
        return new NoteCreatedResponse(n.getId(), n.getTitle());
    }

    @Transactional(readOnly = true)
    public DecryptNoteResponse decrypt(UUID id, String pass) {
        Note n = repo.findById(id).orElseThrow(() -> new NoSuchElementException("Note not found"));
        // 註解：若有存 BCrypt，先檢核，不通過直接 403
        if (n.getPassBcryptHash() != null && !bcrypt.matches(pass, n.getPassBcryptHash())) {
            throw new InvalidPassException("Invalid pass");
        }

        try {
            String plain = crypto.decrypt(
                    n.getCipherTextBase64(), pass, n.getSaltBase64(), n.getIvBase64(), n.getKdfIterations()
            );
            return new DecryptNoteResponse(n.getId(), n.getTitle(), plain);
        } catch (GeneralSecurityException ex) {
            // 註解：GCM 驗證失敗或金鑰不符 → 視同密碼錯誤
            throw new InvalidPassException("Invalid pass");
        }
    }
}

// 程式碼說明
//@Service: 這個註解告訴 Spring 這是一個服務元件，可以被其他類別注入使用。
//
//@Transactional: 這個註解確保方法中的所有資料庫操作（例如 repo.save()）都在一個事務（Transaction）中執行。
//
//依賴注入 (Dependency Injection)：在建構子中，我們使用 NoteRepository 和 CryptoService 這兩個參數。Spring 容器會自動將它們的實例傳遞給這個類別，我們不需要手動建立。
//
//create 方法：
//
//它接收標題、明文和密碼。
//
//呼叫 crypto.encrypt() 取得加密後的資料。
//
//將密碼傳給 bcrypt.encode() 進行雜湊，並將結果存入資料庫。這樣可以在解密前快速檢查密碼是否正確，減少不必要的加解密計算。
//
//將所有資訊組裝成 Note 實體，並呼叫 repo.save() 存入資料庫。
//
//decrypt 方法：
//
//它接收記事 ID 和密碼。
//
//首先，使用 repo.findById() 從資料庫中讀取記事。如果找不到，就拋出 NoSuchElementException，這個例外稍後會被處理成 HTTP 404。
//
//接著，使用 bcrypt.matches() 快速比對使用者提供的密碼和資料庫中儲存的雜湊值。如果比對失敗，直接拋出 AccessControlException，這個例外稍後會被處理成 HTTP 403。
//
//最後，呼叫 crypto.decrypt() 進行解密。如果解密過程失敗（例如密碼錯誤），CryptoService 會拋出 GeneralSecurityException，我們將它轉換成 AccessControlException。