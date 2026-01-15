package com.example.errorstory.engine;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OfflineResult {
    private String title;
    private String story;
    private String explanation;
    private String example;
}
