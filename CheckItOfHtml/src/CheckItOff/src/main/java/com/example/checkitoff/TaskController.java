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

    @FXML
    private DatePicker Date;

    private int checkBoxCount = 0;
    private int descriptionCount = 1;
    private int dateCount = 2;

    //private List<Task> todoList = new ArrayList<>();

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
        addNewTask(textFieldName.getText(), textAreaDescription.getText(), Date.getValue() + "");
        //Form cleanup
        textFieldName.clear();
        textAreaDescription.clear();
    }

    public void Notification(String error)
    {
        //Pop up with text for error
    }

    public void addNewTask(String name, String description, String date)
    {
        CheckBox newCheckBox = new CheckBox(name);
        Label newDescription = new Label(description);
        Label newDate = new Label(date);
        newCheckBox.setSelected(false);
        taskList.getChildren().add(newCheckBox);
        taskList.getChildren().add(newDescription);
        taskList.getChildren().add(newDate);
        Node node = taskList.getChildren().get(checkBoxCount);
        node.setLayoutY(10 * checkBoxCount);
        node = taskList.getChildren().get(descriptionCount);
        node.setLayoutY(10 * checkBoxCount);
        node.setLayoutX(50 +  200);
        node = taskList.getChildren().get(dateCount);
        node.setLayoutY(10 * checkBoxCount);
        node.setLayoutX(50 + 500);
        checkBoxCount = checkBoxCount + 3;
        descriptionCount = descriptionCount + 3;
        dateCount = dateCount + 3;
    }
    //helping method loads all checkBox elements with a label text next to it and a status
    // to a taskList AnchorPane.
    public void loadTasks(int menu) {
        String[] name = new String[0];
        String[] description = new String[0];
        boolean[] status = new boolean[0];
        String[] date = new String[0];
        if (menu == 0) {
            //Change to Upcoming
            String[] Name = {"Prepare for exam", "Grocery shopping"};
            name = Name;
            String[] Description = {"Prepare for tommorows examination on math", "Buy milk, cheese, bread"};
            description = Description;
            boolean[] Status = {true, false};
            status = Status;
            String[] Date = {"2023-05-15", "2023-04-30"};
            date = Date;
            if(AddNewTask.isVisible() == false)
            {
                AddNewTask.setVisible(true);
            }
            Today.setText("Upcoming");
        } else if (menu == 1) {
            //Change to Today
            String[] Name = {"Do laundry", "Do dishes"};
            name = Name;
            String[] Description = {"And don't forget to fold the laundry", "do dishes"};
            description = Description;
            boolean[] Status = {true, false};
            status = Status;
            String[] Date = {"2023-05-15", "2023-04-30"};
            date = Date;
            Today.setText("Today");
            if(AddNewTask.isVisible() == false)
            {
                AddNewTask.setVisible(true);
            }
        } else if (menu == 2) {
            //Change to Calendar
            Today.setText("Calendar");
            AddNewTask.setVisible(false);

        } else {
            //ERROR
            Notification("Error");
        }
        taskList.getChildren().clear();
        checkBoxCount = 0;
        descriptionCount = 1;
        dateCount = 2;
        for (int i = 0; i < name.length; i++) {
            CheckBox newCheckBox = new CheckBox(name[i]);
            Label newDescription = new Label(description[i]);
            Label newDate = new Label(date[i]);
            newCheckBox.setSelected(status[i]);
            taskList.getChildren().add(newCheckBox);
            taskList.getChildren().add(newDescription);
            taskList.getChildren().add(newDate);
            Node node = taskList.getChildren().get(checkBoxCount);
            node.setLayoutY(10 * checkBoxCount);
            node = taskList.getChildren().get(descriptionCount);
            node.setLayoutY(10 * checkBoxCount);
            node.setLayoutX(50 + 200);
            node = taskList.getChildren().get(dateCount);
            node.setLayoutY(10 * checkBoxCount);
            node.setLayoutX(50 + 500);
            checkBoxCount = checkBoxCount + 3;
            descriptionCount = descriptionCount + 3;
            dateCount = dateCount + 3;
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