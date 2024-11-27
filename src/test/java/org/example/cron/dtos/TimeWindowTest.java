package org.example.cron.dtos;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TimeWindowTest {

    @Test
    void testToString() {
        TimeWindow timeWindow = new TimeWindow(TimeWindowType.MINUTE, new TreeSet<>(List.of(1, 2, 3)));
        assertEquals("minute        1 2 3", timeWindow.toString());
    }
}