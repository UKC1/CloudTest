package com.cookshare.security.controller;


import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.nurigo.sdk.NurigoApp;

import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/api/user")
public class OTPController {
    final DefaultMessageService messageService;

    public OTPController(@Value("${coolsms.api.key}") String apiKey,
                         @Value("${coolsms.api.secret}") String apiSecret) {
        this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecret, "https://api.coolsms.co.kr");
    }


    @PostMapping("/memberPhoneCheck")
    public ResponseEntity<Map<String, Object>> memberPhoneCheck(@RequestBody Map<String, Object> payload) {
        String toPhoneNumber = (String) payload.get("mobileNumber");
        if (toPhoneNumber == null || toPhoneNumber.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "휴대폰 번호가 제공되지 않았습니다."));
        }

        String checkNum = generateCheckNum();


        Message message = new Message();
        message.setFrom("01025404366");
        message.setTo(toPhoneNumber);
        message.setText("쿡쉐어(CookShare) 인증번호 입니다: " + checkNum);

        SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));
        System.out.println(response);

        return ResponseEntity.ok(Map.of("checkNum", checkNum));
    }

    // 인증번호 생성 메서드
    private String generateCheckNum() {
        Random random = new Random();
        int number = random.nextInt(999999);
        return String.format("%06d", number);
    }


}

