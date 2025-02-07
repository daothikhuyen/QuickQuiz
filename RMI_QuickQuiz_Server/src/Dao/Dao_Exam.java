package Dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.mysql.cj.xdevapi.Result;

import Model.Exams;
import Model.Results;
import Model.Users;

public class Dao_Exam {
	Connection conn = ConnectionDB.getConnection();

	
//	Lấy toàn bộ bài thi
	public List<Exams> get_listExam() {
		List<Exams> examsList = new ArrayList<>();
		try {
			
			String sql = "SELECT * FROM `exams` WHERE status = 'Cộng Đồng'";
			PreparedStatement pst = conn.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			
			while (rs.next()) {
				Exams exams = new Exams();
				
				exams.setId(rs.getInt("id"));
				exams.setUser_id(rs.getInt("user_id"));
				exams.setSubject(rs.getString("subject"));
				exams.setClass_room(rs.getString("class_room"));
				exams.setQuantity(rs.getInt("quantity"));
				exams.setTotal_time(rs.getInt("total_time"));
				exams.setStatus(rs.getString("status"));
				examsList.add(exams);

			}
			
		} catch (Exception e) {
			System.out.println("Loi get_listExam: "+ e.getMessage());
		}
		
		return examsList;
	}

//	 lấy dữ liệu câu hỏi theo id người dùng
	public List<Exams> getExams_UserId(int user_id) {
		List<Exams> examsList = new ArrayList<>();

		try {
			
			String sql = "SELECT * FROM `exams` WHERE user_id = ?";
			PreparedStatement pst = conn.prepareStatement(sql);
			pst.setInt(1, user_id);
			ResultSet rs = pst.executeQuery();
			
			while (rs.next()) {
				Exams exams = new Exams();
				
				exams.setId(rs.getInt("id"));
				exams.setUser_id(rs.getInt("user_id"));
				exams.setSubject(rs.getString("subject"));
				exams.setClass_room(rs.getString("class_room"));
				exams.setQuantity(rs.getInt("quantity"));
				exams.setTotal_time(rs.getInt("total_time"));
				exams.setStatus(rs.getString("status"));
				examsList.add(exams);

			}
			
		} catch (Exception e) {
			System.out.println("Loi getExams_UserId: "+ e.getMessage());
		}
		
		return examsList;
	}
	
