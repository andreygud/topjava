package ru.javawebinar.topjava.web.format;

import org.springframework.format.Formatter;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotNull;
import java.text.ParseException;
import java.time.LocalTime;
import java.util.Locale;

public class TimeFormatter implements Formatter<LocalTime> {

    @Override
    public LocalTime parse(String text, Locale locale) throws ParseException {
        return StringUtils.isEmpty(text)?null:LocalTime.parse(text);
    }

    @Override
    public String print(@NotNull LocalTime object, Locale locale) {
        return object.toString();
    }
}
