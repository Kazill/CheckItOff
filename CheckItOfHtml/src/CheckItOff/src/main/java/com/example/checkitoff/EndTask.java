/*package com.example.checkitoff;

import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import java.time.LocalDate;
import javafx.scene.control.ListView;


import java.io.IOException;

public class EndTask {
    @Override
    public void completeTask(int taskId) {
        // Find the task in the to-do list using the taskId
        Task task = null;
        for (Task t : todoList) {
            if (t.getId() == taskId) {
                task = t;
                break;
            }
        }
        if (task == null) {
            System.out.println("Task not found!");
            return;
        }

        // Update the task in the database

        // Update the task in the to-do list
        task.setStatus("completed");
        task.setCompletedDate(LocalDate.now());

        // Update the task view
        String taskText = task.toString();
        String completedTaskText = "✓ " + taskText;
        int taskIndex = taskListView.getItems().indexOf(taskText);
        taskListView.getItems().set(taskIndex, completedTaskText);
    }

}*/
