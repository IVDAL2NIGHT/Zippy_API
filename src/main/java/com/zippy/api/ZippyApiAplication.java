package com.zippy.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import java.util.TimeZone;


@SpringBootApplication
@EnableMongoRepositories

public class ZippyApiAplication {

    public static void main(String[] args) {
        SpringApplication.run(ZippyApiAplication.class, args);
    }

}
