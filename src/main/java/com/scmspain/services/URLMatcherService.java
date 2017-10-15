package com.scmspain.services;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.regex.Pattern;

@Service
public class URLMatcherService {

    Pattern urlPattern;

    @PostConstruct
    public void init() {
        // TODO define pattern to detect URLs
        urlPattern = Pattern.compile("", Pattern.CASE_INSENSITIVE);
    }

    /**
     *
     * @param text
     * @return
     */
    public int match(String text) {
        //TODO implement!
        return -1;
    }

}
