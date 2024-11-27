package org.example.cron.dtos;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Value
@AllArgsConstructor
public class CronSchedule {
    String command;
    List<TimeWindow> timeWindows;
    public static final int SIZE = 5;
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

    public Optional<LocalDateTime> getNextScheduleTime(LocalDateTime time){
        int[] currentValues = new int[SIZE];
        currentValues[0] = time.getYear();
        currentValues[1] = time.getMonthValue();
        currentValues[2] = time.getDayOfMonth();
        currentValues[3] = time.getHour();
        currentValues[4] = time.getMinute();

        int[] timeWindowTypesOfImportance = new int[5];
        for(int i = 0; i < SIZE; i++)
            timeWindowTypesOfImportance[i] = SIZE-i-1;

        for (int i = 1; i < SIZE; i++) {
            findNextTimeWindow(timeWindowTypesOfImportance, i, currentValues, i == SIZE - 1, time);
        }

        return Optional.of(LocalDateTime.of(currentValues[0], currentValues[1], currentValues[2], currentValues[3], currentValues[4]));
    }

    private void findNextTimeWindow(int[] timeWindowTypesOfImportance, int index, int[] currentValues, boolean higher, LocalDateTime currentDateTime) {
        int timeWindowType = timeWindowTypesOfImportance[index];
        int currentValue = currentValues[index];

        var allValues = timeWindows.get(timeWindowType).getValues();
        Integer ceilValue = higher ? allValues.higher(currentValue) : allValues.ceiling(currentValue);
        if(ceilValue == null) {
            if (index == 1)
                currentValues[0]++;
            else {
                if (!LocalDateTime.of(currentValues[0], currentValues[1], currentValues[2], currentValues[3], currentValues[4]).isAfter(currentDateTime))
                    findNextTimeWindow(timeWindowTypesOfImportance, index - 1, currentValues, true, currentDateTime);
            }
            currentValues[index] = allValues.first();
        }else{
            currentValues[index] = ceilValue;
        }

        if(index == 2 && !matchesDayOfWeek(currentValues)){
            findNextTimeWindow(timeWindowTypesOfImportance, index, currentValues, true, LocalDateTime.of(currentValues[0], currentValues[1], currentValues[2], currentValues[3], currentValues[4]));
        }
    }

    private boolean matchesDayOfWeek(int[] currentValues) {
        LocalDate time = LocalDate.of(currentValues[0], currentValues[1], currentValues[2]);
        int dayOfWeek = time.getDayOfWeek().getValue();
        if(dayOfWeek == 7) dayOfWeek = 0;
        return timeWindows.get(TimeWindowType.DAY_OF_WEEK.getIndex()).getValues().contains(dayOfWeek);
    }
}
