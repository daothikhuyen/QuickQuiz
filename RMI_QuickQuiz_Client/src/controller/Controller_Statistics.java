package controller;

import java.net.URL;
import java.rmi.RemoteException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.ResourceBundle;

import Model.Exams;
import Model.QuizManager;
import Model.Results;
import Response.Server_Response;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import serverInterface.ExamService;
import serverInterface.QuestionService;
import javafx.fxml.Initializable;

public class Controller_Statistics implements Initializable{
	
    @FXML
    private TableColumn<String, Exams> col_id,col_creator,col_quantity,col_title,col_room,col_totaltime;

    @FXML
    private Label lb_NumContestant,lb_ScoreAverage,lb_ScoreMax,lb_ScoreMin;

    @FXML
    private TableView<Exams> table_Statistics;
    
    @FXML
    private AnchorPane root;
    
    ExamService quizExam = QuizManager.getQuizExam();
    QuestionService quizQuestion = QuizManager.getQuizQuestion();

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
    
    @FXML
    void HandClick_Exam(MouseEvent event) throws RemoteException {
    	Exams selectedExam = table_Statistics.getSelectionModel().getSelectedItem();
    	if(event.getClickCount() == 1) {
    		
    		if(selectedExam != null) {
    			Server_Response response = null;
    			
    			response = quizQuestion.statistic(selectedExam.getId());
    			
    			if(response.getStatus().equals("OK")) {
    				DecimalFormat df = new DecimalFormat("0.00");
    				
    				List<Object> result = (List<Object>) response.getResult();
    				lb_NumContestant.setText(result.get(0).toString());
    				lb_ScoreMax.setText(result.get(1).toString());
    				lb_ScoreMin.setText(result.get(2).toString());
    				lb_ScoreAverage.setText(result.get(3).toString());
    				
    			}
    		}
    	}
    }
    
	private void load_contestant() {
		ObservableList<Exams> observableList_Exam = FXCollections.observableArrayList();
		
		try {
			Server_Response response = null;
			
			response = quizExam.get_list_exam();
			
			if(response.getStatus().equals("OK")) {
				
				List<Exams> listExam = (List<Exams>) response.getResult();
				
				for (Exams exams : listExam) {
					Exams exam = new Exams(
							exams.getId(),
							exams.getSubject(),
							exams.getClass_room(),
							exams.getQuantity(),
							exams.getTotal_time(),
							exams.getStatus(),
							exams.getUser_id());
					observableList_Exam.add(exam);
				}
				table_Statistics.setItems(observableList_Exam);
			}else {
				showAlert(response.getMessage(), AlertType.ERROR);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			showAlert("Lỗi thống kê: " + e.getMessage(), AlertType.ERROR);
		}

		
	}
	
	public void setUp_Table() {
		col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
		col_title.setCellValueFactory(new PropertyValueFactory<>("subject"));
		col_room.setCellValueFactory(new PropertyValueFactory<>("class_room"));
		col_quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
		col_totaltime.setCellValueFactory(new PropertyValueFactory<>("total_time"));
		col_creator.setCellValueFactory(new PropertyValueFactory<>("user_id"));
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		setUp_Table();
		load_contestant();
		
	}



}
