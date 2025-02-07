package server;

import java.net.InetAddress;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import service.ExamServiceImpl;
import service.QuestionServiceImpl;
import service.UserServiceImpl;

public class server {
    public static void main(String[] args) throws RemoteException {
        try {
        	
            // Lấy IP của server
            String currentIP = InetAddress.getLocalHost().getHostAddress();
            System.out.println("Dia chi IP server: " + currentIP);

            // Tạo registry tại cổng 1099
            LocateRegistry.createRegistry(1099);

            // Tạo các object service  
            UserServiceImpl userServer = new UserServiceImpl();
            ExamServiceImpl examServer = new ExamServiceImpl();
            QuestionServiceImpl questionServer = new QuestionServiceImpl();

            // Đăng ký các object vào registry với Naming.rebind (kết hợp với URL)
            Naming.rebind("rmi://" + currentIP + ":1099/QuickQuiz", userServer);
            Naming.rebind("rmi://" + currentIP + ":1099/Exam_QuickQuiz", examServer);
            Naming.rebind("rmi://" + currentIP + ":1099/Question_QuickQuiz", questionServer);

            System.out.println("QuickQuiz is running...");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
