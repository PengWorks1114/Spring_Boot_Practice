package com.example.notes.dto;

import java.util.UUID;

public record DecryptNoteResponse(UUID id, String title, String plainText) {}

//這個 DTO 用來傳送解密後的記事內容，包含 ID、標題和純文字內容。