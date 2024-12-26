package com.example.assign02_final_s3988110.controllers;

import com.example.assign02_final_s3988110.models.*;
import com.example.assign02_final_s3988110.DAO.*;
import com.example.assign02_final_s3988110.utils.*;
import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Controller class for managing the order details view.
 * This class handles displaying order item information and navigating to other views.
 */
public class OrderDetailsController {
    @FXML
    private TableView<OrderItem> orderTable;
    @FXML
    private TableColumn<OrderItem, String> orderNumberColumn;
    @FXML
    private TableColumn<OrderItem, String> bookTitleColumn;
    @FXML
    private TableColumn<OrderItem, Integer> quantityColumn;
    @FXML
    private TableColumn<OrderItem, String> dateColumn;
    @FXML
    private TableColumn<OrderItem, Double> totalPriceColumn;
    @FXML
    private TextField usernameField;

    private User currentUser;
    private ObservableList<OrderItem> orderItems = FXCollections.observableArrayList();
    private final OrderDaoImpl orderDao = new OrderDaoImpl();
    private final UserDaoImpl userDao = new UserDaoImpl();
    private final OrderDetailsUtil orderDetailsUtil = new OrderDetailsUtil();



    /**
     * Initializes the controller and loads the order items into the table.
     * Sets up the table columns with their respective cell value factories.
     */
    @FXML
    public void initialize() {
        orderNumberColumn.setCellValueFactory(cellData -> cellData.getValue().orderNumberProperty());
        bookTitleColumn.setCellValueFactory(cellData -> cellData.getValue().bookTitleProperty());
        quantityColumn.setCellValueFactory(cellData -> cellData.getValue().quantityProperty().asObject());
        totalPriceColumn.setCellValueFactory(cellData -> cellData.getValue().orderTotalPriceProperty().asObject()); // Change here
        dateColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());

        TableView.TableViewSelectionModel<OrderItem> selector = orderTable.getSelectionModel();
        selector.setSelectionMode(SelectionMode.MULTIPLE);
        orderTable.setItems(orderItems);

        currentUser = UserSingleton.getInstance().getUser();
        if (currentUser != null) {
            loadOrdersForUser(currentUser.getUsername());
        }
    }



    /**
     * Loads the orders for a specific user and sets the order items in the table view.
     *
     * @param username the username of the user
     */
    public void loadOrdersForUser(String username) {
        try {
            List<Order> orders = orderDao.getOrdersByUsername(username);
            orders.sort((o1, o2) -> o2.getDate().compareTo(o1.getDate()));
            ObservableList<OrderItem> allItems = FXCollections.observableArrayList();
            for (Order order : orders) {
                for (OrderItem item : order.getOrderItems()) {
                    item.setDate(order.getDate());
                    item.setOrderNumber(order.getOrderNumber());
                    item.setOrderTotalPrice(order.getTotalPrice());
                    allItems.add(item);
                }
            }
            setOrderItems(allItems);
        } catch (SQLException e) {
            System.err.println("Error loading orders for user: " + username);
            e.printStackTrace();
        }
    }



    /**
     * Sets the order items in the table view.
     *
     * @param orders the list of order items
     */
    public void setOrderItems(ObservableList<OrderItem> orders) {
        this.orderItems = orders;
        orderTable.setItems(orderItems);
    }



    /**
     * Handles the action of exporting the order details.
     * Loads the export order details view.
     */
    @FXML
    private void handleExport() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/assign02_final_s3988110/views/exportOrderDetailsView.fxml"));
            Parent exportRoot = loader.load();
            ExportOrderDetailsController exportController = loader.getController();
            Stage stage = (Stage) orderTable.getScene().getWindow();
            stage.setScene(new Scene(exportRoot));
            stage.setTitle("Export Order Details");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    /**
     * Handles the action of navigating back to the dashboard.
     * Loads the dashboard view.
     */
    @FXML
    private void handleBackButtonAction() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/assign02_final_s3988110/views/dashboardView.fxml"));
            Parent dashboardRoot = loader.load();
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(dashboardRoot));
            stage.setTitle("Dashboard");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    
}
