package com.scmspain.controller.command;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DiscardTweetCommand {

    @JsonProperty("tweet")
    private Long tweetId;

    public Long getTweetId() {
        return tweetId;
    }

    public void setTweet(Long tweetId) {
        this.tweetId = tweetId;
    }
}