	public Exams selectExam(int id_exam) {
		
		Exams exam = new Exams();
		
		try {
			
			String sql = "SELECT * FROM `exams` WHERE id = ?";
			PreparedStatement pst = conn.prepareStatement(sql);
			pst.setInt(1, id_exam);
			ResultSet rs = pst.executeQuery();
			
			while(rs.next()) {
				exam.setId(rs.getInt("id"));
				exam.setUser_id(rs.getInt("user_id"));
				exam.setSubject(rs.getString("subject"));
				exam.setClass_room(rs.getString("class_room"));
				exam.setQuantity(rs.getInt("quantity"));
				exam.setTotal_time(rs.getInt("total_time"));
				exam.setStatus(rs.getString("status"));
			}
			
			return exam;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("loi seclect exam: "+ e.getMessage());
			return null;
		}
	}

//	Sáng tạo
	public Exams created_exam(Exams exam) {
	    int insertId = 0;
	    System.out.println(exam.getUser_id());
	    try {
	        String sql = "INSERT INTO `exams`(`user_id`, `subject`, `class_room`, `quantity`, `status`, `total_time`, `created_at`) VALUES (?, ?, ?, ?, ?, ?, ?)";
	        
	        // Thêm Statement.RETURN_GENERATED_KEYS để yêu cầu khóa sinh ra
	        PreparedStatement pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
	        pst.setInt(1, exam.getUser_id());
	        pst.setString(2, exam.getSubject());
	        pst.setString(3, exam.getClass_room());
	        pst.setInt(4, exam.getQuantity());
	        pst.setString(5, exam.getStatus());
	        pst.setInt(6, exam.getTotal_time());
	        pst.setDate(7, Date.valueOf(LocalDate.now()));
	        
	        int affectedRows = pst.executeUpdate();
	        
	        if (affectedRows > 0) {
	            // Nhận khóa sinh ra
	            ResultSet generatedKeys = pst.getGeneratedKeys();
	            if (generatedKeys.next()) {
	                insertId = generatedKeys.getInt(1);
	            }
	        } else {
	            System.out.println("Không có hàng nào được chèn vào");
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        System.out.println("Lỗi tạo bài kiểm tra: " + e.getMessage());
	        return null;
	    }
	    
	    return selectExam(insertId);
	}

	public Boolean delete_Exam(int exam_id) {
		try {
			String sql = "DELETE FROM `exams` WHERE id = ?";
			PreparedStatement pst = conn.prepareStatement(sql);
			pst.setInt(1,exam_id);
			
			return pst.executeUpdate() > 0; 
		} catch (Exception e) {
			System.out.println("Loi xoa bai thi: "+ e.getMessage());
		}
		return false;
	}
	
// Cập nhập
	public Exams update_exam(Exams exam) {

		try {
			String sql = "UPDATE `exams` SET `subject`=?,`class_room`=?,`quantity`=?,`status`=?,`total_time`=?,`created_at`= ? WHERE id = ?";
			PreparedStatement pst = conn.prepareStatement(sql);
	        pst.setString(1, exam.getSubject());
	        pst.setString(2, exam.getClass_room());
	        pst.setInt(3, exam.getQuantity());
	        pst.setString(4, exam.getStatus());
	        pst.setInt(5, exam.getTotal_time());
	        pst.setDate(6, Date.valueOf(LocalDate.now()));
	        pst.setInt(7, exam.getId());
	        
	        int rowsAffected = pst.executeUpdate();
	        if( rowsAffected > 0) {
	        	return selectExam(exam.getId());
	        }
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("loi cap nhap bai thi: "+ e.getMessage());
		}
		return null;
	}
	
	public String check_level(int user_id,int exam_id, int result_id) {
		
		try {
			
			String sql = "SELECT * FROM "
					+ "( SELECT id, user_id, score, RANK() OVER (ORDER BY score DESC) AS user_rank "
							+ "FROM results "
							+ "WHERE exam_id = ?"
					+ ") AS ranked_results "
					+ "WHERE id = ? AND user_id = ?";
			
			PreparedStatement pst = conn.prepareStatement(sql);
			pst.setInt(1, exam_id);
			pst.setInt(2,result_id);
			pst.setInt(3,user_id);
			ResultSet rs = pst.executeQuery();
			
			while (rs.next()) {
				String rank = String.valueOf(rs.getInt("user_rank"));
				return rank;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String save_result_exam(Results results) {

		int insertId = 0;
		try {
			String sql = "INSERT INTO `results`(`exam_id`, `user_id`, `score`, `created_at`) "
						+ "VALUES (?,?,?,?)";
			
			PreparedStatement pst = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			pst.setInt(1, results.getExam_id());
			pst.setInt(2, results.getUser_id());
			pst.setFloat(3, results.getScore());
			pst.setDate(4, Date.valueOf(LocalDate.now()));
			
			int affectedRows = pst.executeUpdate();
			if (affectedRows > 0) {
	            // Nhận khóa sinh ra
	            ResultSet generatedKeys = pst.getGeneratedKeys();
	            if (generatedKeys.next()) {
	                insertId = generatedKeys.getInt(1);
	            }
	        } 
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("loi lu ket qua: "+ e.getMessage());
		}
		return check_level(results.getUser_id(),results.getExam_id(),insertId) ;
	}
	
//	Kiếm tra xem có người dùng từng làm bài kiểm tra chưa
	public Boolean getPerson_JoinExam(int exam_id) {
		Integer count = 0;
		try {
			String sql = "SELECT COUNT(*) AS numPerson FROM `results` WHERE exam_id = ?";
			
			PreparedStatement pst = conn.prepareStatement(sql);
			pst.setInt(1, exam_id);
			ResultSet rs = pst.executeQuery();
			
			 if (rs.next()) { // Kiểm tra xem có dữ liệu không
	            count = rs.getInt(1);
	        }
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return count > 0;
	}
	

	
}
