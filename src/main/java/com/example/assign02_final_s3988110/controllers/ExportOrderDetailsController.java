package com.example.assign02_final_s3988110.controllers;

import com.example.assign02_final_s3988110.models.Order;
import com.example.assign02_final_s3988110.models.OrderItem;
import com.example.assign02_final_s3988110.models.User;
import com.example.assign02_final_s3988110.models.UserSingleton;
import com.example.assign02_final_s3988110.DAO.OrderDaoImpl;
import com.example.assign02_final_s3988110.utils.OrderDetailsUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Controller for exporting order details.
 * This controller allows users to select and view past orders, and provides an option to export the order details.
 */
public class ExportOrderDetailsController {
    @FXML
    private ComboBox<Order> orderDropdown;
    @FXML
    private TableView<OrderItem> selectedOrdersTable;
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
    private TableColumn<OrderItem, Void> decreaseQuantityColumn;

    private final ObservableList<OrderItem> selectedOrderItems = FXCollections.observableArrayList();
    private final OrderDaoImpl orderDao = new OrderDaoImpl();
    private final OrderDetailsUtil orderDetailsUtil = new OrderDetailsUtil();
    private User currentUser;

    /**
     * Initializes the controller, loads orders into the dropdown, and configures table columns.
     */
    @FXML
    public void initialize() {
        setupTable();
        setupDecreaseQuantityColumn();
        currentUser = UserSingleton.getInstance().getUser();

        try {
            loadOrdersIntoComboBox();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    /**
     * Loads the list of orders for the current user into the order dropdown.
     */
    private void setupTable() {
        orderNumberColumn.setCellValueFactory(cellData -> cellData.getValue().orderNumberProperty());
        bookTitleColumn.setCellValueFactory(cellData -> cellData.getValue().bookTitleProperty());
        quantityColumn.setCellValueFactory(cellData -> cellData.getValue().quantityProperty().asObject());
        totalPriceColumn.setCellValueFactory(cellData -> cellData.getValue().totalPriceProperty().asObject());
        dateColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        selectedOrdersTable.setItems(selectedOrderItems);
    }



    /**
     * Adds a button to decrease the quantity of each item in the order.
     */
    private void setupDecreaseQuantityColumn() {
        decreaseQuantityColumn.setCellFactory(param -> new TableCell<>() {
            private final Button decreaseButton = new Button("-");
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(decreaseButton);
                    decreaseButton.setOnAction(event -> {
                        OrderItem orderItem = getTableView().getItems().get(getIndex());
                        selectedOrderItems.remove(orderItem);
                        getTableView().refresh();
                    });
                }
            }
        });
    }



    /**
     * Loads the orders for the current user into the ComboBox.
     * Retrieves orders from the database associated with the logged-in user and givd them into the ComboBox.
     * Each order is displayed with its order number, total price, and date.
     *
     * @throws SQLException if an error occurs while retrieving orders from the database.
     */
    public void loadOrdersIntoComboBox() throws SQLException {
        if (currentUser != null) {
            List<Order> orders = orderDao.getOrdersByUsername(currentUser.getUsername());
            ObservableList<Order> orderEntries = FXCollections.observableArrayList(orders);
            orderDropdown.setItems(orderEntries);
            orderDropdown.setConverter(new javafx.util.StringConverter<>() {
                @Override
                public String toString(Order order) {
                    if (order == null) {
                        return null;
                    }
                    return "Order No: " + order.getOrderNumber() + " - Total: $" + order.getTotalPrice() + " - Date: " + order.getDate();
                }

                @Override
                public Order fromString(String string) {
                    return null;
                }
            });
        }
    }



    /**
     * Adds the selected order and its items to the table view for display.
     * Retrieves the selected order from the ComboBox and adds each order item to the table.
     * If an order is not selected, displays an alert to prompt selection.
     */
    @FXML
    private void handleAddOrder() {
        Order selectedOrder = orderDropdown.getSelectionModel().getSelectedItem();
        if (selectedOrder != null) {
            for (OrderItem item : selectedOrder.getOrderItems()) {
                item.setDate(selectedOrder.getDate());
                item.setOrderNumber(selectedOrder.getOrderNumber());
                if (!selectedOrderItems.contains(item)) {
                    selectedOrderItems.add(item);
                }
            }
            selectedOrdersTable.refresh();
        } else {
            showAlert("No Order Selected", "Please select an order to add.");
        }
    }



    /**
     * Exports the selected order items to a file.
     * Checks if any order items are present to export. If not, displays an alert.
     * Otherwise, prompts the user to choose a file location, then saves the order details.
     */
    @FXML
    private void handleExport() {
        if (selectedOrderItems.isEmpty()) {
            showAlert("No Orders", "Please add an order with items to export.");
            return;
        }
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Orders");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fileChooser.showSaveDialog(selectedOrdersTable.getScene().getWindow());
        if (file != null) {
            orderDetailsUtil.exportOrderItemsToCSV(selectedOrderItems, file);
        }
    }



    /**
     * Handles the action for the back button.
     * Navigates the user back to the dashboard view and initializes the dashboard controller if necessary.
     */
    @FXML
    private void handleBackButtonAction() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/assign02_final_s3988110/views/dashboardView.fxml"));
            Parent dashboardRoot = loader.load();
            Stage stage = (Stage) selectedOrdersTable.getScene().getWindow();
            stage.setScene(new Scene(dashboardRoot));
            stage.setTitle("Dashboard");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    /**
     * Displays an alert with a specified title and message.
     *
     * @param title the title of the alert dialog.
     * @param message the message content of the alert.
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }
}














