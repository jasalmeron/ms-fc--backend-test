package com.scmspain.controller;

import com.scmspain.controller.command.DiscardTweetCommand;
import com.scmspain.controller.command.PublishTweetCommand;
import com.scmspain.domain.entities.Tweet;
import com.scmspain.domain.exception.TweetNotFoundException;
import com.scmspain.domain.services.TweetService;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
public class TweetController {
    private TweetService tweetService;

    public TweetController(TweetService tweetService) {
        this.tweetService = tweetService;
    }

    @GetMapping("/tweet")
    public List<Tweet> listAllTweets() {
        return this.tweetService.listAllTweets();
    }

    @PostMapping("/tweet")
    @ResponseStatus(CREATED)
    public void publishTweet(@RequestBody PublishTweetCommand publishTweetCommand) {
        this.tweetService.publishTweet(publishTweetCommand.getPublisher(), publishTweetCommand.getTweet());
    }

    @PostMapping("/discarded")
    public void discardTweet(@RequestBody DiscardTweetCommand discardTweetCommand) {
        this.tweetService.discardTweet(discardTweetCommand.getTweetId());
    }

    @GetMapping("/discarded")
    public List<Tweet> getDicardedTweets() {
        return this.tweetService.listAllDiscardedTweets();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(BAD_REQUEST)
    @ResponseBody
    public Object invalidArgumentException(IllegalArgumentException ex) {
        return new Object() {
            public String message = ex.getMessage();
            public String exceptionClass = ex.getClass().getSimpleName();
        };
    }

    @ExceptionHandler({TweetNotFoundException.class, EntityNotFoundException.class})
    @ResponseStatus(NOT_FOUND)
    @ResponseBody
    public Object entityNotFoundException(TweetNotFoundException ex) {
        return new Object() {
            public String message = ex.getMessage();
            public String exceptionClass = ex.getClass().getSimpleName();
        };
    }

}
