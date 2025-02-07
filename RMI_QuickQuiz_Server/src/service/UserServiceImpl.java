package service;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import Dao.Dao_User;
import Model.Questions;
import Model.Users;
import Request.Request_Register;
import Response.Server_Response;
import serverInterface.UserService;

public class UserServiceImpl extends UnicastRemoteObject implements UserService {
	
	Dao_User dao_User = new Dao_User();
	
	Server_Response server_Response = null;
	
	SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
	
	public UserServiceImpl() throws RemoteException {
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Boolean register(Request_Register request) throws RemoteException {

		Date utilDate = null;
		try {
			utilDate = dateFormat.parse(request.getBirthday());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		Users user = new Users();
		user.setUsername(request.getUsername());
		user.setEmail(request.getEmail());
		user.setPassword(request.getPassword());
		user.setGender(request.getGender());
		user.setBirthday(new java.sql.Date(utilDate.getTime()));
		
		Boolean checkExist = dao_User.Existence_check(request.getEmail());
		
		if(checkExist) {
			Boolean result = dao_User.createdUser(user);
			return result;
		}
		
		return false;
	}

	@Override
	public Server_Response login(String email, String password) throws RemoteException {
		
		Users result = dao_User.login(email, password);

		if(result != null && result.getEmail() != null) {
			return new Server_Response("LOGIN","OK", "Đăng Nhập Thành Công", result);
		}
		
		return new Server_Response("LOGIN","ERROR", "Kiểm tra lại mật khẩu và email", null);
	}

	@Override
	public Boolean logout(String email) throws RemoteException {
		Boolean exit = null;
		try {
			
			exit =  dao_User.Update_Status(email.toString(),"OFF");
			
		} catch (Exception e) {
			e.printStackTrace();
				
		}
		return exit;
	}

//	Cập nhập avatar
	@Override
	public Server_Response updateUser(Users user) throws RemoteException {
		
		user.setBirthday(new java.sql.Date(user.getBirthday().getTime()));
		
		Users result = dao_User.setUp_Profile(user);
		
		if(result != null) {
			server_Response = new Server_Response("SETUP_PROFILE","OK","Cập nhật tài khoản thành công", result);
		}else {
			server_Response = new Server_Response("SETUP_PROFILE","ERROR","Cập nhật tài khoản khong thành công", null);
		}
		
		return server_Response;
	}

	@Override
	public void registerClient(String clinetName) throws RemoteException {
		System.out.println(clinetName);
		
	}

	@Override
	public void unregisterClient(String clientName) throws RemoteException {
		System.out.println(clientName);
		
	}


	
}
