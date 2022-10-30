package com.example.mailbackend.dto.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPayload {
    private String name;
    private String username;
    private String password;
    private String appPassword;
}
