package CallBack;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import Model.Exams;
import Response.Server_Response;

public interface ClientCallback extends Remote {
	void notifyNewExam(List<Exams> list) throws RemoteException;
	

}
