package com.example.checkitoff;

import java.time.LocalDate;

public class Task {
    private String name;
    private String description;
    private String date;

    // Constructor
    public Task(String name, String description, String date) {
        this.name = name;
        this.description = description;
        this.date = date;
    }

    // Getters
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getDate() { return date; }
}
