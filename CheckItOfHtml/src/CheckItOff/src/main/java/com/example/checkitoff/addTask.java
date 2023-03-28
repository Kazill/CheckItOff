/*package com.example.checkitoff;

import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import java.time.LocalDate;
import javafx.scene.Scene;
import javafx.scene.control.ListView;

public class addTask {

    public void addTask(String name, LocalDate deadline) {
        // Generate a unique id for the new task
        int id = nextId++;

        if (name == null || name.trim().isEmpty()) {
            // Show error message to the user
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Task name cannot be empty");
            alert.showAndWait();
            return;
        }
        if (deadline == null || deadline.isBefore(LocalDate.now())) {
            // Show error message to the user
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid deadline");
            alert.setContentText("Deadline must be in the future");
            alert.showAndWait();
            return;
        }

        // Create a new task with the given name and deadline
        Task newTask = new Task(id, name);
        newTask.setDeadline(deadline);


        // Add the task to the database

        // Add the task to the to-do list
        taskList.add(newTask);

        // Update the UI to show the new task
        ListView<Task> listView = (ListView<Task>) scene.lookup("#taskListView");
        listView.getItems().add(newTask);
    }

    public void setDeadline(Task task, LocalDate deadline) {
        // Update the deadline for the task in the database

        // Update the deadline for the task in the to-do list
        task.setDeadline(deadline);
    }
}*/
