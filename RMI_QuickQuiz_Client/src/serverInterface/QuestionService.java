package serverInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import CallBack.ClientCallback;
import Model.Questions;
import Response.Server_Response;

public interface QuestionService extends Remote {

	Server_Response getQuestion_ByExamId(int exam_id) throws RemoteException;
	
	Server_Response save_question(List<Questions> listQuestion) throws RemoteException;
	
	Server_Response update_question(List<Questions> listQuestion) throws RemoteException;

	Server_Response statistic(int exam_id) throws RemoteException;
}
