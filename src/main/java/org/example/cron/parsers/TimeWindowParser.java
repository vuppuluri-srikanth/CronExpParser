package org.example.cron.parsers;

import lombok.AllArgsConstructor;
import lombok.Value;
import org.example.cron.dtos.TimeWindow;
import org.example.cron.dtos.TimeWindowType;
import org.example.cron.exceptions.InvalidCronExpressionException;

import java.util.ArrayList;

@Value
@AllArgsConstructor
public class TimeWindowParser {
    TimeWindowType timeWindowType;

    public TimeWindow parse(String expression) throws InvalidCronExpressionException {
        TimeWindow timeWindow = new TimeWindow(TimeWindowType.MINUTE, new ArrayList<>());
        if(!expression.matches("[0-9*,/-]*"))
            throw new InvalidCronExpressionException(String.format("'%s' contains invalid characters", timeWindowType));

        if(expression.startsWith("*")){
            int repeat = 1;
            if(expression.length() > 1 && expression.charAt(1) != '/'){
                throw new InvalidCronExpressionException(String.format("'%s' has a invalid format", timeWindowType));
            }
            if(expression.length() > 1)
                repeat = parseNumber(expression.split("/")[1]);

            for(int i = timeWindowType.getStartValue(); i <= timeWindowType.getEndValue(); i = i+repeat){
                timeWindow.getValues().add(i);
            }
        }else if(expression.contains(",")){
            String[] tokens = expression.split(",");
            for(String token : tokens){
                int num = parseNumber(token);
                timeWindow.getValues().add(num);
            }
        }else if(expression.contains("-")){
            String[] tokens = expression.split("-");
            int leftBound = parseNumber(tokens[0]);
            int rightBound = parseNumber(tokens[1]);
            for (int i = leftBound; i <= rightBound; i++) {
                timeWindow.getValues().add(i);
            }
        }else{
            int num = parseNumber(expression);
            timeWindow.getValues().add(num);
        }
        return timeWindow;
    }

    private int parseNumber(String expression) throws InvalidCronExpressionException {
        int number;
        try {
            number = Integer.parseInt(expression);
        }catch(NumberFormatException e){
            throw new InvalidCronExpressionException(String.format("'%s' is invalid. Invalid number provided", timeWindowType));
        }
        if(number < timeWindowType.getStartValue() || number > timeWindowType.getEndValue())
            throw new InvalidCronExpressionException(String.format("'%s' is invalid. Number provided is outside bounds: %d - %d",
                    timeWindowType, timeWindowType.getStartValue(), timeWindowType.getEndValue()));
        return number;
    }
}
