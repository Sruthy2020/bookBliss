package com.example.assign02_final_s3988110.utils;

import com.example.assign02_final_s3988110.models.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * utility class for managing order-related operations.
 * this class provides methods for generating order numbers, fetching the current date,
 * and exporting order items to a CSV file.
 */
public class OrderDetailsUtil {



    /**
     * generates a unique order number.
     *
     * @return a unique order number
     */
    public static String generateOrderNumber() {
        return "ORD" + System.currentTimeMillis();
    }



    /**
     * gets the current date and time in the format "yyyy-MM-dd HH:mm".
     *
     * @return the current date and time as a string
     */
    public String getCurrentDate() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }



    /**
     * exports the order items to a CSV file.
     *
     * @param orderItems the list of order items to export
     * @param file the file to which the order items will be exported
     */
    public void exportOrderItemsToCSV(List<OrderItem> orderItems, File file) {
        try (FileWriter writer = new FileWriter(file)) {
            writer.append("Order Number,Book Title,Quantity,Date,Total Price\n");
            for (OrderItem item : orderItems) {
                writer.append(item.getOrderNumber()).append(",");
                writer.append(item.getBookTitle()).append(",");
                writer.append(String.valueOf(item.getQuantity())).append(",");
                writer.append(item.getDate()).append(",");
                writer.append(String.valueOf(item.getTotalPrice())).append("\n");
            }
            writer.flush();
            System.out.println("Order items exported successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



