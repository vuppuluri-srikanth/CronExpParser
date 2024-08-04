package org.example.cron.dtos;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.List;

@Value
@AllArgsConstructor
public class CronSchedule {
    String command;
    List<TimeWindow> timeWindows;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (TimeWindow timeWindow : timeWindows) {
            sb.append(timeWindow.toString());
            sb.append("\n");
        }
        sb.append(String.format("%-14s%s", "command", command));
        sb.append("\n");
        return sb.toString();
    }
}
