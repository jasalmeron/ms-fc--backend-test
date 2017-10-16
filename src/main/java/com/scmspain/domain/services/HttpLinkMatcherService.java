package com.scmspain.domain.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class HttpLinkMatcherService {

    private final static Logger LOGGER = LoggerFactory.getLogger(HttpLinkMatcherService.class);

    Pattern urlPattern;

    @PostConstruct
    public void init() {
        urlPattern = Pattern.compile("http(s)?://\\S+", Pattern.CASE_INSENSITIVE);
    }

    /**
     * Detects if there are http links given a text and returns the total length of them
     * @param text
     * @return sum of the total lengths of found http links
     */
    public int match(String text) {
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
