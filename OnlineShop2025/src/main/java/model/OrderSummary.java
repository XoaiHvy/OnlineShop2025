// File: src/model/OrderSummary.java
package model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class OrderSummary {
    private final SimpleIntegerProperty orderId; // Có thể hiểu là order_item_id
    private final SimpleStringProperty orderDate;
    private final SimpleIntegerProperty productId; // Thêm productId để tham khảo
    private final SimpleIntegerProperty quantity;  // THÊM QUANTITY
    private final SimpleDoubleProperty totalPrice; // Đây là total_price của dòng này (item_subtotal)
    private final SimpleStringProperty status;     // Tạm thời

    // Cập nhật constructor
    public OrderSummary(int orderId, String orderDate, int productId, int quantity, double totalPrice, String status) {
        this.orderId = new SimpleIntegerProperty(orderId);
        this.orderDate = new SimpleStringProperty(orderDate);
        this.productId = new SimpleIntegerProperty(productId);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.totalPrice = new SimpleDoubleProperty(totalPrice);
        this.status = new SimpleStringProperty(status);
    }

    // Property getters
    public SimpleIntegerProperty orderIdProperty() { return orderId; }
    public SimpleStringProperty orderDateProperty() { return orderDate; }
    public SimpleIntegerProperty productIdProperty() { return productId; }
    public SimpleIntegerProperty quantityProperty() { return quantity; } // Getter cho quantity
    public SimpleDoubleProperty totalPriceProperty() { return totalPrice; }
    public SimpleStringProperty statusProperty() { return status; }

    // Standard getters
    public int getOrderId() { return orderId.get(); }
    public String getOrderDate() { return orderDate.get(); }
    public int getProductId() { return productId.get(); }
    public int getQuantity() { return quantity.get(); } // Getter cho quantity
    public double getTotalPrice() { return totalPrice.get(); }
    public String getStatus() { return status.get(); }
}