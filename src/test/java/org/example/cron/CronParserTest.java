package org.example.cron;

import org.example.cron.exceptions.InvalidCronExpressionException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CronParserTest {

    private static Stream<Arguments> provideInputAndErrorMessageForFailureScenarios() {
        return Stream.of(
                //Expression tests
                Arguments.of("", "Cron Expression is invalid. Expression is empty"),
                Arguments.of(" ", "Cron Expression is invalid. Expression is empty"),
                Arguments.of("*/15 0 1,15 * 1-5", "Cron Expression is invalid. Expression has less than 6 tokens"),
                Arguments.of("*/15 0 1,15 ? 1-5 /usr/bin/find", "Cron Expression is invalid. ? is not supported"),

                Arguments.of("*/15 0 W * L @yearly /usr/bin/find", "Cron Expression is invalid. Expression has more than 6 tokens"),
                Arguments.of("  0 1,15 * 1-5 /usr/bin/find", "Cron Expression is invalid. Expression has less than 6 tokens"),

                //Minutes tests
                Arguments.of("*/@ 0 1,15 * 1-5 /usr/bin/find", "Cron Expression is invalid. 'minute' contains invalid characters"),
                Arguments.of("*/1-5 0 1,15 * 1-5 /usr/bin/find", "Cron Expression is invalid. 'minute' is invalid. Invalid number provided"),
                Arguments.of("*/60 0 1,15 * 1-5 /usr/bin/find", "Cron Expression is invalid. 'minute' is invalid. Number provided is outside bounds: 0 - 59"),
                Arguments.of("-1 0 1,15 * 1-5 /usr/bin/find", "Cron Expression is invalid. 'minute' is invalid. Invalid number provided"),
                Arguments.of("25,60,100 0 1,15 * 1-5 /usr/bin/find", "Cron Expression is invalid. 'minute' is invalid. Number provided is outside bounds: 0 - 59"),

                //Minutes tests
                Arguments.of("* */@ 1,15 * 1-5 /usr/bin/find", "Cron Expression is invalid. 'hour' contains invalid characters"),
                Arguments.of("* */1-5 1,15 * 1-5 /usr/bin/find", "Cron Expression is invalid. 'hour' is invalid. Invalid number provided"),
                Arguments.of("* */24 1,15 * 1-5 /usr/bin/find", "Cron Expression is invalid. 'hour' is invalid. Number provided is outside bounds: 0 - 23"),
                Arguments.of("0 -1 1,15 * 1-5 /usr/bin/find", "Cron Expression is invalid. 'hour' is invalid. Invalid number provided"),
                Arguments.of("0 25,60,100 1,15 * 1-5 /usr/bin/find", "Cron Expression is invalid. 'hour' is invalid. Number provided is outside bounds: 0 - 23"),

                //Day of Month tests
                Arguments.of("*/15 0 W * * /usr/bin/find", "Cron Expression is invalid. 'day of month' contains invalid characters"),
                Arguments.of("* * */@ * 1-5 /usr/bin/find", "Cron Expression is invalid. 'day of month' contains invalid characters"),
                Arguments.of("* * */1-15 * 1-5 /usr/bin/find", "Cron Expression is invalid. 'day of month' is invalid. Invalid number provided"),
                Arguments.of("* * */32 * 1-5 /usr/bin/find", "Cron Expression is invalid. 'day of month' is invalid. Number provided is outside bounds: 1 - 31"),
                Arguments.of("0 * -1 * 1-5 /usr/bin/find", "Cron Expression is invalid. 'day of month' is invalid. Invalid number provided"),
                Arguments.of("0 * 25,60,100 * 1-5 /usr/bin/find", "Cron Expression is invalid. 'day of month' is invalid. Number provided is outside bounds: 1 - 31"),

                //Month
                Arguments.of("* * * */@ 1-5 /usr/bin/find", "Cron Expression is invalid. 'month' contains invalid characters"),
                Arguments.of("* * * */1-5 1-5 /usr/bin/find", "Cron Expression is invalid. 'month' is invalid. Invalid number provided"),
                Arguments.of("* * * */13 1-5 /usr/bin/find", "Cron Expression is invalid. 'month' is invalid. Number provided is outside bounds: 1 - 12"),
                Arguments.of("0 * * -1 1-5 /usr/bin/find", "Cron Expression is invalid. 'month' is invalid. Invalid number provided"),
                Arguments.of("0 * * 25,60,100 1-5 /usr/bin/find", "Cron Expression is invalid. 'month' is invalid. Number provided is outside bounds: 1 - 12"),

                //Day of Week tests
                Arguments.of("*/15 0 * * L /usr/bin/find", "Cron Expression is invalid. 'day of week' contains invalid characters"),
                Arguments.of("* * * * */@ /usr/bin/find", "Cron Expression is invalid. 'day of week' contains invalid characters"),
                Arguments.of("* * * * */1-5 /usr/bin/find", "Cron Expression is invalid. 'day of week' is invalid. Invalid number provided"),
                Arguments.of("* * * * */7 /usr/bin/find", "Cron Expression is invalid. 'day of week' is invalid. Number provided is outside bounds: 0 - 6"),
                Arguments.of("0 * * * -1 /usr/bin/find", "Cron Expression is invalid. 'day of week' is invalid. Invalid number provided"),
                Arguments.of("0 * * * 25,60,100 /usr/bin/find", "Cron Expression is invalid. 'day of week' is invalid. Number provided is outside bounds: 0 - 6")
        );
    }

    @ParameterizedTest
    @MethodSource("provideInputAndErrorMessageForFailureScenarios")
    void testInvalidExpression(String input, String errorMessage) {
        CronParser cronParser = new CronParser();
        Throwable e = assertThrows(InvalidCronExpressionException.class, () -> cronParser.parseExpression(input));
        assertEquals(errorMessage, e.getMessage());
    }

}