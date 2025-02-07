package CallBack;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.util.List;

import Model.Exams;
import Response.Server_Response;

public interface ClientCallback extends Remote {
	
//	void callbackExamUserId(List<Exams> list) throws RemoteException;
	
	void notifyNewExam(List<Exams> list) throws RemoteException;
	
	
	
	
}
