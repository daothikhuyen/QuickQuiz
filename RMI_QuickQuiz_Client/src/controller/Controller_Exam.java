package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

import Model.Exams;
import Model.Questions;
import Model.QuizManager;
import Model.Results;
import Model.Users;
import Response.Server_Response;
import Response.SharedData;
import application.Main;
import javafx.animation.KeyFrame;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javafx.animation.Timeline;
import javafx.util.Duration;
import serverInterface.ExamService;

public class Controller_Exam implements Initializable{

    @FXML
    private ToggleGroup Group_Answer;

    @FXML
    private Label id_code,lb_A,lb_B,lb_C,lb_D;
    @FXML
    private Label lb_ProcessTime,lb_Title,lb_Quantity,lb_Question,lb_Room,lb_Time,lb_UserId,lb_Username,lb_numQuestion;

    @FXML
    private RadioButton rid_A,rid_B,rid_C,rid_D;
    
    @FXML
    private AnchorPane root;
    
    @FXML
    private Button btn_Result,btnComplete,button_Prev;
    
    public BorderPane mainPane;
	public Exams exams = Exams.getInstance();
	public Users users = Users.getInstance();
	
    private List<Questions> list_questions ;
    private List<List<String>> listAnswer_Shuffle = new ArrayList<>() ;
    private List<String> userAnswers;
    private List<String> correctAnswers;
    
    private Timeline examTimer;
    private int totalSeconds;
    
    private int currentQuestionIndex = 0; 
 
	private int time_spent;

	private float score;

	private int total_numCorrect;

	private String rank;
	
    ExamService quizExam = QuizManager.getQuizExam();
    
