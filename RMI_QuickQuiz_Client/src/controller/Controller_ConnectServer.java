package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.control.Alert.AlertType;

import java.net.InetAddress;
import java.net.URL;
import java.rmi.Naming;
import java.util.ResourceBundle;

import CallBack.ClientCallback;
import CallBack.ClientCallbackImpl;
import Model.QuizManager;
import javafx.event.ActionEvent;

import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import serverInterface.ExamService;
import serverInterface.QuestionService;
import serverInterface.UserService;

public class Controller_ConnectServer implements Initializable {
    @FXML
    private AnchorPane root;
    @FXML
    private TextField IP;
    @FXML
    private TextField Port;

    QuizManager quizManager;

    public void showAlert(String message, AlertType information) {
        Stage primaryStage = (Stage) root.getScene().getWindow();

        AlertType type = information;
        Alert alert = new Alert(type, "");

        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(primaryStage);

        alert.getDialogPane().setContentText(message);
        alert.getDialogPane().setHeaderText("Thông Báo");

        alert.showAndWait();
    }

    // Event Listener on Button.onAction
    @FXML
    public void connect_server(ActionEvent event) {
        try {
            String ip = IP.getText().trim();
            int port = Integer.parseInt(Port.getText().trim());

            // Tạo URL kết nối đến các dịch vụ RMI
            String baseUrl = "rmi://" + ip + ":" + port + "/";

            // Tìm kiếm các dịch vụ bằng URL
            UserService quiz = (UserService) Naming.lookup(baseUrl + "QuickQuiz");
            ExamService exam_quiz = (ExamService) Naming.lookup(baseUrl + "Exam_QuickQuiz");
            QuestionService question_quiz = (QuestionService) Naming.lookup(baseUrl + "Question_QuickQuiz");

            // Thiết lập các dịch vụ vào QuizManager
            quizManager.setQuizService(quiz);
            quizManager.setQuizExam(exam_quiz);
            quizManager.setQuizQuestion(question_quiz);

            // Đăng ký client với server
            String clientName = "Client_" + InetAddress.getLocalHost().getHostAddress() + " ket noi";
            quiz.registerClient(clientName);

            // Chuyển đến giao diện đăng ký tài khoản
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Frm_Register.fxml"));
            Parent parent = loader.load();
            Scene scene = new Scene(parent);
            Stage primaryStage = (Stage) root.getScene().getWindow();
            primaryStage.setScene(scene);
            primaryStage.setTitle("Đăng Ký Tài Khoản");

            primaryStage.centerOnScreen();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Kiểm tra lại cổng thông tin", AlertType.INFORMATION);
            Stage primaryStage = (Stage) root.getScene().getWindow();
            primaryStage.close();
        }
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		 // Lắng nghe phím Enter trên toàn bộ giao diện (root)
	    root.setOnKeyPressed(event -> {
	        if (event.getCode() == KeyCode.ENTER) {
	            connect_server(new ActionEvent());
	        }
	    });
		
	}
}
