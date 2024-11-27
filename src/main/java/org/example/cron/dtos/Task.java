package org.example.cron.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class Task implements Comparable<Task> {
    private LocalDateTime time;
    private Runnable func;
    private CronSchedule cronSchedule;

    @Override
    public int compareTo(Task t) {
        return time.compareTo(t.getTime());
    }
}
