package org.example.cron.exceptions;

public class InvalidCronExpressionException extends Exception{
    public static final String EXP_INVALID = "Cron Expression is invalid. %s";
    public InvalidCronExpressionException(String s) {
        super(String.format(EXP_INVALID, s));
    }
}
