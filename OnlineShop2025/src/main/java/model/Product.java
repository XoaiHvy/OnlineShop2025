// File: src/model/Product.java
package model;

public class Product {
    private int productId;
    private String productName;
    private String category;
    private double price;
    private String imagePath; // Add this for product images

    public Product(int productId, String productName, String category, double price, String imagePath) {
        this.productId = productId;
        this.productName = productName;
        this.category = category;
        this.price = price;
        this.imagePath = imagePath; // Initialize (can be placeholder path for now)
    }

    // Getters
    public int getProductId() { return productId; }
    public String getProductName() { return productName; }
    public String getCategory() { return category; }
    public double getPrice() { return price; }
    public String getImagePath() { return imagePath; }

    // Setters (optional, depending on if you need to modify product data after creation)
    public void setProductName(String productName) { this.productName = productName; }
    public void setCategory(String category) { this.category = category; }
    public void setPrice(double price) { this.price = price; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    @Override
    public String toString() {
        return "Product{" +
               "productId=" + productId +
               ", productName='" + productName + '\'' +
               ", category='" + category + '\'' +
               ", price=" + price +
               ", imagePath='" + imagePath + '\'' +
               '}';
    }
}