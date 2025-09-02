package com.example.todolist.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import lombok.*;

@Entity @Table(name = "todos")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Todo {
    // 主鍵，自增
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 標題，必填，長度限制
    @Column(nullable = false, length = 100)
    private String title;

    // 完成狀態，預設 false
    @Column(nullable = false)
    private boolean done = false;

    // 建立時間，自動填入
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
}

// 程式碼說明：
//
//package com.example.todolist.domain;: 這是這個類別的套件路徑，用來組織專案。
//
//@Entity: 這個註解告訴 JPA，Todo是一個需要對應到資料庫資料表的實體。
//
//@Table(name = "todos"): 這個註解指定這個實體對應的資料表名稱為 todos。
//
//@Getter, @Setter, @NoArgsConstructor, @AllArgsConstructor, @Builder: 這些是 Lombok 套件的註解。它們會在編譯時自動幫我們產生 getters、setters、無參數建構子、全參數建構子以及一個 Builder 模式的建構子，大幅減少程式碼的冗長。
//
//@Id: 標記 id 欄位為資料表的主鍵（Primary Key）。
//
//@GeneratedValue(strategy = GenerationType.IDENTITY): 告訴資料庫 id 是一個會自動增長的欄位，也就是每新增一筆資料，id 的值會自動加 1。
//
//@Column(nullable = false, length = 100): 指定資料庫欄位的一些屬性。nullable = false 表示這個欄位不能是空的，length = 100 則限制了字串的最大長度。
//
//@CreationTimestamp: 這個註解是 Hibernate（JPA 的一個實作）提供的，它會確保在新增資料時，createdAt 欄位會自動填入當前的時間戳記。