package org.example.cron.dtos;

import lombok.Getter;

@Getter
public enum TimeWindowType {
    MINUTE ("minute", 0, 59),
    HOUR ("hour", 0, 23),
    DAY_OF_MONTH ("day of month", 1, 31),
    MONTH ("month", 1, 12),
    DAY_OF_WEEK("day of week", 0, 6);

    private final String timeWindowType;
    private final int startValue;
    private final int endValue;
    TimeWindowType(String s, int startValue, int endValue){
        timeWindowType = s;
        this.startValue = startValue;
        this.endValue = endValue;
    }
    public String toString() {
        return this.timeWindowType;
    }
}
