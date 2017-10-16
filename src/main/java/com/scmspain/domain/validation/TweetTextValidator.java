package com.scmspain.domain.validation;

import com.scmspain.domain.entities.Tweet;
import com.scmspain.domain.services.HttpLinkMatcherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class TweetTextValidator implements TweetValidator {

    public static final int TWEET_MAX_CHAR = 140;
    private HttpLinkMatcherService httpLinkMatcherService;

    @Autowired
    public TweetTextValidator(HttpLinkMatcherService httpLinkMatcherService) {
        this.httpLinkMatcherService = httpLinkMatcherService;
    }

    @Override
    public void validate(Tweet tweet) {
        if(StringUtils.isEmpty(tweet.getTweet()) || aboveMaximumAllowedLength(tweet)) {
            throw new IllegalArgumentException("Tweet must not be greater than 140 characters");
        }
    }

    private boolean aboveMaximumAllowedLength(Tweet tweet) {
        return tweet.getTweet().length() > allowedTweetLength(tweet.getTweet());
    }

    private int allowedTweetLength(String tweet) {
        return httpLinkMatcherService.match(tweet) + TWEET_MAX_CHAR;
    }

}
