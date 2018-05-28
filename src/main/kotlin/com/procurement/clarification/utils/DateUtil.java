package com.procurement.clarification.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Date;

public class DateUtil {

    public Date nowDateTime() {
        return localToDate(nowUTCLocalDateTime());
    }

    public LocalDateTime nowUTCLocalDateTime() {
        return LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC);
    }

    public long milliNowUTC() {
        return nowUTCLocalDateTime().toInstant(ZoneOffset.UTC).toEpochMilli();
    }

    public long milliUTCfromLocalDateTime(final LocalDateTime localDateTime) {
        return localDateTime.toInstant(ZoneOffset.UTC).toEpochMilli();
    }

    public LocalDateTime dateToLocal(final Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneOffset.UTC);
    }

    public Date localToDate(final LocalDateTime startDate) {
        return Date.from(startDate.toInstant(ZoneOffset.UTC));
    }

}
