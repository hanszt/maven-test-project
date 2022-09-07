package hzt;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public final class JavaDate {

    private JavaDate() {
    }

    public static LocalDate toLocalDate(Date date) {
        if (date instanceof java.sql.Date) {
            return ((java.sql.Date) date).toLocalDate();
        }
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }
}
