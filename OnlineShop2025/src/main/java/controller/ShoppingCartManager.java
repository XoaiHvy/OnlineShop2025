// File: src/controller/ShoppingCartManager.java (or src/service/)
package controller; // Or service

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.CartItem;
import model.Product;

public class ShoppingCartManager {

    private static ShoppingCartManager instance;
    private ObservableList<CartItem> cartItems;

    private ShoppingCartManager() {
        cartItems = FXCollections.observableArrayList();
    }

    public static synchronized ShoppingCartManager getInstance() {
        if (instance == null) {
            instance = new ShoppingCartManager();
        }
        return instance;
    }

    public ObservableList<CartItem> getCartItems() {
        return cartItems;
    }

    public void addProduct(Product product, int quantity) {
        if (product == null || quantity <= 0) {
            return; // Invalid input
        }

        // Check if product already in cart
        for (CartItem item : cartItems) {
            if (item.getProduct().getProductId() == product.getProductId()) {
                item.setQuantity(item.getQuantity() + quantity); // Update quantity
                return;
            }
        }
        // If not in cart, add new item
        cartItems.add(new CartItem(product, quantity));
    }

    public void removeProduct(CartItem cartItem) {
        cartItems.remove(cartItem);
    }

    public void updateQuantity(CartItem cartItem, int newQuantity) {
        if (cartItem != null && cartItems.contains(cartItem)) {
            if (newQuantity <= 0) {
                removeProduct(cartItem); // Remove if quantity is 0 or less
            } else {
                cartItem.setQuantity(newQuantity);
            }
        }
    }

    public double getTotalCartPrice() {
        double total = 0;
        for (CartItem item : cartItems) {
            total += item.getTotalPrice();
        }
        return total;
    }

    public void clearCart() {
        cartItems.clear();
    }

    public int getItemCount() {
        return cartItems.size(); // Number of unique product types
        // Or if you want total number of items:
        // return cartItems.stream().mapToInt(CartItem::getQuantity).sum();
    }
}