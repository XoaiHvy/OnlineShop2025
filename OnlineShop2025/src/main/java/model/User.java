// File: src/model/User.java
package model;

// Import các SimpleProperty nếu bạn muốn dùng cho binding trong tương lai,
// nhưng với việc hiển thị và cập nhật đơn giản, các kiểu dữ liệu Java thông thường là đủ.
import java.time.LocalDateTime;

public class User {
    private int userId;
    private String username;
    // Không nên lưu trữ hoặc truyền password_hash trực tiếp trong đối tượng User thường xuyên
    // Trừ khi đang xử lý việc thay đổi mật khẩu.
    private String email;
    private String fullName;
    private String address;
    private String phoneNumber;
    private LocalDateTime registrationDate; // Hoặc String nếu bạn muốn định dạng

    // Constructor đầy đủ
    public User(int userId, String username, String email, String fullName, String address, String phoneNumber, LocalDateTime registrationDate) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.fullName = fullName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.registrationDate = registrationDate;
    }

    // Constructor đơn giản hơn cho việc hiển thị/cập nhật (không bao gồm password)
    public User(int userId, String username, String email, String fullName, String address, String phoneNumber) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.fullName = fullName;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }
    
    // Constructor rỗng (có thể cần cho một số thư viện hoặc FXML nếu dùng binding phức tạp)
    public User() {}


    // Getters
    public int getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getFullName() { return fullName; }
    public String getAddress() { return address; }
    public String getPhoneNumber() { return phoneNumber; }
    public LocalDateTime getRegistrationDate() { return registrationDate; }

    // Setters
    public void setUserId(int userId) { this.userId = userId; }
    public void setUsername(String username) { this.username = username; }
    public void setEmail(String email) { this.email = email; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setAddress(String address) { this.address = address; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setRegistrationDate(LocalDateTime registrationDate) { this.registrationDate = registrationDate; }
}