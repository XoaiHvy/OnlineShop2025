package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Product; // Lớp Product model của bạn

public class ProductDetailController {

    @FXML
    private Label productNameLabel;

    @FXML
    private ImageView productImageView;

    @FXML
    private Label productPriceLabel;

    @FXML
    private Label productCategoryLabel;

    @FXML
    private Label productDescriptionLabel; // Sẽ cần thêm trường description vào model Product

    private Product currentProduct;

    @FXML
    public void initialize() {
        System.out.println("ProductDetailController initialized.");
    }

    // Phương thức này sẽ được gọi từ DashboardController để truyền dữ liệu sản phẩm
    public void setProduct(Product product) {
        this.currentProduct = product;
        if (product != null) {
            productNameLabel.setText(product.getProductName());
            productPriceLabel.setText(String.format("$%.2f", product.getPrice()));
            productCategoryLabel.setText("Category: " + product.getCategory());
            // productDescriptionLabel.setText(product.getDescription()); // Cần thêm getDescription() vào Product

            // Load product image
            try {
                String imagePath = product.getImagePath();
                if (imagePath != null && !imagePath.isEmpty() && getClass().getResource(imagePath) != null) {
                    productImageView.setImage(new Image(getClass().getResourceAsStream(imagePath)));
                } else {
                    // Fallback to a placeholder if no specific image or path is invalid
                    productImageView.setImage(new Image(getClass().getResourceAsStream("/images/product_placeholder.png")));
                }
            } catch (Exception e) {
                System.err.println("Error loading product detail image: " + product.getImagePath() + " - " + e.getMessage());
                productImageView.setImage(new Image(getClass().getResourceAsStream("/images/product_placeholder.png")));
            }
        }
    }

    @FXML
    void handleAddToCart(ActionEvent event) {
        if (currentProduct != null) {
            System.out.println("Add to cart (from detail): " + currentProduct.getProductName());
            // TODO: Implement add to cart logic
        }
    }
}