//控制器 (Controller) 是 RESTful API 的核心，它扮演著「總指揮」的角色。當前端發出 HTTP 請求（例如 GET、POST、PUT、DELETE）到特定的網址時，控制器會接收這些請求，然後呼叫 Service 層來處理業務邏輯，最後將結果回傳給前端。
//
//這個控制器會包含設計書中所有定義的 API 端點，包括：
//
//建立待辦事項 (POST /todos)
//
//查詢列表 (GET /todos)
//
//讀取單筆 (GET /todos/{id})
//
//更新 (PUT /todos/{id})
//
//刪除 (DELETE /todos/{id})

package com.example.todolist.controller;

import com.example.todolist.domain.Todo;
import com.example.todolist.dto.*;
import com.example.todolist.service.TodoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/todos")
@RequiredArgsConstructor
public class TodoController {

    private final TodoService service;

    // 建立
    @PostMapping
    public ResponseEntity<Todo> create(@Valid @RequestBody TodoCreateRequest req) {
        Todo saved = service.create(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // 列表：?done=&sort=&page=&size=
    @GetMapping
    public ResponseEntity<TodoPageResponse> list(
            @RequestParam(required = false) Boolean done,
            @RequestParam(defaultValue = "createdAt,desc") String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Sort s = parseSort(sort);
        Pageable pageable = PageRequest.of(page, size, s);
        Page<Todo> p = service.list(done, pageable);

        List<TodoPageResponse.Item> items = p.getContent().stream()
                .map(t -> new TodoPageResponse.Item(t.getId(), t.getTitle(), t.isDone(), t.getCreatedAt()))
                .toList();

        return ResponseEntity.ok(new TodoPageResponse(items, p.getNumber(), p.getSize(), p.getTotalElements(), p.getTotalPages()));
    }

    // 讀取單筆
    @GetMapping("/{id}")
    public ResponseEntity<Todo> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.get(id));
    }

    // 更新
    @PutMapping("/{id}")
    public ResponseEntity<Todo> update(@PathVariable Long id, @Valid @RequestBody TodoUpdateRequest req) {
        return ResponseEntity.ok(service.update(id, req));
    }

    // 刪除
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    // 解析 sort 參數（格式：field,dir）
    private Sort parseSort(String sort) {
        String[] parts = sort.split(",", 2);
        String field = parts[0];
        String dir = parts.length > 1 ? parts[1] : "desc";
        Sort.Direction direction = "asc".equalsIgnoreCase(dir) ? Sort.Direction.ASC : Sort.Direction.DESC;
        return Sort.by(direction, field);
    }
}

//程式碼說明：
//
//@RestController: 這個註解是 @Controller 和 @ResponseBody 的組合，它告訴 Spring 這個類別會處理 HTTP 請求，並且它的方法回傳值會直接被轉換成 JSON 格式並寫入 HTTP 回應主體。
//
//@RequestMapping("/todos"): 這是這個控制器所有處理請求的基礎路徑。例如，create 方法的路徑會是 POST /todos，get 方法的路徑會是 GET /todos/{id}。
//
//@RequiredArgsConstructor: 同樣來自 Lombok，用來自動注入 TodoService。
//
//@PostMapping, @GetMapping, @PutMapping, @DeleteMapping: 這些是 Spring Web 提供的註解，分別對應到 RESTful API 的四種基本操作。
//
//@RequestBody TodoCreateRequest req: 告訴 Spring 將 HTTP 請求主體（通常是 JSON 格式）轉換成 TodoCreateRequest 物件。
//
//@Valid: 與 @RequestBody 搭配使用，它會觸發我們在 DTO 裡設定的 驗證規則（例如 @NotBlank 和 @Size）。
//
//ResponseEntity: 這個類別讓我們可以完全掌控 HTTP 回應，包括狀態碼（HttpStatus）和回應主體（Body）。例如，ResponseEntity.status(HttpStatus.CREATED).body(saved) 會回傳 201 Created 狀態碼和建立後的 Todo 物件。
//
//@RequestParam: 用於接收來自 URL 查詢參數的資料，例如 ?done=true。
//
//@PathVariable: 用於接收來自 URL 路徑中的變數，例如 /todos/123 中的 123。
//
//parseSort(): 這是一個輔助方法，用於解析 URL 參數中的 sort 欄位，例如將 createdAt,desc 轉換成 Spring Data JPA 可以使用的 Sort 物件。