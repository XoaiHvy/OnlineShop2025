// File: src/controller/LoginController.java
package controller;

import Connect.DBConnection; // Import nếu bạn định dùng sau này
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;       // Import cho loginFormVBox
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.User; // Import User model nếu bạn sẽ dùng UserSession
import util.UserSession; // Import UserSession nếu bạn sẽ dùng

import java.io.IOException;
import java.net.URL;
// Các import cho SQL nếu bạn sẽ chuyển sang DB authentication
// import java.sql.Connection;
// import java.sql.PreparedStatement;
// import java.sql.ResultSet;
// import java.sql.SQLException;
// import java.sql.Timestamp;

public class LoginController {

    @FXML
    private AnchorPane rootPane; // Đảm bảo fx:id="rootPane" có trong Login.fxml

    @FXML
    private MediaView loginBackgroundVideoView; // fx:id cho video nền

    @FXML
    private VBox loginFormVBox; // fx:id cho VBox chứa form (cho animation)

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private CheckBox rememberMeCheckBox;

    @FXML
    private Hyperlink forgotPasswordLink;

    @FXML
    private Button loginButton;

    @FXML
    private Button googleLoginButton; // Giữ lại nếu FXML mới của bạn vẫn có nút này

    @FXML
    private Hyperlink signUpLink;

    private MediaPlayer backgroundMediaPlayer;

    @FXML
    void initialize() {
        System.out.println("LoginController initialized with new UI logic.");
        if (usernameField != null) {
            usernameField.requestFocus();
        }
        initializeLoginBackgroundVideo();

        // Áp dụng animation xuất hiện cho form bằng JavaFX
        // Nếu bạn đã dùng animation CSS "@keyframes formAppear" cho ".glass-login-form",
        // thì comment out hoặc xóa đoạn code này để tránh xung đột. Chọn MỘT cách.
        applyLoginFormAppearAnimation();

        // --- BINDING KÍCH THƯỚC MEDIAVIEW VỚI ROOTPANE ---
        if (loginBackgroundVideoView != null && rootPane != null) {
            loginBackgroundVideoView.fitWidthProperty().bind(rootPane.widthProperty());
            loginBackgroundVideoView.fitHeightProperty().bind(rootPane.heightProperty());
            System.out.println("DEBUG: MediaView size bound to rootPane size.");
        } else {
            System.err.println("Cannot bind MediaView size: rootPane or loginBackgroundVideoView is null. Check FXML fx:ids.");
        }
        // --- KẾT THÚC BINDING ---
    }

    private void initializeLoginBackgroundVideo() {
        if (loginBackgroundVideoView == null) {
            System.err.println("loginBackgroundVideoView is null. Cannot initialize video.");
            return;
        }
        try {
            String videoResourcePath = "/images/glass_login_video_bg.mp4"; // Đảm bảo tên tệp đúng
            URL videoUrl = getClass().getResource(videoResourcePath);

            if (videoUrl == null) {
                System.err.println("Cannot find login background video resource: " + videoResourcePath);
                loginBackgroundVideoView.setVisible(false);
                return;
            }

            Media media = new Media(videoUrl.toExternalForm());
            backgroundMediaPlayer = new MediaPlayer(media);
            loginBackgroundVideoView.setMediaPlayer(backgroundMediaPlayer);

            backgroundMediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            backgroundMediaPlayer.setMute(true);
            backgroundMediaPlayer.setAutoPlay(true);

            backgroundMediaPlayer.setOnError(() -> {
                System.err.println("Login Background MediaPlayer Error: " + backgroundMediaPlayer.getError().getMessage());
                loginBackgroundVideoView.setVisible(false);
            });

            backgroundMediaPlayer.setOnReady(() -> {
                 System.out.println("Login background video is ready to play.");
            });

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error initializing login background video: " + e.getMessage());
            if (loginBackgroundVideoView != null) loginBackgroundVideoView.setVisible(false);
        }
    }

    private void applyLoginFormAppearAnimation() {
        if (loginFormVBox != null) {
            loginFormVBox.setOpacity(0);
            loginFormVBox.setTranslateY(20);

            FadeTransition ft = new FadeTransition(Duration.millis(600), loginFormVBox);
            ft.setFromValue(0.0);
            ft.setToValue(1.0);
            ft.setDelay(Duration.millis(300));

            TranslateTransition tt = new TranslateTransition(Duration.millis(600), loginFormVBox);
            tt.setFromY(20);
            tt.setToY(0);
            tt.setDelay(Duration.millis(300));

            ft.play();
            tt.play();
        } else {
            System.err.println("loginFormVBox is null, cannot apply appear animation.");
        }
    }

    private void stopLoginBackgroundVideo() {
        if (backgroundMediaPlayer != null) {
            backgroundMediaPlayer.stop();
            backgroundMediaPlayer.dispose();
            backgroundMediaPlayer = null;
            System.out.println("Login background MediaPlayer stopped and disposed.");
        }
    }

    @FXML
    void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.equals("admin") && password.equals("123")) {
            System.out.println("Login success!");
            stopLoginBackgroundVideo();

            User loggedInAdmin = new User();
            loggedInAdmin.setUserId(1);
            loggedInAdmin.setUsername("admin");
            loggedInAdmin.setEmail("admin@example.com");
            loggedInAdmin.setFullName("Administrator");
            UserSession.getInstance().setLoggedInUser(loggedInAdmin);

            try {
                URL dashboardFxmlUrl = getClass().getResource("/view/Dashboard.fxml");
                if (dashboardFxmlUrl == null) {
                    showAlert(Alert.AlertType.ERROR, "Application Error", "Dashboard screen is missing.");
                    return;
                }
                Parent root = FXMLLoader.load(dashboardFxmlUrl);
                Stage stage = (Stage) loginButton.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Online Shop - Dashboard");
                stage.centerOnScreen();
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Navigation Error", "Error loading Dashboard: " + e.getMessage());
            }
        } else {
            System.out.println("Login fail. Username: [" + username + "], Password: [" + password + "]");
            showAlert(Alert.AlertType.ERROR, "Login Error", "The username or password is incorrect!");
        }
    }

    @FXML
    void handleForgotPassword(ActionEvent event) {
        System.out.println("The 'Forgot Password?' link has been clicked!");
        showAlert(Alert.AlertType.INFORMATION, "Feature Unavailable", "The password recovery function is currently under development.");
    }

    @FXML
    void handleSignUp(ActionEvent event) {
        System.out.println("The 'Sign Up Now' link has been clicked!");
        showAlert(Alert.AlertType.INFORMATION, "Feature Unavailable", "The registration function is currently under development.");
    }

    @FXML
    void handleGoogleLogin(ActionEvent event) {
        System.out.println("The 'Log in with Google' button has been clicked!");
        showAlert(Alert.AlertType.INFORMATION, "Feature Unavailable", "The Google login feature is currently under development.");
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}