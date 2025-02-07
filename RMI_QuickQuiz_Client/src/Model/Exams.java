package Model;

import java.io.Serializable;

import org.json.simple.JSONObject;

import javafx.animation.Timeline;

public class Exams implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private static Exams instance;
	private int id;
	private int user_id;
	private String subject;
	private String class_room;
	private int quantity;
	private String status;
	private int total_time;
	
    private Timeline examTimer;
    private int totalSeconds;
	
	
	public Exams() {
	}

	public Exams(int id, String subject, String class_room, int quantity, int total_time, String status, int user_id) {
		this.id = id;
		this.subject = subject;
		this.class_room = class_room;
		this.quantity = quantity;
		this.total_time = total_time;
		this.status = status;
		this.user_id = user_id;
	}

	public static Exams getInstance() {
        if (instance == null) {
            instance = new Exams();
        }
        return instance;
    }
	
	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public int getUser_id() {
		return user_id;
	}


	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}


	public String getSubject() {
		return subject;
	}


	public void setSubject(String subject) {
		this.subject = subject;
	}


	public String getClass_room() {
		return class_room;
	}


	public void setClass_room(String class_room) {
		this.class_room = class_room;
	}


	public int getQuantity() {
		return quantity;
	}


	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public int getTotal_time() {
		return total_time;
	}


	public void setTotal_time(int total_time) {
		this.total_time = total_time;
	}
	
}
