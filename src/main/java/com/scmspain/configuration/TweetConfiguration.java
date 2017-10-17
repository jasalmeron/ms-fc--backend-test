package com.scmspain.configuration;

import com.scmspain.controller.TweetController;
import com.scmspain.domain.services.HttpLinkMatcherService;
import com.scmspain.domain.services.TweetService;
import com.scmspain.domain.validation.TweetPublisherValidator;
import com.scmspain.domain.validation.TweetTextValidator;
import com.scmspain.domain.validation.TweetValidator;
import com.scmspain.domain.validation.TweetValidatorChain;
import org.springframework.boot.actuate.metrics.writer.MetricWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;

@Configuration
@ComponentScan("com.scmspain.domain.validation")
public class TweetConfiguration {

    @Bean
    public HttpLinkMatcherService httpLinkMatcherService() {
        return new HttpLinkMatcherService();
    }

    @Bean
    public TweetService getTweetService(EntityManager entityManager, MetricWriter metricWriter, TweetTextValidator tweetValidatorChain) {
        return new TweetService(entityManager, metricWriter, tweetValidatorChain);
    }

    @Bean
    public TweetController getTweetConfiguration(TweetService tweetService) {
        return new TweetController(tweetService);
    }

    @Bean
    public TweetValidator tweetValidatorChain(TweetTextValidator tweetTextValidator, TweetPublisherValidator tweetPublisherValidator) {
        TweetValidatorChain tweetValidatorChain = new TweetValidatorChain();
        tweetValidatorChain.addValidator(tweetPublisherValidator);
        tweetValidatorChain.addValidator(tweetTextValidator);
        return tweetValidatorChain;
    }
}
