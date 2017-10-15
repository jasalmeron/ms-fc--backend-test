package com.scmspain.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class URLMatcherService {

    private final static Logger LOGGER = LoggerFactory.getLogger(URLMatcherService.class);

    Pattern urlPattern;

    @PostConstruct
    public void init() {
        // TODO define pattern to detect URLs
        urlPattern = Pattern.compile("http(s)?:\\S+", Pattern.CASE_INSENSITIVE);
    }

    /**
     *
     * @param text
     * @return
     */
    public int match(String text) {
        //TODO implement!
        Matcher matcher = urlPattern.matcher(text);
        List<String> matches = getMatches(matcher);
        return getTotalMatchesLength(matches);
    }

    private List<String> getMatches(Matcher matcher) {
        List<String> matches = new ArrayList<>();
        while (matcher.find()) {
            String group = matcher.group();
            LOGGER.debug("url match: {}", group);
            matches.add(group);
        }

        return matches;
    }

    private int getTotalMatchesLength(List<String> matches) {
        return matches.stream().mapToInt(match -> match.length()).sum();
    }

}
