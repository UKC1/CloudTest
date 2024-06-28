package com.cookshare.security.service;

public interface LoginService {
    boolean checkPassword(String rawPassword, String encodedPassword);

    boolean checkNicknameDuplicate(String nickname);
}
