
-----

# Todo List (待辦清單)

這是一個基於 **Spring Boot** 的待辦清單應用程式。它實現了一個完整的 **CRUD**（建立、讀取、更新、刪除）功能，並結合了現代極簡風格的前端介面，讓使用者可以輕鬆地管理他們的任務。這個專案是學習 Spring Boot、JPA 和前端互動的理想範例。

## 專案目的

  * **核心功能**：實作一個最小可用（MVP）的待辦事項管理服務。
  * **技術棧**：
      * **後端**：Java 17、Spring Boot 3.x、Spring Data JPA、H2 Database
      * **前端**：HTML5、Bootstrap 5、原生 JavaScript
  * **設計理念**：專案採用**前後端分離**的架構，後端提供 RESTful API，前端則透過 API 進行資料互動，實現無重新整理的即時體驗。

-----

## 功能特色

  * **CRUD 完整操作**：支援新增、編輯、刪除和查詢待辦事項。
  * **資料驗證**：後端使用 `jakarta.validation` 確保資料的正確性與安全性。
  * **H2 記憶體資料庫**：開發階段使用輕量級的 H2 資料庫，無需額外安裝，開箱即用。
  * **自動化資料表**：透過 Spring Data JPA，自動根據實體類別建立和更新資料表。
  * **分頁與排序**：查詢列表支援分頁功能，並可依據建立時間或標題進行排序。
  * **條件篩選**：可依據任務的「完成」或「未完成」狀態進行篩選。
  * **現代簡潔介面**：使用 Bootstrap 5 打造極簡風格的單頁式應用程式，提供流暢的使用體驗。

-----

## 程式畫面預覽

以下是應用程式的實際畫面：

### 初始畫面
![初始畫面](https://i.imgur.com/jGEURWu)
### 新增任務
![新增任務](https://i.imgur.com/crLnvbo)
### 編輯內容
![編輯內容](https://i.imgur.com/gNp7Z5x)

-----

## 如何執行專案

本專案使用 Maven 進行建置。請確保您的開發環境已安裝 **JDK 17** 及 **Maven**。

### 1\. 本地端建置與執行

在專案的根目錄下，開啟終端機並執行以下指令：

```sh
# 使用 Maven 建置專案（會自動執行測試）
./mvnw clean install

# 執行應用程式
./mvnw spring-boot:run
```

應用程式預設會在 `http://localhost:8080` 啟動，請打開瀏覽器來查看。

### 2\. H2 資料庫主控台

當專案啟動後，您可以透過以下網址訪問 H2 網頁主控台，直接檢視資料庫中的 `todos` 資料表：

  * **網址**：`http://localhost:8080/h2-console`
  * **JDBC URL**：`jdbc:h2:mem:todo_db`

-----

## API 端點

以下是後端提供的 RESTful API 列表：

### 待辦清單 (Todo List)

| 方法 | 路徑 | 說明 |
|---|---|---|
| `POST` | `/todos` | **建立**一個新的待辦事項。 |
| `GET` | `/todos` | **查詢**待辦事項列表（支援篩選、排序、分頁）。 |
| `GET` | `/todos/{id}` | **讀取**單一待辦事項。 |
| `PUT` | `/todos/{id}` | **更新**指定的待辦事項。 |
| `DELETE` | `/todos/{id}` | **刪除**指定的待辦事項。 |

-----

## 專案架構

```
src
├── main
│   ├── java/com/example/todolist/
│   │   ├── controller/      # API 控制器
│   │   ├── service/         # 核心業務邏輯
│   │   ├── repository/      # 資料庫存取層
│   │   ├── domain/          # 資料模型（JPA 實體）
│   │   ├── dto/             # 資料傳輸物件
│   │   └── exception/       # 全域錯誤處理
│   └── resources/
│       ├── application.yml  # 專案設定檔
│       └── static/
│           └── index.html   # 前端靜態網頁
└── test/
    └── java/com/example/todolist/ # 測試程式碼
```
