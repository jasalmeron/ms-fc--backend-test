package com.scmspain.domain.exception;

public class TweetNotFoundException extends RuntimeException {

    public TweetNotFoundException(Long tweetId) {
        super("Tweet with id" + tweetId + " doesn't exists.");
    }
}
