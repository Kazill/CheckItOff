package com.example.checkitoff;

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
    private ListView<?> CategoryList;
    @FXML
    private MenuItem Calendar;
    @FXML
    private Label Description;
    @FXML
    private ListView<?> MenuList;
    @FXML
    private Button SaveButton;
    @FXML
    private Label TaskName;
    @FXML
    private MenuItem Today;
    @FXML
    private MenuItem Upcoming;
    @FXML
    private TextArea textAreaDescription;
    @FXML
    private TextField textFieldName;

    @FXML
    private AnchorPane taskList;

    private int taskCount = 0;

    @FXML
    void onAddButtonClick(ActionEvent event) {
        //Sets the Task form visible if its already not
        if (!AddBar.isVisible()) {
            AddBar.setVisible(true);
        }
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
    //helping method loads all checkBox elements with a label text next to it and a status
    // to a taskList AnchorPane.
    public void loadTasks(String[] label, boolean[] status) {
        taskList.getChildren().clear();
        for(int i = 0; i < label.length; i++)
        {
            CheckBox checkBox = new CheckBox(label[i]);
            checkBox.setSelected(status[i]);
            taskList.getChildren().add(checkBox);
            Node node = taskList.getChildren().get(taskCount);
            node.setLayoutY(20 * taskCount);
            taskCount++;
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
        loadTasks(DataBase_TaskName, DataBase_TaskCompletion);
    }
}
