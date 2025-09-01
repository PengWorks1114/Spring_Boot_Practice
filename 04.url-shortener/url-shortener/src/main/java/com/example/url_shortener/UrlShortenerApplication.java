package com.example.url_shortener;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling // 啟用排程功能
// 設計書中提到，我們需要一個排程任務，定期檢查並清理過期的短碼，以防止記憶體無限制地增長。我們將使用 Spring Boot 的 @Scheduled 註解來實現這個功能。

public class UrlShortenerApplication {

	public static void main(String[] args) {
		SpringApplication.run(UrlShortenerApplication.class, args);
	}

}


//@EnableScheduling: 這個註解告訴 Spring Boot 啟用排程任務的處理，
// 它會尋找並執行所有標有 @Scheduled 的方法。