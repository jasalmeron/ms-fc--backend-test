package com.scmspain.domain.validation;

import com.scmspain.domain.entities.Tweet;

import java.util.ArrayList;
import java.util.List;

public class TweetValidatorChain implements TweetValidator {

    private List<TweetValidator> validators = new ArrayList<>();

    @Override
    public void validate(Tweet tweet) {
       validators.stream().forEach(validator -> validator.validate(tweet));
    }

    public void addValidator(TweetValidator tweetValidator) {
        this.validators.add(tweetValidator);
    }
}
