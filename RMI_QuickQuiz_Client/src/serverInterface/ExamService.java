package serverInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;

import CallBack.ClientCallback;
import Model.Exams;
import Model.Results;
import Response.Server_Response;
import controller.Controller_ListOfExam;

public interface ExamService extends Remote {
	
	Server_Response get_list_exam() throws RemoteException;
	
	Server_Response get_exmas_userId(int i) throws RemoteException;
	
	Server_Response created_Exam(Exams exam)throws RemoteException;
	
	Server_Response deleted_Exam(int id) throws RemoteException;
	
	Server_Response update_exam(Exams exam) throws RemoteException;

	Server_Response save_result(Results result) throws RemoteException;
	
	Boolean registerCallback(String clientID,ClientCallback client) throws RemoteException;  // Đăng ký nhận thông báo

	Server_Response unregisterCallback(String hostAddress);


}
