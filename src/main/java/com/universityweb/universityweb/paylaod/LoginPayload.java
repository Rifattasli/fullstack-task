package com.universityweb.universityweb.paylaod;

import lombok.Data;

@Data
public class LoginPayload {
    private String email;
    private String password;
    private String role;
}
