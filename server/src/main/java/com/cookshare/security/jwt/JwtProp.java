package com.cookshare.security.jwt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

// application.properties 에서 Secret-key 갖고와서 쓰기
@Data
@Component
@ConfigurationProperties("com.foodshare.jwt")  //하위 속성들을 지정
public class JwtProp {
    //com.foodshare.jwt.secret-key를 secretKey로 인코딩
    private String secretKey;
}

