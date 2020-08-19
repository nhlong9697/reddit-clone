package com.reddit.redditbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class RedditBackEndApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedditBackEndApplication.class, args);
    }

}
