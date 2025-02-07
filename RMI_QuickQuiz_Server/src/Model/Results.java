package Model;

import java.io.Serializable;

public class Results implements Serializable{
	private int id;
	private int exam_id;
	private int user_id;
	private Float score;
	
	
	
	public Results() {
	}

	public Results(int exam_id, int user_id, Float score) {
		this.exam_id = exam_id;
		this.user_id = user_id;
		this.score = score;
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

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public Float getScore() {
		return score;
	}

	public void setScore(Float score) {
		this.score = score;
	}
	
	
	
}
