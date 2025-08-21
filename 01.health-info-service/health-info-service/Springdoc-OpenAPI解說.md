### **什麼是 Springdoc-OpenAPI？**

首先，我們需要理解 **OpenAPI**。

* **OpenAPI**：你可以把它想像成一個為 RESTful API 撰寫的標準「說明書」。它定義了一套規則，說明如何描述你的 API：有哪些路徑（例如 `/health`），每個路徑需要什麼參數，會回傳什麼樣的資料，以及可能的錯誤碼等等。
    * **好處**：有了這份標準說明書，無論是前端開發者、QA 測試人員，還是其他後端服務，都能快速理解你的 API 如何使用，而不需要去翻閱程式碼。

而 **Springdoc-OpenAPI** 則是一個專為 Spring Boot 設計的工具，它的主要功能就是 **「自動產生這份標準說明書」**。

### **如何運作？**

Springdoc-OpenAPI 的核心思想是透過「註解」（Annotations）來工作。

1.  **加入依賴**：當你在 `pom.xml` 中加入了 `springdoc-openapi-starter-webmvc-ui` 這個依賴後，Spring Boot 啟動時，它就會載入 Springdoc-OpenAPI 這個工具。
2.  **掃描程式碼**：這個工具會自動掃描你的專案中所有帶有特定註解的類別和方法，例如 `@RestController`、`@GetMapping`、`@PostMapping` 等。
3.  **解析註解**：它會解析這些註解來了解你的 API 結構。例如：
    * `@RequestMapping("/health")`：它知道有一個 API 路徑是 `/health`。
    * `@GetMapping`：它知道這個 API 是一個 `GET` 請求。
    * 方法回傳的 `Map<String, String>`：它知道這個 API 會回傳一個包含字串鍵和字串值的 JSON 物件。
4.  **生成文件**：根據這些解析出來的資訊，Springdoc-OpenAPI 會自動生成一份符合 OpenAPI 標準的 JSON 或 YAML 檔案（通常是 `/v3/api-docs`）。這份檔案就是 API 的「機器可讀」說明書。
5.  **提供 UI**：除了生成檔案，`springdoc-openapi-starter-webmvc-ui` 這個依賴還內建了 **Swagger UI**。這是一個美觀的網頁介面，可以自動讀取剛剛生成的說明書檔案，並以一個互動式的、人類可讀的格式呈現出來。
    * 你可以直接在 Swagger UI 頁面中，看到你的 API 路徑列表、每個路徑的詳細資訊，甚至可以直接點擊按鈕來發送請求測試 API。

這就是為什麼當你訪問 `http://localhost:8080/swagger` 時，會看到一個包含了 `/health` 和 `/info` 的 API 文件頁面，而你不需要手動去寫任何 HTML 或說明文件。

### **總結**

簡單來說，**Springdoc-OpenAPI 是一個自動化工具，它讓你的 Spring Boot 應用程式能夠自己「撰寫」API 說明文件，並以一個美觀的網頁介面（Swagger UI）呈現出來。**

這項技術極大地減少了開發者的工作量，並確保 API 文件始終與程式碼保持同步，是現代後端開發不可或缺的利器。

