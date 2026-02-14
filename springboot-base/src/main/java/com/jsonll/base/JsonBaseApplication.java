package com.jsonll.base;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.jsonll.base.mapper")
@EnableScheduling
public class JsonBaseApplication {

    public static void main(String[] args) {
        SpringApplication.run(JsonBaseApplication.class, args);
    }
}
