package com.cinema;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.cinema.mapper")
public class CinemaManageSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(CinemaManageSystemApplication.class, args);
    }
}
