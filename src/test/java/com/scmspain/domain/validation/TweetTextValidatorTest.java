package com.scmspain.domain.validation;

import com.scmspain.domain.entities.Tweet;
import com.scmspain.domain.services.HttpLinkMatcherService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TweetTextValidatorTest {

    @InjectMocks
    private TweetTextValidator testSubject;

    @Mock
    private HttpLinkMatcherService httpLinkMatcherService;

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrownExceptionIfTweetIsEmpty() {
        testSubject.validate(new Tweet("jasalmeron", ""));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrownExceptionIfTweetIsNull() {
        testSubject.validate(new Tweet("jasalmeron", ""));
    }

    @Test
    public void shouldBeValidIfTweetIsNotEmptyAndUnderTheMaximumCharLimit() {
        testSubject.validate(new Tweet("publisher", "Lionel Messi scores another hat-trick leading the Barça to a new victory in Champions League Cup."));
    }

}
