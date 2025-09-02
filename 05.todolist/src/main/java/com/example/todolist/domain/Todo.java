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