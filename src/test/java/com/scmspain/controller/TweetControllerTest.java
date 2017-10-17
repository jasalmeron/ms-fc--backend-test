package com.scmspain.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scmspain.comparator.DiscardedDateDescComparator;
import com.scmspain.comparator.PublicateDateDescComparator;
import com.scmspain.configuration.TestConfiguration;
import com.scmspain.controller.command.DiscardTweetCommand;
import com.scmspain.domain.entities.Tweet;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.stream.IntStream;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestConfiguration.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class TweetControllerTest {
    @Autowired
    private WebApplicationContext context;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void setUp() {
        this.mockMvc = webAppContextSetup(this.context).build();
    }

    @Test
    public void shouldReturn200WhenInsertingAValidTweet() throws Exception {
        mockMvc.perform(publishTweetBuilder("Prospect", "Breaking the law"))
                .andExpect(status().is(201));
    }

    @Test
    public void shouldReturn400WhenInsertingAValidTweetWithLink() throws Exception {
        mockMvc.perform(publishTweetBuilder("Schibsted Spain", "We are Schibsted Spain (look at our home page http://www.schibsted.es/), we own Vibbo, InfoJobs, fotocasa, coches.net and milanuncios. Welcome!"))
                .andExpect(status().is(201));
    }

    @Test
    public void shouldReturn400WhenInsertingAnInvalidTweet() throws Exception {
        mockMvc.perform(publishTweetBuilder("Schibsted Spain", "We are Schibsted Spain (look at our home page http://www.schibsted.es/), we own Vibbo, InfoJobs, fotocasa, coches.net and milanuncios. Welcome! For more information please refer to http://www.schibsted.es/faq or email us at contact@schibsted.es"))
                .andExpect(status().is(400));
    }

    @Test
    public void shouldReturnAllPublishedTweetsSortedByPublishDateDesc() throws Exception {
        IntStream.range(1, 4).forEach(i -> buildPublishTweet(i));
        String content = getTweets().getResponse().getContentAsString();
        List<Tweet> list = objectMapper.readValue(content, new TypeReference<List<Tweet>>() {
        });
        assertThat(list.size()).isEqualTo(3);
        assertThat(list).isSortedAccordingTo(new PublicateDateDescComparator());
    }


    @Test
    public void shouldReturn200DiscardingAPublishedTweet() throws Exception {
        buildPublishTweet();
        List<Tweet> list = getAllTweets();
        mockMvc.perform(discardTweetBuilder(list.get(0).getId()))
                .andExpect(status().is(200))
                .andReturn();
    }

    @Test
    public void shouldReturn404DiscardingAnUncreatedTweet() throws Exception {
        mockMvc.perform(discardTweetBuilder(2L))
                .andExpect(status().is(404))
                .andReturn();
    }

    @Test
    public void shouldReturnAllDiscardedTweetsSortedByDiscardedDateAnZeroPublishedTweets() throws Exception {
        // Given
        IntStream.range(1, 4).forEach(i -> buildPublishTweet(i));
        getAllTweets().forEach(tweet -> discardTweet(tweet.getId()));

        // When
        MvcResult getDiscardedResult = mockMvc.perform(get("/discarded"))
                .andExpect(status().is(200))
                .andReturn();
        String content = getDiscardedResult.getResponse().getContentAsString();
        List<Tweet> discardedTweets = objectMapper.readValue(content, new TypeReference<List<Tweet>>() {
        });
        List<Tweet> allTweets = getAllTweets();

        // Then
        assertThat(discardedTweets.size()).isEqualTo(3);
        assertThat(discardedTweets).isSortedAccordingTo(new DiscardedDateDescComparator());
        assertThat(allTweets.size()).isEqualTo(0);
    }

    private List<Tweet> getAllTweets() throws Exception {
        String content = getTweets().getResponse().getContentAsString();
        return objectMapper.readValue(content, new TypeReference<List<Tweet>>() {
        });
    }

    private MvcResult getTweets() throws Exception {
        return mockMvc.perform(get("/tweet"))
                .andExpect(status().is(200))
                .andReturn();
    }

    private void buildPublishTweet() {
        buildPublishTweet(1);
    }

    private void buildPublishTweet(int number) {
        try {
            mockMvc.perform(publishTweetBuilder("Yo", "Tweet " + number))
                    .andExpect(status().is(201));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private MockHttpServletRequestBuilder publishTweetBuilder(String publisher, String tweet) {
        return post("/tweet")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(format("{\"publisher\": \"%s\", \"tweet\": \"%s\"}", publisher, tweet));
    }

    private void discardTweet(Long number) {
        try {
            mockMvc.perform(discardTweetBuilder(number))
                    .andExpect(status().is(200));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private MockHttpServletRequestBuilder discardTweetBuilder(Long tweetId) throws JsonProcessingException {
        DiscardTweetCommand discardTweetCommand = new DiscardTweetCommand();
        discardTweetCommand.setTweet(tweetId);
        return post("/discarded")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(discardTweetCommand));
    }

}
