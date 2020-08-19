package com.reddit.redditbackend.exception;

public class SpringRedditException extends RuntimeException {
    public SpringRedditException(String message) {
        super(message);
    }
}
