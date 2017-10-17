package com.scmspain.domain.services;

import com.scmspain.domain.entities.Tweet;
import com.scmspain.domain.validation.TweetValidator;
import com.scmspain.domain.validation.TweetValidatorChain;
import org.springframework.boot.actuate.metrics.writer.Delta;
import org.springframework.boot.actuate.metrics.writer.MetricWriter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class TweetService {
    private EntityManager entityManager;
    private MetricWriter metricWriter;
    private TweetValidator tweetValidator;

    public TweetService(EntityManager entityManager, MetricWriter metricWriter, TweetValidator tweetValidatorChain) {
        this.entityManager = entityManager;
        this.metricWriter = metricWriter;
        this.tweetValidator = tweetValidatorChain;
    }

    /**
     * Push tweet to repository
     * Parameter - publisher - creator of the Tweet
     * Parameter - text - Content of the Tweet
     * Result - recovered Tweet
     */
    public void publishTweet(String publisher, String text) {
        Tweet tweet = new Tweet(publisher, text);
        tweetValidator.validate(tweet);
        this.entityManager.persist(tweet);
        this.metricWriter.increment(new Delta<Number>("published-tweets", 1));
    }


    /**
     * Recover tweet from repository
     * Parameter - id - id of the Tweet to retrieve
     * Result - retrieved Tweet
     */
    public Tweet getTweet(Long id) {
        return this.entityManager.find(Tweet.class, id);
    }

    /**
     * Recover all tweets from repository
     * Result - retrieved list of published Tweet
     */
    public List<Tweet> listAllTweets() {
        List<Tweet> tweets = listTweets(false);
        this.metricWriter.increment(new Delta<Number>("times-queried-tweets", 1));
        return tweets;
    }

    /**
     * Recover all discarded tweets from repository
     * @return retrieved list of discarded Tweet
     */
    public List<Tweet> listAllDiscardedTweets() {
        List<Tweet> tweets = listTweets(true);
        this.metricWriter.increment(new Delta<Number>("times-queried-ignored-tweets", 1));
        return tweets;
    }

    private List<Tweet> listTweets(boolean ignored) {
        return this.entityManager.createQuery("SELECT tweet FROM Tweet AS tweet WHERE pre2015MigrationStatus<>99 AND discarded=" + ignored +" ORDER BY id DESC", Tweet.class).getResultList();
    }

    /**
     * Discard tweet
     * @param id identifier of the tweet to be discarded.
     */
    public void discardTweet(Long id) {
        Assert.notNull(id, "id can't be null to delete a tweet");
        Tweet tweet = getTweet(id);
        tweet.setDiscarded(true);
        this.entityManager.merge(tweet);
        metricWriter.increment(new Delta<Number>("published-tweets", -1));
    }
}
