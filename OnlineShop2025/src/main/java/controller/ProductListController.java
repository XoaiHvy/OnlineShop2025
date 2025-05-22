package controller; 

import javafx.fxml.FXML;
import javafx.scene.control.Label; 

public class ProductListController {

    @FXML
    private Label categoryLabel; 

    @FXML
    public void initialize() {
        System.out.println("ProductListController initialized.");
       
    }

   
    public void loadProductsBySearch(String searchTerm) {
        System.out.println("ProductListController: Loading products by search term: " + searchTerm);
        if (categoryLabel != null) { 
            categoryLabel.setText("Search Results for: " + searchTerm);
        }
       
    }

    public void loadProductsByCategory(String category) {
        System.out.println("ProductListController: Loading products for category: " + category);
        if (categoryLabel != null) {
            categoryLabel.setText("Category: " + category);
        }
        
    }

    public void loadAllProducts() {
        System.out.println("ProductListController: Loading all products.");
        if (categoryLabel != null) {
            categoryLabel.setText("All Products");
        }
        
    }
}