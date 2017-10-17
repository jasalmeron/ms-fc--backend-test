package com.scmspain.domain.services;

import com.scmspain.domain.entities.Tweet;
import com.scmspain.domain.exception.TweetNotFoundException;
import com.scmspain.domain.validation.TweetValidator;
import org.springframework.boot.actuate.metrics.writer.Delta;
import org.springframework.boot.actuate.metrics.writer.MetricWriter;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.Date;
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

        TypedQuery<Tweet> query = this.entityManager.createQuery("SELECT tweet FROM Tweet AS tweet WHERE pre2015MigrationStatus<> :status AND discarded= :discarded ORDER BY publicationDate DESC", Tweet.class);
        query.setParameter("status", 99L);
        query.setParameter("discarded", false );
        List<Tweet> tweets = query.getResultList();
        this.metricWriter.increment(new Delta<Number>("times-queried-tweets", 1));
        return tweets;
    }

    /**
     * Recover all discarded tweets from repository
     * @return retrieved list of discarded Tweet
     */
    public List<Tweet> listAllDiscardedTweets() {
        TypedQuery<Tweet> query = this.entityManager.createQuery("SELECT tweet FROM Tweet AS tweet WHERE pre2015MigrationStatus<> :status AND discarded= :discarded ORDER BY discardedDate DESC", Tweet.class);
        query.setParameter("status", 99L);
        query.setParameter("discarded", true );
        List<Tweet> discardedTweets = query.getResultList();
        this.metricWriter.increment(new Delta<Number>("times-queried-ignored-tweets", 1));
        return discardedTweets;
    }

    /**
     * Discard tweet
     * @param id identifier of the tweet to be discarded.
     */
    public void discardTweet(Long id) {
        Assert.notNull(id, "id can't be null to delete a tweet");
        Tweet tweet = getTweet(id);
        if(tweet == null) {
            throw new TweetNotFoundException(id);
        }
        tweet.setDiscarded(true);
        tweet.setDiscardedDate(new Date());
        this.entityManager.merge(tweet);
        metricWriter.increment(new Delta<Number>("published-tweets", -1));
    }
}
