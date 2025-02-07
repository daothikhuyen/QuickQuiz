package service;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import CallBack.ClientCallback;
import Dao.Dao_Exam;
import Dao.Dao_Question;
import Model.Exams;
import Model.Questions;
import Model.Results;
import Response.Server_Response;
import serverInterface.ExamService;

public class ExamServiceImpl extends UnicastRemoteObject implements ExamService {

	Dao_Exam dao_Exam = new Dao_Exam();
	
	Server_Response server_Response = null;
	
	private Map<String, ClientCallback> callbackSet;
	
	public ExamServiceImpl() throws RemoteException {
		callbackSet = new HashMap<>();
		
	}


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Server_Response get_exmas_userId(int userId) throws RemoteException {
		try {
			
			List<Exams> result = dao_Exam.getExams_UserId(userId);
			
			if(result != null && !result.isEmpty()) {

				server_Response =  new Server_Response("GET_EXAMS_ID","OK", "Lấy dữ liệu thành công", result);
			}else {
				server_Response = new Server_Response("GET_EXAMS_ID","ERROR", "Lấy dữ liệu thất bại", null);
			}
		} catch (Exception e) {
			System.out.println("loi get_exmas_userId: "+ e.getMessage());
		}
		
		
		
		return server_Response;
	}

	@Override
	public Server_Response created_Exam(Exams exam) throws RemoteException {
		
		Exams result = dao_Exam.created_exam(exam);
		
		if (result != null) {
			notifyAllClients(dao_Exam.get_listExam());
			return new Server_Response("CREATE_EXAM","OK", "Tạo bài thi thành công", result);
		}
		
		return new Server_Response("CREATE_EXAM","ERROR", "Tạo bài thi thất bại", null);
	}


	@Override
	public Server_Response deleted_Exam(int id) throws RemoteException {
		
		Boolean result =  dao_Exam.delete_Exam(id);
		
		if (result) {
			notifyAllClients(dao_Exam.get_listExam());
			return new Server_Response("DELETE_EXAM","OK", "Xoá dữ liệu thành công", null);
		}
		
		return new Server_Response("DELETE_EXAM","ERROR", "Xoá dữ liệu thất bại", null);
		
	}

	@Override
	public Server_Response update_exam(Exams exam) throws RemoteException {
		
		try {
			
			Boolean check_PersonJoin = dao_Exam.getPerson_JoinExam(exam.getId());
			
			if(check_PersonJoin) {
				server_Response =  new Server_Response("UPDATE_EXAM","ERROR", "Đề thi của bạn đã có thí sinh tham gia , xin vui lòng không chỉnh sửa!", null);
			}else {
				Exams update_exam = dao_Exam.update_exam(exam);
				
				if(update_exam != null) {
					Dao_Question dao_Question = new Dao_Question();
					List<Questions> result = dao_Question.getQuestion_ByExamId(exam.getId());
					
					if(result != null && !result.isEmpty()) {
						
						HashMap<String, Object> capitalCities = new HashMap<String, Object>();
						capitalCities.put("exam", update_exam);
						capitalCities.put("questions", result);
						
						server_Response = new Server_Response("UPDATE_EXAM","OK", "Lấy dữ liệu thành công", capitalCities);
					}else {
						server_Response = new Server_Response("UPDATE_EXAM","NO_QUESTION", "Chưa có dữ liệu các câu hỏi", update_exam);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Loi created exam: "+ e.getMessage());
		}
		return server_Response;
	}

	@Override
	public Server_Response get_list_exam() throws RemoteException {
		
		try {
			
			List<Exams> result = dao_Exam.get_listExam();
			
			if(result != null && !result.isEmpty()) {
				
				return new Server_Response("GET_EXAMS_ID","OK", "Lấy dữ liệu thành công", result);
			}
			
		} catch (Exception e) {
			System.out.println("loi get_listExam: "+ e.getMessage());
		}
		
		return  new Server_Response("GET_EXAMS_ID","ERROR", "Lấy dữ liệu thất bại", null);
		
	}
	
	public Server_Response save_result(Results result) throws RemoteException {
		
		String rank = dao_Exam.save_result_exam(result);
		
		if (rank!= null) {
			
			server_Response =  new Server_Response("SAVE_RESULT_EXAM","OK", rank, null);
		}else {
			server_Response =  new Server_Response("SAVE_RESULT_EXAM","ERROR", "Lưu kết quả thất bại", null);
		}

		return server_Response;
	}
	
//	Callback

	@Override
	public Boolean registerCallback(String clientID, ClientCallback client) throws RemoteException {
		 
		 if (!callbackSet.containsKey(clientID)) {
			 callbackSet.put(clientID, client);
	            return true;
	      }
		 
		 return false;
		
	}

	@Override
	public Server_Response unregisterCallback(String clientID) throws RemoteException {

		if(callbackSet.containsKey(clientID)) {
			 // Lấy danh sách các ClientCallback từ callbackSet
			
			List<ClientCallback> callbacksToReturn = new ArrayList<>();
		    
		    if (callbackSet.containsKey(clientID)) {
		    	
		        ClientCallback clientCallback = callbackSet.get(clientID);
		        
		        callbackSet.remove(clientID);

		        // Thêm vào danh sách callbacksToReturn
		        callbacksToReturn.add(clientCallback);

		        return new Server_Response("remove unregisterCallback", "OK", "Xoá thành công", callbacksToReturn);
		    }
		}
		return new Server_Response("remove unregisterCallback", "ERROR", "Xoá thất bại", null);
		
	}
	
	 private void notifyAllClients(List<Exams> list) {
		 
	        for (ClientCallback client : callbackSet.values()) {
	            try {
	                client.notifyNewExam(list); // Gọi callback trên từng client
	            } catch (RemoteException e) {
	                e.printStackTrace();
	            }
	        }
	    }

	


}
