package serverInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;

import Model.Users;
import Request.Request_Register;
import Response.Server_Response;

public interface UserService extends Remote {
	
	void registerClient(String clinetName) throws RemoteException;
	
	void unregisterClient(String clientName) throws RemoteException;
	
	Boolean register(Request_Register users) throws RemoteException;
	
	Boolean logout(String email) throws RemoteException;
	
	Server_Response login(String email, String password) throws RemoteException;
	
	Server_Response updateUser(Users user) throws RemoteException;

}
