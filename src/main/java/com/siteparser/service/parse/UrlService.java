package com.siteparser.service.parse;

import org.springframework.stereotype.Service;

@Service
public class UrlService {

    public String normalize(String url) {
        String urlParts[] = url.split("\\?");
        url = urlParts[0];
        String hashParts[] = url.split("#");
        url = hashParts[0];
        if (url.endsWith("/")) url = url.substring(0, url.length() - 1);
        return url;
    }
}
