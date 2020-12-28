package com.example.demo;

import com.pixelcat.spring.boot.autoconfigure.init.EnablePixelCatClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnablePixelCatClient   // 用户只需引入此注解即可
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}
