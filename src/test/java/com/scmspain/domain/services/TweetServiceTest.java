package com.scmspain.domain.services;

import com.scmspain.domain.entities.Tweet;
import com.scmspain.domain.exception.TweetNotFoundException;
import com.scmspain.domain.validation.TweetValidator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.actuate.metrics.writer.Delta;
import org.springframework.boot.actuate.metrics.writer.MetricWriter;

import javax.persistence.EntityManager;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TweetServiceTest {
    private EntityManager entityManager;
    private MetricWriter metricWriter;
    private TweetService testSubject;
    private TweetValidator tweetValidator;

    @Captor
    private ArgumentCaptor<Delta<Number>> deltaCaptor;

    @Captor
    private ArgumentCaptor<Tweet> tweetCaptor;

    @Before
    public void setUp() throws Exception {
        this.entityManager = mock(EntityManager.class);
        this.metricWriter = mock(MetricWriter.class);
        this.tweetValidator = mock(TweetValidator.class);
        this.testSubject = new TweetService(entityManager, metricWriter, tweetValidator);
    }

    @Test
    public void shouldInsertANewTweet() throws Exception {
        // When
        testSubject.publishTweet("Guybrush Threepwood", "I am Guybrush Threepwood, mighty pirate.");

        // Then
        verify(metricWriter).increment(deltaCaptor.capture());
        verify(entityManager).persist(any(Tweet.class));
        Delta<Number> delta = deltaCaptor.getValue();
        assertThat(delta.getValue()).isEqualTo(1);
        assertThat(delta.getName()).isEqualTo("published-tweets");
    }

    @Test
    public void shouldDiscardATweet() {
        // Given
        testSubject.publishTweet("Guybrush Threepwood", "I am Guybrush Threepwood, mighty pirate.");
        given(this.entityManager.find(eq(Tweet.class), eq(1L))).willReturn(new Tweet(1L));

        // When
        testSubject.discardTweet(1L);

        // Then
        verify(metricWriter, times(2)).increment(deltaCaptor.capture());
        verify(entityManager).merge(tweetCaptor.capture());
        Delta<Number> delta = deltaCaptor.getAllValues().get(1);
        assertThat(delta.getValue()).isEqualTo(-1);
        assertThat(delta.getName()).isEqualTo("published-tweets");
        Tweet mergedTweet = tweetCaptor.getValue();
        assertThat(mergedTweet.isDiscarded()).isEqualTo(true);
        assertThat(mergedTweet.getDiscardedDate()).isCloseTo(new Date(), 1000);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowAnExceptionWhenTweetLengthIsInvalid() throws Exception {
        doThrow(IllegalArgumentException.class).when(tweetValidator).validate(any(Tweet.class));
        testSubject.publishTweet("Pirate", "LeChuck? He's the guy that went to the Governor's for dinner and never wanted to leave. He fell for her in a big way, but she told him to drop dead. So he did. Then things really got ugly.");
    }

    @Test(expected = TweetNotFoundException.class)
    public void shouldThownAnExceptionIfDiscardedTweetDoesntExist() {
        testSubject.discardTweet(1L);
    }
}
