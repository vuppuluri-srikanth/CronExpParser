package org.example.cron;

import org.example.cron.exceptions.InvalidCronExpressionException;
import org.junit.jupiter.api.Test;
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

    private static Stream<Arguments> provideInputAndOrderForSuccessScenarios() {
        return Stream.of(
                Arguments.of("*/15 0 1,15 * 1-5 /usr/bin/find",
                        """
                        minute        0 15 30 45
                        hour          0
                        day of month  1 15
                        month         1 2 3 4 5 6 7 8 9 10 11 12
                        day of week   1 2 3 4 5
                        command       /usr/bin/find
                        """),
                Arguments.of("* * * * * /usr/bin/find",
                        """
                        minute        0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 30 31 32 33 34 35 36 37 38 39 40 41 42 43 44 45 46 47 48 49 50 51 52 53 54 55 56 57 58 59
                        hour          0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23
                        day of month  1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 30 31
                        month         1 2 3 4 5 6 7 8 9 10 11 12
                        day of week   0 1 2 3 4 5 6
                        command       /usr/bin/find
                        """),
                Arguments.of("*/57 10-15 5 1,5 0-3 /usr/bin/find",
                        """
                        minute        0 57
                        hour          10 11 12 13 14 15
                        day of month  5
                        month         1 5
                        day of week   0 1 2 3
                        command       /usr/bin/find
                        """)
        );
    }

    @ParameterizedTest
    @MethodSource("provideInputAndErrorMessageForFailureScenarios")
    void testInvalidExpression(String input, String errorMessage) {
        CronParser cronParser = new CronParser();
        Throwable e = assertThrows(InvalidCronExpressionException.class, () -> cronParser.parseExpression(input));
        assertEquals(errorMessage, e.getMessage());
    }

    @ParameterizedTest
    @MethodSource("provideInputAndOrderForSuccessScenarios")
    void testCronExpression(String input, String output) throws InvalidCronExpressionException {
        CronParser cronParser = new CronParser();
        assertEquals(output, cronParser.parseExpression(input).toString());
    }
}