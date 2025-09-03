

# Plain-Text Encrypted Notes

這是一個基於 **Spring Boot** 的純文字加解密筆記應用程式。它實現了端對端加解密服務的核心流程，確保使用者筆記的**明文內容永遠不會儲存在伺服器端**。此專案是學習 Java 密碼學（AES-GCM）、Spring Boot API 開發，以及前後端資料安全互動的理想範例。

## 專案目的

  * **核心功能**：提供一個安全儲存純文字筆記的服務，伺服端只儲存加密後的密文與必要的加密參數（例如鹽、IV）。
  * **技術棧**：
      * **後端**：Java 17、Spring Boot 3.x、Spring Data JPA、H2 Database、`spring-security-crypto`
      * **前端**：HTML5、Bootstrap 5、原生 JavaScript (Fetch API)
  * **設計理念**：專案採用**前後端分離**架構。後端提供安全的 RESTful API 負責加解密與儲存，前端則提供簡潔的單頁式介面來與 API 互動，實現流暢的使用體驗。

-----

## 功能特色

  * **端對端加密**：使用工業標準的 **AES-256-GCM** 演算法進行加密，確保資料在傳輸和儲存過程中的安全。
  * **安全金鑰衍生**：透過 **PBKDF2WithHmacSHA256** 演算法，將使用者的密碼安全地轉換為加密金鑰，有效抵抗暴力破解攻擊。
  * **伺服器零明文**：後端服務從不記錄或儲存任何明文筆記或原始密碼，僅處理密文與參數。
  * **H2 記憶體資料庫**：開發階段使用輕量級的 H2 資料庫，無需額外安裝，開箱即用。
  * **統一錯誤回應**：所有 API 錯誤（例如密碼錯誤、找不到筆記等）都會以標準化的 JSON 格式回傳，方便前端處理。
  * **現代簡潔介面**：使用 Bootstrap 5 打造極簡風格的單頁式應用程式，提供流暢的使用體驗。

-----

## 程式畫面預覽

以下是應用程式的實際畫面：
---

### 初始畫面
<img src="https://imgur.com/JeL6tGL.png" width="50%">

---

### 輸入內容
<img src="https://imgur.com/JlUlX1r.png" width="50%">

---

### 儲存後產生 ID
> 按下「儲存」後，系統會產生唯一的筆記 ID，並自動清除畫面上的筆記內容。  
<img src="https://imgur.com/NfTTqwa.png" width="50%">

---

### 輸入 ID 和密碼
<img src="https://imgur.com/PY08AY0.png" width="50%">

---

### 正確解密內容
> 成功輸入正確的 ID 與密碼後，會還原並顯示原本的筆記內容。  
<img src="https://imgur.com/XyukNOE.png" width="50%">

-----

## 如何執行專案

本專案使用 Maven 進行建置。請確保您的開發環境已安裝 **JDK 17** 及 **Maven**。

### 1\. 本地端建置與執行

在專案的根目錄下，開啟終端機並執行以下指令：

```sh
# 使用 Maven 建置專案
./mvnw clean install

# 執行應用程式
./mvnw spring-boot:run
```

應用程式預設會在 `http://localhost:8080` 啟動，請打開瀏覽器來查看。

### 2\. H2 資料庫主控台

當專案啟動後，您可以透過以下網址訪問 H2 網頁主控台，直接檢視資料庫中的 `notes` 資料表：

  * **網址**：`http://localhost:8080/h2-console`
  * **JDBC URL**：`jdbc:h2:mem:notes`

-----

## API 端點

以下是後端提供的 RESTful API 列表：

### 加密筆記 (Encrypted Notes)

| 方法 | 路徑 | 說明 |
|---|---|---|
| `POST` | `/notes` | **建立**一個新的加密筆記。 |
| `GET` | `/notes/{id}` | **解密**並讀取指定的筆記。 |

-----

## 專案架構

```
src
├── main
│   ├── java/com/example/notes/
│   │   ├── web/           # API 控制器與全域錯誤處理
│   │   ├── service/       # 核心業務邏輯與加解密服務
│   │   ├── repo/          # 資料庫存取層
│   │   ├── entity/        # 資料模型（JPA 實體）
│   │   ├── dto/           # 資料傳輸物件
│   │   └── exception/     # 自訂例外類別
│   └── resources/
│       ├── application.yml   # 專案設定檔
│       └── static/
│           └── index.html    # 前端靜態網頁
└── test/
    └── java/com/example/notes/ # 測試程式碼
```
