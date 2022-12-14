package org.hzt;

import com.cronutils.builder.CronBuilder;
import com.cronutils.model.Cron;
import com.cronutils.model.CronType;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.model.field.value.SpecialChar;
import org.junit.jupiter.api.Test;

import static com.cronutils.model.field.expression.FieldExpression.always;
import static com.cronutils.model.field.expression.FieldExpression.questionMark;
import static com.cronutils.model.field.expression.FieldExpressionFactory.between;
import static com.cronutils.model.field.expression.FieldExpressionFactory.on;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CronUtilsTests {

    @Test
    void testCronByBuilder() {
        Cron cron = CronBuilder.cron(CronDefinitionBuilder.instanceDefinitionFor(CronType.QUARTZ))
                .withYear(always())
                .withDoM(between(SpecialChar.L, 3))
                .withMonth(always())
                .withDoW(questionMark())
                .withHour(always())
                .withMinute(always())
                .withSecond(on(0))
                .instance();

        String cronAsString = cron.asString();

        assertEquals("0 * * L-3 * ? *", cronAsString);
    }
    
    @Test
    void testCronZonedDateTime() {
        var cron = new Cron("*/5 * * * *");
        var date = ZonedDateTime.parse("2000-01-01T09:00:00+01:00[Europe/Paris]");

        final var next = cron.next(date);
        final var prev = cron.prev(date);

        System.out.println("Next execution: " + next);
        System.out.println("Prev execution: " + prev);

        assertAll(
                () -> assertEquals("2000-01-01T09:05+01:00[Europe/Paris]", next.toString()),
                () -> assertEquals("2000-01-01T08:55+01:00[Europe/Paris]", prev.toString())
        );
    }

    @Test
    void cronUsingFields() {
        final var starField = new StarField();
        final var cron = new Cron(new ValueField(FieldType.SECOND, 5), starField, starField, starField, starField, starField);
        var date = ZonedDateTime.parse("2000-01-01T09:00:00+01:00[Europe/Paris]");

        final var next = cron.next(date);
        final var prev = cron.prev(date);

        System.out.println("Next execution: " + next);
        System.out.println("Prev execution: " + prev);

        assertAll(
                () -> assertEquals("2000-01-01T09:00:05+01:00[Europe/Paris]", next.toString()),
                () -> assertEquals("2000-01-01T08:59:05+01:00[Europe/Paris]", prev.toString())
        );
    }

    @ParameterizedTest(name = "alias: `{0}` should return the same schedule as cron: `{1}`")
    @CsvSource(value = {
            "@yearly, 0 0 1 1 *",
            "@annually, 0 0 1 1 *",
            "@monthly, 0 0 1 * *",
            "@weekly, 0 0 * * 0",
            "@daily, 0 0 * * *",
            "@midnight, 0 0 * * *",
            "@hourly, 0 * * * *",
    })
    void testAliases(String alias, String cron) {
        Cron cronAlias = new Cron(alias);
        Cron cronCron = new Cron(cron);

        ZonedDateTime dateByAlias = Support.brt("2016-10-15 13:14:00");
        ZonedDateTime dateByCron = Support.brt("2016-10-15 13:14:00");

        for (int i = 0; i < 1000; i++) {
            dateByAlias = cronAlias.next(dateByAlias);
            dateByCron = cronCron.next(dateByCron);

            ZonedDateTime finalNextDateByCron = dateByCron;
            ZonedDateTime finalNextDateByAlias = dateByAlias;

            assertAll(
                    () -> assertTrue(cronAlias.matches(finalNextDateByCron)),
                    () -> assertTrue(cronCron.matches(finalNextDateByAlias)),
                    () -> assertEquals(finalNextDateByAlias, finalNextDateByCron)
            );
        }
    }

    private static class Support {

        private static final DateTimeFormatter FORMATTER =
                new DateTimeFormatterBuilder()
                        .appendPattern("yyyy-MM-dd HH:mm:ss") // .parseLenient()
                        .appendFraction(ChronoField.NANO_OF_SECOND, 0, 9, true)
                        .toFormatter();

        public static final DateTimeFormatter BRT = FORMATTER.withZone(ZoneId.of("America/Sao_Paulo"));
        public static final DateTimeFormatter UTC = FORMATTER.withZone(ZoneId.of("UTC"));

        public static ZonedDateTime brt(String s) {
            return ZonedDateTime.parse(s, BRT);
        }

        public static ZonedDateTime utc(String s) {
            return ZonedDateTime.parse(s, UTC);
        }
    }
}
