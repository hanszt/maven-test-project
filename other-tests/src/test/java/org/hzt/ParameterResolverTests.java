package org.hzt;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ParameterResolverTests {

    @Nested
    @ExtendWith(ParameterMonthResolverTest.MonthParameterResolver.class)
    class ParameterMonthResolverTest {

        @Test
        void testWithParameter(Month month) {
            assertEquals(Month.MAY, month);
        }

        /**
         * @see <a href="https://www.baeldung.com/junit-5-parameters">Inject Parameters into JUnit Jupiter Unit Tests</a>
         */
        static class MonthParameterResolver implements ParameterResolver {

            @Override
            public boolean supportsParameter(ParameterContext parameterContext,
                                             ExtensionContext extensionContext) throws ParameterResolutionException {
                return parameterContext.getParameter().getType() == Month.class;
            }

            @Override
            public Object resolveParameter(ParameterContext parameterContext,
                                           ExtensionContext extensionContext) throws ParameterResolutionException {
                return Month.MAY;
            }
        }
    }

    @ParameterizedTest
    @ValueSource(strings = "MAY")
    void testParameterizedTest(Month month) {
        assertEquals(Month.MAY, month);
    }

}
