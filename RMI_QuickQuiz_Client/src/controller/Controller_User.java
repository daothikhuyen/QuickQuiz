package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;

import Model.QuizManager;
import Model.Users;
import Request.Request_Register;
import Response.Server_Response;
import javafx.event.ActionEvent;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import serverInterface.UserService;
import javafx.scene.control.RadioButton;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.effect.DropShadow;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;

public class Controller_User implements Initializable{
	@FXML
	private AnchorPane root, root_SetUpUser;
    @FXML
    private Label txt_URLFile;
	
	@FXML
	private TextField txtUsername, txt_Username, txt_UserId;
	@FXML
	private TextField txtEmail,txt_Email;
	@FXML
	private PasswordField txtPassword;
	@FXML
	private PasswordField txtConfirm;
	@FXML
	private RadioButton rMale ,rid_Male;
	@FXML
	private RadioButton rFemale, rid_Female;
	@FXML
	private DatePicker txtBirthday,Box_Birthday;
    @FXML
    private ImageView image_avatar;
//    @FXML
//    private Circle circle;
	
	private String birthday;

    @FXML
    private ToggleGroup Group_Gender;


    UserService quizService = QuizManager.getQuizService();

// CHỨC NĂNG CHUNG
	
	// cập nhập lại thông tin vào model user
    public void assign_data(Users result) {

		try {			
			
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

			Users user = Users.getInstance();
			
			user.setId(result.getId());
			user.setUsername(result.getUsername());
			user.setEmail(result.getEmail());
			user.setPassword(result.getPassword());
			user.setGender(result.getGender());
			user.setBirthday(result.getBirthday());
			user.setStatus(result.getStatus());
			user.setRole(result.getRole());
			
			if(result.getAvatar() != null) {
				user.setAvatar(result.getAvatar());
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Loi: "+ e.getMessage());
		}	
	}

	// thay đổi giao diện
	public void ChangeInterface(String url){
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(url));
	        Parent parent = loader.load();

	        Scene scene = new Scene(parent);
	        Stage primaryStage = (Stage) root.getScene().getWindow();
	        primaryStage.setScene(scene);
	        primaryStage.setTitle("QUICK QUIZ");
	        primaryStage.centerOnScreen();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
//	CHỨC NĂNG CỦA ĐĂNG KY
	
	
	public void showAlert(String message, AlertType information) {
		
		Stage primaryStage;
		if(this.root != null) {
			 primaryStage = (Stage) root.getScene().getWindow();
		}else {
			 primaryStage = (Stage) root_SetUpUser.getScene().getWindow();
		}
		
		AlertType type = information;
		Alert alert = new Alert(type, "");
		
		alert.initModality(Modality.APPLICATION_MODAL);
		alert.initOwner(primaryStage);
		
		alert.getDialogPane().setContentText(message);
		alert.getDialogPane().setHeaderText("Thông Báo");
		
		alert.showAndWait();
		
	}
	
	
// PHẦN CHỨC NĂNG ĐĂNG KÝ
	
// PHẦN CHỨC NĂNG CỦA REGISTER
	
	// Event Listener on Button.onAction
	@FXML
	public void Activite_Register(ActionEvent event) throws RemoteException {
		String username = txtUsername.getText().trim();
		String email = txtEmail.getText().trim();
		String password = txtPassword.getText().trim();
		String confirm = txtConfirm.getText().trim();
		String gender = null;

		StringBuilder sb = new StringBuilder();
		
		if(username.equals("")) {

			sb.append("Vui lòng không để trống tên người dùng\n");
		}
		
		String regexPattern = "^(.+)@(\\S+)$";
		if(email.equals("")) {

			sb.append("Không được để trống email!\n");
		}else if(!email.matches(regexPattern)){

			sb.append("Email không hợp lệ\n");
		}
		
		if(password.equals("")) {
			sb.append("Vui lòng không để trống password\n");

		}else if(password.length() < 8) {

			sb.append("Mật khẩu phải ít nhất 8 kí tự trở lên\n");
		}
		
		if(!password.equals(confirm)) {

			sb.append("Mật khẩu không trùng khớp\n");
		}
		
		if(rMale.isSelected()) {
			gender = "Nam";
		}else if(rFemale.isSelected()) {
			gender = "Nữ";
		}else if(gender == null){
			
			sb.append("Vui lòng chọn giới tính của bạn\n");
		}
		
		if(txtBirthday.getValue() == null) {
			sb.append("Vui lòng nhập ngày sinh của bạn\n");
		}else {
			birthday = txtBirthday.getValue().toString();
		}
		
		
		if(sb.length() > 0 ) {
			showAlert(sb.toString(),AlertType.INFORMATION);
		}else {
			Request_Register request_Register = new Request_Register(username, email, password, gender, birthday);
			
			Boolean result = false;
			
			 if (quizService != null) {
				 result = quizService.register(request_Register);
				 
				 if(result) {
					 showAlert("Đăng Kí Tài Khoản Thành Công", AlertType.CONFIRMATION);
					 ChangeInterface("/view/Frm_Login.fxml");
				 }else {
					 showAlert("Người dùng đã tồn tại", AlertType.ERROR);
				 }
			 }
		}
	}
	


	@FXML
	public void btnExit(ActionEvent event) {
		Stage primaryStage = (Stage) root.getScene().getWindow();
		primaryStage.close();
	}
	

	@FXML
	public void btnLogin(ActionEvent event) throws IOException {
		ChangeInterface("/view/Frm_Login.fxml");
	}
	
	
// PHẦN CHỨC NĂNG ĐĂNG NHẬP
	
	
	@FXML
	public void Activite_Login(ActionEvent event) throws RemoteException {
		String email = txtEmail.getText().trim();
		String password = txtPassword.getText().trim();
		
		StringBuilder sb = new StringBuilder();
		
		String regexPattern = "^(.+)@(\\S+)$";
		if(email.equals("")) {

			sb.append("Không được để trống email!\n");
		}else if(!email.matches(regexPattern)){

			sb.append("Email không hợp lệ\n");
		}
		
		if(password.equals("")) {
			sb.append("Vui lòng không để trống password\n");

		}else if(password.length() < 8) {

			sb.append("Mật khẩu phải ít nhất 8 kí tự trở lên\n");
		}
		
		if(sb.length() > 0) {
			showAlert(sb.toString(),AlertType.INFORMATION);
			
			return;
		}else {
			
			Server_Response<Users> response = null;
			
			response = quizService.login(email, password);
			
			if(response.getStatus().equals("OK")) {
				assign_data(response.getResult());
				
				showAlert(response.getMessage(), AlertType.INFORMATION);
				ChangeInterface("/view/Home.fxml");
			}else {
				showAlert(response.getMessage(), AlertType.ERROR);		
			}
			
		}
	}
	
	@FXML
	public void btnRegister(ActionEvent event) throws IOException {
		ChangeInterface("/view/Frm_Register.fxml");
	}
	
//	CHỨC NĂNG CỦA NGƯỜI DÙNG

    @FXML
    void Change_Avatar(MouseEvent event) {
    	FileChooser fileChooser = new FileChooser();
    	
    	fileChooser.getExtensionFilters().addAll(
			new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
		);
    	
    	File selectedFile = fileChooser.showOpenDialog((Stage) image_avatar.getScene().getWindow());
 
    	if(selectedFile != null) {
    		Path pathFile = UploadFile(selectedFile);
    		String url = pathFile.toUri().toString();
    		image_avatar.setImage(new Image(url));
    		

    		txt_URLFile.setText(url);
    	}
    }

    @FXML
    void btnUpdate_User(ActionEvent event) {
    	Users user = Users.getInstance();
    	
    	System.out.println("avatar: "+ user.getAvatar());
    	String username = txt_Username.getText().trim();
		String email = txt_Email.getText().trim();
		String gender = null;
		String avatar = user.getAvatar();

		StringBuilder sb = new StringBuilder();
		
		if(username.equals("")) {

			sb.append("Vui lòng không để trống tên người dùng\n");
		}
		
		String regexPattern = "^(.+)@(\\S+)$";
		if(email.equals("")) {

			sb.append("Không được để trống email!\n");
		}else if(!email.matches(regexPattern)){

			sb.append("Email không hợp lệ\n");
		}
		
		if(rid_Male.isSelected()) {
			gender = "Nam";
		}else if(rid_Female.isSelected()) {
			gender = "Nữ";
		}else if(gender == null){
			
			sb.append("Vui lòng chọn giới tính của bạn\n");
		}
		
		if(Box_Birthday.getValue() == null) {
			sb.append("Vui lòng nhập ngày sinh của bạn\n");
		}else {
			birthday = Box_Birthday.getValue().toString();
		}
		
		Image image = image_avatar.getImage();
		System.out.println("ảnh: " + txt_URLFile.getText());
		if(txt_URLFile.getText() != "") {
			System.out.println("xin chào");
			avatar = txt_URLFile.getText();
		}
		
		System.out.println("avatar: "+ avatar);
		
		
		if(sb.length() > 0 ) {
			showAlert(sb.toString(),AlertType.INFORMATION);
		}else {
			try {
				
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				 
				Users users = new Users(username, email, user.getPassword(), gender, formatter.parse(birthday), avatar);
				
				SetUp_Profile(users);
				
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("loi: "+ e.getMessage());
			}
		}
    }
    
    private void SetUp_Profile(Users user) throws RemoteException {
    	
		Server_Response response = null;
		
    	response =  quizService.updateUser(user);
    	
    	if(response.getStatus().equals("OK")) {
    		Users u = (Users) response.getResult();
    		
    		assign_data(u);
    		showAlert(response.getMessage(), AlertType.INFORMATION);
    	}else {
    		showAlert(response.getMessage(), AlertType.ERROR);
    	}
		
	}

	public Path UploadFile(File selectedFile) {

    	try {
      	  	String currentWorkingDir = System.getProperty("user.dir"); // lây ra ổ đĩa lưu project
    		Path pathFull = Paths.get(currentWorkingDir,"src/assets");
    		
    		// Tạo thư mục nếu chưa tồn tại
			Files.createDirectories(pathFull.getParent());
			
			Path path = pathFull.resolve(selectedFile.getName());
			
			Files.copy(selectedFile.toPath(), path,StandardCopyOption.REPLACE_EXISTING);

			return path;
			
		} catch (IOException e) {
			System.out.println("loi copy file"+ e.getMessage());
			e.printStackTrace();
		}
    	
    	return null;
    }
    
	private void load_DataUser() {
		
		if(root_SetUpUser != null) {
			
			Users user = Users.getInstance();
			
	    	txt_UserId.setText(String.valueOf(user.getId()));
	    	txt_Username.setText(user.getUsername());
	    	txt_Email.setText(user.getEmail());
	    	
	    	if(user.getGender().equals("Nam")) {
	    		rid_Male.setSelected(true);
	    	}else {
	    		rid_Female.setSelected(true);
	    	}
	    	
	    	Date birthday = null;
	    	
	    	if (user.getBirthday() instanceof java.sql.Date) {
				
				birthday = new java.util.Date(user.getBirthday().getTime()) ;
			}
	    	
	    	
	    	// Chuyển đổi Date sang LocalDate
	    	LocalDate localDate = birthday.toInstant()
	    	                              .atZone(ZoneId.systemDefault())
	    	                              .toLocalDate();

	    	Box_Birthday.setValue(localDate);
	    	
			if(user.getAvatar() != null) {
				image_avatar.setImage(new Image(user.getAvatar().toString()));
			}else {
				image_avatar.setImage(new Image("/assets/person.png"));
			}
			
			Circle mycircle = new Circle(100,100,100); 
			mycircle.setEffect(new DropShadow(8, Color.BLACK)); 
			image_avatar.setClip(mycircle);
		}
	}
    	

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
//		Đẩy dữ liruj người dùng cho phần thông tin người dùng
		load_DataUser();

	}
}
