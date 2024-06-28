package com.cookshare.security.jwt;

import lombok.Data;

@Data
public class AuthenticationRequest {
    private  String mobileNumber;
    private  String password;
}

