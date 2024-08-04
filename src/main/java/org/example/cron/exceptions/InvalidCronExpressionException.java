package org.example.cron.exceptions;

import static org.example.cron.constants.ErrorConstants.EXP_INVALID_FORMAT;

public class InvalidCronExpressionException extends Exception{
    public InvalidCronExpressionException(String s) {
        super(String.format(EXP_INVALID_FORMAT, s));
    }
}
