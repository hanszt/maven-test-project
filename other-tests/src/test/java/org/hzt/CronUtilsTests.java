package org.hzt;

import com.cronutils.builder.CronBuilder;
import com.cronutils.descriptor.CronDescriptor;
import com.cronutils.model.Cron;
import com.cronutils.model.CronType;
import com.cronutils.model.field.value.SpecialChar;
import com.cronutils.model.time.ExecutionTime;
import com.cronutils.parser.CronParser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Locale;

import static com.cronutils.model.definition.CronDefinitionBuilder.instanceDefinitionFor;
import static com.cronutils.model.field.expression.FieldExpression.always;
import static com.cronutils.model.field.expression.FieldExpression.questionMark;
import static com.cronutils.model.field.expression.FieldExpressionFactory.between;
import static com.cronutils.model.field.expression.FieldExpressionFactory.on;
import static org.junit.jupiter.api.Assertions.*;

class CronUtilsTests {

    private static final DateTimeFormatter UTC = new DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM-dd HH:mm:ss")
            .appendFraction(ChronoField.NANO_OF_SECOND, 0, 9, true)
            .toFormatter()
            .withZone(ZoneId.of("UTC"));

    @Test
    void testCronByBuilder() {
        Cron cron = CronBuilder.cron(instanceDefinitionFor(CronType.QUARTZ))
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
        final var cronParser = new CronParser(instanceDefinitionFor(CronType.SPRING));
        var cron = cronParser.parse("0 */5 * * * *");

        var date = ZonedDateTime.parse("2000-01-01T09:00:00+01:00[Europe/Paris]");

        final var prev = ExecutionTime.forCron(cron).lastExecution(date).orElseThrow();
        final var next = ExecutionTime.forCron(cron).nextExecution(date).orElseThrow();

        System.out.println("Next execution: " + next);
        System.out.println("Prev execution: " + prev);

        assertAll(
                () -> assertEquals("2000-01-01T09:05+01:00[Europe/Paris]", next.toString()),
                () -> assertEquals("2000-01-01T08:55+01:00[Europe/Paris]", prev.toString())
        );
    }

    @ParameterizedTest(name = "alias: `{0}` should return the same schedule as cron: `{1}` for cron type SPRING53")
    @CsvSource(value = {
            "@yearly,   0 0 0 1 1 *",
            "@annually, 0 0 0 1 1 *",
            "@monthly,  0 0 0 1 * *",
            "@weekly,   0 0 0 * * 0",
            "@daily,    0 0 0 * * *",
            "@midnight, 0 0 0 * * *",
            "@hourly,   0 0 * * * *",
    })
    void testAliases(String alias, String cron) {
        final var cronParser = new CronParser(instanceDefinitionFor(CronType.SPRING53));

        Cron cronAlias = cronParser.parse(alias);
        Cron cronCron = cronParser.parse(cron);

        ZonedDateTime dateByAlias = ZonedDateTime.parse("2016-10-15 13:14:00", UTC);
        ZonedDateTime dateByCron = ZonedDateTime.parse("2016-10-15 13:14:00", UTC);

        for (int i = 0; i < 100; i++) {
            dateByAlias = ExecutionTime.forCron(cronAlias).nextExecution(dateByAlias).orElseThrow();
            dateByCron = ExecutionTime.forCron(cronCron).nextExecution(dateByCron).orElseThrow();

            ZonedDateTime finalNextDateByCron = dateByCron;
            ZonedDateTime finalNextDateByAlias = dateByAlias;

            assertAll(
                    () -> assertTrue(ExecutionTime.forCron(cronAlias).isMatch(finalNextDateByCron)),
                    () -> assertTrue(ExecutionTime.forCron(cronCron).isMatch(finalNextDateByAlias)),
                    () -> assertEquals(finalNextDateByAlias, finalNextDateByCron)
            );
        }
    }

    @ParameterizedTest(name = "The english description of cron `{0}` of type `{1}`, should be: `{2}`")
    @CsvSource({
            "@weekly,              SPRING53, UK,  at 00:00 at Sunday day",
            "0 * * * * *,          SPRING53, UK,  every minute",
            "0 0 * * * *,          SPRING53, UK,  every hour",
            "0 0 0 * * *,          SPRING53, UK,  at 00:00",
            "0 0 0 1 * *,          SPRING53, UK,  at 00:00 at 1 day",
            "@yearly,              SPRING53, UK,  at 00:00 at 1 day at January month",
            "0 0 0 1 1 *,          SPRING53, UK,  at 00:00 at 1 day at January month",
            "0 0 0 1 1 1,          SPRING53, UK,  at 00:00 at 1 day at January month at Monday day",
            "0 0 0 1 1 1,          SPRING53, NL,  om 00:00 om 1 dag om januari maand om maandag dag",
            "0 0 0 1 1 1,          SPRING53, FR,  à 00:00 à 1 jour à janvier mois à lundi jour",
            "*/45 * * * * ?,       SPRING53, UK,  every 45 seconds",
            "0 23 * ? * MON-FRI *, QUARTZ,   UK,  every hour at minute 23 every day between Monday and Friday",
            "0 23 * ? * 1-5 *,     QUARTZ,   UK,  every hour at minute 23 every day between Sunday and Thursday"})
    void testDescription(String cron, CronType cronType, Locale locale, String expected) {

        final var parser = new CronParser(instanceDefinitionFor(cronType));
        final var description = CronDescriptor.instance(locale).describe(parser.parse(cron));

        assertEquals(expected, description);
    }

}
