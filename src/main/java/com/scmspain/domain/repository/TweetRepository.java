package com.scmspain.domain.repository;

import com.scmspain.domain.entities.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TweetRepository extends JpaRepository<Tweet, Long> {


/*    public List<Tweet> findByPremigrationStatusDifferent99AndIgnored(@Param("ignored") Boolean ignored);

    SELECT id FROM Tweet AS tweetId WHERE pre2015MigrationStatus<>99 and ignored=false ORDER BY id DESC
*/

}
