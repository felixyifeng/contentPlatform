package com.fyf.contentplatform;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ContentPlatformApplicationTests {

    @Autowired
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @Test
    void generateBcryptHash() {
        // BCrypt 每次加密结果都不同（自带随机盐），都合法，打印出来即可
        System.out.println("123456 的BCrypt密文：" + passwordEncoder.encode("123456"));
    }

    @Test
    void contextLoads() {
    }

}
