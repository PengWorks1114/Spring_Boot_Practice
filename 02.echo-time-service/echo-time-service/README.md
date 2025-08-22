# Echo & Time Service 專案

## 專案簡介

此專案是一個基於 Spring Boot 的簡單 REST API 應用程式，旨在透過兩個核心端點 (`/echo` 和 `/now`)，展示 Request/Response 流程、參數驗證、全域錯誤處理與前後端串接的基本概念。

後端 API 以 Java 17 和 Spring Boot 3.x 開發，並搭配一個使用原生 JavaScript 和 Bootstrap 5 製作的簡單前端測試頁面。

## 技術棧

* **後端**：
    * Java 17
    * Spring Boot 3.x
    * `spring-boot-starter-web`
    * `spring-boot-starter-validation` (JSR 380)
    * `springdoc-openapi-starter-webmvc-ui`
    * Lombok
* **前端**：
    * HTML 5
    * Bootstrap 5
    * 原生 JavaScript (fetch API)
* **建置工具**：Maven

## 如何運行專案

### 1. 系統需求

* Java Development Kit (JDK) 17 或以上版本
* Maven 3.x

### 2. 啟動應用程式

1.  開啟終端機，進入專案根目錄。
2.  使用 Maven 指令編譯並啟動應用程式：
    ```bash
    ./mvnw spring-boot:run
    ```
    或者在 IDE 中直接執行主程式 `EchoTimeServiceApplication.java`。

### 3. 專案存取

* **前端測試頁**：`http://localhost:8080/echo-time-client.html`
* **API 文件 (Swagger UI)**：`http://localhost:8080/swagger-ui.html`

## API 端點規格

### 1. `POST /echo`

* **說明**：回傳請求主體中的文字內容及其字數。
* **請求主體 (Request Body)**：
    ```json
    {
      "text": "Hello world"
    }
    ```
* **成功回應 (200 OK)**：
    ```json
    {
      "original": "Hello world",
      "length": 11
    }
    ```
* **驗證與錯誤處理**：
    * `text` 欄位不可為 `null` 或空白。
    * `text` 長度上限為 2000 個字元。
    * 驗證失敗會回傳 `400 Bad Request`，並提供 `errors` 欄位詳述錯誤。

### 2. `GET /now`

* **說明**：回傳指定時區的當前時間。
* **查詢參數 (Query Parameters)**：
    * `tz` (選填)：IANA 時區字串，例如 `Asia/Tokyo`, `UTC`, `Europe/Paris`。
* **成功回應 (200 OK)**：
    ```json
    {
      "zone": "Asia/Tokyo",
      "instant": "2025-08-22T04:20:00Z",
      "localDateTime": "2025-08-22T13:20:00",
      "offset": "+09:00",
      "epochMilli": 1766444400000
    }
    ```
* **驗證與錯誤處理**：
    * 若未提供 `tz`，預設為 `Asia/Tokyo`。
    * 若提供無效的 `tz`，回傳 `400 Bad Request`。

## 程式碼架構

專案採用標準分層架構，程式包結構如下：
com.example.echo_time_service
├─ controller/    // 處理 HTTP 請求與回應
├─ dto/           // 資料傳輸物件 (Data Transfer Objects)
├─ exception/     // 全域例外處理與錯誤物件定義
├─ service/       // 核心商業邏輯實作
├─ util/          // 放置通用工具類別 (目前為空)
└─ EchoTimeServiceApplication.java


## 測試與驗收

* **自動化測試**：專案包含單元測試，可使用以下指令執行：`./mvnw test`
* **手動測試**：
    * 使用前端測試頁面或 Postman/VS Code Rest Client 等工具，向 API 端點發送請求。
    * 檢查 HTTP 狀態碼與 JSON 回應格式是否符合預期。
    * 確認錯誤回應是否統一為 Problem Details 格式。
