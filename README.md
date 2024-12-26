# BookBliss
![bookBliss.png](src%2Fmain%2Fresources%2Fcom%2Fexample%2Fassign02_final_s3988110%2Fviews%2FbookBliss.png)

**Project Overview**

BookBliss is a  Java-based application designed for book enthusiasts to explore, manage, and purchase books seamlessly. Built with a user-friendly interface using JavaFX, BookBliss enables both Users and Admins to navigate various functionalities tailored to their roles. Users can manage their shopping cart, view their purchase history, and see popular books, while Admins can manage inventory, orders, and user accounts.

**Features**

Admin Functionality
* Login: Use credentials admin and password reading_admin to access the admin dashboard.
* Admin Dashboard
* Manage Inventory: update book quantities.
* View All Books: Access a comprehensive list of books in the inventory.
* View Orders: Access order details.
* User Management: View all registered users.

User Functionality
* Create and Manage Profile: Users can sign up, log in, edit their profile, and delete it if needed.
* User Dashboard
* Popular Books: View the top 5 most popular books based on sales.
* Shopping Cart: A quick cart icon allows users to view and manage items in their cart.
* Shopping Cart Management
* Add and Remove Items: Add books to the cart, adjust quantities, and view a real-time cart summary.
* Checkout: Place an order with built-in validations, clear the cart upon checkout, and save the order.
* Order History: Access previous orders and export order details to a CSV file. 

**Design Patterns**

Singleton Pattern:
UserSingleton: Ensures only one instance of the current logged-in user is active at any time.
DatabaseSingleton: Manages a single connection instance to the database, optimizing resource usage and consistency.

**Technologies Used**

* Java (JDK 20): Core programming language.
* JavaFX: For building a responsive GUI.
* SQLite: A lightweight, file-based RDBMS for data storage.
* Maven: Manages dependencies and project structure.
* CSV Export Utility: Export order details to CSV format for easy record-keeping.
* JUNIT: Testing the database.

**Prerequisites**
* Java JDK 20
* Maven
* JavaFX SDK (if JavaFX isn’t included in your JDK)

**Usage Instructions**

* Login as Admin: Use admin as the username and reading_admin as the password.
User Account:
* Sign Up: Create a new user account to access the BookBliss store.
* Manage Cart: Browse books, add items to your cart, adjust quantities, and proceed to checkout.
* Order History: Access your previous orders, and export order details for record-keeping.

**Folder Structure**

* models/: Contains data classes (User, Admin, CartItem, etc.).
* DAO/: Handles database operations for books, orders, and users.
* controllers/: Manages GUI events and user interactions.
* utils/: Helper classes for cart operations, validations, and CSV export functionality.
