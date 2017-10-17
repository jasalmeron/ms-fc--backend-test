package com.scmspain.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@EntityListeners(AuditingEntityListener.class)
@Entity
public class Tweet {
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false)
    private String publisher;
    @Column(nullable = false, length = 200)
    private String tweet;
    @Column (nullable=true)
    private Long pre2015MigrationStatus = 0L;
    @JsonIgnore
    @Column(nullable = false)
    private boolean discarded = false;

    @CreatedDate
    @Column(nullable = false)
    private Date publicationDate;

    @Column
    private Date discardedDate;

    public Tweet() {
    }

    public Tweet(Long id) {
        this.id = id;
    }

    public Tweet(String publisher, String tweet) {
        this.publisher = publisher;
        this.tweet = tweet;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getTweet() {
        return tweet;
    }

    public void setTweet(String tweet) {
        this.tweet = tweet;
    }

    public Long getPre2015MigrationStatus() {
        return pre2015MigrationStatus;
    }

    public void setPre2015MigrationStatus(Long pre2015MigrationStatus) {
        this.pre2015MigrationStatus = pre2015MigrationStatus;
    }

    public boolean isDiscarded() {
        return discarded;
    }

    public void setDiscarded(boolean discarded) {
        this.discarded = discarded;
    }

    public Date getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
    }

    public Date getDiscardedDate() {
        return discardedDate;
    }

    public void setDiscardedDate(Date discardedDate) {
        this.discardedDate = discardedDate;
    }
}
