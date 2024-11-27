package org.example.cron.dtos;

import lombok.Getter;

@Getter
public enum TimeWindowType {
    MINUTE ("minute", 0, 59, 0),
    HOUR ("hour", 0, 23, 1),
    DAY_OF_MONTH ("day of month", 1, 31, 2),
    MONTH ("month", 1, 12, 3),
    DAY_OF_WEEK("day of week", 0, 6, 4);

    private final String timeWindowType;
    private final int startValue;
    private final int endValue;
    private final int index;
    TimeWindowType(String s, int startValue, int endValue, int index){
        timeWindowType = s;
        this.startValue = startValue;
        this.endValue = endValue;
        this.index = index;
    }
    public String toString() {
        return this.timeWindowType;
    }
}
