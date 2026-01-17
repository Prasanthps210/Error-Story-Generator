package com.example.errorstory.engine;

import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ErrorParser {

    public ParsedError parse(String errorText) {

        ParsedError parsedError = new ParsedError();

        // Detect exception type
        if (errorText.contains("NullPointerException")) {

            parsedError.setExceptionType("NullPointerException");
        } else if (errorText.contains("ArrayIndexOutOfBounds")) {
            parsedError.setExceptionType("ArrayIndexOutOfBoundsException");
        } else if (errorText.contains("ArithmeticException")) {
            parsedError.setExceptionType("ArithmeticException");
        } else {
            parsedError.setExceptionType("UnknownException");
        }

        // Extract ClassName.java and line number
        Pattern pattern = Pattern.compile("(\\w+\\.java):(\\d+)");
        Matcher matcher = pattern.matcher(errorText);

        if (matcher.find()) {
            parsedError.setClassName(matcher.group(1));
            parsedError.setLineNumber(Integer.parseInt(matcher.group(2)));
        } else {
            parsedError.setClassName("UnknownLand");
            parsedError.setLineNumber(0);
        }

        return parsedError;
    }
}
