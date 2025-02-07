package controller;

import java.net.InetAddress;
import java.net.URL;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.json.simple.parser.JSONParser;

import CallBack.ClientCallback;
import CallBack.ClientCallbackImpl;
import Model.Exams;
import Model.Questions;
import Model.QuizManager;
import Response.Server_Response;
import Response.SharedData;
import application.Main;
import javafx.fxml.Initializable;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import serverInterface.ExamService;
import serverInterface.QuestionService;
import javafx.scene.control.cell.PropertyValueFactory;

public class Controller_ListOfExam implements Initializable{

	 private static final long serialVersionUID = 1L; // Khuyến khích thêm serialVersionUID
	
	@FXML
    private AnchorPane root;
	
	@FXML
	private transient TableView<Exams> table_ListExam;
	
    @FXML
    private TableColumn<String, Exams> col_creator,col_room,col_time,col_title;

    @FXML
    private TableColumn<Integer, Exams> col_quantity;

    private BorderPane mainPane;
    
    ExamService quizExam = QuizManager.getQuizExam();
    QuestionService quizQuestion = QuizManager.getQuizQuestion();
    public static ClientCallback callback ;
    
	
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
            
            
            if(fileName.equals("Exam_Infomation")) {
            	Controller_ExamInfomation controller_ExamInfomation = loader.getController();
                controller_ExamInfomation.setMainPane(mainPane);
            }
 
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
    
    public void Update_DataExam(Exams selectedExam) {
		Exams data_exam = Exams.getInstance();
		
		data_exam.setId(selectedExam.getId());
		data_exam.setUser_id(selectedExam.getUser_id());
		data_exam.setSubject(selectedExam.getSubject());
		data_exam.setClass_room(selectedExam.getClass_room());
		data_exam.setQuantity(selectedExam.getQuantity());
		data_exam.setTotal_time(selectedExam.getTotal_time());
		
	}
    
//    Sau 5 giây sẽ tự động load lại để cập nhập các đề thi mới nhất
    private void startAutoRefresh() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(5), event -> {
        	load_ListExam();
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
	
	@FXML
    void HandClick_Exam(MouseEvent event) {
		Exams selectedExam = table_ListExam.getSelectionModel().getSelectedItem();
    	
    	if (event.getClickCount() == 1) { // Kiểm tra nếu double click
            if (selectedExam != null) {

            	int exam_id = selectedExam.getId();
            	int quantity = selectedExam.getQuantity();
            	
            	ObservableList<Questions> observableList_Ques = FXCollections.observableArrayList();
            	try {

            		Server_Response response = null;
        			
            		response = quizQuestion.getQuestion_ByExamId(exam_id);

            		if(response.getStatus().equals("OK")) {
            			List<Questions> list_ques = (List<Questions>) response.getResult();
            			if(list_ques.size() < quantity) {
                			showAlert("Đề Thi đang trong quá trình cập nhật", AlertType.INFORMATION);
            			}else {
            				this.Update_DataExam(selectedExam);
    						SharedData.getInstance().listData(list_ques, "questions");
    						
    						Pane new2 = this.getPage("Exam_Infomation");
    			            mainPane.setCenter(new2); 
            			}
            		}else {
            			showAlert("Đề Thi đang trong quá trình cập nhật", AlertType.INFORMATION);
            		}
            		
        		} catch (Exception e) {
        			e.printStackTrace();
        		}

            }
        }
    }
	

	
// Lấy ra tất cả bài Thi
	public void load_ListExam() {
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
	   			        exams.getUser_id()
	   			    );
	    			
	    			observableList_Exam.add(exam);
				}
	    		
	    		if (table_ListExam != null) {
	    			table_ListExam.setItems(observableList_Exam);
				}
	    		
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Loi load_ListExam: "+ e.getMessage());
		}
		
	}

	public void setUp_Table() {

        col_title.setCellValueFactory(new PropertyValueFactory<>("subject"));
        col_room.setCellValueFactory(new PropertyValueFactory<>("class_room"));
        col_quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        col_time.setCellValueFactory(new PropertyValueFactory<>("total_time"));
        col_creator.setCellValueFactory(new PropertyValueFactory<>("user_id"));
	}
    
	   // Method này dùng để đăng ký với server 
    public void registerForUpdates() {
    	
		// Tạo instance callback và đăng ký với server
        try {
        	callback = new ClientCallbackImpl(table_ListExam);
        	
            Boolean check = quizExam.registerCallback(InetAddress.getLocalHost().getHostAddress(), callback);
        	System.out.println("Dang Ky CallBack Thanh Cong");
  
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void unregisterCallback() {
    	try {
			
    		if(callback != null) {
    			UnicastRemoteObject.unexportObject(callback, false);
    			System.out.println("Huy Dang Ky CallBack Thanh Cong");
    		}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		setUp_Table();
		
		load_ListExam();
		
		registerForUpdates();
		
		
	}





}
