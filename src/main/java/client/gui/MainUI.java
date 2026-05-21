package client.gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainUI extends Application {

    private BorderPane root;

    @Override
    public void start(Stage stage) {

        root = new BorderPane();

        Label title = new Label("SYOS Inventory Management System");

        title.setStyle("""
                -fx-font-size: 24px;
                -fx-font-weight: bold;
                -fx-padding: 20;
                """);
        BorderPane.setAlignment(title, Pos.CENTER);
        root.setTop(title);

        //SIDEBAR
        VBox sidebar = new VBox(15);
        Label userLabel = new Label("👤 Admin");

        userLabel.setStyle("""
        -fx-text-fill: white;
        -fx-font-size: 18px;
        -fx-font-weight: bold;
        """);
        Label roleLabel = new Label("System Administrator");
        roleLabel.setStyle("""
        -fx-text-fill: #bdc3c7;
        -fx-font-size: 12px;
        """);

        VBox userBox = new VBox(5, userLabel, roleLabel);

        userBox.setPadding(new Insets(0, 0, 20, 0));

        sidebar.setPadding(new Insets(20));

        sidebar.setStyle("""
                -fx-background-color: #2c3e50;""");

        sidebar.setPrefWidth(200);

        Button dashboardBtn = createSidebarButton("Dashboard");
        Button inventoryBtn = createSidebarButton("Inventory");
        Button billingBtn = createSidebarButton("Billing");
        Button reportsBtn = createSidebarButton("Reports");
        Button onlineStoreBtn = createSidebarButton("Online Store");
        Button logoutBtn = createSidebarButton("Logout");

        sidebar.getChildren().addAll(
                userBox,
                dashboardBtn,
                inventoryBtn,
                billingBtn,
                reportsBtn,
                onlineStoreBtn,
                logoutBtn
        );
        root.setLeft(sidebar);

        //CENTER DASHBOARD
        VBox dashboardPane = new VBox(20);
        dashboardPane.setAlignment(Pos.CENTER);
        Label welcome = new Label("Welcome to SYOS System");
        welcome.setStyle("""
                -fx-font-size: 22px;
                -fx-font-weight: bold;
                """);
        dashboardPane.getChildren().add(welcome);
        root.setCenter(dashboardPane);

        //BUTTON ACTIONS
        dashboardBtn.setOnAction(e -> {
            root.setCenter(dashboardPane);
        });
        inventoryBtn.setOnAction(e -> {
            InventoryPane inventoryPane = new InventoryPane();
            root.setCenter(inventoryPane.getView());
        });
        billingBtn.setOnAction(e -> {
            BillingUI billingUI = new BillingUI();
            root.setCenter(billingUI.getView());
        });
        reportsBtn.setOnAction(e -> {

            ReportsUI reportsUI = new ReportsUI();
            root.setCenter(reportsUI.getView());
        });
        logoutBtn.setOnAction(e -> {
            try {
                LoginUI loginUI = new LoginUI();
                Stage loginStage = new Stage();
                loginUI.start(loginStage);
                Stage currentStage = (Stage) root.getScene().getWindow();
                currentStage.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        Scene scene = new Scene(root, 1200, 700);
        stage.setTitle("SYOS System");
        stage.setScene(scene);
        stage.show();
    }

    //SIDEBAR BUTTON STYLE
    private Button createSidebarButton(String text) {

        Button button = new Button(text);
        button.setPrefWidth(160);
        button.setStyle("""
                -fx-background-color: #34495e;
                -fx-text-fill: white;
                -fx-font-size: 16px;
                -fx-padding: 10;
                """);
        return button;
    }

    public static void main(String[] args) {
        launch();
    }
}