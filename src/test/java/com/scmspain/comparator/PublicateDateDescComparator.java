package com.scmspain.comparator;


import com.scmspain.domain.entities.Tweet;

import java.util.Comparator;

public class PublicateDateDescComparator implements Comparator<Tweet> {

    @Override
    public int compare(Tweet o1, Tweet o2) {
        return o2.getPublicationDate().compareTo(o1.getPublicationDate());
    }
}
