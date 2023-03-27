package com.example.checkitoff;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.*;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class TaskController implements Initializable {

    @FXML
    private AnchorPane AddBar;
    @FXML
    private Button AddNewTask;
    @FXML
    private Button CategoryButton;
    @FXML
    private ListView<Task> CategoryList;
    @FXML
    private MenuItem Calendar;
    @FXML
    private Label Description;
    @FXML
    private ListView<String> MenuList;
    @FXML
    private Button SaveButton;
    @FXML
    private Label TaskName;
    //@FXML
    //private MenuItem Today;
    @FXML
    private MenuItem Upcoming;
    @FXML
    private TextArea textAreaDescription;
    @FXML
    private TextField textFieldName;
    @FXML
    private CheckBox checkBox;

    @FXML
    private AnchorPane taskList;

    @FXML
    private Label Today;

    private int taskCount = 0;
    
    private List<Task> todoList = new ArrayList<>();

    @FXML
    void onAddButtonClick(ActionEvent event) {
        //Sets the Task form visible if its already not
        if (!AddBar.isVisible()) {
            AddBar.setVisible(true);
        }
        String taskName = taskNameField.getText();
        LocalDate deadline = deadlinePicker.getValue();

        addTask taskManager = new addTask();
        taskManager.addTask(taskName, deadline);

        AddBar.setVisible(false);
    }

    @FXML
    public void onSaveButtonClick(ActionEvent actionEvent) {
        //Sets the Task form invisible if its already visible
        if (AddBar.isVisible()) {
            AddBar.setVisible(false);
        }

        //Data transfer to the database

        //Adding task with given data

        //Form cleanup
        textFieldName.clear();
        textAreaDescription.clear();
    }

    public void Notification(String error)
    {
        //Pop up with text for error
    }

    //helping method loads all checkBox elements with a label text next to it and a status
    // to a taskList AnchorPane.
    public void loadTasks(int menu) {
        String[] label = new String[0];
        boolean[] status = new boolean[0];
        if (menu == 0) {
            //Change to Upcoming
            String[] text = {"Prepare for exam", "Grocery shopping"};
            label = text;
            boolean[] Status = {true, false};
            status = Status;
            Today.setText("Upcoming");
        } else if (menu == 1) {
            //Change to Today
            String[] text = {"Do laundry", "Do dishes"};
            label = text;
            boolean[] Status = {true, false};
            status = Status;
            Today.setText("Today");
        } else if (menu == 2) {
            //Change to Calendar
            Today.setText("Calendar");
            AddNewTask.setVisible(false);

        } else {
            //ERROR
            Notification("Error");
        }
        taskList.getChildren().clear();
        taskCount = 0;
        for (int i = 0; i < label.length; i++) {
            CheckBox checkBox = new CheckBox(label[i]);
            checkBox.setSelected(status[i]);
            taskList.getChildren().add(checkBox);
            Node node = taskList.getChildren().get(taskCount);
            node.setLayoutY(20 * taskCount);
            taskCount++;
        }
    }

    public void menuSetup(String[] text) {
        ObservableList<String> Text = FXCollections.observableArrayList();
        for (int i = 0; i < text.length; i++) {
            Text.add(text[i]);
        }
        MenuList.setItems(Text);
    }

    public void onMenuSelection() {
        int n = MenuList.getSelectionModel().getSelectedIndex();
        loadTasks(n);
    }

    @FXML
    void onTaskCompleted(ActionEvent event) {
        CheckBox checkbox = (CheckBox) event.getSource();
        String taskText = checkbox.getText();
        Task task = null;

        // Get the corresponding task from the to-do list
        for (Task t : todoList) {
            if (t.getText().equals(taskText)) {
                task = t;
                break;
            }
        }

        if (task != null) {
            EndTask.completeTask(task);

        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Setting the Task form to be invisible
        AddBar.setVisible(false);

        //Loading DataBase data to selected menu type.

        //DataBase imitation
        String[] DataBase_TaskName = {"Do laundry", "Do dishes"};
        boolean[] DataBase_TaskCompletion = {true, false};

        //Loading Tasks
        loadTasks(1);

        //MenuList setup
        String[] text = {"Upcoming", "Today", "Calendar"};
        menuSetup(text);
    }
}
