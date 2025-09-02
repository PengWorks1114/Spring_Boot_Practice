程式碼說明：

server.port: 8080: 這表示我們的應用程式將會在 8080 這個連接埠上運行。

spring.datasource: 這一組設定是關於資料庫的連線資訊。

url: jdbc:h2:mem:todo_db: 這裡我們指定使用 H2 記憶體資料庫，且資料庫名稱為 todo_db。mem 代表資料會儲存在記憶體中，當應用程式關閉後，資料會消失。

spring.jpa.hibernate.ddl-auto: update: 這是一個非常重要的設定。它告訴 Spring Data JPA，當應用程式啟動時，它會自動檢查資料庫的結構是否符合我們的 Java 實體類別（Entity），如果不符，就會自動進行更新（例如：新增資料表、新增欄位）。

spring.jpa.show-sql: true: 這個設定會讓我們在應用程式執行時，在主控台（console）中看到 JPA 所產生的 SQL 語法，方便我們除錯。

spring.h2.console.enabled: true: 這個設定會啟用 H2 內建的網頁式主控台。

spring.h2.console.path: /h2-console: 這表示我們可以透過 http://localhost:8080/h2-console 網址來存取 H2 主控台，方便我們直接檢視資料庫中的資料。