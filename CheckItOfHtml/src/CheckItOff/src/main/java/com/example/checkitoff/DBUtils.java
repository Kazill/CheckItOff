package com.example.checkitoff;

import java.sql.*;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class DBUtils {
    static Stage stage = new Stage();
    static String userID = null;

    public static void changeScene(ActionEvent event, String fxmlFile, String title, String username) {
        Parent root = null;

        if (username != null) {
            try {
                FXMLLoader loader = new FXMLLoader(DBUtils.class.getResource(fxmlFile));
                root = loader.load();
                LoggedInController loggedInController = loader.getController();
                loggedInController.setUserInformation(username);
            } catch (IOException e) {
                //e.printStackTrace();
            }
        } else {
            try {
                root = FXMLLoader.load(DBUtils.class.getResource(fxmlFile));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }

    public static void signUpUser(ActionEvent event, String username, String password) {
        Connection connection = null;
        PreparedStatement psInsert = null;
        PreparedStatement psCheckUserExists = null;
        ResultSet resultSet = null;

        try {
            // load and register JDBC driver for MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/checkitoff", "root", "");
            psCheckUserExists = connection.prepareStatement("SELECT * FROM user WHERE username = ?");
            psCheckUserExists.setString(1, username);
            resultSet = psCheckUserExists.executeQuery();

            if (resultSet.isBeforeFirst()) {
                System.out.println("User already exists!");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("You cannot use this username.");
                alert.show();
            } else {
                psInsert = connection.prepareStatement("INSERT INTO `user` (`ID`, `Username`, `Password`) VALUES (NULL, ?, ?)");
                psInsert.setString(1, username);
                psInsert.setString(2, password);
                psInsert.executeUpdate();

                changeScene(event, "logged_in.fxml", "Welcome!", username);
                openSecondWindow("hello-view.fxml");
            }
        } catch (SQLException e) {
            e.printStackTrace();

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (psCheckUserExists != null) {
                try {
                    psCheckUserExists.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (psInsert != null) {
                try {
                    psInsert.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void logInUser(ActionEvent event, String username, String password) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            // load and register JDBC driver for MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/checkitoff", "root", "");
            preparedStatement = connection.prepareStatement("SELECT ID, password FROM user WHERE username = ?");
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();

            if (!resultSet.isBeforeFirst()) {
                System.out.println("User not found in the database!");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Provided credentials are incorrect!");
                alert.show();
            } else {
                while (resultSet.next()) {
                    String retrievedPassword = resultSet.getString("password");
                    String retrievedID = resultSet.getString("ID");
                    if (retrievedPassword.equals(password)) {
                        userID = retrievedID;
                        changeScene(event, "logged_in.fxml", "Welcome!", username);
                        openSecondWindow("hello-view.fxml");
                    } else {
                        System.out.println("Passwords did not match!");
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("The provided credentials are incorrect!");
                        alert.show();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void closeWindow(ActionEvent event, String fxmlFile) {
        stage.close();
    }

    public static void openSecondWindow(String fxmlFile) {
        try {
            // Užkrauname antrąjį FXML failą
            FXMLLoader loader = new FXMLLoader(DBUtils.class.getResource(fxmlFile));
            Parent root = loader.load();

            // Sukuriame antrą langą
            stage.setTitle("CheckItOff");
            stage.setScene(new Scene(root));

            // Nustatome antrojo lango valdiklį
            TaskController secondController = loader.getController();

            // Atidaryti antrą langą ir rodyti jį
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sendTaskForm(String name, String description, String date, boolean daily) {
        if (userID != null && name != null && description != null && date != null && !name.equals("") && !description.equals("") && !date.equals("null")) {
            Connection connection = null;
            PreparedStatement preparedStatement = null;
            ResultSet resultSet = null;
            try {
                // load and register JDBC driver for MySQL
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/checkitoff", "root", "");
                preparedStatement = connection.prepareStatement("INSERT INTO `task` (`ID`, `Name`, `Description`, `Date`, `Daily`) VALUES (?, ?, ?, ?, ?)");
                preparedStatement.setInt(1, Integer.parseInt(userID));
                preparedStatement.setString(2, name);
                preparedStatement.setString(3, description);
                preparedStatement.setString(4, date);
                preparedStatement.setBoolean(5, daily);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Task form was not filled correctly or the user is offline.");
            alert.show();
        }
    }
    public static void createDailyTasks() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/checkitoff", "root", "");
            preparedStatement = connection.prepareStatement("SELECT * FROM task WHERE daily = 1");
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String name = resultSet.getString("Name");
                String description = resultSet.getString("Description");
                // Check if a task with this name and description already exists for today
                PreparedStatement preparedStatement2 = connection.prepareStatement(
                        "SELECT COUNT(*) FROM task WHERE Name = ? AND Description = ? AND Date = CURDATE()");
                preparedStatement2.setString(1, name);
                preparedStatement2.setString(2, description);
                ResultSet resultSet2 = preparedStatement2.executeQuery();

                if (resultSet2.next() && resultSet2.getInt(1) == 0) {
                    // No task for today exists, create a new one
                    PreparedStatement preparedStatement3 = connection.prepareStatement(
                            "INSERT INTO task (ID, Name, Description, Date, daily) VALUES (?, ?, ?, CURDATE(), 1)");
                    preparedStatement3.setString(1, userID);
                    preparedStatement3.setString(2, name);
                    preparedStatement3.setString(3, description);
                    preparedStatement3.executeUpdate();
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            // close ResultSet
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            // close PreparedStatement
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            // close Connection
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static List<Task> getTaskForm() {
        List<Task> tasks = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            // load and register JDBC driver for MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/checkitoff", "root", "");
            preparedStatement = connection.prepareStatement("SELECT * FROM `task` WHERE ID = ?");
            preparedStatement.setString(1, userID);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                String name = resultSet.getString("Name");
                String description = resultSet.getString("Description");
                String date = resultSet.getString("Date");
                tasks.add(new Task(name, description, date));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            // close ResultSet
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            // close PreparedStatement
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            // close Connection
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return tasks;
    }
    public static void updateTask(String taskName, boolean isChecked) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            // load and register JDBC driver for MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/checkitoff", "root", "");
            preparedStatement = connection.prepareStatement("UPDATE task SET isChecked = ? WHERE Name = ? AND ID = ?");
            preparedStatement.setBoolean(1, isChecked);
            preparedStatement.setString(2, taskName);
            preparedStatement.setString(3, userID); // assuming userID is a string, use setInt if it's an integer
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            // close PreparedStatement
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            // close Connection
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static boolean isTaskChecked(String taskName) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        boolean isChecked = false;
        try {
            // load and register JDBC driver for MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/checkitoff", "root", "");
            preparedStatement = connection.prepareStatement("SELECT isChecked FROM task WHERE Name = ? AND ID = ?");
            preparedStatement.setString(1, taskName);
            preparedStatement.setString(2, userID); // assuming userID is a string, use setInt if it's an integer
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                isChecked = resultSet.getBoolean("isChecked");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            // close ResultSet
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            // close PreparedStatement
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            // close Connection
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return isChecked;
    }



}
