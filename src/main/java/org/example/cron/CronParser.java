package org.example.cron;

import org.example.cron.dtos.CronSchedule;
import org.example.cron.dtos.TimeWindow;
import org.example.cron.dtos.TimeWindowType;
import org.example.cron.exceptions.InvalidCronExpressionException;
import org.example.cron.parsers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.System.exit;
import static org.example.cron.constants.ErrorConstants.TOO_FEW_TOKENS;

public class CronParser {
    public static final String CRON_EXP = "([^\s]*) +([^\s]*) +([^\s]*) +([^\s]*) +([^\s]*) +(.*)";
    public static final Pattern CRON_PATTERN = Pattern.compile(CRON_EXP);

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Error: Cron expression not passed. Usage: CronParser <Cron Expression>");
            exit(1);
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
        Matcher matcher = CRON_PATTERN.matcher(expression.strip());

        if(!matcher.matches()){
            throw new InvalidCronExpressionException(TOO_FEW_TOKENS);
        }

        TimeWindowType[] types = TimeWindowType.values();
        List<TimeWindow> timeWindows = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            String token = matcher.group(i + 1);
            TimeWindowType type = types[i];

            TimeWindowParser timeWindowParser = new TimeWindowParser(type);
            timeWindows.add(timeWindowParser.parse(token));
        }

        String command = matcher.group(6);
        return new CronSchedule(command, timeWindows);
    }
}
