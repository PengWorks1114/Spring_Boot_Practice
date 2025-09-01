

-----

# Confidential Password Generator

這是一個基於 **Spring Boot** 和 **純 JavaScript** 的簡易應用程式，旨在提供一個安全、可客製化的隨機密碼產生服務。此專案特別著重於後端 API 的設計，包含 RESTful 風格、驗證機制和例外處理，是一個非常適合學習的專案範例。

## 專案目的

* **核心功能**：透過 RESTful API 產生符合特定條件的隨機密碼。
* **技術棧**：
    * **後端**：Java 17, Spring Boot 3.x
    * **前端**：純 HTML, 原生 JavaScript, Bootstrap 5
    * **資料庫**：無（純計算型應用）
* **設計理念**：實作一個以服務層 (Service) 與參數物件 (DTO) 為核心的清晰架構，並確保密碼產生過程的安全性。

-----

## 功能特色

* **安全隨機**：使用 **`SecureRandom`** 產生密碼，而非不安全的 `Math.random()`。
* **自訂選項**：可設定密碼長度、是否包含數字及符號，以及排除易混淆字元。
* **簡易介面**：提供一個直觀的前端頁面，即時顯示密碼強度並支援一鍵複製。
* **強健的後端**：包含 API 驗證、全域例外處理，確保應用程式的穩定性。

-----

## 程式範例

**訪問初始狀態**  
![訪問初始狀態](https://imgur.com/tcErNgp.png)

**按下生成密碼後**  
![按下生成密碼後](https://imgur.com/0PBjHPo.png)


## 如何執行專案

本專案使用 Maven 進行建置。請確保您的開發環境已安裝 **JDK 17** 及 **Maven**。

### 1\. 本地端建置與執行

在專案的根目錄下，開啟終端機並執行以下指令：

```sh
# 使用 Maven 建置專案
mvn clean package

# 執行 JAR 檔案
java -jar target/cpg-1.0.0-SNAPSHOT.jar
```

應用程式預設會在 **`http://localhost:8080`** 啟動。您可以打開瀏覽器來查看並使用密碼產生器。

### 2\. Docker 部署 (可選)

如果您希望使用 Docker 來部署，可以依循以下步驟：

```sh
# 建置 Docker 映像檔
docker build -t cpg-app .

# 執行 Docker 容器
docker run -p 8080:8080 cpg-app
```

-----

## API 端點

### `POST /passwords`

* **說明**：產生一個新的隨機密碼。
* **請求格式**：`application/json`

| 欄位              | 類型    | 說明                          | 預設值 |
| ----------------- | ------- | ----------------------------- | ------ |
| `length`          | `integer` | 密碼長度（必填，範圍 6–64）   | 無     |
| `digits`          | `boolean` | 是否包含數字                  | `true` |
| `symbols`         | `boolean` | 是否包含符號                  | `true` |
| `excludeAmbiguous`| `boolean` | 是否排除易混淆字（`0Oo1lI` 等）| `false`|

* **成功回應範例**：

<!-- end list -->

```json
{
  "password": "A8f!mZpQ1@tc"
}
```

* **錯誤回應範例**：

<!-- end list -->

```json
{
  "timestamp": "2025-08-28T06:10:00Z",
  "path": "/passwords",
  "error": "VALIDATION_ERROR",
  "message": "length must be between 6 and 64"
}
```

-----

## 專案架構

```
src
├── main
│   ├── java/com/example/cpg/
│   │   ├── advice/            # 全域例外處理
│   │   ├── controller/        # API 控制器
│   │   ├── domain/            # 業務領域物件
│   │   ├── dto/               # 資料傳輸物件
│   │   └── service/           # 密碼產生核心邏輯
│   └── resources/
│       └── static/
│           └── index.html     # 前端靜態檔案
└── test/
    └── java/com/example/cpg/ # 測試程式碼
```

-----

## 貢獻

歡迎任何形式的貢獻！如果您發現任何錯誤或有新的功能想法，請隨時提交 Pull Request 或 Issue。

-----
