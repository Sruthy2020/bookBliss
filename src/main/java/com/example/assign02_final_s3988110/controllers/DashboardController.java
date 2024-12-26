package com.example.assign02_final_s3988110.controllers;

import com.example.assign02_final_s3988110.models.*;
import com.example.assign02_final_s3988110.DAO.*;
import com.example.assign02_final_s3988110.utils.*;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Controller class for the Dashboard view.
 */
public class DashboardController {
    @FXML
    private Label welcomeLabel;
    @FXML
    private ImageView viewCartImage;
    @FXML
    private TableView<Book> popularBooksListView;
    @FXML
    private TableColumn<Book, String> bookTitleColumn;
    @FXML
    private TableColumn<Book, Integer> booksSoldColumn;

    private final BookDaoImpl bookDao = new BookDaoImpl();
    private final UserDaoImpl userDao = new UserDaoImpl();
    private final BookUtil bookUtil = new BookUtil();

    /**
     * Initializes the DashboardController.
     * Sets up the initial view components and loads user and book data.
     */
    @FXML
    public void initialize() {
        try {
            userDao.setup();
            bookDao.setup();
            bookTitleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
            booksSoldColumn.setCellValueFactory(cellData -> cellData.getValue().soldCopiesProperty().asObject());
            User user = UserSingleton.getInstance().getUser();
            welcomeLabel.setText("Welcome, " + user.getFirstName() + "!");
            loadTop5PopularBooks();
        } catch (SQLException e) {
            e.printStackTrace();
            welcomeLabel.setText("Error fetching user details.");
        }
    }



    /**
     * Loads the top 5 popular books and sets them in the TableView.
     */
    private void loadTop5PopularBooks() {
        try {
            ObservableList<Book> bookList = bookUtil.getTop5PopularBooks();
            popularBooksListView.setItems(bookList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    /**
     * Handles the action when the view cart image is clicked.
     * Navigates to the view cart view.
     */
    @FXML
    private void handleViewCartClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/assign02_final_s3988110/views/viewCart.fxml"));
            Parent viewCartRoot = loader.load();
            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            stage.setScene(new Scene(viewCartRoot));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    /**
     * Navigates to the Edit Profile view.
     */
    @FXML
    private void navigateToEditProfile() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/assign02_final_s3988110/views/editProfileView.fxml"));
            Parent editProfileRoot = loader.load();
            EditProfileController editProfileController = loader.getController();
            editProfileController.initData(UserSingleton.getInstance().getUser());
            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            stage.setScene(new Scene(editProfileRoot));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    /**
     * Handles logout button action.
     * Logs out the user and navigates to the welcome view.
     */
    @FXML
    private void handleLogoutButtonAction() {
        try {
            UserSingleton.getInstance().setUser(null);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/assign02_final_s3988110/views/welcomeView.fxml"));
            Parent welcomeRoot = loader.load();
            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            stage.setScene(new Scene(welcomeRoot));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    /**
     * Handles delete profile button action.
     * Deletes the user's profile after confirmation and navigates to the welcome view.
     */
    @FXML
    private void handleDeleteProfileButtonAction() {
        try {
            User user = UserSingleton.getInstance().getUser();
            if (user != null) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Delete Profile");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to delete your profile? This action cannot be undone.");
                if (alert.showAndWait().get() == ButtonType.OK) {
                    userDao.deleteUser(user.getUsername());
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/assign02_final_s3988110/views/welcomeView.fxml"));
                    Parent welcomeRoot = loader.load();
                    Stage stage = (Stage) welcomeLabel.getScene().getWindow();
                    stage.setScene(new Scene(welcomeRoot));
                }
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("An error occurred while deleting your profile.");
            alert.showAndWait();
        }
    }



    /**
     * Navigates to the shopping cart view.
     */
    @FXML
    private void navigateToShoppingCart() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/assign02_final_s3988110/views/shoppingCartView.fxml"));
            Parent shoppingCartRoot = loader.load();
            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            stage.setScene(new Scene(shoppingCartRoot));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    /**
     * Navigates to the order details view.
     */
    @FXML
    private void navigateToOrderDetails() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/assign02_final_s3988110/views/orderDetailsView.fxml"));
            Parent orderDetailsRoot = loader.load();
            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            stage.setScene(new Scene(orderDetailsRoot));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
