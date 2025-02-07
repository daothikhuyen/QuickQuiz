package controller;

import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;

import CallBack.ClientCallback;
import CallBack.ClientCallbackImpl;
import Model.QuizManager;
import Model.Users;
import Response.Server_Response;
import application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import serverInterface.ExamService;
import serverInterface.UserService;

public class Controller_Home implements Initializable {

    @FXML
    private AnchorPane home;

    @FXML
    private Label lbl_Username;

    @FXML
    private BorderPane mainPane;

    @FXML
    private ImageView menu;

    @FXML
    private AnchorPane root;

    Users user = Users.getInstance();
    UserService quizUser = QuizManager.getQuizService();
    ExamService quizExam = QuizManager.getQuizExam();
    
    public Pane getPage(String fileName) {
        Pane view = null;
        
        try {
            URL fileUrl = Main.class.getResource("/view/"+fileName+".fxml");
            
            if(fileUrl == null) {
                throw new java.io.FileNotFoundException("FXML file không tìm thấy");
            }
            
            FXMLLoader loader = new FXMLLoader(fileUrl); // Sử dụng FXMLLoader đúng cách
            
            view = loader.load(); 
         
            if(fileName.equals("Page_ManagerExam")) {
                Controller_PageManagerExam controller_PageManagerExam = loader.getController();
                controller_PageManagerExam.setMainPane(mainPane);
            }
            
            if(fileName.equals("Page_ListOfExam")) {
                Controller_ListOfExam controller_ListOfExam = loader.getController();
                controller_ListOfExam.setMainPane(mainPane);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Không có trang " + fileName + ", hãy kiểm tra lại");
        }
        return view;
    }
    
    @FXML
    void btnExam(ActionEvent event) {
    	Pane view = this.getPage("Page_ListOfExam");
		
	    if (view != null) {
	        mainPane.setCenter(view);
	    } else {
	        System.out.println("Không thể tải trang Page_ListOfExam");
	    }
    }

    @FXML
    void btnExit(ActionEvent event) throws RemoteException, UnknownHostException{
    	
    	boolean logout = false;
		 
    	logout = quizUser.logout(user.getEmail());
    	
    	if(logout) {
        	String clientName = "Client_" + InetAddress.getLocalHost().getHostAddress() + " da ngat ket noi"; // Hoặc dùng hostname/IP
        	quizUser.unregisterClient(clientName);
        	
        	Server_Response response = quizExam.unregisterCallback(InetAddress.getLocalHost().getHostAddress());
        	
        	
        	if(response.getStatus().equals("OK")) {
        		Controller_ListOfExam controller_ListOfExam = new Controller_ListOfExam();
        		controller_ListOfExam.unregisterCallback();
        	}
        	
    		Stage primaryStage = (Stage) root.getScene().getWindow();
    		primaryStage.close();
    	}
    	
    }


    @FXML
    void btnManagerExam(ActionEvent event) {
    	
    	 Pane view = this.getPage("Page_ManagerExam");
    		
    	    if (view != null) {
    	    	
    	        
    	        mainPane.setCenter(view);

    	    } else {
    	        System.out.println("Không thể tải trang Page_ManagerExam");
    	    }
    	    
    }

    @FXML
    void btnManagerUser(ActionEvent event) {
    	Pane view = this.getPage("User");
		
	    if (view != null) {
	    	
	        mainPane.setCenter(view);

	    } else {
	        System.out.println("Không thể tải trang user");
	    }
    }

    @FXML
    void btnStatistical(ActionEvent event) {
    	Pane view = this.getPage("Statistics");
		
	    if (view != null) {
	    	
	        mainPane.setCenter(view);

	    } else {
	        System.out.println("Không thể tải trang Statistics");
	    }
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.lbl_Username.setText(user.getEmail());
		btnManagerExam(null);
		
	}

}
