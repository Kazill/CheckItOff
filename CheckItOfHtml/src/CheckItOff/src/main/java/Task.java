package com.example.checkitoff;

public class Task {
    private int id;
    private String description;
    private LocalDate deadline;

    public Task(int id, String description, LocalDate deadline) {
        this.description = description;
        this.deadline = deadline;
    }

    // Getters and setters for all fields

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    // Override toString() method for displaying task in the ListView

    @Override
    public String toString() {
        return description;
    }
}
