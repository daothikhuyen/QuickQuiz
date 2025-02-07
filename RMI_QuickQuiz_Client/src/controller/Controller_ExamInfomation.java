package controller;

import java.net.URL;
import java.util.ResourceBundle;

import Model.Exams;
import application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

public class Controller_ExamInfomation implements Initializable{

    @FXML
    private Label id_code,lb_Quantity,lb_Room,lb_Time,lb_Title,lb_creator;

    @FXML
    private AnchorPane root;
    
    private BorderPane mainPane;
    
    
    public void setMainPane(BorderPane mainPane2) {
		this.mainPane = mainPane2;
		
	}
    
    public Pane getPage(String fileName) {
        Pane view = null;
        
        try {
            URL fileUrl = Main.class.getResource("/view/"+fileName+".fxml");
            
            if(fileUrl == null) {
                throw new java.io.FileNotFoundException("FXML file không tìm thấy");
            }
            FXMLLoader loader = new FXMLLoader(fileUrl); // Sử dụng FXMLLoader đúng cách
            view = loader.load();
            
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
    void btnBack(ActionEvent event) {
    	Pane new2 = this.getPage("Page_ListOfExam");
        mainPane.setCenter(new2); 
    }

    @FXML
    void btnStart(ActionEvent event) {
    	Pane new2 = this.getPage("Page_Exam");
        mainPane.setCenter(new2); 
    }
    
	private void load_InfoExam() {
		Exams exams = Exams.getInstance();

		id_code.setText(String.valueOf(exams.getId()));
		lb_Title.setText(exams.getSubject());
		lb_Room.setText(exams.getClass_room());
		lb_Quantity.setText(String.valueOf(exams.getQuantity()));
		lb_Time.setText(String.valueOf(exams.getTotal_time()));
		lb_creator.setText(String.valueOf(exams.getUser_id()));
		
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		load_InfoExam();
		
	}


}