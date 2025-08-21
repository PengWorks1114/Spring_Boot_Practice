### **專案運作流程概述**

這個應用程式的運作流程可以分為三個主要部分：使用者互動、後端 API 處理，以及資料處理和回應。我們可以將它想像成一個簡單的流水線：

1.  **使用者發出請求**：使用者在瀏覽器中訪問 `http://localhost:8080`。
2.  **Spring Boot 接收請求**：Spring Boot 應用程式作為伺服器，接收到這個請求。
3.  **提供靜態資源**：
      * 應用程式發現請求路徑是根目錄 (`/`)，它會去 `src/main/resources/static` 資料夾尋找 `index.html`。
      * 找到 `index.html` 後，它會將這個檔案內容（包含 HTML、CSS 和 JavaScript）作為回應傳送給瀏覽器。
4.  **前端 JavaScript 運作**：
      * 瀏覽器接收到 `index.html` 後，會立即執行內部的 JavaScript 程式碼。
      * 腳本會發出兩個新的異步請求：
          * `GET /health`
          * `GET /info`
5.  **後端 API 處理請求**：
      * Spring Boot 再次接收到這些 API 請求。
      * **`/health` 請求**：`HealthController` 會處理這個請求，並回傳一個包含 `"status": "UP"` 的 JSON 物件。
      * **`/info` 請求**：`InfoController` 會處理這個請求，它從 `application.yml` 設定檔中讀取 `name`、`version` 和 `buildTime` 的值，然後將它們包裝成一個 JSON 物件回傳。
6.  **前端更新畫面**：
      * 瀏覽器接收到來自 `/health` 和 `/info` 的 JSON 回應。
      * JavaScript 腳本解析這些 JSON 資料。
      * 腳本根據 `health` 回應的 `status` 來改變健康徽章的顏色和文字。
      * 腳本將 `info` 回應中的 `name`、`version` 和 `buildTime` 顯示在畫面上。
7.  **完成**：使用者在瀏覽器上看到了完整的健康狀態和版本資訊，整個流程圓滿結束。

**這個流程的關鍵點在於：** 後端負責提供資料，前端負責呈現資料。它們透過 API 進行通訊，實現了前後端分離的架構。

-----

### **《健康檢查與版本資訊 API》專案 README.md**

-----

\<br\>

# 《健康檢查與版本資訊 API》專案

### **概要**

本專案是一個基於 **Spring Boot 3.3.2 (Java 17)** 建立的最小可用服務（MVP），旨在提供基礎的健康檢查與應用程式版本資訊查詢功能。此專案可作為後續開發的基底，集成了後端 API、前端單頁介面與自動化 API 文件。

### **功能**

  * **GET /health**：回傳服務的健康狀態，固定為 `{"status":"UP"}`。
  * **GET /info**：回傳應用程式的名稱、版本號及建置時間，資訊來源於 `application.yml` 設定檔。
  * **前端單頁**：一個位於根路徑 (`/`) 的可視化網頁，能顯示健康與版本資訊，並提供「重新整理」功能。
  * **Swagger UI**：自動產生 API 文件，可於 `/swagger` 網址查閱與測試。

### **技術棧**

  * **後端**：
      * **框架**：Spring Boot 3.3.2
      * **語言**：Java 17
      * **依賴**：`spring-boot-starter-web`、`springdoc-openapi-starter-webmvc-ui`、`lombok`
  * **前端**：
      * **技術**：純 HTML、CSS (Bootstrap 5)、JavaScript
      * **檔案**：`src/main/resources/static/index.html`
  * **建置工具**：
      * Maven

### **專案結構**

```
health-info-service/
├── .idea/
├── .mvn/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/health_info_service/
│   │   │       ├── advice/
│   │   │       │   └── GlobalExceptionHandler.java
│   │   │       ├── controller/
│   │   │       │   ├── HealthController.java
│   │   │       │   └── InfoController.java
│   │   │       ├── model/
│   │   │       │   ├── ErrorResponse.java
│   │   │       │   └── InfoResponse.java
│   │   │       ├── util/
│   │   │       │   └── TraceIdFilter.java
│   │   │       └── HealthInfoServiceApplication.java
│   │   └── resources/
│   │       ├── static/
│   │       │   └── index.html
│   │       └── application.yml
│   └── test/
├── .gitignore
├── pom.xml
└── README.md
```

### **快速啟動**

1.  **環境需求**：
      * JDK 17 或更高版本。
      * Maven 3.6.3 或更高版本。
2.  **克隆專案**：
    ```sh
    git clone [你的專案網址]
    cd health-info-service
    ```
3.  **編譯與啟動**：
    ```sh
    ./mvnw clean install
    ./mvnw spring-boot:run
    ```
4.  **訪問應用程式**：
      * **前端介面**：[http://localhost:8080/](https://www.google.com/search?q=http://localhost:8080/)
      * **健康檢查 API**：[http://localhost:8080/health](https://www.google.com/search?q=http://localhost:8080/health)
      * **版本資訊 API**：[http://localhost:8080/info](https://www.google.com/search?q=http://localhost:8080/info)
      * **Swagger UI**：[http://localhost:8080/swagger](https://www.google.com/search?q=http://localhost:8080/swagger)

### **組態設定**

應用程式的設定檔位於 `src/main/resources/application.yml`，你可以在此修改端口、應用程式名稱等資訊。

### **貢獻**

歡迎對本專案提出建議與貢獻。

-----

希望這份詳細的解釋和 `README.md` 能幫助你更全面地理解這個專案。
