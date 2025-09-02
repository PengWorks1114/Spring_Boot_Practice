//在 Spring Data JPA 的架構中，Repository 是我們與資料庫互動的核心。它是一個介面，只要繼承 JpaRepository，Spring 就會自動幫我們實作好最常見的 CRUD（建立、讀取、更新、刪除）方法，像是 save()、findById()、findAll() 和 deleteById()。
//
//除此之外，我們還可以定義自己的查詢方法。Spring 會根據方法的名稱，自動幫我們產生對應的 SQL 語法。


package com.example.todolist.repository;

import com.example.todolist.domain.Todo;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    Page<Todo> findByDone(boolean done, Pageable pageable);
}

// 程式碼說明：
//
//package com.example.todolist.repository;: 這個是 Repository 介面的套件路徑。
//
//public interface TodoRepository extends JpaRepository<Todo, Long>:
//
//interface 表示這是一個介面，而不是一個類別。
//
//extends JpaRepository: 這是關鍵！它繼承了 Spring Data JPA 提供的核心介面。
//
//<Todo, Long>: 這裡有兩個泛型參數。第一個參數 <Todo> 指定了我們這個 Repository 所處理的實體類別是 Todo，第二個參數 <Long> 則指定了該實體類別主鍵的型別是 Long。
//
//Page<Todo> findByDone(boolean done, Pageable pageable);:
//
//這是一個我們自定義的查詢方法。
//
//Spring Data JPA 具有強大的「方法名稱查詢（Method Name Query）」功能。它會解析方法名稱，並自動生成查詢語法。
//
//這裡的 findByDone 會被解析為 WHERE done = ? 的 SQL 查詢。
//
//Page<Todo> 則表示查詢的結果會以 分頁（Pagination） 的形式回傳。
//
//Pageable pageable 參數則讓我們可以傳入分頁資訊（例如頁碼、每頁大小、排序方式）。