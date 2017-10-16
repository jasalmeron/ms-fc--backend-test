package com.scmspain.domain.validation;

import com.scmspain.domain.entities.Tweet;

@FunctionalInterface
public interface TweetValidator {

    void validate(Tweet tweet);
}
