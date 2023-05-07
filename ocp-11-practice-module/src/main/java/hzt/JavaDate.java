package hzt;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

public final class JavaDate {

    private JavaDate() {
    }

    public static LocalDate toLocalDate(Date date, ZoneId zoneId) {
        return date instanceof java.sql.Date sqlDate ? sqlDate.toLocalDate() : date.toInstant()
                .atZone(zoneId)
                .toLocalDate();
    }

    public static LocalTime toLocalTime(Date date, ZoneId zoneId) {
        return date instanceof java.sql.Time sqlTime ? sqlTime.toLocalTime() : date.toInstant()
                .atZone(zoneId)
                .toLocalTime();
    }
}
