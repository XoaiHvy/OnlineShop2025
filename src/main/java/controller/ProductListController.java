package controller; // Đảm bảo đúng package

import javafx.fxml.FXML;
import javafx.scene.control.Label; // Import cho Label

public class ProductListController {

    @FXML
    private Label categoryLabel; // Để hiển thị category hoặc search term (tùy chọn)

    @FXML
    public void initialize() {
        System.out.println("ProductListController initialized.");
        // Ban đầu, không làm gì nhiều ở đây
    }

    // Các phương thức này sẽ được gọi từ DashboardController
    // Hiện tại chúng chỉ in ra console để bạn biết chúng được gọi
    public void loadProductsBySearch(String searchTerm) {
        System.out.println("ProductListController: Loading products by search term: " + searchTerm);
        if (categoryLabel != null) { // Kiểm tra null trước khi dùng
            categoryLabel.setText("Search Results for: " + searchTerm);
        }
        // TODO: Implement logic to fetch and display products based on search term
    }

    public void loadProductsByCategory(String category) {
        System.out.println("ProductListController: Loading products for category: " + category);
        if (categoryLabel != null) {
            categoryLabel.setText("Category: " + category);
        }
        // TODO: Implement logic to fetch and display products for the
    }

    public void loadAllProducts() {
        System.out.println("ProductListController: Loading all products.");
        if (categoryLabel != null) {
            categoryLabel.setText("All Products");
        }
        // TODO: Implement logic to fetch and display all products
    }
}