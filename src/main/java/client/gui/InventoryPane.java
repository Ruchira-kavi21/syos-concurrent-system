package client.gui;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import server.domain.Item;
import shared.dto.Request;
import shared.dto.Response;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class InventoryPane {

    private final VBox view = new VBox(15);

    private final TableView<BatchRow> table = new TableView<>();

    private final Label statusLabel = new Label();

    public InventoryPane() {

        view.setPadding(new Insets(20));

        createTable();

        Button refreshBtn = new Button("Refresh Inventory");

        Button addItemBtn = new Button("Add Item");

        Button addBatchBtn = new Button("Add Batch Stock");

        refreshBtn.setOnAction(e -> loadInventory());

        addItemBtn.setOnAction(e -> addItem());

        addBatchBtn.setOnAction(e -> addBatchStock());

        view.getChildren().addAll(
                new Label("Inventory Management"),
                table,
                addItemBtn,
                addBatchBtn,
                refreshBtn,
                statusLabel
        );
        loadInventory();
    }

    private void createTable() {

        //BATCH
        TableColumn<BatchRow, String> batchCol = new TableColumn<>("Batch");

        batchCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBatchNumber()));

        batchCol.setPrefWidth(120);

        //ITEM

        TableColumn<BatchRow, String> itemCol = new TableColumn<>("Item");

        itemCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getItemCode()));

        itemCol.setPrefWidth(120);

        TableColumn<BatchRow, String> itemNameCol = new TableColumn<>("Item Name");

        itemCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getItemName()));

        itemCol.setPrefWidth(120);

        // ===== PURCHASE DATE =====

        TableColumn<BatchRow, String> purchaseCol = new TableColumn<>("Purchase");

        purchaseCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPurchaseDate()));

        purchaseCol.setPrefWidth(130);

        // ===== EXPIRY DATE =====

        TableColumn<BatchRow, String> expiryCol = new TableColumn<>("Expiry");

        expiryCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getExpiryDate()));

        expiryCol.setPrefWidth(130);

        // ===== QUANTITY =====

        TableColumn<BatchRow, Number> qtyCol = new TableColumn<>("Qty");

        qtyCol.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getQuantity()));

        // ===== LOCATION =====

        TableColumn<BatchRow, String> locationCol = new TableColumn<>("Location");

        locationCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getLocation()));

        locationCol.setPrefWidth(120);

        // ===== ADD COLUMNS =====

        table.getColumns().addAll(
                batchCol,
                itemCol,
                itemNameCol,
                purchaseCol,
                expiryCol,
                qtyCol,
                locationCol
        );
    }

    private void loadInventory() {

        try {

            Socket socket = new Socket("localhost", 5000);

            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());

            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
            Request request = new Request("GET_BATCHES", null);

            output.writeObject(request);

            Response response = (Response) input.readObject();

            List<BatchRow> rows = (List<BatchRow>) response.getData();

            ObservableList<BatchRow> itemList = FXCollections.observableArrayList(rows);

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
    private void addBatchStock() {
        statusLabel.setText("Batch stock feature coming next.");
    }

    public VBox getView() {
        return view;
    }
}