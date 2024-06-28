package com.cookshare.security.controller;


import org.mindrot.jbcrypt.BCrypt;

public class BCryptExample {
    public static void main(String[] args) {
        // 새 비밀번호
        String password = "user01!@";

        // 해시 생성 및 솔트 포함
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        // 출력
        System.out.println(hashedPassword);
    }
}
