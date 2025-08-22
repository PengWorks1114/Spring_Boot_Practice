package com.example.echo_time_service.dto;



import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class NowResponse {
    private String zone;
    private String instant;
    private String localDateTime;
    private String offset;
    private long epochMilli;
}

//說明：
//
//這三個 DTO 類別定義了 API 請求和回應的資料結構。它們是我們前後端溝通的橋樑。