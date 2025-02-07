package service;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import CallBack.ClientCallback;
import Dao.Dao_Exam;
import Dao.Dao_Question;
import Model.Exams;
import Model.Questions;
import Response.Server_Response;
import serverInterface.QuestionService;

public class QuestionServiceImpl extends UnicastRemoteObject implements QuestionService {

	Dao_Question dao_Question = new Dao_Question();
	
	public List<Questions> list_questions = new ArrayList<>();
	
	
	
	public QuestionServiceImpl() throws RemoteException {

	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Server_Response getQuestion_ByExamId(int exam_id) {
		
		try {
			List<Questions> result = dao_Question.getQuestion_ByExamId(exam_id);
			
			if(result != null && !result.isEmpty()) {
				
				return new Server_Response("GET_QUES_EXAMID","OK", "Lấy dữ liệu thành công", result);
			}
		} catch (Exception e) {
			System.out.println("Loi lay cau hoi theo exam_id: "+ e.getMessage());	
		}		
		return new Server_Response("GET_QUES_EXAMID","ERROR", "Lấy dữ liệu thất bại", null);

	}

	@Override
	public Server_Response save_question(List<Questions> listQuestion) throws RemoteException {
		
		try {
			
			for (Questions ques : listQuestion) {
				Questions question = new Questions(ques.getExam_id(),ques.getQuestion(),ques.getA(),ques.getB(),ques.getC(),ques.getD(),ques.getAnswer());
    			list_questions.add(question);
			}
			
			Boolean result = dao_Question.created_question(listQuestion);
			
			if(result) {
				
				return new Server_Response( "UPDATE_QUESTIONS","OK", "Cập nhật thành công", null);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new Server_Response( "UPDATE_QUESTIONS","ERROR", "Cập nhật thất bại", null);
	}

	@Override
	public Server_Response update_question(List<Questions> listQuestion) throws RemoteException {
		
		try {
			
			for (Questions ques : listQuestion) {
				Questions question = new Questions(ques.getId(), ques.getExam_id(),ques.getQuestion(),ques.getA(),ques.getB(),ques.getC(),ques.getD(),ques.getAnswer());
    			list_questions.add(question);
			}
			
			Boolean result = dao_Question.update_question(listQuestion);
			
			if(result) {
				return new Server_Response( "UPDATE_QUESTIONS","OK", "Cập nhật thành công", null);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new Server_Response( "UPDATE_QUESTIONS","ERROR", "Cập nhật thất bại", null);
		
	}

	@Override
	public Server_Response statistic(int exam_id) throws RemoteException {
		List<Object> list_statistics = dao_Question.getStatistic(exam_id);
		
		if(list_statistics != null) {
	        
	        return new Server_Response("STATISTIC","OK", "Thống Kê Thành Công", list_statistics);
		}
		
		return  new Server_Response<JSONObject>("STATISTIC","ERROR", "Thống Kê Không Được Tìm Thấy", null);
	}

	

}
