package org.example.cron;

import org.example.cron.dtos.CronSchedule;
import org.example.cron.dtos.TimeWindow;
import org.example.cron.dtos.TimeWindowType;
import org.example.cron.exceptions.InvalidCronExpressionException;
import org.example.cron.parsers.*;

import java.util.ArrayList;
import java.util.List;

public class CronParser {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Cron expression not passed. Usage: CronParser <Cron Expression>");
        }
        CronParser cronParser = new CronParser();
        try {
            CronSchedule cronSchedule = cronParser.parseExpression(args[0]);
            System.out.println(cronSchedule);
        } catch (InvalidCronExpressionException e) {
            System.out.println(e.getMessage());
        }
    }

    public CronSchedule parseExpression(String expression) throws InvalidCronExpressionException{
        validateExpression(expression);
        String[] tokens = expression.strip().split(" ");
        TimeWindowType[] types = TimeWindowType.values();
        List<TimeWindow> windows = new ArrayList<>();
        for(int i = 0; i < types.length; i++){
            windows.add(new TimeWindowParser(types[i]).parse(tokens[i]));
        }
        return new CronSchedule(tokens[5], windows);
    }

    private void validateExpression(String expression) throws InvalidCronExpressionException {
        if (expression.isBlank())
            throw new InvalidCronExpressionException("Expression is empty");
        if (expression.contains("?"))
            throw new InvalidCronExpressionException("? is not supported");

        String[] tokens = expression.strip().split(" ");
        if(tokens.length < 6)
            throw new InvalidCronExpressionException("Expression has less than 6 tokens");
        if(tokens.length > 6)
            throw new InvalidCronExpressionException("Expression has more than 6 tokens");
    }
}
