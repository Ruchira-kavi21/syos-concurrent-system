package client.gui;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import server.domain.Bill;
import server.domain.BillItem;
import server.domain.Item;
import server.domain.TransactionType;
import shared.dto.PurchaseRequest;
import shared.dto.Request;
import shared.dto.Response;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BillingUI {
    private final VBox view = new VBox(15);

    private final TableView<CartUI> cartTable = new TableView<>();

    private final ObservableList<CartUI> cartItems = FXCollections.observableArrayList();

    private final TextField itemCodeField = new TextField();

    private final TextField quantityField = new TextField();

    private final TextField cashField = new TextField();

    private final Label totalLabel = new Label("Total: 0.00");

    private final Label changeLabel = new Label("Change: 0.00");

    public BillingUI() {
        view.setPadding(new Insets(20));

        Label title = new Label("Billing System");

        title.setStyle("""
                -fx-font-size: 24px;
                -fx-font-weight: bold;
                """);
        // ===== INPUTS =====

        itemCodeField.setPromptText("Item Code");
        quantityField.setPromptText("Quantity");

        Button addBtn = new Button("Add Item");
        Button deleteBtn = new Button("Delete Item");

        HBox inputBox =
                new HBox(
                        10,
                        itemCodeField,
                        quantityField,
                        addBtn,
                        deleteBtn
                );

        // ===== TABLE =====
        createCartTable();

        // ===== PAYMENT =====
        cashField.setPromptText("Cash Tendered");

        Button calculateBtn = new Button("Calculate Change");

        Button generateBillBtn = new Button("Generate Bill");

        // ===== ACTIONS =====

        addBtn.setOnAction(e -> addItem());
        deleteBtn.setOnAction(e -> deleteSelectedItem());
        calculateBtn.setOnAction(e -> calculateChange());
        generateBillBtn.setOnAction(e -> generateBill());

        // ===== LAYOUT =====
        view.getChildren().addAll(
                title,
                inputBox,
                cartTable,
                totalLabel,
                cashField,
                calculateBtn,
                changeLabel,
                generateBillBtn
        );
    }

    private void createCartTable() {

        TableColumn<CartUI, String> codeCol = new TableColumn<>("Code");

        codeCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCode()));

        TableColumn<CartUI, String> nameCol = new TableColumn<>("Name");

        nameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));

        TableColumn<CartUI, Number> qtyCol = new TableColumn<>("Quantity");

        qtyCol.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getQuantity()));

        TableColumn<CartUI, Number> priceCol = new TableColumn<>("Price");

        priceCol.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getPrice()));

        TableColumn<CartUI, Number> totalCol = new TableColumn<>("Total");

        totalCol.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getTotal()));

        cartTable.getColumns().addAll(
                codeCol,
                nameCol,
                qtyCol,
                priceCol,
                totalCol
        );

        cartTable.setItems(cartItems);
    }

    private void addItem() {

        try {

            String code = itemCodeField.getText();

            int quantity = Integer.parseInt(quantityField.getText());

            Socket socket = new Socket("localhost", 5000);

            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());

            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());

            Request request = new Request("GET_ITEM_BY_CODE", code);

            output.writeObject(request);

            Response response = (Response) input.readObject();

            if (!response.getStatus().equals("SUCCESS")) {
                System.out.println(response.getMessage());
                return;
            }

            Item item = (Item) response.getData();

            CartUI row = new CartUI(
                    item.getCode(),
                    item.getName(),
                    quantity,
                    item.getPrice()
            );

            cartItems.add(row);
            updateTotal();
            itemCodeField.clear();
            quantityField.clear();
            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void deleteSelectedItem() {

        CartUI selectedRow = cartTable.getSelectionModel().getSelectedItem();
        if (selectedRow != null) {
            cartItems.remove(selectedRow);
            updateTotal();
        }
    }

    private void updateTotal() {

        double total = 0;
        for (CartUI row : cartItems) {
            total += row.getTotal();
        }
        totalLabel.setText("Total: " + total);
    }

    private void calculateChange() {

        try {
            double cash = Double.parseDouble(cashField.getText());
            double total = 0;

            for (CartUI row : cartItems) {
                total += row.getTotal();
            }
            double change = cash - total;

            changeLabel.setText("Change: " + change);

        } catch (Exception e) {
            changeLabel.setText("Invalid cash amount");
        }
    }
    private void generateBill() {

        try {
            for (CartUI row : cartItems) {
                Socket socket = new Socket("localhost", 5000);
                ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());

                ObjectInputStream input = new ObjectInputStream(socket.getInputStream());

                PurchaseRequest purchase = new PurchaseRequest(row.getCode(), row.getQuantity());

                Request request = new Request("BUY_ITEM", purchase);

                output.writeObject(request);

                Response response = (Response) input.readObject();

                if (!response.getStatus().equals("SUCCESS")) {
                    changeLabel.setText("Purchase failed for: " + row.getCode());
                    return;
                }
                socket.close();
            }

            List<BillItem> billItems = new ArrayList<>();

            for (CartUI row : cartItems) {
                BillItem item = new BillItem(
                        row.getName(),
                        row.getQuantity(),
                        row.getPrice(),
                        row.getTotal()
                );
                billItems.add(item);
            }

            double total = 0;
            for (CartUI row : cartItems) {
                total += row.getTotal();
            }

            double cash = Double.parseDouble(cashField.getText());

            double change = cash - total;

            String billNumber = "BILL-" + UUID.randomUUID().toString().substring(0, 5);

            Bill bill = new Bill.BillBuilder()
                            .setBillNumber(billNumber)
                            .setDate(LocalDateTime.now())
                            .setType(TransactionType.IN_STORE)
                            .setTotalAmount(total)
                            .setDiscount(0)
                            .setCashReceived(cash)
                            .setChangeAmount(change)
                            .addItemList(billItems)
                            .build();
            Socket socket = new Socket("localhost", 5000);

            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());

            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());

            Request request = new Request("SAVE_BILL", bill);
            output.writeObject(request);

            Response response = (Response) input.readObject();
            changeLabel.setText(response.getMessage());
            socket.close();
            cartItems.clear();
            updateTotal();
            cashField.clear();

        } catch (Exception e) {
            e.printStackTrace();
            changeLabel.setText("Bill generation failed");
        }
    }

    public VBox getView() {
        return view;
    }
}
