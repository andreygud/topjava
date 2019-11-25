package ru.javawebinar.topjava.web.format;

import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Formatter;
import org.springframework.format.Parser;
import org.springframework.format.Printer;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

public final class DateTimeFormatAnnotationFormatterFactory implements AnnotationFormatterFactory<CustomDateTimeFormat> {
    @Override
    public Set<Class<?>> getFieldTypes() {
        return Set.of(LocalDate.class, LocalTime.class);
    }

    @Override
    public Printer<?> getPrinter(CustomDateTimeFormat annotation, Class<?> fieldType) {
        return findOutFormatter(annotation, fieldType);
    }

    @Override
    public Parser<?> getParser(CustomDateTimeFormat annotation, Class<?> fieldType) {
        return findOutFormatter(annotation, fieldType);
    }


    private Formatter<?> findOutFormatter(CustomDateTimeFormat annotation, Class<?> fieldType) {
        if (fieldType == LocalDate.class) {
            return new DateFormatter();
        } else if (fieldType == LocalTime.class) {
            return new TimeFormatter();
        }
        return null;
    }
}
