package com.reddit.redditbackend;

import com.reddit.redditbackend.config.SwaggerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@Import(SwaggerConfiguration.class)

public class RedditBackEndApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedditBackEndApplication.class, args);
    }

}
