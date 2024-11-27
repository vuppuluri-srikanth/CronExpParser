package org.example.cron.parsers;

import lombok.AllArgsConstructor;
import lombok.Value;
import org.example.cron.dtos.TimeWindow;
import org.example.cron.dtos.TimeWindowType;
import org.example.cron.exceptions.InvalidCronExpressionException;

import java.util.TreeSet;

import static org.example.cron.constants.ErrorConstants.*;

@Value
@AllArgsConstructor
public class TimeWindowParser {
    TimeWindowType timeWindowType;

    public TimeWindow parse(String token) throws InvalidCronExpressionException {
        TimeWindow timeWindow = new TimeWindow(timeWindowType, new TreeSet<>());
        if(!token.matches("[0-9*,/-]*"))
            throw new InvalidCronExpressionException(String.format(TOKEN_INVALID_CHARS, timeWindowType));

        if(token.startsWith("*")){
            handleRepeatPattern(token, timeWindow);
        }else if(token.contains(",")){
            handleCSV(token, timeWindow);
        }else if(token.contains("-")){
            handleRange(token, timeWindow);
        }else{
            int num = parseNumber(token);
            timeWindow.getValues().add(num);
        }
        return timeWindow;
    }

    // Examples - 1-10
    private void handleRange(String token, TimeWindow timeWindow) throws InvalidCronExpressionException {
        String[] subTokens = token.split("-");
        int leftBound = parseNumber(subTokens[0]);
        int rightBound = parseNumber(subTokens[1]);
        for (int i = leftBound; i <= rightBound; i++) {
            timeWindow.getValues().add(i);
        }
    }

    // Examples - 10,20,30
    private void handleCSV(String token, TimeWindow timeWindow) throws InvalidCronExpressionException {
        String[] subTokens = token.split(",");
        for(String subToken : subTokens){
            int num = parseNumber(subToken);
            timeWindow.getValues().add(num);
        }
    }

    // Examples: */15 or *
    private void handleRepeatPattern(String token, TimeWindow timeWindow) throws InvalidCronExpressionException {
        int repeat = 1;
        if(token.length() > 1 && token.charAt(1) != '/'){
            throw new InvalidCronExpressionException(String.format(TOKEN_INVALID, timeWindowType));
        }
        if(token.length() > 1)
            repeat = parseNumber(token.split("/")[1]);

        for(int i = timeWindowType.getStartValue(); i <= timeWindowType.getEndValue(); i = i+repeat){
            timeWindow.getValues().add(i);
        }
    }

    // Validates the number based on the limits
    private int parseNumber(String expression) throws InvalidCronExpressionException {
        int number;
        try {
            number = Integer.parseInt(expression);
        }catch(NumberFormatException e){
            throw new InvalidCronExpressionException(String.format(TOKEN_INVALID_NOT_NUMBER, timeWindowType));
        }

        if(number < timeWindowType.getStartValue() || number > timeWindowType.getEndValue())
            throw new InvalidCronExpressionException(String.format(NUMBER_OUT_OF_BOUNDS,timeWindowType,
                    timeWindowType.getStartValue(), timeWindowType.getEndValue()));
        return number;
    }
}
