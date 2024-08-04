package org.example.cron.dtos;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CronScheduleTest {


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
                new TimeWindow(TimeWindowType.MINUTE, List.of(0, 15, 30, 45)),
                new TimeWindow(TimeWindowType.HOUR, List.of(0)),
                new TimeWindow(TimeWindowType.DAY_OF_MONTH, List.of(1, 15)),
                new TimeWindow(TimeWindowType.MONTH, List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)),
                new TimeWindow(TimeWindowType.DAY_OF_WEEK, List.of(1, 2, 3, 4, 5))));
        assertEquals(expectedOutput, cronSchedule.toString());
    }
}