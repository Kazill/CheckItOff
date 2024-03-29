package com.example.checkitoff;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import net.synedra.validatorfx.Check;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class TaskController implements Initializable {

    @FXML
    private AnchorPane AddBar;
    @FXML
    private Button AddNewTask;
    @FXML
    private Button XTaskButton;
    @FXML
    private Button CategoryButton;
    @FXML
    private AnchorPane CategoryList;
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
    @FXML
    private ColorPicker colorPicker;
    @FXML
    private TextField categoryName;
    @FXML
    private Button X;
    @FXML
    private ChoiceBox CategoryPicker;
    @FXML
    private Label TaskCount;
    @FXML
    private Button SearchButton;
    @FXML
    private TextField SearchText;
    @FXML
    private CheckBox Daily;
    @FXML
    private Label Title;
    @FXML
    private Label DescriptionLabel;
    @FXML
    private Label EndDate;

    //Calendar
    ZonedDateTime dateFocus;
    ZonedDateTime today;

    @FXML
    private Text year;

    @FXML
    private Text month;

    @FXML
    private FlowPane calendar;
    @FXML
    private AnchorPane ShowCalendar;
    //Calendar

    private int checkBoxCount = 0;
    private int descriptionCount = 1;
    private int dateCount = 2;

    private int CategoryNameCount = 0;
    private int taskCount = 0;


    @FXML
    void onAddButtonClick(ActionEvent event) {
        //Sets the Task form visible if its already not
        if (!AddBar.isVisible()) {
            AddBar.setVisible(true);
            AddBar.toFront();
        }
    }

    @FXML
    public void handleSearchButton(ActionEvent event) {
        String searchText = SearchText.getText().trim(); // Get text and remove leading/trailing whitespaces
        List<Task> tasks = DBUtils.getTaskForm(); // Get all tasks

        if (!searchText.isEmpty()) {
            // Filter tasks based on search text (ignoring case)
            tasks = tasks.stream()
                    .filter(task -> task.getName().toLowerCase().contains(searchText.toLowerCase()))
                    .collect(Collectors.toList());
        }

        // Clear and load the taskList with the filtered tasks
        taskList.getChildren().clear();
        checkBoxCount = 0;
        descriptionCount = 1;
        dateCount = 2;
        taskCount = 0;
        addTasksToTaskList(tasks);
        Today.setText("Found");
    }



    @FXML
    private void closeAnchorPane(ActionEvent event) {
        AddBar.setVisible(false);
    }

    @FXML
    public void onSaveButtonClick(ActionEvent actionEvent) {
        //Sets the Task form invisible if its already visible
        if (AddBar.isVisible()) {
            AddBar.setVisible(false);
        }

        //Data transfer to the database
        DBUtils.sendTaskForm(textFieldName.getText(), textAreaDescription.getText(), Date.getValue() + "", Daily.isSelected());
        //Adding task with given data

        addNewTask(textFieldName.getText(), textAreaDescription.getText(), Date.getValue() + "", false);
        //Form cleanup
        textFieldName.clear();
        textAreaDescription.clear();
        Daily.setSelected(false);
    }

    public void Notification(String error) {
        //Pop up with text for error
    }

    public void addNewTask(String name, String description, String date, boolean isChecked) {
        CheckBox newCheckBox = new CheckBox(name);
        Label newDescription = new Label(description);
        Label newDate = new Label(date);
        newCheckBox.setSelected(isChecked);
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
        taskCount++;
        TaskCount.setText("" + taskCount);
    }

    //helping method loads all checkBox elements with a label text next to it and a status
    // to a taskList AnchorPane.
    public void loadTasks(int menu) {
        taskList.getChildren().clear();
        checkBoxCount = 0;
        descriptionCount = 1;
        dateCount = 2;

        if (menu == 0) {
            // Change to Upcoming
            ShowCalendar.setVisible(false);
            List<Task> tasks = DBUtils.getTaskForm();
            // Filter tasks to get only upcoming tasks
            tasks = tasks.stream()
                    .filter(task -> LocalDate.parse(task.getDate()).isAfter(LocalDate.now()))
                    .collect(Collectors.toList());
            Today.setText("Upcoming");
            if (!AddNewTask.isVisible()) {
                AddNewTask.setVisible(true);
            }
            Title.setVisible(true);
            DescriptionLabel.setVisible(true);
            EndDate.setVisible(true);
            addTasksToTaskList(tasks);
        } else if (menu == 1) {
            // Change to Today
            ShowCalendar.setVisible(false);
            List<Task> tasks = DBUtils.getTaskForm();
            // Filter tasks to get only today's tasks
            tasks = tasks.stream()
                    .filter(task -> LocalDate.parse(task.getDate()).isEqual(LocalDate.now()))
                    .collect(Collectors.toList());
            Today.setText("Today");
            if (!AddNewTask.isVisible()) {
                AddNewTask.setVisible(true);
            }
            Title.setVisible(true);
            DescriptionLabel.setVisible(true);
            EndDate.setVisible(true);
            addTasksToTaskList(tasks);
        }
        else if (menu == 2) {
            // Change to Calendar
            Today.setText("Calendar");
            AddNewTask.setVisible(false);
            ShowCalendar.setVisible(true);
            Title.setVisible(false);
            DescriptionLabel.setVisible(false);
            EndDate.setVisible(false);
            TaskCount.setText("");
        } else {
            // ERROR
            Notification("Error");
        }
    }

    private void addTasksToTaskList(List<Task> tasks) {
        for (Task task : tasks) {
            String name = task.getName();
            String description = task.getDescription();
            String date = task.getDate();
            boolean isChecked = DBUtils.isTaskChecked(name);
            CheckBox newCheckBox = new CheckBox(name);

            newCheckBox.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    String taskName = newCheckBox.getText();
                    boolean isChecked = newCheckBox.isSelected();
                    DBUtils.updateTask(taskName, isChecked);
                }
            });

            addNewTask(name, description, date, isChecked);
        }
    }


    public void menuSetup(String[] text) {
        taskCount = 0;
        TaskCount.setText("" + taskCount);
        ObservableList<String> Text = FXCollections.observableArrayList();
        for (int i = 0; i < text.length; i++) {
            Text.add(text[i]);
        }
        MenuList.setItems(Text);
    }

    public void onAddNewCategory() throws IOException {

        Stage category = new Stage();
        // Užkrauname antrąjį FXML failą
        FXMLLoader loader = new FXMLLoader(TaskController.class.getResource("add_category.fxml"));
        Parent root = loader.load();

        // Sukuriame antrą langą
        category.setTitle("CheckItOff");
        category.setScene(new Scene(root));

        // Nustatome antrojo lango valdiklį
        TaskController secondController = loader.getController();

        // Atidaryti antrą langą ir rodyti jį
        category.show();
        addNewCategory("yes", Color.color(1, 1, 1));//addNewCategory(categoryName.getText(), colorPicker.getValue());
        ObservableList CategoryItems = CategoryPicker.getItems();
        CategoryItems.add(CategoryNameCount / 2, "yes");//CategoryItems.add(CategoryNameCount / 2, categoryName.getText())
        CategoryPicker.setItems(CategoryItems);
    }

    public void onMenuSelection() {
        taskCount = 0;
        TaskCount.setText("" + taskCount);
        int n = MenuList.getSelectionModel().getSelectedIndex();
        loadTasks(n);
    }

    public void addNewCategory(String name, Color categoryColor) {
        Label newName = new Label(name);
        newName.setTextFill(categoryColor);
        CategoryList.getChildren().add(newName);
        Node node = CategoryList.getChildren().get(CategoryNameCount);
        node.setLayoutY(10 * CategoryNameCount);
        node.setLayoutX(10);
        CategoryNameCount = CategoryNameCount + 1;
    }

    //X button click on category window
    public void onXButtonClick(ActionEvent actionEvent) {

        //Adding category with given data
        if (categoryName.getText() != "" && CategoryList != null) {
            //if (CategoryList != null) {
            addNewCategory(categoryName.getText(), colorPicker.getValue());
            //}
            //Closing the window after category is added
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.close();
        } else {
            //Closing the window
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.close();
        }
        //Form cleanup
        categoryName.clear();
        //colorPicker.clear();
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
            // Start timer to check for reminders every hour
            //Timer timer = new Timer();
            //timer.schedule(new RemindersTask(), 0, TimeUnit.HOURS.toMillis(1));
        DBUtils.createDailyTasks();
        dateFocus = ZonedDateTime.now();
        today = ZonedDateTime.now();
        if (year != null) {
            drawCalendar();
        }
        //Setting the Task form to be invisible
        if (AddBar != null) {
            AddBar.setVisible(false);
        }

        //Loading DataBase data to selected menu type.

        //DataBase imitation
        String[] DataBase_TaskName = {"Do laundry", "Do dishes"};
        boolean[] DataBase_TaskCompletion = {true, false};

        if (Today != null) {
            //Loading Tasks
            loadTasks(1);
        }

        //MenuList setup
        String[] text = {"Upcoming", "Today", "Calendar"};
        if (MenuList != null) {
            menuSetup(text);
        }
    }


    @FXML
    void backOneMonth(ActionEvent event) {
        dateFocus = dateFocus.minusMonths(1);
        calendar.getChildren().clear();
        drawCalendar();
    }

    @FXML
    void forwardOneMonth(ActionEvent event) {
        dateFocus = dateFocus.plusMonths(1);
        calendar.getChildren().clear();
        drawCalendar();
    }

    private void drawCalendar() {
        year.setText(String.valueOf(dateFocus.getYear()));
        month.setText(String.valueOf(dateFocus.getMonth()));

        double calendarWidth = calendar.getPrefWidth();
        double calendarHeight = calendar.getPrefHeight();
        double strokeWidth = 1;
        double spacingH = calendar.getHgap();
        double spacingV = calendar.getVgap();

        //List of activities for a given month
        Map<Integer, List<CalendarActivity>> calendarActivityMap = getCalendarActivitiesMonth(dateFocus);

        int monthMaxDate = dateFocus.getMonth().maxLength();
        //Check for leap year
        if (dateFocus.getYear() % 4 != 0 && monthMaxDate == 29) {
            monthMaxDate = 28;
        }
        int dateOffset = ZonedDateTime.of(dateFocus.getYear(), dateFocus.getMonthValue(), 1, 0, 0, 0, 0, dateFocus.getZone()).getDayOfWeek().getValue();

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                StackPane stackPane = new StackPane();

                Rectangle rectangle = new Rectangle();
                rectangle.setFill(Color.TRANSPARENT);
                rectangle.setStroke(Color.BLACK);
                rectangle.setStrokeWidth(strokeWidth);
                double rectangleWidth = (calendarWidth / 7) - strokeWidth - spacingH;
                rectangle.setWidth(rectangleWidth);
                double rectangleHeight = (calendarHeight / 6) - strokeWidth - spacingV;
                rectangle.setHeight(rectangleHeight);
                stackPane.getChildren().add(rectangle);
                int calculatedDate = (j + 1) + (7 * i);
                if (calculatedDate > dateOffset) {
                    int currentDate = calculatedDate - dateOffset;
                    if (currentDate <= monthMaxDate) {
                        Text date = new Text(String.valueOf(currentDate));
                        double textTranslationY = -(rectangleHeight / 2) * 0.75;
                        date.setTranslateY(textTranslationY);
                        stackPane.getChildren().add(date);

                        List<CalendarActivity> calendarActivities = calendarActivityMap.get(currentDate);
                        if (calendarActivities != null) {
                            createCalendarActivity(calendarActivities, rectangleHeight, rectangleWidth, stackPane);
                        }
                    }
                    if (today.getYear() == dateFocus.getYear() && today.getMonth() == dateFocus.getMonth() && today.getDayOfMonth() == currentDate) {
                        rectangle.setStroke(Color.BLUE);
                    }
                }
                stackPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        /*stackPane.setVisible(false);*/
                    }
                });
                calendar.getChildren().add(stackPane);
            }
        }
    }

    private void createCalendarActivity(List<CalendarActivity> calendarActivities, double rectangleHeight, double rectangleWidth, StackPane stackPane) {
        VBox calendarActivityBox = new VBox();
        for (int k = 0; k < calendarActivities.size(); k++) {
            if (k >= 2) {
                Text moreActivities = new Text("...");
                calendarActivityBox.getChildren().add(moreActivities);
                moreActivities.setOnMouseClicked(mouseEvent -> {
                    //On ... click print all activities for given date
                    System.out.println(calendarActivities);
                });
                break;
            }
            Text text = new Text(calendarActivities.get(k).getClientName() + ", " + calendarActivities.get(k).getDate().toLocalTime());
            calendarActivityBox.getChildren().add(text);
            text.setOnMouseClicked(mouseEvent -> {
                //On Text clicked
                System.out.println(text.getText());
            });
        }
        calendarActivityBox.setTranslateY((rectangleHeight / 2) * 0.20);
        calendarActivityBox.setMaxWidth(rectangleWidth * 0.8);
        calendarActivityBox.setMaxHeight(rectangleHeight * 0.65);
        calendarActivityBox.setStyle("-fx-background-color:GRAY");
        stackPane.getChildren().add(calendarActivityBox);
    }

    private Map<Integer, List<CalendarActivity>> createCalendarMap(List<CalendarActivity> calendarActivities) {
        Map<Integer, List<CalendarActivity>> calendarActivityMap = new HashMap<>();

        for (CalendarActivity activity : calendarActivities) {
            int activityDate = activity.getDate().getDayOfMonth();
            if (!calendarActivityMap.containsKey(activityDate)) {
                calendarActivityMap.put(activityDate, List.of(activity));
            } else {
                List<CalendarActivity> OldListByDate = calendarActivityMap.get(activityDate);

                List<CalendarActivity> newList = new ArrayList<>(OldListByDate);
                newList.add(activity);
                calendarActivityMap.put(activityDate, newList);
            }
        }
        return calendarActivityMap;
    }

    private Map<Integer, List<CalendarActivity>> getCalendarActivitiesMonth(ZonedDateTime dateFocus) {
        List<CalendarActivity> calendarActivities = new ArrayList<>();
        int year = dateFocus.getYear();
        int month = dateFocus.getMonth().getValue();

        Random random = new Random();
        List<Task> tasks = DBUtils.getTaskForm();
        for (Task task : tasks) {
            String name = task.getName();
            String date = task.getDate();
            LocalDate localDate = LocalDate.parse(date);
            ZonedDateTime zonedDateTime = localDate.atStartOfDay(ZoneId.systemDefault());
            calendarActivities.add(new CalendarActivity(zonedDateTime, name));
        }
        return createCalendarMap(calendarActivities);
    }

    //Reminder
    @FXML
    private Label notifLabel;

    private void showReminder(String taskName, LocalDate taskDeadline) throws IOException {
        Platform.runLater(() -> {
            // Check if task is due today or was due yesterday
            LocalDate today = LocalDate.now();
            if (taskDeadline.isEqual(today)) {
                // Create reminder/notification UI
                FXMLLoader loader = new FXMLLoader(ReminderController.class.getResource("notification.fxml"));
                try {
                    Parent root = loader.load();
                    //Label messageLabel = (Label) loader.getNamespace().get("messageLabel");
                    notifLabel.setText("You have a task due today: " + taskName);
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
                    //Label messageLabel = (Label) loader.getNamespace().get("messageLabel");
                    //if (messageLabel != null) {
                    notifLabel.setText("You missed a deadline yesterday: " + taskName);
                    //}
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

    class RemindersTask extends TimerTask {
        @Override
        public void run() {
            // Retrieve task data from database
            List<Task> tasks = DBUtils.getTaskForm(); // replace with actual user ID
            if (tasks != null && !tasks.isEmpty()) {
                for (Task task : tasks) {
                    String taskName = task.getName();
                    String taskDescription = task.getDescription();
                    LocalDate taskDeadline = LocalDate.parse(task.getDate()); // assuming that the date in Task is in a format that can be parsed to LocalDate
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

}

