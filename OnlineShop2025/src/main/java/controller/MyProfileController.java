// File: src/controller/MyProfileController.java
package controller;

import Connect.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label; // Import Label if needed for non-editable fields
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.User; // Your User model

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MyProfileController {

    @FXML private TextField usernameField; 
    @FXML private TextField fullNameField;
    @FXML private TextField emailField;
    @FXML private TextArea addressTextArea;
    @FXML private TextField phoneNumberField;
    @FXML private Button changePasswordButton;
    @FXML private Button saveChangesButton;
    private int currentLoggedInUserId = 2;
    private User currentUser;

    @FXML
    public void initialize() {
        System.out.println("MyProfileController initialized.");
        usernameField.setEditable(false); 
        loadUserProfile();
    }

    private void loadUserProfile() {
        String sql = "SELECT user_id, username, email, full_name, address, phone_number FROM users WHERE user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (conn == null) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to connect to the database.");
                return;
            }

            pstmt.setInt(1, currentLoggedInUserId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                currentUser = new User(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("full_name"),
                        rs.getString("address"),
                        rs.getString("phone_number")
                );
                populateFields(currentUser);
            } else {
                showAlert(Alert.AlertType.WARNING, "User Not Found", "Could not find profile for user ID: " + currentLoggedInUserId);
                // disableFields(); // Optionally disable fields if user not found
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "SQL Error", "Error loading user profile: " + e.getMessage());
        }
    }

    private void populateFields(User user) {
        if (user == null) return;
        usernameField.setText(user.getUsername());
        fullNameField.setText(user.getFullName() != null ? user.getFullName() : "");
        emailField.setText(user.getEmail() != null ? user.getEmail() : "");
        addressTextArea.setText(user.getAddress() != null ? user.getAddress() : "");
        phoneNumberField.setText(user.getPhoneNumber() != null ? user.getPhoneNumber() : "");
    }

    @FXML
    void handleSaveChanges(ActionEvent event) {
        System.out.println("Save Changes clicked.");
        if (currentUser == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "No user data loaded to save.");
            return;
        }

        String sql = "UPDATE users SET full_name = ?, email = ?, address = ?, phone_number = ? WHERE user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (conn == null) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to connect to the database.");
                return;
            }

            pstmt.setString(1, fullNameField.getText());
            pstmt.setString(2, emailField.getText());
            pstmt.setString(3, addressTextArea.getText());
            pstmt.setString(4, phoneNumberField.getText());
            pstmt.setInt(5, currentUser.getUserId());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Profile updated successfully!");
                // Optionally reload profile to reflect changes if not automatically bound
                loadUserProfile(); 
            } else {
                showAlert(Alert.AlertType.ERROR, "Update Failed", "Could not update profile. Please try again.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "SQL Error", "Error saving profile: " + e.getMessage());
        }
    }

    @FXML
    void handleChangePassword(ActionEvent event) {
        System.out.println("Change Password clicked.");
        // TODO: Implement change password functionality
        // This usually involves opening a new dialog/window asking for:
        // 1. Current Password
        // 2. New Password
        // 3. Confirm New Password
        // Then, validate and update the password_hash in the database.
        showAlert(Alert.AlertType.INFORMATION, "Feature Not Implemented", "Change password functionality is not yet implemented.");
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

	public void setCurrentUser(User userToPass) {
		// TODO Auto-generated method stub
		
	}
}