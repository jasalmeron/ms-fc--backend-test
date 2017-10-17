package com.scmspain.domain.validation;


import com.scmspain.domain.entities.Tweet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TweetPublisherValidatorTest {

    private TweetPublisherValidator testSubject = new TweetPublisherValidator();

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfBlankPublisher() {
        testSubject.validate(new Tweet("", "a test tweet"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfNullPublisher() {
        testSubject.validate(new Tweet(null, "a test tweet"));
    }

    @Test
    public void shouldBeValidIfPublisherIsNotEmpty() {
        testSubject.validate(new Tweet("jasalmeron", ""));
    }

}
