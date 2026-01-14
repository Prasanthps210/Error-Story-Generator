package com.example.errorstory.engine;

import lombok.Data;

@Data
public class ParsedError {
    private String exceptionType;
    private String className;
    private int lineNumber;
}
