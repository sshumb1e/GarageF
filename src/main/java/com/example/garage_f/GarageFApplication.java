package com.example.garage_f;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class GarageFApplication {

    public static void main(String[] args) {
        SpringApplication.run(GarageFApplication.class, args);
    }

}
