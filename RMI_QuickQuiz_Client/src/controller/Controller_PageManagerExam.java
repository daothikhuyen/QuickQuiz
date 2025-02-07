package controller;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import org.json.simple.JSONObject;

import CallBack.ClientCallback;
import CallBack.ClientCallbackImpl;
import Model.Exams;
import Model.Questions;
import Model.QuizManager;
import Model.Users;
import Response.Server_Response;
import Response.SharedData;
import application.Main;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import serverInterface.ExamService;
import serverInterface.QuestionService;


public class Controller_PageManagerExam implements Initializable{

	@FXML
    private ComboBox<String> Box_Quantity,Box_Status,Box_TotalTime;

    @FXML
    private TextField txtId,txtSubject,txtClass_room,txtUserName;
    
    @FXML
    public TableView<Exams> table_ListOfExams;
    
    @FXML
    private TableView<Questions> table_question;
    
    @FXML
    public TableColumn<Exams, Integer> col_id; // Cột ID
    @FXML
    public TableColumn<Exams, String> col_title,col_room,col_status;
  
    @FXML
    public TableColumn<Exams, Integer> col_quantity; // Cột số lượng
    @FXML
    public TableColumn<Exams, Integer> col_time; // Cột thời gian
    
    @FXML
    private AnchorPane root;
    @FXML
    private VBox VBox;
    
    @FXML
    private TableColumn<Questions, String> ques_A,ques_B,ques_C,ques_D,ques_Ans,ques_question;

    @FXML
    private TableColumn<Questions, Integer> ques_id;


    public Users user = Users.getInstance();
    ExamService quizExam = QuizManager.getQuizExam();
    QuestionService questionService = QuizManager.getQuizQuestion();
    
    
    private BorderPane mainPane;
    
    
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
    
    public void Update_DataExam(Exams e) {
		Exams data_exam = Exams.getInstance();
		
		data_exam.setId(e.getId());
		data_exam.setUser_id(e.getUser_id());
		data_exam.setSubject(e.getSubject());
		data_exam.setClass_room(e.getClass_room());
		data_exam.setQuantity(e.getQuantity());
		data_exam.setTotal_time(e.getTotal_time());
		
	}
    

