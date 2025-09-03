package com.example.notes.dto;

import java.util.UUID;

public record NoteCreatedResponse(UUID id, String title) {}

//這個 DTO 是一個簡單的回應物件，用來告訴客戶端新建立的記事 ID 和標題。