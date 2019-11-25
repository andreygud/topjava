package ru.javawebinar.topjava.web.format;

import org.springframework.format.Formatter;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotNull;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.Locale;

public class DateFormatter implements Formatter<LocalDate> {

    @Override
    public LocalDate parse(String text, Locale locale) throws ParseException {
        return StringUtils.isEmpty(text) ? null : LocalDate.parse(text);
    }

    @Override
    public String print(@NotNull LocalDate object, Locale locale) {
        return object.toString();
    }
}
