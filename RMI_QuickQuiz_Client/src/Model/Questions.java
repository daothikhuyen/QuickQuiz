package Model;

import java.io.Serializable;

import org.json.simple.JSONObject;

public class Questions implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public int id;
	public int exam_id;
	public String question;
	public String A;
	public String B;
	public String C;
	public String D;
	public String answer;
	
	
	
	public Questions() {
	}


	public Questions(int id, int exam_id, String question, String A, String B, String C, String D, String answer) {
		this.id = id;
		this.exam_id = exam_id;
		this.question = question;
		this.A = A;
		this.B = B;
		this.C = C;
		this.D = D;
		this.answer = answer;
	}
	
	


	public Questions(int exam_id, String question, String A, String B, String C, String D, String answer) {
		this.exam_id = exam_id;
		this.question = question;
		this.A = A;
		this.B = B;
		this.C = C;
		this.D = D;
		this.answer = answer;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public int getExam_id() {
		return exam_id;
	}


	public void setExam_id(int exam_id) {
		this.exam_id = exam_id;
	}


	public String getQuestion() {
		return question;
	}


	public void setQuestion(String question) {
		this.question = question;
	}


	public String getA() {
		return A;
	}


	public void setA(String a) {
		A = a;
	}


	public String getB() {
		return B;
	}


	public void setB(String b) {
		B = b;
	}


	public String getC() {
		return C;
	}


	public void setC(String c) {
		C = c;
	}


	public String getD() {
		return D;
	}


	public void setD(String d) {
		D = d;
	}


	public String getAnswer() {
		return answer;
	}


	public void setAnswer(String answer) {
		this.answer = answer;
	}
	
	
	
	
}
