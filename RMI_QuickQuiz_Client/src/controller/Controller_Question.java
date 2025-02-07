package controller;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.scene.control.TextArea;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import javafx.scene.control.Button;
import Model.Exams;
import Model.Questions;
import Model.QuizManager;
import Response.Server_Response;
import Response.SharedData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import serverInterface.QuestionService;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class Controller_Question implements Initializable{
	
	public Exams exams = Exams.getInstance();
	
	// Danh sách lưu các câu hỏi và đáp án
    private List<Questions> list_questions;
    private int currentQuestionIndex = 0;  // Theo dõi câu hỏi hiện tại 
	
	@FXML
	private AnchorPane root;
	
	@FXML
    private Button btnNext,btn_Pre,btn_Complete,btn_Update;
	 
    @FXML
    private ToggleGroup GroupAns;

    @FXML
    private TextArea txtQuestion;
    
    @FXML
    private RadioButton rdiA, rdiB, rdiC, rdiD;

    @FXML
    private TextField txtAnsA, txtAnsB, txtAnsC, txtAnsD;

    @FXML
    private TextField txtCreator, txtExamCode, txtNum, txtNumQuestion, txtRoomCode, txtTime, txtTitle;
    
	public String answer = null;
	public Boolean Check_SaveList = false;
	public Boolean check_update = false;

    StringBuilder sb ;
    
    QuestionService quizQuestion  = QuizManager.getQuizQuestion();
    
    @FXML
    void btnNext_Ques(ActionEvent event) {
    	Check_InfoQuestion();
    	
    	if(list_questions.isEmpty()|| currentQuestionIndex == list_questions.size() ) {
    		sb.append("Vui lòng lưu câu hỏi trước khi qua câu mới\n");
    	}
    	
    	if(sb.length() > 0) {
    		showAlert(sb.toString(),AlertType.INFORMATION);
    	}else {
    		
    		if(currentQuestionIndex < exams.getQuantity()) {
    			currentQuestionIndex++;
        		txtNum.setText(String.valueOf(currentQuestionIndex+1));
        		updateQuestionFields(currentQuestionIndex);
        	}
    		
    		btnNext.setVisible(currentQuestionIndex != (exams.getQuantity()-1));
    		btn_Pre.setVisible(currentQuestionIndex > 0);
    		
    		if(!check_update) {
    			btn_Complete.setVisible(currentQuestionIndex == (exams.getQuantity()-1));
    		}
        	
    	}
    	
    }

	@FXML
    void btnPre_Question(ActionEvent event) {
		
		if (currentQuestionIndex >= 0) {
            currentQuestionIndex--;
            txtNum.setText(String.valueOf(currentQuestionIndex+1));
            updateQuestionFields(currentQuestionIndex); // Hiển thị lại câu hỏi trước đó
        }else {
        	System.out.println("rỗng");
        }
        btn_Pre.setVisible(currentQuestionIndex > 0);
        btnNext.setVisible(currentQuestionIndex != (exams.getQuantity()-1));
        btn_Complete.setVisible(currentQuestionIndex == (exams.getQuantity()-1));
    }

//	Lưu câu hỏi vào list
    @FXML
    void btnSave_Ques(ActionEvent event) {
    	
    	Check_SaveList = true;
    	Check_InfoQuestion();
    	
    	if(sb.length() > 0) {
    		showAlert(sb.toString(),AlertType.INFORMATION);
    	}else {
    		
        	int exam_id = exams.getId();
        	String question = txtQuestion.getText().trim();
        	String ansA = txtAnsA.getText().trim();
        	String ansB = txtAnsB.getText().trim();
        	String ansC = txtAnsC.getText().trim();
        	String ansD = txtAnsD.getText().trim();
        	
        	Questions questions;

    		if(SharedData.getInstance().getData()!= null && SharedData.getInstance().getData().size() > currentQuestionIndex ) {

    			 questions = new Questions(list_questions.get(currentQuestionIndex).getId(), exam_id, question, ansA, ansB, ansC, ansD, answer);
    		}else {
    			 questions = new Questions(exam_id, question, ansA, ansB, ansC, ansD, answer);
    		}
    		
    		if (currentQuestionIndex >= 0 && currentQuestionIndex < list_questions.size()) {
                if (list_questions.get(currentQuestionIndex) != null) {
                    list_questions.set(currentQuestionIndex, questions); // Cập nhật câu hỏi
                }
            } else {

            	list_questions.add(questions);
            }
    	}
    	
    }
  
// Lưu câu hỏi vào sql
    @FXML
    void btnComplete(ActionEvent event) throws RemoteException {
    	Check_InfoQuestion();
    	
    	if(list_questions.isEmpty()|| currentQuestionIndex == list_questions.size() ) {
    		sb.append("Vui lòng lưu câu hỏi trước khi qua câu mới\n");
    	}
    	
    	if(sb.length() > 0) {
    		showAlert(sb.toString(),AlertType.INFORMATION);
    	}else {
    		
    		try {
				
    			Server_Response response = null;
        		
        		response = quizQuestion.save_question(list_questions);
        		
        		if(response.getStatus().equals("OK")) {
        			showAlert("Thêm Thành Công", AlertType.CONFIRMATION);
    				
    				FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Home.fxml"));
    		        Parent parent = loader.load();

    		        Scene scene = new Scene(parent);
    		        Stage primaryStage = (Stage) root.getScene().getWindow();
    		        primaryStage.setScene(scene);
    		        primaryStage.setTitle("QUICK QUIZ");
    		        primaryStage.centerOnScreen();
        		}else {
    				showAlert(response.getMessage(), AlertType.ERROR);
    			}
        		
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Loi luu cau hoi: "+ e.getMessage());
			}
    	}
    	
    }
  
//  Update lại câu hỏi
    @FXML
    void Update_Question(ActionEvent event) throws RemoteException {
    	Check_InfoQuestion();
    	
    	if(list_questions.isEmpty()|| currentQuestionIndex == list_questions.size() ) {
    		sb.append("Vui lòng lưu câu hỏi trước khi qua câu mới\n");
    	}
    	
    	if(sb.length() > 0) {
    		showAlert(sb.toString(),AlertType.INFORMATION);
    	}else {
    		try {
    			Server_Response response = null;
//    			for (Questions ques : list_questions) {
//    				System.out.println(ques.getQuestion());
//    			}
        		response = quizQuestion.update_question(list_questions);
        		
        		if(response.getStatus().equals("OK")) {
        			showAlert("Cập nhật thành công", AlertType.CONFIRMATION);
    				
    				FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Home.fxml"));
    		        Parent parent = loader.load();

    		        Scene scene = new Scene(parent);
    		        Stage primaryStage = (Stage) root.getScene().getWindow();
    		        primaryStage.setScene(scene);
    		        primaryStage.setTitle("QUICK QUIZ");
    		        primaryStage.centerOnScreen();
        		}else {
        			showAlert(response.getMessage(), AlertType.ERROR);
        		}
//        		
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Loi cao nhat cau hoi: "+ e.getMessage());
			}
    	}
    }
    
//  Kiểm tra xem người dùng đã nhập thông tin câu hỏi trước khi chuyển câu chưa
    void Check_InfoQuestion(){
    	sb = new StringBuilder();
    	
	  	if(txtQuestion.getText().equals("")) {
	  		sb.append("Vui lòng nhập câu hỏi\n");
	  	}
	  	
	  	if(txtAnsA.getText().equals("")||txtAnsB.getText().equals("")||txtAnsC.getText().equals("")||txtAnsD.getText().equals("")) {
	  		sb.append("Vui lòng nhập câu trả lời\n");
	  	}
	  	
    	if(rdiA.isSelected()) {
    		answer = "A";
    	}else if(rdiB.isSelected()) {
    		answer = "B";
    	}else if(rdiC.isSelected()) {
    		answer = "C";
    	}else if(rdiD.isSelected()) {
    		answer = "D";
    	}else {
    		sb.append("Vui lòng chọn đáp án đúng cho câu hỏi\n");
    	}

  }
 
// Load lại câu hỏi trước đó đã được lưu vào list    
    private void updateQuestionFields(int index) {
    	
    	if(index < list_questions.size()) {
        	Questions ques = list_questions.get(index);

        	txtQuestion.setText(ques.getQuestion());
        	txtAnsA.setText(ques.getA());
        	txtAnsB.setText(ques.getB());
        	txtAnsC.setText(ques.getC());
        	txtAnsD.setText(ques.getD());
        	answer = ques.getAnswer();
        	rdiA.setSelected(ques.getAnswer().equals("A"));
        	rdiB.setSelected(ques.getAnswer().equals("B"));
        	rdiC.setSelected(ques.getAnswer().equals("C"));
        	rdiD.setSelected(ques.getAnswer().equals("D"));
    	}else {
    		delete_txtBox();
    	}

		
	}
   
 // Xoá hết thông tin trong ô nhập
    public void delete_txtBox() {
    	txtQuestion.setText("");
    	txtAnsA.setText("");
    	txtAnsB.setText("");
    	txtAnsC.setText("");
    	txtAnsD.setText("");
    	answer = null;
    	rdiA.setSelected(false);
    	rdiB.setSelected(false);
    	rdiC.setSelected(false);
    	rdiD.setSelected(false);
    	
    }
    
//   Thông báo lỗi 
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
    

//    load thông tin bài kiểm tra
    public void load_Exam() {
    	
    	txtNum.setText(String.valueOf(currentQuestionIndex + 1));
    	txtExamCode.setText(String.valueOf(exams.getId()));
    	txtTitle.setText(exams.getSubject());
    	txtRoomCode.setText(exams.getClass_room());
    	txtCreator.setText(String.valueOf(exams.getUser_id()));
    	txtNumQuestion.setText(String.valueOf(exams.getQuantity()));
    	txtTime.setText(String.valueOf(exams.getTotal_time()));
    }
    
// load các câu hỏi đã có để được cập nhập hoặc thêm mới cho phù hợp với bài thi
    public void load_question(List<Questions> list_quesList) {
		
    	Questions currentQuestion = list_quesList.get(currentQuestionIndex);
    	
	 	txtQuestion.setText(currentQuestion.getQuestion());
	    txtAnsA.setText(currentQuestion.getA());
	    txtAnsB.setText(currentQuestion.getB());
	    txtAnsC.setText(currentQuestion.getC());
	    txtAnsD.setText(currentQuestion.getD());
		
	    String answer = currentQuestion.getAnswer();
	    rdiA.setSelected("A".equals(answer));
	    rdiB.setSelected("B".equals(answer));
	    rdiC.setSelected("C".equals(answer));
	    rdiD.setSelected("D".equals(answer));
    
    }
    
//    Hiểm thị các hỏi 
    public void load_Question() {
    	VBox vbox = new VBox(exams.getQuantity());
    	
    	list_questions = new ArrayList<>();
    	btn_Pre.setVisible(false);
    	btn_Complete.setVisible(false);
    	btn_Update.setVisible(false);
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		load_Exam();
		load_Question();
		
		list_questions = SharedData.getInstance().getData();
		if (list_questions == null) {
		    list_questions = new ArrayList<>();
		}else {
			check_update = true;
			btn_Update.setVisible(true);
			load_question(list_questions);
		}
	}

}
