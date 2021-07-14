package com.siteparser.service.convert;

import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class DateFormatService {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
    private static final SimpleDateFormat HOURS_DATE_FORMAT = new SimpleDateFormat("HH:mm:ss");

    public String formatDate(Date date, Date timeDate) {
        if (date == null || timeDate == null) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        builder.append(DATE_FORMAT.format(date), 0, 6)
                .append(DATE_FORMAT.format(date), 8, 10)
                .append(" " + HOURS_DATE_FORMAT.format(timeDate).substring(0, 5));

        return builder.toString();
    }
}
