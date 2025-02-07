package Model;

import java.io.Serializable;
import java.util.Date;


//Serializable để tuần tự hoá đổi tượng chuyển thành sạng byte
public class Users implements Serializable{
	
	// Đảm bảo cùng giá trị serialVersionUID trên cả client và server
	private static final long serialVersionUID = 1L; 
	
	private static Users instance;
	private int id;
	private String username;
	private String email;
	private String password;
	private String gender;
	private Date birthday;
	private String status;
	public String avatar;
	private String role;
	
	
	public Users() {

	}


	public Users(int id, String username, String email, String password, String gender, Date birthday,
			String status, String role) {
		this.username = username;
		this.email = email;
		this.password = password;
		this.gender = gender;
		this.birthday = birthday;
		this.status = status;
		this.role = role;
	}

	public Users(String username, String email, String password, String gender, Date birthday, String avatar) {
		this.username = username;
		this.email = email;
		this.password = password;
		this.gender = gender;
		this.birthday = birthday;
		this.avatar = avatar;
	}

	
	public static Users getInstance() {
        if (instance == null) {
            instance = new Users();
        }
        return instance;
    }


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getGender() {
		return gender;
	}


	public void setGender(String gender) {
		this.gender = gender;
	}


	public Date getBirthday() {
		return birthday;
	}


	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public String getRole() {
		return role;
	}


	public void setRole(String role) {
		this.role = role;
	}
	
	
	
	public String getAvatar() {
		return avatar;
	}


	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
}

