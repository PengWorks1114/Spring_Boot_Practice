//現在我們已經有了資料模型 (Note.java)，接下來需要一個機制來與資料庫進行實際的互動，例如儲存、查詢、更新或刪除資料。
//
//在 Spring Boot 中，我們不需要自己寫複雜的 SQL 語句。有了 Spring Data JPA，我們只需要定義一個介面（Interface），框架就會自動幫我們實現所有的基本功能。這個介面就稱為 Repository。

package com.example.notes.repo;

import com.example.notes.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

// 註解：處理 Note 實體的資料庫操作
public interface NoteRepository extends JpaRepository<Note, UUID> {
}

//程式碼說明
//public interface NoteRepository: 我們定義了一個名為 NoteRepository 的介面。
//
//extends JpaRepository<Note, UUID>: 這段程式碼是整個步驟的核心。
//
//我們讓 NoteRepository 繼承 JpaRepository 這個 Spring Data JPA 提供的介面。
//
//在 <> 中，我們傳入了兩個參數：
//
//Note: 告訴框架我們要處理的實體類別是 Note。
//
//UUID: 告訴框架這個實體主鍵 (id) 的資料型別是 UUID。
//
//透過這樣簡單的繼承，NoteRepository 就自動獲得了許多強大的方法，例如：
//
//save(Note note): 儲存或更新一筆記事。
//
//findById(UUID id): 根據 ID 查詢一筆記事。
//
//findAll(): 查詢所有記事。
//
//deleteById(UUID id): 根據 ID 刪除一筆記事。
//
//這一步驟完成後，我們已經建立了與資料庫互動的基礎。 Spring Boot 啟動時會自動找到這個介面，並為它創建一個實例（Bean），我們之後就可以在服務（Service）中使用它了。