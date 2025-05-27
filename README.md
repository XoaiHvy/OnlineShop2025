# Online Shop 2025

An Object-Oriented JavaFX application that simulates a modern e-commerce desktop shopping platform with integrated user interface, cart, order management, and statistics via JFreeChart.

##  Features

-  Login/Logout system with session management
-  Product listing, search, category filter
-  Shopping cart with quantity control
-  Order history (per user)
-  Dashboard with statistical charts (category distribution)
-  User profile management (view & edit)
-  Password update placeholder (not yet implemented)

## ⚙ Technologies Used

- **Java 22.0.2**
- **JavaFX** (UI with FXML & SceneBuilder)
- **MySQL** (JDBC for database)
- **JFreeChart** (for charts and analytics)
- **Maven** (project build & dependencies)

##  Project Structure

```bash
src/
├── controller/        # JavaFX controllers (UI logic)
├── model/             # POJO model classes (Product, User, OrderSummary...)
├── util/              # Helper classes (ChartGenerator, UserSession)
├── view/              # FXML UI files
├── Connect/           # Database connection (DBConnection.java)
└── Main.java          # Application entry point
```
🔧 Setup Instructions
Install Requirements:

Java JDK 17+

MySQL Server

JavaFX SDK (add to classpath)

Maven (optional for building)

Import the Project into an IDE (Eclipse or IntelliJ recommended).

Configure Database:

Import the provided .sql schema (or products.csv and orders.csv into tables).

Update DBConnection.java with your DB credentials.

Run the Application:

Launch Main.java from your IDE.

Login with a valid user from the users table.

DEMO ACCOUNT
Username: admin
Password: 123

Notes
Currently supports only basic admin/customer functions.

Payment processing, delivery system, and security enhancements are not implemented yet. 

!!! LINK MY YOUTUBE:  https://youtu.be/vM9qQWqIPpI
!!! LiNK GITHUB: https://github.com/XoaiHvy/OnlineShop2025.git
