// 這個介面定義了密碼產生服務應該具備的功能，也就是 generate() 方法。

package com.example.cpg.service;

import com.example.cpg.domain.PasswordPolicy;

public interface PasswordGeneratorService {
    String generate(PasswordPolicy policy);
}

// 程式碼解說
//
//public interface PasswordGeneratorService: 介面定義了類別的行為契約。任何實作這個介面的類別都必須提供 generate() 方法的具體實現。
//
//String generate(PasswordPolicy policy);: 這個方法接收我們剛才建立的 PasswordPolicy 物件作為參數，並回傳一個 String 密碼。這讓我們的服務層只專注於處理策略物件，而不需要知道前端的請求細節。