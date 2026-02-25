package com.streamroom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class StreamroomApplication {

    public static void main(String[] args) {
        SpringApplication.run(StreamroomApplication.class, args);
    }
}
