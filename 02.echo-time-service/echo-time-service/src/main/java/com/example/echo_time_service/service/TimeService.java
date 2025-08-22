package com.example.echo_time_service.service;

import com.example.echo_time_service.dto.NowResponse;
import org.springframework.stereotype.Service;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

@Service
public class TimeService {

    public NowResponse now(String tz) {
        final String zoneId = Optional.ofNullable(tz)
                .filter(s -> !s.isBlank())
                .orElse("Asia/Tokyo");
        try {
            ZoneId zone = ZoneId.of(zoneId);
            Instant instant = Instant.now();
            ZonedDateTime zdt = instant.atZone(zone);
            return new NowResponse(
                    zone.getId(),
                    instant.toString(),
                    zdt.toLocalDateTime().toString(),
                    zdt.getOffset().toString(),
                    instant.toEpochMilli()
            );
        } catch (DateTimeException ex) {
            throw new IllegalArgumentException("Invalid time zone: " + zoneId);
        }
    }
}

//說明：
//
//@Service 註解同樣適用於這裡。
//
//now 方法接收一個可選的 tz (時區) 字串。
//Optional.ofNullable(...) 是一種更現代、更安全的處理 null 的方式，
//它能確保如果 tz 是 null 或空字串，我們就會使用預設值 "Asia/Tokyo"。
//ZoneId.of(zoneId) 嘗試將字串轉換為有效的時區物件。
//
//如果時區字串無效，ZoneId.of() 會拋出 DateTimeException，我們用 try-catch 區塊捕捉它，
//並拋出一個 IllegalArgumentException。這個異常將在後續的步驟中由全域例外處理器捕捉並處理。