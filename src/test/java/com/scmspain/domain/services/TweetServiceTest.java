package com.scmspain.domain.services;

import com.scmspain.domain.entities.Tweet;
import com.scmspain.domain.validation.TweetValidator;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.actuate.metrics.writer.MetricWriter;

import javax.persistence.EntityManager;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class TweetServiceTest {
    private EntityManager entityManager;
    private MetricWriter metricWriter;
    private TweetService tweetService;
    private TweetValidator tweetValidator;

    @Before
    public void setUp() throws Exception {
        this.entityManager = mock(EntityManager.class);
        this.metricWriter = mock(MetricWriter.class);
        this.tweetValidator = mock(TweetValidator.class);


        this.tweetService = new TweetService(entityManager, metricWriter, tweetValidator);
    }

    @Test
    public void shouldInsertANewTweet() throws Exception {
        tweetService.publishTweet("Guybrush Threepwood", "I am Guybrush Threepwood, mighty pirate.");

        verify(entityManager).persist(any(Tweet.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowAnExceptionWhenTweetLengthIsInvalid() throws Exception {
        tweetService.publishTweet("Pirate", "LeChuck? He's the guy that went to the Governor's for dinner and never wanted to leave. He fell for her in a big way, but she told him to drop dead. So he did. Then things really got ugly.");
    }
}
