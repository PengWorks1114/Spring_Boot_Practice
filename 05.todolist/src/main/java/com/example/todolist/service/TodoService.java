package com.example.todolist.service;

import com.example.todolist.domain.Todo;
import com.example.todolist.dto.*;
import com.example.todolist.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service @RequiredArgsConstructor
public class TodoService {
    private final TodoRepository repo;

    // 建立
    public Todo create(TodoCreateRequest req) {
        Todo t = Todo.builder().title(req.getTitle().trim()).done(false).build();
        return repo.save(t);
    }

    // 查詢（可選 done 篩選 + 分頁 + 排序）
    public Page<Todo> list(Boolean done, Pageable pageable) {
        if (done == null) return repo.findAll(pageable);
        return repo.findByDone(done, pageable);
    }

    // 讀取
    public Todo get(Long id) {
        return repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Todo not found"));
    }

    // 更新（僅更新提供的欄位）
    public Todo update(Long id, TodoUpdateRequest req) {
        Todo t = get(id);
        if (req.getTitle() != null) t.setTitle(req.getTitle().trim());
        if (req.getDone() != null) t.setDone(req.getDone());
        return repo.save(t);
    }

    // 刪除
    public void delete(Long id) {
        if (!repo.existsById(id)) throw new IllegalArgumentException("Todo not found");
        repo.deleteById(id);
    }
}

// 程式碼說明：
//
//@Service: 這個註解告訴 Spring 這個類別是一個服務元件。Spring 會自動管理它的生命週期，並將它註冊為一個 Bean，這樣我們就可以在其他地方（例如控制器）輕鬆地注入和使用它。
//
//@RequiredArgsConstructor: 這是 Lombok 提供的註解，它會自動為所有 final 欄位生成一個帶有參數的建構子。這使得我們可以透過 建構子注入 的方式，將 TodoRepository 注入到這個服務中，而不需要手動寫程式碼。
//
//private final TodoRepository repo;: 這裡我們聲明了一個 TodoRepository 的實例。final 關鍵字表示它必須被初始化，而 @RequiredArgsConstructor 正是為此服務。
//
//create(): 處理建立新待辦事項的邏輯。它接收一個請求物件 (TodoCreateRequest)，使用 Todo.builder() 建立一個新的 Todo 實例，然後呼叫 repo.save() 將其存入資料庫。trim() 則用來移除標題前後的空白。
//
//list(): 負責處理待辦事項列表的查詢，支援篩選和分頁。它會根據 done 參數是否為空來決定呼叫 repo.findAll() 或 repo.findByDone()。
//
//get(): 讀取單筆待辦事項。如果找不到，orElseThrow() 會拋出一個 IllegalArgumentException，這會被我們之後建立的全域錯誤處理來處理，並回傳 404 Not Found 錯誤。
//
//update(): 處理更新邏輯。它先呼叫 get(id) 找到現有的 Todo，然後根據請求物件中的欄位來更新 title 或 done，最後再用 repo.save() 儲存變更。
//
//delete(): 刪除指定 ID 的待辦事項。它會先檢查該 ID 是否存在，若不存在則拋出錯誤，確保我們不會嘗試刪除不存在的資料。