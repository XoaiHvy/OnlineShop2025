// File: src/controller/ShoppingCartController.java
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
// import model.Product; // Không cần trực tiếp ở đây nếu CartItem đã chứa Product

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

        // Gán danh sách cartItems cho TableView
        // ObservableList sẽ tự động thông báo cho TableView khi có thay đổi
        cartTableView.setItems(cartManager.getCartItems());

        updateSubtotal(); // Cập nhật tổng tiền ban đầu

        // Listener để cập nhật tổng tiền và placeholder khi danh sách thay đổi
        cartManager.getCartItems().addListener((ListChangeListener<CartItem>) c -> {
            System.out.println("Cart items list changed. Re-calculating subtotal and checking placeholder.");
            updateSubtotal(); // Cập nhật tổng tiền
            if (cartManager.getCartItems().isEmpty()) {
                cartTableView.setPlaceholder(new Label("Your shopping cart is empty."));
            } else {
                // Nếu bạn muốn xóa placeholder khi có item, bạn có thể đặt nó là null
                // hoặc TableView sẽ tự ẩn nó khi có dữ liệu.
                cartTableView.setPlaceholder(null);
            }
            // TableView nên tự động cập nhật khi ObservableList thay đổi.
            // cartTableView.refresh(); // Thường không cần thiết nếu CellValueFactory đúng và list là Observable
        });
        
        // Đặt placeholder ban đầu nếu giỏ hàng trống
        if (cartManager.getCartItems().isEmpty()) {
            cartTableView.setPlaceholder(new Label("Your shopping cart is empty."));
        }
    }

    private void setupTableColumns() {
        productNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProductName()));
        productPriceColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getProductPrice()));
        totalItemPriceColumn.setCellValueFactory(cellData -> cellData.getValue().totalPriceProperty().asObject());

        // Custom cell for Quantity with Spinner
        quantityColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue()));
        quantityColumn.setCellFactory(col -> new TableCell<CartItem, CartItem>() {
            private final Spinner<Integer> quantitySpinner = new Spinner<>();
            // Di chuyển HBox ra ngoài khối khởi tạo để tránh tạo lại mỗi lần updateItem
            private final HBox pane; 

            { // Khối khởi tạo instance cho mỗi cell
                pane = new HBox(5, quantitySpinner); // Khởi tạo HBox ở đây
                pane.setAlignment(Pos.CENTER);
                quantitySpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
                    CartItem currentItem = getItem(); // Lấy item của cell hiện tại
                    if (currentItem != null && newValue != null && !newValue.equals(oldValue)) {
                        System.out.println("Quantity changed for " + currentItem.getProductName() + " to " + newValue);
                        cartManager.updateQuantity(currentItem, newValue);
                        // updateSubtotal() sẽ được gọi bởi ListChangeListener trên cartItems
                        // hoặc bởi listener trên totalPriceProperty của CartItem nếu có.
                        // TableView nên tự refresh cột totalPrice khi totalPriceProperty thay đổi.
                    }
                });
            }

            @Override
            protected void updateItem(CartItem item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    // Chỉ cấu hình lại ValueFactory nếu item thay đổi hoặc spinner chưa được cấu hình cho item này
                    if (quantitySpinner.getValueFactory() == null || 
                        ((SpinnerValueFactory.IntegerSpinnerValueFactory)quantitySpinner.getValueFactory()).getValue() != item.getQuantity()) {
                        SpinnerValueFactory<Integer> valueFactory =
                                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, item.getQuantity()); // Min 0 để có thể xóa
                        quantitySpinner.setValueFactory(valueFactory);
                    }
                    setGraphic(pane);
                }
            }
        });
        quantityColumn.setStyle("-fx-alignment: CENTER;");


        // Custom cell for Actions (Remove button)
        actionsColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue()));
        actionsColumn.setCellFactory(col -> new TableCell<CartItem, CartItem>() {
            private final Button removeButton = new Button("Remove");
            { // Khối khởi tạo instance cho mỗi cell
                removeButton.getStyleClass().add("remove-button");
                removeButton.setOnAction(event -> {
                    CartItem itemToRemove = getItem(); // Lấy CartItem của dòng hiện tại
                    if (itemToRemove != null) {
                        System.out.println("Remove button clicked for: " + itemToRemove.getProductName());
                        cartManager.removeProduct(itemToRemove); // Đây sẽ kích hoạt ListChangeListener
                        // Không cần gọi updateSubtotal() hoặc refresh() ở đây nữa vì ListChangeListener sẽ xử lý
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
        // ListChangeListener sẽ tự động gọi updateSubtotal và cập nhật placeholder
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