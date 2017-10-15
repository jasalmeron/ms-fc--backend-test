package com.scmspain.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
public class URLMatcherServiceTest {

    public static final String MERISTATION_UNSECURE_URL = "http://www.meristation.com";
    public static final String MERISTATION_SECURE_URL = "https://www.meristation.com";
    @InjectMocks
    URLMatcherService urlMatcherService;

    @Before
    public void setup() {
        urlMatcherService.init();
    }

    @Test
    public void shouldNotDetectAnyURL() {
        // Given
        List<String> texts = Arrays.asList("More news at Bloomberg", "I'm going to read more about HTTP protocol", "");
        // When
        long numberOfTextWithUrls = texts.stream().mapToInt(text -> urlMatcherService.match(text)).sum();
        // Then
        assertThat(numberOfTextWithUrls).isEqualTo(0L);
    }

    @Test
    public void shouldDetect1URLAtBeginningForEachText() {
        // Given
        int totalUrlsLength = MERISTATION_UNSECURE_URL.length() + MERISTATION_SECURE_URL.length();
        List<String> texts = Arrays.asList(MERISTATION_UNSECURE_URL, MERISTATION_SECURE_URL + " is one of the best spanish videogames site");
        // When
        long totalUrlLength = texts.stream().mapToInt(text -> urlMatcherService.match(text)).sum();
        // Then
        assertThat(totalUrlLength).isEqualTo(totalUrlsLength);
    }

    @Test
    public void shouldDetect1UppercaseURLAtBeginningForEachText() {
        // Given
        int totalUrlsLength = MERISTATION_UNSECURE_URL.length() + MERISTATION_SECURE_URL.length();
        List<String> texts = Arrays.asList(MERISTATION_UNSECURE_URL.toUpperCase(), MERISTATION_SECURE_URL.toUpperCase() + " is one of the best spanish videogames site");
        // When
        long totalUrlLength = texts.stream().mapToInt(text -> urlMatcherService.match(text)).sum();
        // Then
        assertThat(totalUrlLength).isEqualTo(totalUrlsLength);
    }

    @Test
    public void shouldDetect1URLInTheMiddleForEachText() {
        // Given
        int totalUrlsLength = MERISTATION_UNSECURE_URL.length() + MERISTATION_SECURE_URL.length();
        List<String> texts = Arrays.asList("http url:" + MERISTATION_UNSECURE_URL + " in the middle of this text",
                "Obviously " + MERISTATION_SECURE_URL + " is one of the best spanish videogames site");
        // When
        long totalUrlLength = texts.stream().mapToInt(text -> urlMatcherService.match(text)).sum();
        // Then
        assertThat(totalUrlLength).isEqualTo(totalUrlsLength);
    }

    @Test
    public void shouldDetect1URLInTheEndForEachText() {
        // Given
        int totalUrlsLength = MERISTATION_UNSECURE_URL.length() + MERISTATION_SECURE_URL.length();
        List<String> texts = Arrays.asList("http url:" + MERISTATION_SECURE_URL + " in the middle of this text",
                "Obviously " + MERISTATION_UNSECURE_URL + " is one of the best spanish videogames site");
        // When
        long totalUrlLength = texts.stream().mapToInt(text -> urlMatcherService.match(text)).sum();
        // Then
        assertThat(totalUrlLength).isEqualTo(totalUrlsLength);
    }

    @Test
    public void shouldDetect2URL() {
        // Given
        int totalUrlsLength = MERISTATION_SECURE_URL.length() * 4;
        List<String> texts = Arrays.asList("http url:" + MERISTATION_SECURE_URL + " and " + MERISTATION_SECURE_URL,
                "Obviously " + MERISTATION_SECURE_URL + " and " + MERISTATION_SECURE_URL +" is one of the best spanish videogames site");
        // When
        long totalUrlLength = texts.stream().mapToInt(text -> urlMatcherService.match(text)).sum();
        // Then
        assertThat(totalUrlLength).isEqualTo(totalUrlsLength);
    }

}