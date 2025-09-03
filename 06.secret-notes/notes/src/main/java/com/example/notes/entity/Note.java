//這個 Entity 類別會告訴 Spring Data JPA 框架，如何將 Java 物件與資料庫中的資料行互相轉換。

package com.example.notes.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

// 註解：資料表 Entity，僅保存密文與加密參數，嚴禁保存明文
@Entity
@Table(name = "notes")
public class Note {

    @Id
    private UUID id;

    @Column(length = 200, nullable = false)
    private String title;

    @Lob
    @Column(name = "cipher_text_base64", nullable = false)
    private String cipherTextBase64;

    @Column(name = "salt_base64", length = 200, nullable = false)
    private String saltBase64;

    @Column(name = "iv_base64", length = 200, nullable = false)
    private String ivBase64;

    @Column(name = "kdf_iterations", nullable = false)
    private Integer kdfIterations;

    @Column(name = "kdf_algorithm", length = 50, nullable = false)
    private String kdfAlgorithm;

    @Column(name = "enc_algorithm", length = 50, nullable = false)
    private String encAlgorithm;

    // 延伸：BCrypt 雜湊（可選）
    @Column(name = "pass_bcrypt_hash", length = 100)
    private String passBcryptHash;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    // 我們需要為所有屬性（欄位）加入 Getter 和 Setter 方法
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCipherTextBase64() {
        return cipherTextBase64;
    }

    public void setCipherTextBase64(String cipherTextBase64) {
        this.cipherTextBase64 = cipherTextBase64;
    }

    public String getSaltBase64() {
        return saltBase64;
    }

    public void setSaltBase64(String saltBase64) {
        this.saltBase64 = saltBase64;
    }

    public String getIvBase64() {
        return ivBase64;
    }

    public void setIvBase64(String ivBase64) {
        this.ivBase64 = ivBase64;
    }

    public Integer getKdfIterations() {
        return kdfIterations;
    }

    public void setKdfIterations(Integer kdfIterations) {
        this.kdfIterations = kdfIterations;
    }

    public String getKdfAlgorithm() {
        return kdfAlgorithm;
    }

    public void setKdfAlgorithm(String kdfAlgorithm) {
        this.kdfAlgorithm = kdfAlgorithm;
    }

    public String getEncAlgorithm() {
        return encAlgorithm;
    }

    public void setEncAlgorithm(String encAlgorithm) {
        this.encAlgorithm = encAlgorithm;
    }

    public String getPassBcryptHash() {
        return passBcryptHash;
    }

    public void setPassBcryptHash(String passBcryptHash) {
        this.passBcryptHash = passBcryptHash;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}

//程式碼說明
//@Entity: 這個註解告訴 Spring Boot 這是一個需要與資料庫對應的實體類別。
//
//@Table(name = "notes"): 這個註解指定這個類別對應的資料表名稱為 notes。
//
//@Id: 這個註解標記 id 屬性為資料表的主鍵（Primary Key）。
//
//@Column: 每個屬性上面的 @Column 註解用來設定資料庫欄位的細節，例如名稱 (name)、長度 (length) 和是否可為空 (nullable)。
//
//@Lob: 這個註解用於像 cipherTextBase64 這種可能包含大量文字的欄位。
//
//我們也為每個屬性加入了 Getter 和 Setter 方法，這是 Spring Data JPA 運作所必需的。
//
//這一步驟完成後，你已經成功定義了資料庫的資料模型。 Spring Boot 啟動時，會根據這個 Note.java 類別，自動在 H2 資料庫中建立一個名為 notes 的資料表。