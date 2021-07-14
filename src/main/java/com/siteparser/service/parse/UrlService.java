package com.siteparser.service.parse;

import org.springframework.stereotype.Service;

@Service
public class UrlService {

    public String normalize(String url) {
        url = removeUrlParams(url);
        url = removeHashParts(url);

        return removeLastSlashIfContains(url);
    }

    private String removeUrlParams(String url) {
        return url.split("\\?")[0];
    }

    private String removeHashParts(String url) {
        return url.split("#")[0];
    }

    private String removeLastSlashIfContains(String url) {
        if (url.endsWith("/")) {
            return url.substring(0, url.length() - 1);
        }

        return url;
    }
}
