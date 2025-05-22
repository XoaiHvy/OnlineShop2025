// File: src/model/CartItem.java
package model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

public class CartItem {
    private final SimpleObjectProperty<Product> product;
    private final SimpleIntegerProperty quantity;
    private final SimpleDoubleProperty totalPrice; // quantity * product.getPrice()

    public CartItem(Product product, int quantity) {
        this.product = new SimpleObjectProperty<>(product);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.totalPrice = new SimpleDoubleProperty(product.getPrice() * quantity);

        // Listen for changes in quantity to update total price
        this.quantity.addListener((obs, oldVal, newVal) ->
            this.totalPrice.set(this.product.get().getPrice() * newVal.intValue())
        );
    }

    // Product Property and Getter/Setter
    public Product getProduct() {
        return product.get();
    }
    public SimpleObjectProperty<Product> productProperty() {
        return product;
    }
    public void setProduct(Product product) {
        this.product.set(product);
        this.totalPrice.set(product.getPrice() * getQuantity()); // Update total price
    }

    // Quantity Property and Getter/Setter
    public int getQuantity() {
        return quantity.get();
    }
    public SimpleIntegerProperty quantityProperty() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        if (quantity >= 0) { // Quantity cannot be negative
            this.quantity.set(quantity);
            // Total price will be updated by the listener
        }
    }

    // Total Price Property and Getter (read-only from outside, calculated internally)
    public double getTotalPrice() {
        return totalPrice.get();
    }
    public SimpleDoubleProperty totalPriceProperty() {
        return totalPrice;
    }

    // Convenience methods
    public String getProductName() {
        return getProduct().getProductName();
    }

    public double getProductPrice() {
        return getProduct().getPrice();
    }
}