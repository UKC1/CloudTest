package com.cookshare.security.controller;

import lombok.Data;

@Data
public class RegisterRequest {
    private String nickname;
    private String password;
    private String location;
    private String mobileNumber;
    private String role;

    public RegisterRequest() {
    }

}
