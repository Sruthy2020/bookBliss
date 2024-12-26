package com.example.assign02_final_s3988110.controllers;

import com.example.assign02_final_s3988110.models.Order;
import com.example.assign02_final_s3988110.DAO.OrderDaoImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Controller class for managing the order list view.
 * This class handles displaying order information and navigating to the admin dashboard.
 */
public class OrderListController {

    @FXML
    private TableView<Order> orderTable;
    @FXML
    private TableColumn<Order, String> orderNumberColumn;
    @FXML
    private TableColumn<Order, String> usernameColumn;
    @FXML
    private TableColumn<Order, Double> totalPriceColumn;
    @FXML
    private TableColumn<Order, String> orderDateColumn;
    private String adminUsername;
    @FXML
    private TextField usernameField;
    private final OrderDaoImpl orderDao = new OrderDaoImpl();



    /**
     * Constructor that initializes the controller with the admin's username.
     *
     * @param adminUsername the admin's username
     */
    public OrderListController(String adminUsername) {
        this.adminUsername = adminUsername;
    }




    /**
     * Initializes the controller and loads the order data into the table.
     * Sets up the table columns with their respective cell value factories.
     */
    @FXML
    public void initialize() {
        orderNumberColumn.setCellValueFactory(new PropertyValueFactory<>("orderNumber"));
        usernameColumn.setCellValueFactory(cellData -> cellData.getValue().getUser().usernameProperty());
        totalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        orderDateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        loadOrderData();
    }




    /**
     * Sets the admin's username.
     *
     * @param adminUsername the admin's username
     */
    public void setAdminUsername(String adminUsername) {
        this.adminUsername = adminUsername;
    }




    /**
     * Loads the order data from the database and sets it in the table view.
     */
    private void loadOrderData() {
        try {
            List<Order> orders = orderDao.getAllOrders();
            ObservableList<Order> orderList = FXCollections.observableArrayList(orders);
            orderTable.setItems(orderList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    /**
     * Handles the action of navigating back to the admin dashboard.
     * Loads the admin dashboard view and passes the admin's username.
     */
    @FXML
    private void handleBackToDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/assign02_final_s3988110/views/adminView.fxml"));
            AdminDashboardController dashboardController = new AdminDashboardController(adminUsername);
            loader.setController(dashboardController);
            Parent dashboardRoot = loader.load();
            Stage stage = (Stage) orderTable.getScene().getWindow();
            stage.setScene(new Scene(dashboardRoot));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

