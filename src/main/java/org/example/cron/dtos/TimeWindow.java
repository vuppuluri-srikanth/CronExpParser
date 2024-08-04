package org.example.cron.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;

import java.util.List;

@Value
@AllArgsConstructor
public class TimeWindow {
    TimeWindowType timeWindowType;
    @Getter
    List<Integer> values;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-13s", timeWindowType.toString()));
        for (int value : values) {
            sb.append(" ");
            sb.append(value);
        }
        return sb.toString();
    }
}
