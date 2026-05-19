package client.gui;

import javafx.application.Application;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import server.domain.Item;
import shared.dto.Request;
import shared.dto.Response;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class InventoryUI extends Application {

    private final TableView<Item> table = new TableView<>();

    @Override
    public void start(Stage stage) {

        TableColumn<Item, String> codeCol = new TableColumn<>("Code");

        codeCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCode()));

        TableColumn<Item, String> nameCol = new TableColumn<>("Name");

        nameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));

        TableColumn<Item, Number> priceCol = new TableColumn<>("Price");

        priceCol.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getPrice()));

        TableColumn<Item, Number> stockCol = new TableColumn<>("Stock");

        stockCol.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getShelfCapacity()));

        table.getColumns().addAll(codeCol, nameCol, priceCol, stockCol);

        // Refresh button
        Button refreshBtn = new Button("Refresh Inventory");

        refreshBtn.setOnAction(e -> loadInventory());

        VBox root = new VBox(10, table, refreshBtn);

        Scene scene = new Scene(root, 700, 400);

        stage.setTitle("SYOS Inventory System");
        stage.setScene(scene);
        stage.show();

        // Load inventory initially
        loadInventory();
    }

    private void loadInventory() {

        try {

            Socket socket = new Socket("localhost", 5000);

            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());

            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());

            Request request = new Request("GET_ITEMS", null);

            output.writeObject(request);

            Response response = (Response) input.readObject();

            List<Item> items = (List<Item>) response.getData();

            ObservableList<Item> itemList = FXCollections.observableArrayList(items);

            table.setItems(itemList);

            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}