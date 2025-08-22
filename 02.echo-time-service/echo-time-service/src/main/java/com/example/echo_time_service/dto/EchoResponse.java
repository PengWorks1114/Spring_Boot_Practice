package com.example.echo_time_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class EchoResponse {
    private String original;
    private int length;
}

//說明：
//
//@AllArgsConstructor 會自動生成一個包含所有欄位的建構子，方便我們建立物件。