package org.example.cron.dtos;

import org.example.cron.CronParser;
import org.example.cron.exceptions.InvalidCronExpressionException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.TreeSet;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CronScheduleTest {
    private static Stream<Arguments> provideInputAndOutput() {
        return Stream.of(
                Arguments.of("* * * * * /usr/bin/find", LocalDateTime.of(2024, 8, 29, 10, 31),
                        LocalDateTime.of(2024, 8, 29, 10, 32)),
                Arguments.of("1 1 1 1 * /usr/bin/find", LocalDateTime.of(2024, 8, 29, 10, 31),
                        LocalDateTime.of(2025, 1, 1, 1, 1)),
                Arguments.of("1 1 1 * * /usr/bin/find", LocalDateTime.of(2024, 8, 29, 10, 31),
                        LocalDateTime.of(2024, 9, 1, 1, 1)),
                Arguments.of("1 1 1 1 1 /usr/bin/find", LocalDateTime.of(2024, 8, 29, 10, 31),
                        LocalDateTime.of(2029, 1, 1, 1, 1))
        );
    }

    @Test
    void testToString() {
        String expectedOutput = """
                minute        0 15 30 45
                hour          0
                day of month  1 15
                month         1 2 3 4 5 6 7 8 9 10 11 12
                day of week   1 2 3 4 5
                command       /usr/bin/find
                """;

        CronSchedule cronSchedule = new CronSchedule("/usr/bin/find", List.of(
                new TimeWindow(TimeWindowType.MINUTE, new TreeSet<>(List.of(0, 15, 30, 45))),
                new TimeWindow(TimeWindowType.HOUR, new TreeSet<>(List.of(0))),
                new TimeWindow(TimeWindowType.DAY_OF_MONTH, new TreeSet<>(List.of(1, 15))),
                new TimeWindow(TimeWindowType.MONTH, new TreeSet<>(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12))),
                new TimeWindow(TimeWindowType.DAY_OF_WEEK, new TreeSet<>(List.of(1, 2, 3, 4, 5)))));
        assertEquals(expectedOutput, cronSchedule.toString());
    }

    @ParameterizedTest
    @MethodSource("provideInputAndOutput")
    void testCronExpression(String cronExpression, LocalDateTime input, LocalDateTime output) throws InvalidCronExpressionException {
        CronParser cronParser = new CronParser();
        CronSchedule cronSchedule = cronParser.parseExpression(cronExpression);
        Optional<LocalDateTime> time = cronSchedule.getNextScheduleTime(input);
        assertTrue(time.isPresent());
        assertEquals(output, time.get());
    }
}