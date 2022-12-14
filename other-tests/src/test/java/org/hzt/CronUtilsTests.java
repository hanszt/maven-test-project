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
}
