package com.scmspain.domain.validation;

import com.scmspain.domain.entities.Tweet;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class TweetPublisherValidator implements TweetValidator {
    @Override
    public void validate(Tweet tweet) {
        if(StringUtils.isEmpty(tweet.getPublisher())) {
            throw new IllegalArgumentException("Tweet must have an associated publisher");
        }

    }
}
