# 🧭 Spring Boot Practice 專案總導覽

此倉庫為個人 Spring Boot 練習專案，依主題與練習目標分成多個資料夾

---

## 📁 章節一覽（點擊可直接前往）

### [Day 1：健康檢查與版本資訊 API](https://github.com/PengWorks1114/Spring_Boot_Practice/tree/master/01.health-info-service/health-info-service)
- Spring Boot 專案建立與設定
- RESTful API 設計與實作 (`/health`, `/info`)
- Springdoc-OpenAPI 整合自動化文件
- 靜態資源服務 (`index.html`)
- 前後端串接與資料動態渲染

---

### [Day 2：回聲與時間伺服器](https://github.com/PengWorks1114/Spring_Boot_Practice/tree/master/02.echo-time-service/echo-time-service)
- **RESTful API 設計與實作**：`POST /echo` (回聲服務) 與 `GET /now` (時間伺服器)。
- **資料驗證**：使用 Bean Validation 進行請求參數驗證。
- **全域例外處理**：統一錯誤回應格式（Problem Details）。
- **前後端串接**：使用原生 JavaScript 與 Bootstrap 實現測試介面。
- **時區處理**：使用 `java.time` 套件處理時區轉換。

---

### [Day 3：安全密碼產生器](https://github.com/PengWorks1114/Spring_Boot_Practice/tree/master/03.Confidential-Password-Generator/cpg)
- **核心服務層**：以 `PasswordPolicy` 策略物件為核心，實作安全密碼產生邏輯。
- **Java 17 新特性**：使用 `record` 語法簡化資料傳輸物件（DTO）。
- **進階資料驗證**：在 `Controller` 層使用 `@Valid` 註解觸發驗證。
- **全域例外處理**：統一處理 `400`（驗證失敗）及 `422`（無法處理）等錯誤回應。
- **安全實作**：使用 `SecureRandom` 產生強隨機性密碼，並提供多種客製化選項。
- **靜態資源服務**：將原生 JavaScript 與 Bootstrap 實現的前端頁面整合至 Spring Boot 專案。

---

### [Day 4：記憶體短網址服務](https://github.com/PengWorks1114/Spring_Boot_Practice/tree/master/04.url-shortener/url-shortener)
- **核心服務實作**：以記憶體為儲存層，實作輕量級短網址服務。
- **高併發資料處理**：使用 `ConcurrentHashMap` 實現高效、執行緒安全的資料讀寫。
- **短碼碰撞處理**：透過 `MurmurHash3` 與遞增 `salt` 解決短碼衝突問題，確保短碼唯一性。
- **冪等性設計**：相同 URL 重複縮短時，保證回傳相同的短碼。
- **排程任務**：使用 `@Scheduled` 定期清理過期資料，避免記憶體溢出。
- **前後端整合**：使用原生 JavaScript 與 Tailwind CSS 打造簡潔現代的網頁介面。


