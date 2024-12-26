package com.example.assign02_final_s3988110;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HelloApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/assign02_final_s3988110/views/welcomeView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 600);

        primaryStage.setTitle("BookBliss\uD83D\uDC81\u200D♀️");
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}




