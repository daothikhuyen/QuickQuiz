package Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import Model.Questions;

public class Dao_Question {
	
	Connection conn = ConnectionDB.getConnection();

	public List<Questions> getQuestion_ByExamId(int exam_id) {
		
		List<Questions> list_Question = new ArrayList<>();
		
		try {
			
			String sql = "SELECT * FROM `questions` WHERE exam_id = ?";
			PreparedStatement pst = conn.prepareStatement(sql);
			pst.setInt(1, exam_id);
			ResultSet rs = pst.executeQuery();
			
			while (rs.next()) {
				Questions ques = new Questions();
				ques.setId(rs.getInt("id"));
				ques.setExam_id(rs.getInt("exam_id"));
				ques.setQuestion(rs.getString("question"));
				ques.setA(rs.getString("A"));
				ques.setB(rs.getString("B"));
				ques.setC(rs.getString("C"));
				ques.setD(rs.getString("D"));
				ques.setAnswer(rs.getString("answer"));
				list_Question.add(ques);
			}
		} catch (Exception e) {
			System.out.println("Loi lay question theo exam_id: "+ e.getMessage());
		}
		return list_Question;
	}

	public boolean  created_question(List<Questions> list_questions) {
		
		try {
			
			String sql ="INSERT INTO `questions`(`exam_id`, `question`, `A`, `B`, `C`, `D`, `answer`) "
					+ "VALUES (?,?,?,?,?,?,?)";
			
			PreparedStatement pst = conn.prepareStatement(sql);
			for (Questions quest : list_questions) {
				pst.setInt(1, quest.getExam_id());
				pst.setString(2,quest.getQuestion());
				pst.setString(3,quest.getA());
				pst.setString(4,quest.getB());
				pst.setString(5,quest.getC());
				pst.setString(6,quest.getD());
				pst.setString(7,quest.getAnswer());
				pst.addBatch(); // thêm nhiều câu lệnh vào batch(chuyến )
			}
			
			int[] results = pst.executeBatch(); // thức thi các câu lệnh cùng 1 lúc
			 
			// Kiểm tra xem có ít nhất một câu hỏi được lưu thành công không
	        for (int result : results) {
	            if (result == PreparedStatement.SUCCESS_NO_INFO || result > 0) {
	                return true; // Nếu ít nhất một câu hỏi được thêm thành công
	            }
	        }
			
		} catch (Exception e) {
			System.out.println("Loi database tạo csdl cau hoi moi: "+ e.getMessage());
		}
		return false;
	}
	
	public boolean  update_question(List<Questions> list_questions) {
		List<Questions> list_newques = new ArrayList<>();
		try {
			
			String sql ="UPDATE `questions` SET `question`=?,`A`=?,`B`=?,`C`=?,`D`=?,`answer`=? WHERE id =?";
			
			PreparedStatement pst = conn.prepareStatement(sql);
			
			for (Questions quest : list_questions) {
				if(quest.getId()!= 0) {
					pst.setString(1,quest.getQuestion());
					pst.setString(2,quest.getA());
					pst.setString(3,quest.getB());
					pst.setString(4,quest.getC());
					pst.setString(5,quest.getD());
					pst.setString(6,quest.getAnswer());
					pst.setInt(7,quest.getId());
					pst.addBatch(); // thêm nhiều câu lệnh vào batch(chuyến )
				}else {
					list_newques.add(quest);
				}
			}
			
			if(list_newques.size() > 0) {
				this.created_question(list_newques);
			}
			
			int[] results = pst.executeBatch(); // thức thi các câu lệnh cùng 1 lúc
			 
			// Kiểm tra xem có câu lệnh nào không thành công
	        for (int result : results) {
	            if (result == PreparedStatement.EXECUTE_FAILED) {
	                return false; // Nếu có bất kỳ câu lệnh nào không thành công, trả về false
	            }
	        }
	        return true; // nếu thành công tất cả
			
		} catch (Exception e) {
			System.out.println("Loi database tạo csdl cap nhat cau hoi: "+ e.getMessage());
			return false;
		}
	}

//	Lấy Thống Kê
	public List<Object> getStatistic(int exam_id) {
		List<Object> statistics = new ArrayList<>();
		DecimalFormat df = new DecimalFormat("0.00");
		
		try {
			String sql = "SELECT COUNT(*) AS NumPerson, MAX(score) AS MaxScore, MIN(score) AS MinScore, AVG(score) AS Score_Average FROM results WHERE exam_id = ?";
			
			PreparedStatement pst = conn.prepareStatement(sql);
			pst.setInt(1, exam_id);
			ResultSet rs = pst.executeQuery();
			
			if (rs.next()) {

                statistics.add(rs.getInt("NumPerson")); // Số người tham gia
                statistics.add(df.format(rs.getFloat("MaxScore")));   // Điểm cao nhất
                statistics.add(df.format(rs.getFloat("MinScore")));   // Điểm thấp nhất
                statistics.add(df.format(rs.getDouble("Score_Average"))); // Điểm trung bình
            }

		} catch (Exception e) {
			System.out.println("Loi thong ke: "+ e.getMessage());
		}
		
		return statistics;
	}
	
}
