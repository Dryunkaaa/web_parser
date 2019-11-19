package com.siteparser.service.convert;

import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class DateFormatService {

    public String formatDate(Date date, Date timeDate) {
        StringBuilder builder = new StringBuilder();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        builder.append(simpleDateFormat.format(date).substring(0, 6));
        builder.append(simpleDateFormat.format(date).substring(8, 10));
        simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        builder.append(" " + simpleDateFormat.format(timeDate).substring(0, 5));
        return builder.toString();
    }
}
