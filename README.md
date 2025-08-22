# 🧭 Spring Boot Practice 專案總導覽

此倉庫為個人 Spring Boot 練習專案，依主題與練習目標分成多個資料夾

---

## 📁 章節一覽（點擊可直接前往）

### 01️ [Day 1：健康檢查與版本資訊 API](https://github.com/PengWorks1114/Spring_Boot_Practice/tree/master/01.health-info-service/health-info-service)
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
