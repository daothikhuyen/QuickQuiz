package CallBack;

import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import Model.Exams;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

public class ClientCallbackImpl extends UnicastRemoteObject implements ClientCallback{

	private TableView<Exams> table_ListExam;
	
	public ClientCallbackImpl(TableView<Exams> table) throws RemoteException {
	    super();
	    this.table_ListExam = table;
	}


	public ObservableList<Exams> loadExam(List<Exams> list) {
		ObservableList<Exams> observableList_Exam = FXCollections.observableArrayList();
		
		for (Exams exams : list) {

			Exams exam = new Exams(
					exams.getId(),
			        exams.getSubject(),
			        exams.getClass_room(),
			        exams.getQuantity(),
			        exams.getTotal_time(),
			        exams.getStatus(),
			        exams.getUser_id()
			    );
			
			observableList_Exam.add(exam);
		}
		
		return observableList_Exam;
		
	}

	@Override
	public void notifyNewExam(List<Exams> list) throws RemoteException {
		
		if(table_ListExam != null) {
			 Platform.runLater(() -> {
				 ObservableList<Exams> observableList_Exam = FXCollections.observableArrayList();
					
				 if (table_ListExam != null) {
					 
			    		observableList_Exam = loadExam(list);
			    		
			    		table_ListExam.setItems(observableList_Exam);
				    }
					
			    });
		}else {
			 System.err.println("table_ListExam is null, cannot update.");
		}	
	}
	
	 public void getEndpoint() throws ServerNotActiveException {
		  String clientHost = getClientHost(); // Lấy địa chỉ của client kết nối
		  System.out.println("Client endpoint: " + clientHost);
	   }
	
	

}
