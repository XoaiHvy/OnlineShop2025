package controller;

import Connect.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos; 
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import util.ChartGenerator; 
import javafx.scene.control.MenuItem; 
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane; 
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;        
import javafx.scene.media.MediaPlayer;  
import javafx.scene.media.MediaView;    
import javafx.stage.Stage;
import model.Product;
import model.User;
import util.UserSession;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DashboardController {

    @FXML private BorderPane mainBorderPane;
    @FXML private ImageView logoImageView;
    @FXML private TextField searchTextField;
    @FXML private Button searchButton;
    @FXML private Button orderHistoryNavButton;
    @FXML private Button cartNavButton;
    @FXML private MenuButton userProfileMenuButton;
    @FXML private HBox categoryNavBar;
    @FXML private ScrollPane mainScrollPane;
    @FXML private VBox contentVBox;
    @FXML private ImageView categoryPieChartImageView;
    // @FXML private ImageView mainBannerImageView; 
    @FXML private MediaView mainBannerMediaView;   
    @FXML private StackPane bannerStackPane;      
    @FXML private FlowPane featuredProductsPane;
    @FXML
    private Button prevPageButton;
    @FXML private StackPane centerContentStackPane; 
    @FXML private MediaView contentBackgroundMediaView;
    @FXML private HBox paginationControlsContainer; 
    @FXML private Label pageInfoLabel;
    @FXML private Button nextPageButton;

    private List<Product> allProductsFromDB;
    private List<Product> currentProductListSource;
    private ShoppingCartManager cartManager;
    private MediaPlayer dashboardBannerPlayer;
    private int currentPage = 1;
    private final int ITEMS_PER_PAGE = 8;
    private int totalPages = 1; 
    private MediaPlayer contentBgPlayer; 
    @FXML
    public void initialize() {
        System.out.println("DashboardController initialized.");
        cartManager = ShoppingCartManager.getInstance();

        initializeDashboardBannerVideo(); 
        initializeContentBackgroundVideo();
        allProductsFromDB = new ArrayList<>();
        currentProductListSource = new ArrayList<>();
        loadProductsFromDatabase();
        if (allProductsFromDB != null && !allProductsFromDB.isEmpty()) {
            Image pieChartImage = ChartGenerator.createCategoryPieChart(allProductsFromDB, 500, 350); 
            if (categoryPieChartImageView != null && pieChartImage != null) {
                categoryPieChartImageView.setImage(pieChartImage);
            } else if (categoryPieChartImageView != null) {
                categoryPieChartImageView.setImage(null); 
                System.err.println("Pie chart image is null, cannot display.");
            }
        } else {
             if (categoryPieChartImageView != null) categoryPieChartImageView.setImage(null);
             System.out.println("No product data for chart.");
        }
        
        showMainContentLayout(); 
    }

    private void initializeDashboardBannerVideo() {
        if (mainBannerMediaView == null) {
            System.err.println("mainBannerMediaView is null. Check FXML fx:id. Static banner will be attempted if ImageView exists.");
            // Optional: Fallback to static image if mainBannerImageView is still defined in FXML
            // and you want a static image if video setup fails or MediaView is missing.
            // try {
            //     URL bannerStaticUrl = getClass().getResource("/images/main_banner_placeholder.png");
            //     ImageView staticBanner = (ImageView) bannerStackPane.lookup("#mainBannerImageView"); // If you had an fx:id for it
            //     if (staticBanner != null && bannerStaticUrl != null) {
            //          staticBanner.setImage(new Image(bannerStaticUrl.toExternalForm()));
            //          staticBanner.setVisible(true);
            //     }
            // } catch (Exception e) {
            //     System.err.println("Error loading static fallback banner: " + e.getMessage());
            // }
            return;
        }
        try {
            String videoResourcePath = "/images/dashboard_banner_video.mp4"; // Tên tệp video của bạn
            URL videoUrl = getClass().getResource(videoResourcePath);

            if (videoUrl == null) {
                System.err.println("Cannot find dashboard banner video resource: " + videoResourcePath);
                if(bannerStackPane != null) bannerStackPane.setVisible(false); else mainBannerMediaView.setVisible(false);
                return;
            }
            
            Media media = new Media(videoUrl.toExternalForm());
            dashboardBannerPlayer = new MediaPlayer(media);
            mainBannerMediaView.setMediaPlayer(dashboardBannerPlayer);

            dashboardBannerPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            dashboardBannerPlayer.setMute(false); // Để video có tiếng
            dashboardBannerPlayer.setAutoPlay(true);

            dashboardBannerPlayer.setOnError(() -> {
                System.err.println("Dashboard Banner MediaPlayer Error: " + dashboardBannerPlayer.getError().getMessage());
                if(bannerStackPane != null) bannerStackPane.setVisible(false); else mainBannerMediaView.setVisible(false);
            });

            dashboardBannerPlayer.setOnReady(() -> {
                 System.out.println("Dashboard banner video is ready to play.");
            });

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error initializing dashboard banner video: " + e.getMessage());
            if(bannerStackPane != null) bannerStackPane.setVisible(false); else if (mainBannerMediaView != null) mainBannerMediaView.setVisible(false);
        }
    }
    private void initializeContentBackgroundVideo() {
        if (contentBackgroundMediaView == null) {
            System.err.println("contentBackgroundMediaView is null. Check FXML fx:id.");
            return;
        }
        try {
           
            String videoPath = "/images/another_background_video.mp4"; 
            URL videoUrl = getClass().getResource(videoPath);

            if (videoUrl == null) {
                System.err.println("Cannot find content background video: " + videoPath);
                contentBackgroundMediaView.setVisible(false);
                return;
            }
            
            Media media = new Media(videoUrl.toExternalForm());
            contentBgPlayer = new MediaPlayer(media);
            contentBackgroundMediaView.setMediaPlayer(contentBgPlayer);

            contentBgPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            contentBgPlayer.setMute(true); 
            contentBgPlayer.setAutoPlay(true);

            contentBgPlayer.setOnError(() -> {
                System.err.println("Content Background MediaPlayer Error: " + contentBgPlayer.getError().getMessage());
                contentBackgroundMediaView.setVisible(false);
            });
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error initializing content background video: " + e.getMessage());
            if (contentBackgroundMediaView != null) contentBackgroundMediaView.setVisible(false);
        }
    }
    public void stopAllMediaPlayer() {
        if (dashboardBannerPlayer != null) {
            dashboardBannerPlayer.stop();
            dashboardBannerPlayer.dispose();
            System.out.println("Dashboard banner MediaPlayer stopped and disposed.");
        }
        if (contentBgPlayer != null) { 
            contentBgPlayer.stop();
            contentBgPlayer.dispose();
            System.out.println("Content background MediaPlayer stopped and disposed.");
        }
    }

    private void showMainContentLayout() {
        if (mainScrollPane != null && contentVBox != null) {
            if (mainScrollPane.getContent() != contentVBox) {
                mainScrollPane.setContent(contentVBox);
            }
            
            if (bannerStackPane != null) { 
                bannerStackPane.setVisible(true);
                bannerStackPane.setManaged(true);
                if (dashboardBannerPlayer != null && dashboardBannerPlayer.getStatus() == MediaPlayer.Status.PAUSED) {
                    dashboardBannerPlayer.play(); // Phát lại video nếu đã tạm dừng
                } else if (dashboardBannerPlayer != null && dashboardBannerPlayer.getStatus() == MediaPlayer.Status.STOPPED) {
                    
                }
            } else if (mainBannerMediaView != null) { 
                 mainBannerMediaView.setVisible(true);
                 mainBannerMediaView.setManaged(true);
                  if (dashboardBannerPlayer != null && dashboardBannerPlayer.getStatus() == MediaPlayer.Status.PAUSED) {
                    dashboardBannerPlayer.play();
                }
            }

            if (featuredProductsPane != null) {
                featuredProductsPane.setVisible(true);
                featuredProductsPane.setManaged(true);
                displayFeaturedProducts();
            }
             mainScrollPane.setVvalue(0.0);
        } else {
            System.err.println("Error: mainScrollPane or contentVBox is null. Cannot show main content.");
        }
    }

   

    private void loadProductsFromDatabase() {
        allProductsFromDB = new ArrayList<>();
        String sql = "SELECT product_id, product_name, category, price FROM products";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            if (conn == null) {
                System.err.println("Failed to connect to the database from DashboardController.");
                displayErrorInScrollPane("Error: Could not connect to the database.");
                return;
            }

            while (rs.next()) {
                int id = rs.getInt("product_id");
                String name = rs.getString("product_name");
                String category = rs.getString("category");
                double price = rs.getDouble("price");
                String imagePathPlaceholder = "/images/product_placeholder.png";
                allProductsFromDB.add(new Product(id, name, category, price, imagePathPlaceholder));
            }
            System.out.println("Loaded " + allProductsFromDB.size() + " products from Database.");

        } catch (SQLException e) {
            System.err.println("SQL Error loading products: " + e.getMessage() + " (SQLState: " + e.getSQLState() + ", ErrorCode: " + e.getErrorCode() + ")");
            displayErrorInScrollPane("Error: Could not load products from the database.");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("General Error during database operation: " + e.getMessage());
            displayErrorInScrollPane("An unexpected error occurred while fetching data.");
        }
    }
    private void displayProductsPage() {
        if (featuredProductsPane == null) {
            System.err.println("featuredProductsPane is null. Cannot display products.");
            return;
        }
        featuredProductsPane.getChildren().clear();

        if (currentProductListSource == null || currentProductListSource.isEmpty()) {
            featuredProductsPane.getChildren().add(new Label("No products to display for the current selection."));
            updatePaginationControlsUI(); 
            return;
        }
        
       
        totalPages = (int) Math.ceil((double) currentProductListSource.size() / ITEMS_PER_PAGE);
        if (currentPage > totalPages && totalPages > 0) currentPage = totalPages;
        if (currentPage < 1 && totalPages > 0) currentPage = 1;
        if (totalPages == 0) currentPage = 1; 


        int startIndex = (currentPage - 1) * ITEMS_PER_PAGE;
        int endIndex = Math.min(startIndex + ITEMS_PER_PAGE, currentProductListSource.size());

        if (startIndex >= currentProductListSource.size() && !currentProductListSource.isEmpty()) { 
           
             currentPage = 1;
             startIndex = 0;
             endIndex = Math.min(ITEMS_PER_PAGE, currentProductListSource.size());
             if (startIndex >= currentProductListSource.size()) { 
                 featuredProductsPane.getChildren().add(new Label("No products on this page."));
                 updatePaginationControlsUI();
                 return;
             }
        } else if (currentProductListSource.isEmpty()) {
             featuredProductsPane.getChildren().add(new Label("No products match your criteria."));
             updatePaginationControlsUI();
             return;
        }


        for (int i = startIndex; i < endIndex; i++) {
            featuredProductsPane.getChildren().add(createProductCard(currentProductListSource.get(i)));
        }
        updatePaginationControlsUI();
    }
    private void updatePaginationControlsUI() {
        if (paginationControlsContainer == null || prevPageButton == null || pageInfoLabel == null || nextPageButton == null) {
            if (paginationControlsContainer != null) {
                paginationControlsContainer.setVisible(false);
                paginationControlsContainer.setManaged(false);
            }
            return;
        }

        if (currentProductListSource == null || currentProductListSource.isEmpty() || totalPages <= 1 ) {
             paginationControlsContainer.setVisible(false);
             paginationControlsContainer.setManaged(false);
        } else {
            paginationControlsContainer.setVisible(true);
            paginationControlsContainer.setManaged(true);
            pageInfoLabel.setText("Page " + currentPage + " of " + totalPages);
            prevPageButton.setDisable(currentPage == 1);
            nextPageButton.setDisable(currentPage == totalPages);
        }
    }
    private void displayErrorInScrollPane(String errorMessage) {
        if (mainScrollPane != null) {
            Label errorLabel = new Label(errorMessage);
            errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 16px; -fx-padding: 20px;");
            VBox errorContainer = new VBox(errorLabel);
            errorContainer.setAlignment(javafx.geometry.Pos.CENTER);
            mainScrollPane.setContent(errorContainer);
        } else {
            System.err.println("Cannot display error message: mainScrollPane is null.");
        }
    }

    private void displayFeaturedProducts() {
        if (featuredProductsPane == null) {
            System.err.println("featuredProductsPane is null in displayFeaturedProducts.");
            return;
        }
        featuredProductsPane.getChildren().clear();

        if (allProductsFromDB == null || allProductsFromDB.isEmpty()) {
            if (featuredProductsPane.getChildren().isEmpty()) {
                 featuredProductsPane.getChildren().add(new Label("No products available to display."));
            }
            return;
        }
        List<Product> featured = allProductsFromDB.stream().limit(8).collect(Collectors.toList());
        for (Product product : featured) {
            featuredProductsPane.getChildren().add(createProductCard(product));
        }
    }

    private void displayProductsByCategory(String targetCategory) {
        if (featuredProductsPane == null) return;
        featuredProductsPane.getChildren().clear();
        if (allProductsFromDB == null) {
             if (featuredProductsPane.getChildren().isEmpty())
                featuredProductsPane.getChildren().add(new Label("Product data not loaded."));
            return;
        }

        List<Product> categoryProducts = allProductsFromDB.stream()
                                           .filter(p -> p.getCategory().equalsIgnoreCase(targetCategory))
                                           .collect(Collectors.toList());
        if (categoryProducts.isEmpty()) {
            featuredProductsPane.getChildren().add(new Label("No products found in " + targetCategory + "."));
        } else {
            for (Product product : categoryProducts) {
                featuredProductsPane.getChildren().add(createProductCard(product));
            }
        }
    }

    private void displayAllProducts() {
        if (featuredProductsPane == null) return;
        featuredProductsPane.getChildren().clear();
         if (allProductsFromDB == null) {
             if (featuredProductsPane.getChildren().isEmpty())
                featuredProductsPane.getChildren().add(new Label("Product data not loaded."));
            return;
        }
        for (Product product : allProductsFromDB) {
            featuredProductsPane.getChildren().add(createProductCard(product));
        }
    }
    
    private void displaySearchedProducts(String searchTerm) {
        if (featuredProductsPane == null) return;
        featuredProductsPane.getChildren().clear();
        if (allProductsFromDB == null) {
             if (featuredProductsPane.getChildren().isEmpty())
                featuredProductsPane.getChildren().add(new Label("Product data not loaded."));
            return;
        }

        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            showMainContentLayout();
            return;
        }
        String lowerSearchTerm = searchTerm.trim().toLowerCase();
        List<Product> searchedProducts = allProductsFromDB.stream()
                .filter(p -> p.getProductName().toLowerCase().contains(lowerSearchTerm) ||
                             p.getCategory().toLowerCase().contains(lowerSearchTerm))
                .collect(Collectors.toList());
        
        if (searchedProducts.isEmpty()) {
            featuredProductsPane.getChildren().add(new Label("No products found for '" + searchTerm + "'."));
        } else {
            for (Product product : searchedProducts) {
                featuredProductsPane.getChildren().add(createProductCard(product));
            }
        }
    }

    private VBox createProductCard(Product product) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(10));
        card.getStyleClass().add("product-card");
        card.setPrefWidth(250);
        card.setPrefHeight(350);

        ImageView imageView = new ImageView();
        try {
            String imagePath = product.getImagePath();
            URL imageUrl = (imagePath != null && !imagePath.isEmpty()) ? getClass().getResource(imagePath) : null;
            
            if (imageUrl != null) {
                 imageView.setImage(new Image(imageUrl.toExternalForm()));
            } else {
                URL placeholderUrl = getClass().getResource("/images/product_placeholder.png");
                if(placeholderUrl != null) {
                    imageView.setImage(new Image(placeholderUrl.toExternalForm()));
                } else {
                     System.err.println("Critical Error: Default product_placeholder.png not found!");
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading image for product '" + product.getProductName() + "' (path: '" + product.getImagePath() + "'): " + e.getMessage());
            URL placeholderUrl = getClass().getResource("/images/product_placeholder.png");
            if(placeholderUrl != null) {
                imageView.setImage(new Image(placeholderUrl.toExternalForm()));
            }
        }
        imageView.setFitHeight(180);
        imageView.setFitWidth(220);
        imageView.setPreserveRatio(true);
        VBox.setMargin(imageView, new Insets(0, 0, 5, 0));

        Label nameLabel = new Label(product.getProductName());
        nameLabel.getStyleClass().add("product-name-label");
        nameLabel.setWrapText(true);
        nameLabel.setMinHeight(40);
        nameLabel.setMaxWidth(Double.MAX_VALUE);

        Label priceLabel = new Label(String.format("$%.2f", product.getPrice()));
        priceLabel.getStyleClass().add("product-price-label");
        priceLabel.setMaxWidth(Double.MAX_VALUE);

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Button addToCartButton = new Button("Add to Cart");
        addToCartButton.getStyleClass().add("add-to-cart-button");
        addToCartButton.setOnAction(event -> {
            System.out.println("Adding to cart: " + product.getProductName());
            cartManager.addProduct(product, 1);
            Alert confirmation = new Alert(Alert.AlertType.INFORMATION);
            confirmation.setTitle("Product Added");
            confirmation.setHeaderText(null);
            confirmation.setContentText(product.getProductName() + " has been added to your cart.");
            confirmation.showAndWait();
        });
        addToCartButton.setMaxWidth(Double.MAX_VALUE);

        card.getChildren().addAll(imageView, nameLabel, priceLabel, spacer, addToCartButton);
        
        card.setOnMouseClicked(event -> {
            System.out.println("Product card clicked (details view temporarily disabled): " + product.getProductName());
            // showProductDetails(product);
        });
        return card;
    }
        
    private void showProductDetails(Product product) {
        System.out.println("Attempting to show details for product: " + product.getProductName());
        loadViewIntoScrollPane("/view/ProductDetail.fxml", "Product Details", product);
    }

    @FXML
    void handleSearch(ActionEvent event) {
        String searchTerm = searchTextField.getText();
        System.out.println("Search initiated for: " + searchTerm);
        
        if (mainScrollPane != null && contentVBox != null) {
            if (mainBannerMediaView != null) { 
                mainBannerMediaView.setVisible(false); 
                mainBannerMediaView.setManaged(false); 
                if (dashboardBannerPlayer != null && dashboardBannerPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                    dashboardBannerPlayer.pause(); 
                }
            }
            
            if (!contentVBox.getChildren().contains(featuredProductsPane)) {
                contentVBox.getChildren().clear(); 
                contentVBox.getChildren().add(featuredProductsPane);
            }
            featuredProductsPane.setVisible(true);
            featuredProductsPane.setManaged(true);
            mainScrollPane.setContent(contentVBox);
        }
        displaySearchedProducts(searchTerm);
        if (mainScrollPane != null) mainScrollPane.setVvalue(0.0);
    }

    @FXML
    void handleOrderHistory(ActionEvent event) {
        System.out.println("Order History clicked.");
        loadViewIntoScrollPane("/view/OrderHistory.fxml", "Order History", null);
    }

    @FXML
    void handleViewCart(ActionEvent event) {
        System.out.println("Cart clicked.");
        loadViewIntoScrollPane("/view/ShoppingCart.fxml", "Shopping Cart", null);
    }
    
    @FXML
    void handleCategoryLinkAction(ActionEvent event) {
        showMainContentLayout(); 

        if (event.getSource() instanceof Button) {
            Button clickedButton = (Button) event.getSource();
            if (clickedButton.getUserData() instanceof String) {
                String category = (String) clickedButton.getUserData();
                System.out.println("Category link clicked: " + category);
                if ("All Products".equalsIgnoreCase(category)) {
                    displayAllProducts();
                } else {
                    displayProductsByCategory(category);
                }
            }
        }
    }

    @FXML
    void handleMyAccount(ActionEvent event) {
        System.out.println("My Account clicked.");
        User loggedInUser = UserSession.getInstance().getLoggedInUser();
        if (loggedInUser == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please log in to view your profile.");
            alert.showAndWait();
            return;
        }
        loadViewIntoScrollPane("/view/MyProfile.fxml", "My Profile", loggedInUser);
    }

    public void stopAllMediaPlayers() {
        if (dashboardBannerPlayer != null) {
            dashboardBannerPlayer.stop();
            dashboardBannerPlayer.dispose(); 
            System.out.println("Dashboard banner MediaPlayer stopped and disposed.");
        }
        
    }
    @FXML
    void handlePreviousPage(ActionEvent event) {
       
        System.out.println("Previous Page Clicked");
        if (currentPage > 1) {
            currentPage--;
            displayProductsPage(); 
        }
    }

    @FXML
    void handleNextPage(ActionEvent event) {
      
        System.out.println("Next Page Clicked");
        if (currentPage < totalPages) {
            currentPage++;
            displayProductsPage();
        }
    }
    @FXML
    void handleLogout(ActionEvent event) {
        System.out.println("Logout clicked.");
        stopAllMediaPlayers(); 
        UserSession.getInstance().clearSession();
        try {
            if (mainBorderPane != null && mainBorderPane.getScene() != null && mainBorderPane.getScene().getWindow() != null) {
                Parent loginRoot = FXMLLoader.load(getClass().getResource("/view/Login.fxml"));
                Stage stage = (Stage) mainBorderPane.getScene().getWindow();
                stage.setScene(new Scene(loginRoot));
                stage.setTitle("Online Shop - Login");
                stage.centerOnScreen();
            } else {
                 System.err.println("Cannot logout: UI elements for stage retrieval are null.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadViewIntoScrollPane(String fxmlPath, String viewName, Object dataToPass) {
       
        if (dashboardBannerPlayer != null && dashboardBannerPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            dashboardBannerPlayer.pause();
        }
        if (bannerStackPane != null) { 
            bannerStackPane.setVisible(false);
            bannerStackPane.setManaged(false);
        }
      
        if (featuredProductsPane != null) {
            featuredProductsPane.setVisible(false);
            featuredProductsPane.setManaged(false);
        }


        try {
            URL fxmlUrl = getClass().getResource(fxmlPath);
            if (fxmlUrl == null) {
                System.err.println("Error: FXML file not found at " + fxmlPath);
                displayErrorInScrollPane("Error: The " + viewName + " view is currently unavailable.");
                return;
            }

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent viewRoot = loader.load();

            if (dataToPass != null) {
                Object controller = loader.getController();
              
            }

            if (mainScrollPane != null) {
                mainScrollPane.setContent(viewRoot); 
                mainScrollPane.setVvalue(0.0);
            } else {
                System.err.println("Cannot display " + viewName + ": mainScrollPane is null.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            displayErrorInScrollPane("Error loading " + viewName + ": " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            displayErrorInScrollPane("An unexpected error occurred while loading " + viewName + ".");
        }
    }
}