    @FXML
    void btnCreate_exam(ActionEvent event) throws RemoteException {
    	StringBuilder sb = new StringBuilder();
    	
    	int userId = user.getId();
    	String subject = txtSubject.getText().trim();
    	String class_room = txtClass_room.getText().trim();
    	int quantity = Integer.parseInt(Box_Quantity.getSelectionModel().getSelectedItem().toString());
    	String status = Box_Status.getSelectionModel().getSelectedItem().toString();
    	int total_time = Integer.parseInt(Box_TotalTime.getSelectionModel().getSelectedItem().toString());
    	
    	
    	if(subject.equals("")) {
    		sb.append("Vui lòng không để trống tên môn học\n");
    	}
    	
    	if(class_room.equals("")) {
    		sb.append("Vui lòng không để trống mã phòng thi\n");
    	}
    	
    	if (sb.length() > 0) {
    		showAlert(sb.toString(),AlertType.INFORMATION);
		}else {
			Exams exam = new Exams(0, subject, class_room, quantity, total_time, status, userId);
			
			try {
				
				Server_Response response = null;
				
				response = quizExam.created_Exam(exam);
				
				if(response.getStatus().equals("OK")) {
					
					SharedData.getInstance().setData(null);
					Exams e = (Exams) response.getResult();
					
					Pane new2 = this.getPage("Question");
		            mainPane.setCenter(new2); 
				}else {
					showAlert(response.getMessage(), AlertType.ERROR);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				showAlert("Lỗi tạo đề thi: " + e.getMessage(), AlertType.ERROR);
			}
			
		}
    }

	@FXML
    void btnDetele_exam(ActionEvent event) {
		String exam_id = txtId.getText().trim();
    	
    	if(exam_id.equals("")) {
    		showAlert("Vui lòng kích đúp chọn bài thi muốn xoá", AlertType.CONFIRMATION);
    	}else {
        	try {
        		
        		Server_Response response = null;
        		
        		response = quizExam.deleted_Exam(Integer.parseInt(exam_id));
    			
        		if(response.getStatus().equals("OK")) {
        			txtId.setText("");
        			loadDate();
        			loadData_Question(Integer.parseInt(exam_id));
    				showAlert(response.getMessage(), AlertType.CONFIRMATION);
    				
    			}else {
    				showAlert(response.getMessage(), AlertType.ERROR);
    			}
       		
    		} catch (Exception e) {
    			System.out.println("Loi xoa bai thi: "+ e.getMessage());
    		}
    	}
    }

    @FXML
    void btnUpdate_exam(ActionEvent event) throws RemoteException {
    	
    	StringBuilder sb = new StringBuilder();
    	
    	Exams selectedExam = table_ListOfExams.getSelectionModel().getSelectedItem();
    	if(selectedExam != null) {
    		
    		int id = Integer.parseInt(txtId.getText().trim());
    		int userId = user.getId();
        	String subject = txtSubject.getText().trim();
        	String class_room = txtClass_room.getText().trim();
        	int quantity = Integer.parseInt(Box_Quantity.getSelectionModel().getSelectedItem().toString());
        	String status = Box_Status.getSelectionModel().getSelectedItem().toString();
        	int total_time = Integer.parseInt(Box_TotalTime.getSelectionModel().getSelectedItem().toString());
        	
        	if(subject.equals("")) {
        		sb.append("Vui lòng không để trống tên môn học\n");
        	}
        	
        	if(class_room.equals("")) {
        		sb.append("Vui lòng không để trống mã phòng thi\n");
        	}
        	
        	if(id == 0) {
        		sb.append("Bạn không thể update do chưa có mã đề thi\n");
        	}
        	
        	if (sb.length() > 0) {
        		showAlert(sb.toString(),AlertType.INFORMATION);
    		}else {
    			Exams exam = new Exams(id, subject, class_room, quantity, total_time, status, userId);

    			Server_Response server_Response = null;
    			
    			server_Response = quizExam.update_exam(exam);
    			
    			if(server_Response.getStatus().equals("OK")) {
    				HashMap<String, Object> capitalCities = new HashMap<String, Object>();
    				
    				capitalCities = (HashMap<String, Object>) server_Response.getResult();
    				
    				this.Update_DataExam((Exams) capitalCities.get("exam"));
    				
    				SharedData.getInstance().listData( (List<Questions>) capitalCities.get("questions") , "questions");

					Pane new2 = this.getPage("Question");
		            mainPane.setCenter(new2); 
    			}
    			else {
					showAlert(server_Response.getMessage(), AlertType.ERROR);
				}
    		}
    	}

    }

    @FXML
    void setUp_DataExam(MouseEvent event) throws RemoteException {
    	
    	Exams selectedExam = table_ListOfExams.getSelectionModel().getSelectedItem();
    	
    	if (event.getClickCount() == 1) { // Kiểm tra nếu double click
            if (selectedExam != null) {

               txtId.setText(String.valueOf(selectedExam.getId()));
               txtSubject.setText(selectedExam.getSubject());
               txtClass_room.setText(selectedExam.getClass_room());
               Box_Quantity.setValue(String.valueOf(selectedExam.getQuantity()));
               Box_Status.setValue(selectedExam.getStatus());
               Box_TotalTime.setValue(String.valueOf(selectedExam.getTotal_time()));
               
               loadData_Question(selectedExam.getId());

            }
        }

    }
    
 // Validate dữ liệu ở ô nhập liệu
    public Boolean Validate() {
    	StringBuilder sb = new StringBuilder();
    	
    	int userId = user.getId();
    	String subject = txtSubject.getText().trim();
    	String class_room = txtClass_room.getText().trim();
    	int quantity = Integer.parseInt(Box_Quantity.getSelectionModel().getSelectedItem().toString());
    	String status = Box_Status.getSelectionModel().getSelectedItem().toString();
    	int total_time = Integer.parseInt(Box_TotalTime.getSelectionModel().getSelectedItem().toString());
    	
    	
    	if(subject.equals("")) {
    		sb.append("Vui lòng không để trống tên môn học\n");
    	}
    	
    	if(class_room.equals("")) {
    		sb.append("Vui lòng không để trống mã phòng thi\n");
    	}
    	
    	if (sb.length() > 0) return false;
		else return true;
    }
    
    public void CreatedUsername() {
    	txtUserName.setText(user.getUsername());
    }
    
//    đổ dữ liệu cho bản question theo cột exam được chọn
    public void loadData_Question(int exam_id) throws RemoteException {
        ObservableList<Questions> observableList_Ques = FXCollections.observableArrayList();
    	
        Server_Response response = null;
        
        response = questionService.getQuestion_ByExamId(exam_id);
        
        if(response.getStatus().equals("OK")) {
        	List<Questions> listQuestion = (List<Questions>) response.getResult();
        
        	for (Questions q : listQuestion) {
        		Questions question = new Questions(q.getExam_id(),q.getExam_id(),q.getQuestion(),q.getA(),q.getB(),q.getC(),q.getD(),q.getAnswer());
    			observableList_Ques.add(question);
			}
        	
        	table_question.setItems(observableList_Ques);
        }else {
  		  table_question.setItems(FXCollections.observableArrayList());
        }
    }
    
//    đổ dữ liệu lên table theo id người đăng nhập
    public void loadDate() throws RemoteException{
    	ObservableList<Exams> observableList_Exam = FXCollections.observableArrayList();
    	
    	Server_Response response = null;
    	
    	response = quizExam.get_exmas_userId(user.getId());
    
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
   			        user.getId()
   			    );
    			
    			observableList_Exam.add(exam);
			}

        	table_ListOfExams.setItems(observableList_Exam);
    	}
    	
    }
    
//    Đẩy dữ liệu các bài thi lên bảng 
    void push_Data() {
    	
    	// Bảng đề thi
    	col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_title.setCellValueFactory(new PropertyValueFactory<>("subject"));
        col_room.setCellValueFactory(new PropertyValueFactory<>("class_room"));
        col_quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        col_time.setCellValueFactory(new PropertyValueFactory<>("total_time"));
        col_status.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        // Bảng câu hỏi theo đề thi
        ques_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        ques_question.setCellValueFactory(new PropertyValueFactory<>("question"));
        ques_A.setCellValueFactory(new PropertyValueFactory<>("A"));
        ques_B.setCellValueFactory(new PropertyValueFactory<>("B"));
        ques_C.setCellValueFactory(new PropertyValueFactory<>("C"));
        ques_D.setCellValueFactory(new PropertyValueFactory<>("D"));
        ques_Ans.setCellValueFactory(new PropertyValueFactory<>("answer"));
       
    }
    
    public void loadDataForBox() {
    	
    	Box_TotalTime.setItems(FXCollections.observableArrayList("5","15", "30", "60", "120"));
    	Box_Quantity.setItems(FXCollections.observableArrayList("3","5","10", "20", "30", "40", "50", "60"));
    	Box_Status.setItems(FXCollections.observableArrayList("Cộng Đồng", "Cá Nhân"));
    	
    	Box_TotalTime.setValue("15");
    	Box_Quantity.setValue("10");
    	Box_Status.setValue("Cộng Đồng");
    	
    }
   

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			
			push_Data();
			loadDate();
			CreatedUsername();
			loadDataForBox();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}


}