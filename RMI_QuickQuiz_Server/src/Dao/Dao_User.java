package Dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

import Model.Users;
import Request.PasswordUtils;


public class Dao_User {
	Connection conn = ConnectionDB.getConnection();
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	
	public Boolean Existence_check(String email){
		
		try {
			String sql = "SELECT * FROM `users` WHERE email = ?";
			PreparedStatement pst = conn.prepareStatement(sql);
			pst.setString(1, email);
			ResultSet rs = pst.executeQuery();// lấy kết quả trả về được
			
			if(rs.next()) return false; // email đã tồn tại
			
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public Users getUser(String email) {
		Users users = new Users();

		try {
			String sql = "SELECT * FROM `users` WHERE email = ?";
			PreparedStatement pst = conn.prepareStatement(sql);
			pst.setString(1, email);
			ResultSet rs = pst.executeQuery();

			while (rs.next()) {
				
				users.setId(rs.getInt("id"));
				users.setUsername(rs.getString("username"));
				users.setEmail(rs.getString("email"));
				users.setPassword(rs.getString("password"));
				users.setGender(rs.getString("gender"));
				users.setBirthday(rs.getDate("birthday"));
				users.setStatus(rs.getString("status"));
				users.setAvatar(rs.getString("avatar"));
				users.setRole(rs.getString("role"));
			}
			
			return users;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Loi: "+ e.getMessage());
			return null;
			
		}
	}
	
	public Boolean createdUser(Users user) {
		try {
			String hashedPassword = PasswordUtils.hashPassword(user.getPassword());
			
			String sql = "INSERT INTO `users`(`username`, `email`, `password`, `gender`, `birthday`, `status`, `role`, `created_at`) "
					+ "VALUES (?,?,?,?,?,?,?,?)";
			
			PreparedStatement pst = conn.prepareStatement(sql);
			pst.setString(1, user.getUsername());
			pst.setString(2, user.getEmail());
			pst.setString(3, hashedPassword);
			pst.setString(4, user.getGender());
			pst.setDate(5, (Date) user.getBirthday());
			pst.setString(6, "OFF");
			pst.setString(7, "USER");
			pst.setDate(8,Date.valueOf(LocalDate.now()));
			pst.execute(); // trả về true hoặc false
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Loi sql register: "+ e.getMessage());
			return false;
		}
		
	}

	public Users login(String email, String password) {
		
		Users users = new Users();
		String hashedPassword = PasswordUtils.hashPassword(password);
		try {
			String sql = "SELECT * FROM `users` WHERE email = ? AND password = ?";
			PreparedStatement pst = conn.prepareStatement(sql);
			pst.setString(1, email);
			pst.setString(2,hashedPassword);
			ResultSet rs = pst.executeQuery();

			while (rs.next()) {
				
				this.Update_Status(email, "ON");
				
				users.setId(rs.getInt("id"));
				users.setUsername(rs.getString("username"));
				users.setEmail(rs.getString("email"));
				users.setPassword(rs.getString("password"));
				users.setGender(rs.getString("gender"));
				users.setBirthday(rs.getDate("birthday"));
				users.setStatus(rs.getString("status"));
				users.setAvatar(rs.getString("avatar"));
				users.setRole(rs.getString("role"));
			}
			
			return users;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Loi: "+ e.getMessage());
			return null;
			
		}
		
	}
	
	public Boolean Update_Status(String email, String status) {
		
		try {
			String sql = "UPDATE `users` SET `status`= ? WHERE `email`= ?" ;
			PreparedStatement pst = conn.prepareStatement(sql);
			pst.setString(1, status);
			pst.setString(2, email);
			
			pst.executeUpdate();
			
			return true;
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Loi :"+ e.getMessage());
		}
		return false;
		
	}

	public Users setUp_Profile(Users user) {

		try {
			
			String sql = "UPDATE `users` SET `username`=?,`gender`=?,`birthday`=?,`avatar`=? WHERE email=?" ;
			PreparedStatement pst = conn.prepareStatement(sql);
			pst.setString(1, user.getUsername());
			pst.setString(2, user.getGender());
			pst.setDate(3, (Date) user.getBirthday());
			pst.setString(4, user.getAvatar());
			pst.setString(5, user.getEmail());
			
			 int rowsAffected = pst.executeUpdate();
			 
			 if(rowsAffected > 0) {
				 return this.getUser(user.getEmail());
			 }
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Loi cap nhat tai khoan nguoi dung: "+ e.getMessage());
		}
		return user;
		
	}
	
	
}
