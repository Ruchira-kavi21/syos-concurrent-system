package client.gui;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import server.domain.Item;
import shared.dto.PurchaseRequest;
import shared.dto.Request;
import shared.dto.Response;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class InventoryPane {

    private final VBox view = new VBox(15);

    private final TableView<Item> table = new TableView<>();


    private final Label statusLabel = new Label();

    public InventoryPane() {

        view.setPadding(new Insets(20));

        createTable();

        Button refreshBtn = new Button("Refresh Inventory");

        Button addItemBtn = new Button("Add Item");

        Button updateItemBtn = new Button("Update Item");

        Button deleteItemBtn = new Button("Delete Item");

        Button addBatchBtn = new Button("Add Batch Stock");

        refreshBtn.setOnAction(e -> loadInventory());

        addItemBtn.setOnAction(e -> addItem());

        updateItemBtn.setOnAction(e -> updateItem());

        deleteItemBtn.setOnAction(e -> deleteItem());

        addBatchBtn.setOnAction(e -> addBatchStock());

        view.getChildren().addAll(
                new Label("Inventory Management"),
                table,
                addItemBtn,
                updateItemBtn,
                deleteItemBtn,
                addBatchBtn,
                refreshBtn,
                statusLabel
        );
        loadInventory();
    }

    private void createTable() {

        TableColumn<Item, String> codeCol = new TableColumn<>("Code");

        codeCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCode()));

        TableColumn<Item, String> nameCol = new TableColumn<>("Name");

        nameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));

        TableColumn<Item, Number> priceCol = new TableColumn<>("Price");

        priceCol.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getPrice()));

        TableColumn<Item, Number> stockCol = new TableColumn<>("Stock");

        stockCol.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getShelfCapacity()));

        table.getColumns().addAll(
                codeCol,
                nameCol,
                priceCol,
                stockCol
        );
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
    private void addItem() {

        Dialog<Item> dialog = new Dialog<>();

        dialog.setTitle("Add Item");

        TextField codeField = new TextField();

        TextField nameField = new TextField();

        TextField priceField = new TextField();

        TextField stockField = new TextField();

        VBox box = new VBox(10,
                    new Label("Code"),
                    codeField,

                    new Label("Name"),
                    nameField,

                    new Label("Price"),
                    priceField,

                    new Label("Shelf Capacity"),
                    stockField
    );

        dialog.getDialogPane().setContent(box);

        ButtonType addButton = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);

        dialog.getDialogPane().getButtonTypes().addAll(addButton, ButtonType.CANCEL);

        dialog.setResultConverter(button -> {

            if (button == addButton) {
                return new Item(
                        codeField.getText(),
                        nameField.getText(),
                        Double.parseDouble(priceField.getText()),
                        Integer.parseInt(stockField.getText())
                );
            }
            return null;
        });

        dialog.showAndWait().ifPresent(item -> {

            try {
                Socket socket = new Socket("localhost", 5000);

                ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());

                ObjectInputStream input = new ObjectInputStream(socket.getInputStream());

                Request request = new Request("ADD_ITEM", item);

                output.writeObject(request);

                Response response = (Response) input.readObject();

                statusLabel.setText(response.getMessage());

                loadInventory();

                socket.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    private void updateItem() {
        Item selectedItem = table.getSelectionModel().getSelectedItem();

        if (selectedItem == null) {
            statusLabel.setText("Select item first.");
            return;
        }

        TextInputDialog dialog = new TextInputDialog(String.valueOf(selectedItem.getPrice()));

        dialog.setTitle("Update Price");

        dialog.setHeaderText("Enter New Price");

        dialog.showAndWait().ifPresent(price -> {
            try {
                selectedItem.setPrice(Double.parseDouble(price));

                Socket socket = new Socket("localhost", 5000);

                ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());

                ObjectInputStream input = new ObjectInputStream(socket.getInputStream());

                Request request = new Request("UPDATE_ITEM", selectedItem);

                output.writeObject(request);

                Response response = (Response) input.readObject();

                statusLabel.setText(response.getMessage());

                loadInventory();

                socket.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    private void deleteItem() {
        Item selectedItem = table.getSelectionModel().getSelectedItem();

        if (selectedItem == null) {

            statusLabel.setText("Select item first.");
            return;
        }

        try {

            Socket socket = new Socket("localhost", 5000);

            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());

            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());

            Request request = new Request("DELETE_ITEM", selectedItem.getCode());

            output.writeObject(request);

            Response response = (Response) input.readObject();

            statusLabel.setText(response.getMessage());

            loadInventory();

            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void addBatchStock() {
        statusLabel.setText("Batch stock feature coming next.");
    }

    public VBox getView() {
        return view;
    }
}