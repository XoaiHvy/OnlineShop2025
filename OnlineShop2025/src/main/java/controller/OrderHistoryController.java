package controller;

import Connect.DBConnection;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.OrderSummary;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class OrderHistoryController {

    @FXML private TableView<OrderSummary> ordersTableView;
    @FXML private TableColumn<OrderSummary, Integer> orderIdColumn; 
    @FXML private TableColumn<OrderSummary, String> orderDateColumn;
    @FXML private TableColumn<OrderSummary, Integer> productIdColumn; 
 
    @FXML private TableColumn<OrderSummary, Integer> quantityColumn; 
    @FXML private TableColumn<OrderSummary, Double> totalPriceColumn; 
    @FXML private TableColumn<OrderSummary, String> statusColumn;

    private ObservableList<OrderSummary> orderList = FXCollections.observableArrayList();
    private int currentUserId = 1; 
    @FXML
    public void initialize() {
        System.out.println("OrderHistoryController initialized.");

        orderIdColumn.setCellValueFactory(cellData -> cellData.getValue().orderIdProperty().asObject());
        orderDateColumn.setCellValueFactory(cellData -> cellData.getValue().orderDateProperty());
        productIdColumn.setCellValueFactory(cellData -> cellData.getValue().productIdProperty().asObject()); 
        quantityColumn.setCellValueFactory(cellData -> cellData.getValue().quantityProperty().asObject());   
        totalPriceColumn.setCellValueFactory(cellData -> cellData.getValue().totalPriceProperty().asObject());
        statusColumn.setCellValueFactory(cellData -> cellData.getValue().statusProperty());

        loadOrderHistory();
        ordersTableView.setItems(orderList);

        if (orderList.isEmpty()) {
            ordersTableView.setPlaceholder(new Label("You have no past orders."));
        }
    }

    private void loadOrderHistory() {
        orderList.clear();
        String sql = "SELECT order_id, order_date, product_id, quantity, total_price FROM orders ORDER BY order_date DESC";


        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (conn == null) {
                System.err.println("OrderHistory: Failed to connect to database.");
                ordersTableView.setPlaceholder(new Label("Error connecting to database."));
                return;
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int orderId = rs.getInt("order_id");
                    LocalDate orderDate = rs.getDate("order_date").toLocalDate();
                    int productId = rs.getInt("product_id");          
                    int quantity = rs.getInt("quantity");           
                    
                    double totalPrice = rs.getDouble("total_price");

                    orderList.add(new OrderSummary(orderId, orderDate.toString(), productId, quantity, totalPrice, "Completed"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("SQL Error loading order history: " + e.getMessage());
            ordersTableView.setPlaceholder(new Label("Error loading order history."));
        }
    }

	public void setCurrentUser(User dataToPass) {
		
		
	}
}