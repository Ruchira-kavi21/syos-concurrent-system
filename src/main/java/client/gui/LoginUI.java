package client.gui;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginUI extends Application {

    @Override
    public void start(Stage stage) {
        Label title = new Label("SYOS Store System");
        title.setStyle("""
                -fx-font-size: 32px;
                -fx-font-weight: bold;
                -fx-text-fill: white;
                """);
        Label subtitle = new Label("Retail Inventory & Billing");
        subtitle.setStyle("""
                -fx-font-size: 16px;
                -fx-text-fill: #bdc3c7;
                """);

        TextField usernameField = new TextField();

        usernameField.setPromptText("Username");

        usernameField.setMaxWidth(300);

        usernameField.setStyle("""
                -fx-font-size: 16px;
                -fx-padding: 12;
                -fx-background-radius: 8;
                """);

        PasswordField passwordField = new PasswordField();

        passwordField.setPromptText("Password");

        passwordField.setMaxWidth(300);

        passwordField.setStyle("""
                -fx-font-size: 16px;
                -fx-padding: 12;
                -fx-background-radius: 8;
                """);

        Label statusLabel = new Label();

        statusLabel.setStyle("""
                -fx-text-fill: #e74c3c;
                -fx-font-size: 14px;
                """);

        Button loginBtn = new Button("Login");

        loginBtn.setPrefWidth(300);

        loginBtn.setStyle("""
                -fx-background-color: #3498db;
                -fx-text-fill: white;
                -fx-font-size: 16px;
                -fx-font-weight: bold;
                -fx-padding: 12;
                -fx-background-radius: 8;
                """);

        loginBtn.setOnAction(e -> {

            String username = usernameField.getText();

            String password = passwordField.getText();

            if (username.equals("admin") && password.equals("1234")) {
                try {
                    MainUI mainUI = new MainUI();

                    Stage mainStage = new Stage();

                    mainUI.start(mainStage);

                    stage.close();

                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            } else {
                statusLabel.setText("Invalid username or password");
            }
        });
        VBox root = new VBox(
                18,
                title,
                subtitle,
                usernameField,
                passwordField,
                loginBtn,
                statusLabel
        );
        root.setAlignment(Pos.CENTER);

        root.setStyle("""
                -fx-background-color: #1f2937;
                """);

        Scene scene = new Scene(root, 900, 600);

        stage.setTitle("SYOS Login");

        stage.setScene(scene);

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}