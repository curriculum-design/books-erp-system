package org.curriculumdesign.bookserp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * @author lesl
 */
@Configuration
public class DateConfig implements Converter<String, LocalDateTime> {

    @Override
    public LocalDateTime convert(String source) {
        ZoneOffset zoneOffset = ZoneOffset.of("+8");
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(Long.parseLong(source)), zoneOffset);
    }
}