package com.fyf.contentplatform;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.fyf.contentplatform.mapper")
public class ContentPlatformApplication {
    public static void main(String[] args) {
        SpringApplication.run(ContentPlatformApplication.class, args);
    }
}
