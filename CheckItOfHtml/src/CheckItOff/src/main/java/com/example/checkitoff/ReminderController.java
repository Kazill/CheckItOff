package com.example.checkitoff;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;


public class ReminderController implements Initializable {
     
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Start timer to check for reminders every hour
        Timer timer = new Timer();
        timer.schedule(new RemindersTask(), 0, TimeUnit.HOURS.toMillis(1));
    }

    private static void showReminder(String taskName, LocalDate taskDeadline) throws IOException {
        Platform.runLater(() -> {
            // Check if task is due today or was due yesterday
            LocalDate today = LocalDate.now();
            if (taskDeadline.isEqual(today)) {
                // Create reminder/notification UI
                FXMLLoader loader = new FXMLLoader(ReminderController.class.getResource("notification.fxml"));
                try {
                    Parent root = loader.load();
                    Label messageLabel = (Label) loader.getNamespace().get("messageLabel");
                    messageLabel.setText("You have a task due today: " + taskName);
                    Scene scene = new Scene(root);
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (taskDeadline.isBefore(today)) {
                // Create reminder/notification UI
                FXMLLoader loader = new FXMLLoader(ReminderController.class.getResource("notification.fxml"));
                try {
                    Parent root = loader.load();
                    Label messageLabel = (Label) loader.getNamespace().get("messageLabel");
                    if (messageLabel != null) {
                        messageLabel.setText("You missed a deadline yesterday: " + taskName);
                    }
                    Scene scene = new Scene(root);
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    static class RemindersTask extends TimerTask {
        @Override
        public void run() {
            // Retrieve task data from database
            String[] taskData = DBUtils.getTaskForm(); // replace with actual user ID
            if (taskData != null) {
                String taskName = taskData[0];
                String taskDescription = taskData[1];
                LocalDate taskDeadline = LocalDate.parse(taskData[2]);
                // Show reminder if necessary
                try {
                    showReminder(taskName, taskDeadline);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

/*
    @FXML
    private ListView<Task> taskListView;

    @FXML
    private Label notifLabel;

    private Timer timer;

    private ObservableList<Task> tasks;

    private DBUtils.TaskDAO taskDAO;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Initialize the taskDAO with the current user's ID
        taskDAO = new TaskDAO(userID);

        // Get all tasks for the current user
        tasks = taskDAO.getTasks();

        // Set the tasks list as the items of the taskListView
        taskListView.setItems(tasks);

        // Check for tasks with yesterday's deadline and show a notification
        for (Task task : tasks) {
            if (task.getDeadline().isBefore(LocalDate.now())) {
                notifLabel.appendText("Task \"" + task.getDescription() + "\" was due yesterday!\n");
            }
        }
    }
        // retrieve tasks from the database
        List<Task> tasks = DBUtils.TaskDAO.getTasks();

        // check if there is a task with a deadline of today
        boolean hasTodayTask = false;
        LocalDate today = LocalDate.now();
        for (Task task : tasks) {
            if (task.getDeadline().isEqual(today)) {
                hasTodayTask = true;
                break;
            }
        }
        if (hasTodayTask) {
            // create and schedule the timer task
            timer = new Timer();
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime reminderTime = now.withHour(14).withMinute(0).withSecond(0); // example: 2pm
            if (now.isAfter(reminderTime)) {
                // if current time is after reminder time, schedule for the next day
                reminderTime = reminderTime.plusDays(1);
            }
            long delay = Duration.between(now, reminderTime).toMillis();
            timer.schedule(new ReminderTask(), delay);
        }

        // populate task list view
        taskListView.getItems().addAll(tasks);
    }

    @Override
    public void finalize() {
        // terminate the timer when the application is closed
        if (timer != null) {
            timer.cancel();
        }
    }

    private class ReminderTask extends TimerTask {
        @Override
        public void run() {
            // retrieve tasks from the database
            List<Task> tasks = DBUtils.TaskDAO.getTasks();

            // generate reminder text
            StringBuilder sb = new StringBuilder();
            sb.append("Upcoming tasks:\n");
            for (Task task : tasks) {
                LocalDate today = LocalDate.now();
                if (task.getDeadline().isEqual(today)) {
                    sb.append(task.getDescription()).append(" is due today!\n");
                }
            }
            String reminderText = sb.toString().trim();

            // update reminder text area on the JavaFX application thread
            javafx.application.Platform.runLater(() -> notifLabel.setText(reminderText));

            // reschedule the timer task for the next day
            timer.schedule(new ReminderTask(), 24 * 60 * 60 * 1000);
        }
    }
}
 */
