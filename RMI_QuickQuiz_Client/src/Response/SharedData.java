package Response;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import Model.Exams;
import Model.Questions;

public class SharedData {
	
	private static SharedData instance;
	
	private List<Questions> data;  
	
	public static SharedData getInstance() {
	    if (instance == null) {
	        instance = new SharedData();
	    }
	    return instance;
	}

	public List<Questions> getData() {
		return data;
	}

	public void setData(List<Questions> data) {
		this.data = data;
	}
	
	public void listData(List<Questions> result, String string) {

		data = new ArrayList<>();
		
		for (Questions question : result) {
			
			Questions questions = new Questions(
					question.getId(),
					question.getExam_id(),
					question.getQuestion(),
					question.getA(),
					question.getB(),
					question.getC(),
					question.getD(),
					question.getAnswer());
			data.add(questions);
		}
		
		
	}

}
