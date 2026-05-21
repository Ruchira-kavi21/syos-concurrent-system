package client.gui;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import shared.dto.Request;
import shared.dto.Response;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class ReportsUI {

    private final VBox view = new VBox();

    public ReportsUI() {
        Label title = new Label("Reports Center");

        title.setStyle("""
                -fx-font-size: 24px;
                -fx-font-weight: bold;
                -fx-padding: 10;
                """);

        TabPane tabPane = new TabPane();

        //DAILY SALES TAB
        Tab dailySalesTab = new Tab("Daily Sales");
        VBox dailyBox = new VBox(15);
        dailyBox.setPadding(new Insets(15));
        Button loadDailyBtn = new Button("Load Daily Sales");
        TableView<DailySalesRow> dailyTable = new TableView<>();

        TableColumn<DailySalesRow, String> dailyCodeCol = new TableColumn<>("Code");

        dailyCodeCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCode()));

        //NAME
        TableColumn<DailySalesRow, String> dailyNameCol = new TableColumn<>("Name");

        dailyNameCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));

        dailyNameCol.setPrefWidth(250);

        //QUANTITY
        TableColumn<DailySalesRow, Number> dailyQtyCol = new TableColumn<>("Quantity Sold");

        dailyQtyCol.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getQuantity()));

        //REVENUE
        TableColumn<DailySalesRow, Number> revenueCol = new TableColumn<>("Revenue");

        revenueCol.setCellValueFactory(data -> new javafx.beans.property.SimpleDoubleProperty(data.getValue().getRevenue()));

        //ADD COLUMNS
        dailyTable.getColumns().addAll(
                dailyCodeCol,
                dailyNameCol,
                dailyQtyCol,
                revenueCol
        );

        //BUTTON ACTION
        loadDailyBtn.setOnAction(e -> {
            try {
                Socket socket = new Socket("localhost", 5000);

                ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());

                ObjectInputStream input = new ObjectInputStream(socket.getInputStream());

                Request request = new Request("GET_DAILY_SALES", null);

                output.writeObject(request);

                Response response = (Response) input.readObject();

                List<DailySalesRow> rows = (List<DailySalesRow>) response.getData();

                dailyTable.setItems(FXCollections.observableArrayList(rows));

                socket.close();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        //ADD TO LAYOUT
        dailyBox.getChildren().addAll(
                loadDailyBtn,
                dailyTable
        );

        dailySalesTab.setContent(dailyBox);

        //RESHELVING TAB
        Tab reshelvingTab = new Tab("Reshelving");

        VBox reshelvingBox = new VBox(15);

        reshelvingBox.setPadding(new Insets(15));

        Button loadReshelvingBtn = new Button("Load Reshelving Report");

        TableView<ReshelvingRow> reshelvingTable = new TableView<>();

        //CODE
        TableColumn<ReshelvingRow, String> reshelfCodeCol = new TableColumn<>("Code");

        reshelfCodeCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCode()));

        //NAME
        TableColumn<ReshelvingRow, String> reshelfNameCol = new TableColumn<>("Name");

        reshelfNameCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));

        reshelfNameCol.setPrefWidth(250);

        //SHELF QTY
        TableColumn<ReshelvingRow, Number> shelfQtyCol = new TableColumn<>("Shelf Qty");

        shelfQtyCol.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getShelfQuantity()));

        //STORE QTY
        TableColumn<ReshelvingRow, Number> storeQtyCol = new TableColumn<>("Store Qty");

        storeQtyCol.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getStoreQuantity()));

        //ADD COLUMNS
        reshelvingTable.getColumns().addAll(
                reshelfCodeCol,
                reshelfNameCol,
                shelfQtyCol,
                storeQtyCol
        );

        //BUTTON ACTION
        loadReshelvingBtn.setOnAction(e -> {
            try {
                Socket socket = new Socket("localhost", 5000);

                ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());

                ObjectInputStream input = new ObjectInputStream(socket.getInputStream());

                Request request = new Request("GET_RESHELVING_REPORT", null);

                output.writeObject(request);

                Response response = (Response) input.readObject();

                List<ReshelvingRow> rows = (List<ReshelvingRow>) response.getData();

                reshelvingTable.setItems(FXCollections.observableArrayList(rows));

                socket.close();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        //ADD TO LAYOUT
        reshelvingBox.getChildren().addAll(
                loadReshelvingBtn,
                reshelvingTable
        );

        reshelvingTab.setContent(reshelvingBox);

        // ===== REORDER TAB =====
        Tab reorderTab = new Tab("Reorder Levels");

        VBox reorderBox = new VBox(15);

        reorderBox.setPadding(new Insets(15));

        Button loadReorderBtn = new Button("Load Reorder Report");

        TableView<ReorderRow> reorderTable = new TableView<>();

        //CODE COLUMN
        TableColumn<ReorderRow, String> codeCol = new TableColumn<>("Code");

        codeCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCode()));

        //NAME COLUMN
        TableColumn<ReorderRow, String> nameCol = new TableColumn<>("Name");nameCol.setPrefWidth(250);

        nameCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));

        //QUANTITY COLUMN
        TableColumn<ReorderRow, Number> qtyCol = new TableColumn<>("Quantity");

        qtyCol.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getQuantity()));

        //ADD COLUMNS
        reorderTable.getColumns().addAll(codeCol, nameCol, qtyCol);

        //BUTTON ACTION
        loadReorderBtn.setOnAction(e -> {

            try {
                Socket socket = new Socket("localhost", 5000);
                ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());

                ObjectInputStream input = new ObjectInputStream(socket.getInputStream());

                Request request = new Request("GET_REORDER_REPORT", null);

                output.writeObject(request);

                Response response = (Response) input.readObject();

                List<ReorderRow> rows = (List<ReorderRow>) response.getData();

                reorderTable.setItems(FXCollections.observableArrayList(rows));

                socket.close();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        //ADD TO LAYOUT
        reorderBox.getChildren().addAll(loadReorderBtn, reorderTable);
        reorderTab.setContent(reorderBox);

        //STOCK REPORT TAB
        Tab stockTab = new Tab("Stock Report");

        VBox stockBox = new VBox(15);

        stockBox.setPadding(new Insets(15));

        Button loadStockBtn = new Button("Load Stock Report");

        TableView<StockRow> stockTable = new TableView<>();

        //BATCH NUMBER
        TableColumn<StockRow, String> batchCol = new TableColumn<>("Batch No");

        batchCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getBatchNumber()));

        batchCol.setPrefWidth(120);

        //ITEM CODE
        TableColumn<StockRow, String> itemCodeCol = new TableColumn<>("Item Code");

        itemCodeCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getItemCode()));

        itemCodeCol.setPrefWidth(120);

        //PURCHASE DATE
        TableColumn<StockRow, String> purchaseCol = new TableColumn<>("Purchase Date");

        purchaseCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getPurchaseDate()));

        purchaseCol.setPrefWidth(140);

        //EXPIRY DATE
        TableColumn<StockRow, String> expiryCol = new TableColumn<>("Expiry Date");

        expiryCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getExpiryDate()));

        expiryCol.setPrefWidth(140);

        //QUANTITY
        TableColumn<StockRow, Number> stockQtyCol = new TableColumn<>("Quantity");

        stockQtyCol.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getQuantity()));

        //LOCATION
        TableColumn<StockRow, String> locationCol = new TableColumn<>("Location");

        locationCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getLocation()));

        locationCol.setPrefWidth(120);

        //ADD COLUMNS
        stockTable.getColumns().addAll(
                batchCol,
                itemCodeCol,
                purchaseCol,
                expiryCol,
                stockQtyCol,
                locationCol
        );

        //BUTTON ACTION
        loadStockBtn.setOnAction(e -> {
            try {
                Socket socket = new Socket("localhost", 5000);

                ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());

                ObjectInputStream input = new ObjectInputStream(socket.getInputStream());

                Request request = new Request("GET_STOCK_REPORT", null);

                output.writeObject(request);

                Response response = (Response) input.readObject();

                List<StockRow> rows = (List<StockRow>) response.getData();

                stockTable.setItems(FXCollections.observableArrayList(rows));

                socket.close();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        //ADD TO LAYOUT
        stockBox.getChildren().addAll(loadStockBtn, stockTable);

        stockTab.setContent(stockBox);

        // ===== BILL REPORT TAB
        Tab billTab = new Tab("Bill Report");

        VBox billBox = new VBox(15);

        billBox.setPadding(new Insets(15));

        Button loadBillBtn = new Button("Load Bill Report");

        TableView<BillReportRow> billTable = new TableView<>();

        //BILL NUMBER
        TableColumn<BillReportRow, String> billNoCol = new TableColumn<>("Bill No");

        billNoCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getBillNumber()));

        billNoCol.setPrefWidth(140);

        //DATE
        TableColumn<BillReportRow, String> dateCol = new TableColumn<>("Date");

        dateCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDate()));

        dateCol.setPrefWidth(220);

        //TOTAL
        TableColumn<BillReportRow, Number> totalCol = new TableColumn<>("Total");

        totalCol.setCellValueFactory(data -> new javafx.beans.property.SimpleDoubleProperty(data.getValue().getTotal()));

        //CASH
        TableColumn<BillReportRow, Number> cashCol = new TableColumn<>("Cash");

        cashCol.setCellValueFactory(data -> new javafx.beans.property.SimpleDoubleProperty(data.getValue().getCash()));

        //CHANGE
        TableColumn<BillReportRow, Number> changeCol = new TableColumn<>("Change");

        changeCol.setCellValueFactory(data -> new javafx.beans.property.SimpleDoubleProperty(data.getValue().getChange()));

        //ADD COLUMNS
        billTable.getColumns().addAll(
                billNoCol,
                dateCol,
                totalCol,
                cashCol,
                changeCol
        );

        //BUTTON ACTION

        loadBillBtn.setOnAction(e -> {
            try {
                Socket socket = new Socket("localhost", 5000);

                ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());

                ObjectInputStream input = new ObjectInputStream(socket.getInputStream());

                Request request = new Request("GET_BILL_REPORT", null);

                output.writeObject(request);

                Response response = (Response) input.readObject();

                List<BillReportRow> rows = (List<BillReportRow>) response.getData();

                billTable.setItems(FXCollections.observableArrayList(rows));

                socket.close();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        //ADD TO LAYOUT
        billBox.getChildren().addAll(loadBillBtn, billTable);

        billTab.setContent(billBox);

        //ADD ALL TABS
        tabPane.getTabs().addAll(
                dailySalesTab,
                reshelvingTab,
                reorderTab,
                stockTab,
                billTab
        );

        // ===== ROOT =====
        view.getChildren().addAll(title, tabPane
        );
    }

    public VBox getView() {
        return view;
    }
}