	public void setMainPane(BorderPane mainPane) {
		this.mainPane = mainPane;
		
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
            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Không có trang " + fileName + ", hãy kiểm tra lại");
        }
        return view;
    }
    
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
    void btnNext(ActionEvent event) {

    	if((currentQuestionIndex + 1) < exams.getQuantity()) {
			currentQuestionIndex++;
    		lb_numQuestion.setText(String.valueOf(currentQuestionIndex+1));
    		
    		if(listAnswer_Shuffle.size() == list_questions.size() && btn_Result.isVisible()) {
    			loadAnswer_ThenComplete(list_questions.get(currentQuestionIndex));
    		}else {
    			load_Question(list_questions.get(currentQuestionIndex));
    		}
    	}else {
    		showAlert("Đã Hết Câu Hỏi Bạn Vui Lòng Nhấn Xem Kết Quả Để Biết Số Điểm", AlertType.INFORMATION);
    	}
    }
    
    @FXML
    void btnPrev(ActionEvent event) {
    	if(currentQuestionIndex > 0) {
			currentQuestionIndex--;
    		lb_numQuestion.setText(String.valueOf(currentQuestionIndex+1));
    		
    		loadAnswer_ThenComplete(list_questions.get(currentQuestionIndex));
    	}else {
    		showAlert("Đã Hết Câu Hỏi Bạn Vui Lòng Nhấn Xem Kết Quả Để Biết Số Điểm", AlertType.INFORMATION);
    	}
    }

	private void loadAnswer_ThenComplete(Questions questions) {
		lb_Question.setText(questions.getQuestion());
		
		List<String> list = listAnswer_Shuffle.get(currentQuestionIndex);
		Label [] list_Label = {lb_A, lb_B, lb_C, lb_D};
		RadioButton [] list_rid = {rid_A,rid_B,rid_C,rid_D};
		
		for (int i = 0; i < list_Label.length; i++) {
			list_Label[i].setText(list.get(i));
		}
		
		
		for (int i = 0; i < list.size(); i++) {
			
			if(list.get(i).equals(userAnswers.get(currentQuestionIndex))) {
				list_rid[i].setSelected(true);
			}
		}
		
		for (int i = 0; i < list_Label.length; i++) {
			
			if(list_Label[i].getText().equals(correctAnswers.get(currentQuestionIndex))) {
				list_Label[i].setStyle("-fx-background-color: #27ae61;");
			}else {
				list_Label[i].setStyle("-fx-background-color: #fff;");
			}
			
		}	
	}

	@FXML
    void handClick_Complete(ActionEvent event) {
		try {
			Server_Response response = null;
			
			examTimer.stop();
			rank = null;
			score= 0;
			total_numCorrect = 0;
	    	float one_question = 10f / list_questions.size();
	    	
	    	for (int i = 0; i < list_questions.size(); i++) {
	    		
	    		if(userAnswers.get(i) != null && correctAnswers.get(i).equals(userAnswers.get(i))) {
	    			total_numCorrect++;
	    		}
			}
	    	score = one_question * total_numCorrect;
	    	
	    	Results results_exam = new Results(exams.getId(), users.getId(), score);
	    	
	    	response  = quizExam.save_result(results_exam);
	    	
	    	if(response.getStatus().equals("OK")) {
	    		btnComplete.setVisible(false);
				btn_Result.setVisible(true);
				button_Prev.setVisible(true);
				
				loadAnswer_ThenComplete(list_questions.get(currentQuestionIndex));
				
				rank = response.getMessage();
	    	}
	    	
	          
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	
//	Xem kết quả bài thi
	@FXML
    void handClick_Result(ActionEvent event) throws IOException {
		String unit = " giây ";
		
		if(time_spent > 60) {
	    	time_spent = time_spent / 60;
	    	unit = " phút ";
	    }
		
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Page_Result.fxml"));
        Parent parent = loader.load();
        Controller_Result controller_Result = loader.getController();
        controller_Result.loadData(score, total_numCorrect,list_questions.size()-total_numCorrect,time_spent + unit, rank);
        
        Stage resultStage = new Stage();
        resultStage.setTitle("Thông Báo Kết Quả");
        
        Scene scene = new Scene(parent);
        resultStage.setScene(scene);
        resultStage.show();
        resultStage.centerOnScreen();
    }
    
	private void load_Question(Questions questions) {
		
		List<String> listAnswer = new ArrayList<>();
		listAnswer.add(questions.getA());
		listAnswer.add(questions.getB());
		listAnswer.add(questions.getC());
		listAnswer.add(questions.getD());
        
		rid_A.setSelected(false);
		rid_B.setSelected(false);
		rid_C.setSelected(false);
		rid_D.setSelected(false);
		
		Shuffle(listAnswer);
		lb_Question.setText(questions.getQuestion());
		
		
		
	}
    
//	Hoán đổi vị trí các câu trả lời
	private void Shuffle(List<String> listAnswer2) {
		
		Random random = new Random();
		
		for (int i = 0; i < listAnswer2.size(); i++) {

			int j = random.nextInt(listAnswer2.size());
			String temp = listAnswer2.get(j);
			listAnswer2.set(j, listAnswer2.get(i));
			listAnswer2.set(i, temp);
		}
		
		lb_A.setText(listAnswer2.get(0));
		lb_B.setText(listAnswer2.get(1));
		lb_C.setText(listAnswer2.get(2));
		lb_D.setText(listAnswer2.get(3));
		
		listAnswer_Shuffle.add(listAnswer2);
		
		
	}

//	Tính thời gian thi
	private void startExamTimer(int totalMinutes) {
	    totalSeconds = totalMinutes * 60;

	    // Khởi tạo `Timeline` chạy mỗi giây để đếm ngược
	    examTimer = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
	        int minutes = totalSeconds / 60;
	        int seconds = totalSeconds % 60;

	        lb_ProcessTime.setText(String.format("%02d:%02d", minutes, seconds));

	        if (totalSeconds <= 0) {
	        	time_spent = (totalMinutes * 60) - totalSeconds;

	            examTimer.stop();
	            showAlert("Hết thời gian làm bài!", AlertType.INFORMATION);
	            handClick_Complete(event);
	        }
	        
	        totalSeconds--;
		    time_spent = (totalMinutes * 60) - totalSeconds;
	    }));

	    examTimer.setCycleCount(Timeline.INDEFINITE); // Chạy mãi cho tới khi dừng
	    examTimer.play(); // Bắt đầu bộ đếm thời gian
	}
	
	private void load_InfoExam() {
		
		lb_numQuestion.setText(String.valueOf(currentQuestionIndex + 1));
		id_code.setText(String.valueOf(exams.getId()));
		lb_Title.setText(exams.getSubject());
		lb_Room.setText(exams.getClass_room());
		lb_Quantity.setText(String.valueOf(exams.getQuantity()));
		lb_Time.setText(String.valueOf(exams.getTotal_time()));
		
		lb_UserId.setText(String.valueOf(users.getId()));
		lb_Username.setText(users.getUsername());
		
		btn_Result.setVisible(false);
		button_Prev.setVisible(false);
		
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		userAnswers = new ArrayList<>();
		list_questions = new ArrayList<>();
		list_questions = SharedData.getInstance().getData();
		
		load_InfoExam();
		load_Question(list_questions.get(currentQuestionIndex));
		startExamTimer(exams.getTotal_time());
		
		correctAnswers = new ArrayList<>();
		for (Questions ques : list_questions) {
			switch (ques.getAnswer()) {
			case "A":
				correctAnswers.add(ques.getA());
				break;
			case "B":
				correctAnswers.add(ques.getB());
				break;
			case "C":
				correctAnswers.add(ques.getC());
				break;
			case "D":
				correctAnswers.add(ques.getD());
				break;
			}
		}
		
		Group_Answer.selectedToggleProperty().addListener((observable,oldValue,newValue) -> {
			 if (newValue != null) {
	                String selectedOption = ((RadioButton) newValue).getText();
	                
	                switch (selectedOption) {
					case "A":
						userAnswers.add(lb_A.getText().trim());
						break;
					case "B":
						userAnswers.add(lb_B.getText().trim());
						break;
					case "C":
						userAnswers.add(lb_C.getText().trim());
						break;
					case "D":
						userAnswers.add(lb_D.getText().trim());
						break;
					default:
						userAnswers.add(null);
						break;
					}
	            }
		});
		
	}

}
