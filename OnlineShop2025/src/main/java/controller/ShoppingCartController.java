
package controller;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import model.CartItem;


public class ShoppingCartController {

    @FXML private TableView<CartItem> cartTableView;
    @FXML private TableColumn<CartItem, String> productNameColumn;
    @FXML private TableColumn<CartItem, Double> productPriceColumn;
    @FXML private TableColumn<CartItem, CartItem> quantityColumn;
    @FXML private TableColumn<CartItem, Double> totalItemPriceColumn;
    @FXML private TableColumn<CartItem, CartItem> actionsColumn;
    @FXML private Label subtotalLabel;
    @FXML private Button clearCartButton;
    @FXML private Button checkoutButton;

    private ShoppingCartManager cartManager;

    @FXML
    public void initialize() {
        System.out.println("ShoppingCartController initialized.");
        cartManager = ShoppingCartManager.getInstance();

        setupTableColumns();

        
        cartTableView.setItems(cartManager.getCartItems());

        updateSubtotal(); 

        cartManager.getCartItems().addListener((ListChangeListener<CartItem>) c -> {
            System.out.println("Cart items list changed. Re-calculating subtotal and checking placeholder.");
            updateSubtotal(); 
            if (cartManager.getCartItems().isEmpty()) {
                cartTableView.setPlaceholder(new Label("Your shopping cart is empty."));
            } else {
                
                cartTableView.setPlaceholder(null);
            }
           
        });
        
        if (cartManager.getCartItems().isEmpty()) {
            cartTableView.setPlaceholder(new Label("Your shopping cart is empty."));
        }
    }

    private void setupTableColumns() {
        productNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProductName()));
        productPriceColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getProductPrice()));
        totalItemPriceColumn.setCellValueFactory(cellData -> cellData.getValue().totalPriceProperty().asObject());

        quantityColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue()));
        quantityColumn.setCellFactory(col -> new TableCell<CartItem, CartItem>() {
            private final Spinner<Integer> quantitySpinner = new Spinner<>();
           
            private final HBox pane; 

            { 
                pane = new HBox(5, quantitySpinner); 
                pane.setAlignment(Pos.CENTER);
                quantitySpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
                    CartItem currentItem = getItem(); 
                    if (currentItem != null && newValue != null && !newValue.equals(oldValue)) {
                        System.out.println("Quantity changed for " + currentItem.getProductName() + " to " + newValue);
                        cartManager.updateQuantity(currentItem, newValue);
                       
                    }
                });
            }

            @Override
            protected void updateItem(CartItem item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    
                    if (quantitySpinner.getValueFactory() == null || 
                        ((SpinnerValueFactory.IntegerSpinnerValueFactory)quantitySpinner.getValueFactory()).getValue() != item.getQuantity()) {
                        SpinnerValueFactory<Integer> valueFactory =
                                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, item.getQuantity()); 
                        quantitySpinner.setValueFactory(valueFactory);
                    }
                    setGraphic(pane);
                }
            }
        });
        quantityColumn.setStyle("-fx-alignment: CENTER;");


        actionsColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue()));
        actionsColumn.setCellFactory(col -> new TableCell<CartItem, CartItem>() {
            private final Button removeButton = new Button("Remove");
            { 
                removeButton.getStyleClass().add("remove-button");
                removeButton.setOnAction(event -> {
                    CartItem itemToRemove = getItem(); 
                    if (itemToRemove != null) {
                        System.out.println("Remove button clicked for: " + itemToRemove.getProductName());
                        cartManager.removeProduct(itemToRemove);                        
                    } else {
                        System.out.println("Remove button clicked, but item associated with this cell is null.");
                    }
                });
            }

            @Override
            protected void updateItem(CartItem item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    setGraphic(removeButton);
                }
            }
        });
        actionsColumn.setStyle("-fx-alignment: CENTER;");
    }

    private void updateSubtotal() {
        double total = cartManager.getTotalCartPrice();
        subtotalLabel.setText(String.format("$%.2f", total));
        boolean cartIsEmpty = cartManager.getCartItems().isEmpty();
        checkoutButton.setDisable(cartIsEmpty);
        clearCartButton.setDisable(cartIsEmpty);
    }

    @FXML
    void handleClearCart(ActionEvent event) {
        System.out.println("Clear Cart clicked.");
        cartManager.clearCart();
        
    }

    @FXML
    void handleCheckout(ActionEvent event) {
        System.out.println("Proceed to Checkout clicked.");
        if (cartManager.getCartItems().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Your cart is empty. Please add items to proceed.");
            alert.showAndWait();
            return;
        }
        Alert info = new Alert(Alert.AlertType.INFORMATION, "Checkout process is not yet implemented.");
        info.showAndWait();
    }
}