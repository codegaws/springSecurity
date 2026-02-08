package com.george.springsecurity.entities;

import lombok.Data;

@Data
public class JWTRequest {

    private String username;
    private String password;
}
