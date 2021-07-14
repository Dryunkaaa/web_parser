package com.siteparser.service.export;

import com.siteparser.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.StringJoiner;

@Service
public class PageCsvExporter {

    public String exportPages(Collection<Page> pages) {
        StringBuilder result = new StringBuilder();

        result.append(makeCsvLine("№", "URL", "Title", "Description", "Content"));

        int index = 0;
        for (Page page : pages) {
            result.append("\n");
            String line = makeCsvLine(++index, page.getUrl(), page.getTitle(), page.getDescription(), page.getContent());
            result.append(line);
        }

        return result.toString();
    }

    private String makeCsvLine(Object... args) {
        StringJoiner stringJoiner = new StringJoiner(";", "", "");

        for (Object arg : args) {
            stringJoiner.add(arg == null ? "" : arg.toString());
        }

        return stringJoiner.toString();
    }
}
