package com.example.mailbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String username;
    private String accessToken;
//    private String refreshToken;

//    public AuthResponse(String username,String accessToken){
//        this.username = username;
//        this.accessToken = accessToken;
//    }
}
