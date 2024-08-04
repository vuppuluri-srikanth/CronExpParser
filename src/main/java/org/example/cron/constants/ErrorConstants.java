package org.example.cron.constants;

public class ErrorConstants {
    public static final String EXP_INVALID_FORMAT = "Cron Expression is invalid. %s";
    public static final String TOO_FEW_TOKENS = "Too few tokens in the expression";
    public static final String TOKEN_INVALID_CHARS = "'%s' contains invalid characters";
    public static final String TOKEN_INVALID_NOT_NUMBER = "'%s' is invalid. Invalid number provided";
    public static final String NUMBER_OUT_OF_BOUNDS = "'%s' is invalid. Number provided is outside bounds: %d - %d";
}
