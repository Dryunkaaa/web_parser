package com.siteparser.service.convert;

import org.springframework.stereotype.Service;

@Service
public class ContentFormatService {

    public String formatContent(String content){
        if (content == null) return "";
        if (content.length() < 500) return content;
        return content.substring(0,500) + "...";
    }
}
