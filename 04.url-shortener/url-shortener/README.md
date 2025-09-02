<img width="1915" height="872" alt="image" src="https://github.com/user-attachments/assets/310875a5-74e1-453a-b54b-ea3c85f516ad" />
-----

# In-Memory URL Shortener

這是一個基於 **Spring Boot** 的短網址服務，將所有資料儲存在應用程式的記憶體中。此專案專注於後端 API 的設計、記憶體快取處理，以及自動化的排程清理，是學習高效能後端服務設計的理想範例。

## 專案目的

  * **核心功能**：提供一個輕量級、高效的短網址服務，用於縮短、轉址及管理短連結。
  * **技術棧**：
      * **後端**：Java 17, Spring Boot 3.x, Guava (MurmurHash3)
      * **前端**：純 HTML, 原生 JavaScript, Tailwind CSS
      * **儲存**：無（使用 `ConcurrentHashMap` 作為 In-Memory 儲存）
  * **設計理念**：實作冪等性、短碼碰撞處理與 TTL 到期自動作廢的機制，以驗證記憶體儲存的應用場景。

-----

## 功能特色

  * **即時縮短**：透過 RESTful API 快速將長網址轉換為短連結。
  * **高效能 In-Memory 儲存**：使用執行緒安全的 `ConcurrentHashMap`，確保高併發環境下的讀寫效能。
  * **冪等性處理**：相同網址重複縮短會回傳相同的短碼，避免重複建立。
  * **碰撞解決方案**：當不同網址產生相同初始短碼時，能自動使用 `salt` 重新生成，避免衝突。
  * **自動清理**：內建排程任務，自動刪除過期的短連結，有效管理記憶體。
  * **簡潔 UI**：提供一個現代化的前端介面，支援複製與管理最近建立的短連結。

-----

## 程式範例
**訪問初始狀態** 
![訪問初始狀態](https://imgur.com/5ZDehKE.png)

**輸入網址後產生短網址** 
![輸入網址後產生短網址](https://imgur.com/NUNTKHb.png)

## 如何執行專案

本專案使用 Maven 進行建置。請確保您的開發環境已安裝 **JDK 17** 及 **Maven**。

### 1\. 本地端建置與執行

在專案的根目錄下，開啟終端機並執行以下指令：

```sh
# 使用 Maven 建置專案
mvn clean package

# 執行 JAR 檔案
java -jar target/url-shortener-0.0.1-SNAPSHOT.jar
```

應用程式預設會在 **`http://localhost:8080`** 啟動。您可以打開瀏覽器來查看並使用短網址服務。

### 2\. Docker 部署 (可選)

如果您希望使用 Docker 來部署，可以依循以下步驟：

```sh
# 建置 Docker 映像檔
docker build -t url-shortener-app .

# 執行 Docker 容器
docker run -p 8080:8080 url-shortener-app
```

-----

## API 端點

### `POST /shorten`

  * **說明**：將原始 URL 縮短為短碼。
  * **請求格式**：`application/json`

| 欄位 | 類型 | 說明 | 預設值 |
|---|---|---|---|
| `url` | `string` | 原始 URL（必填，`http/https`） | 無 |
| `ttlSeconds` | `integer` | 有效期限（秒數，可選） | `2592000` (30 天) |

  * **成功回應範例**：

<!-- end list -->

```json
{
  "code": "a1B9xYz",
  "shortUrl": "http://localhost:8080/r/a1B9xYz",
  "expiresAt": "2025-10-01T02:34:56Z"
}
```

  * **錯誤回應範例**：

<!-- end list -->

```json
{
  "timestamp": "...",
  "status": 400,
  "error": "Bad Request",
  "path": "/shorten",
  "message": "URL must start with http or https"
}
```

### `GET /r/{code}`

  * **說明**：透過短碼轉址至原始 URL。
  * **成功回應**：

| 狀態碼 | 標頭 |
|---|---|
| `302 Found` | `Location: <original-url>` |

  * **錯誤回應範例**：

| 狀態碼 | 錯誤碼 |
|---|---|
| `404 Not Found` | `E4041_CODE_NOT_FOUND` |
| `404 Not Found` 或 `410 Gone` | `E4101_CODE_EXPIRED` |

-----

## 專案架構

```
src
├── main
│   ├── java/com/example/url_shortener/
│   │   ├── api/             # API 控制器與資料傳輸物件
│   │   ├── config/          # 專案設定
│   │   ├── model/           # 資料模型
│   │   ├── service/         # 核心業務邏輯
│   │   ├── store/           # In-Memory 儲存
│   │   └── util/            # 工具類
│   └── resources/
│       ├── application.properties  # 設定檔
│       └── static/
│           └── index.html   # 前端靜態檔案
└── test/
    └── java/com/example/url_shortener/ # 測試程式碼
```

-----

## 貢獻

歡迎任何形式的貢獻！如果您發現任何錯誤或有新的功能想法，請隨時提交 Pull Request 或 Issue。
