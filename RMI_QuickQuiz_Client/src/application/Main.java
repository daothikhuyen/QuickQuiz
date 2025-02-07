package application;
	
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/connect_server.fxml"));
            Parent root = loader.load();
            primaryStage.setTitle("QUICK QUIZ");
            primaryStage.setScene(new Scene(root));
            
            primaryStage.setOnCloseRequest(event -> {
            	event.consume(); // Ngăn việc tắt màn hình đột ngột
            	
            });
            
            primaryStage.show();
        } catch (Exception e) {
        	System.out.println("Loi main: "+ e.getMessage());
            e.printStackTrace(); // In thông tin lỗi ra console
        }
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
