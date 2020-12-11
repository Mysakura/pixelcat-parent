package com.pixelcat.pixelcat.web;

import com.pixelcat.spring.boot.autoconfigure.init.EnablePixelCatCenter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnablePixelCatCenter
public class PixelcatWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(PixelcatWebApplication.class, args);
    }

}
