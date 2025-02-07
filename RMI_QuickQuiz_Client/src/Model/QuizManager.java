package Model;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import serverInterface.ExamService;
import serverInterface.QuestionService;
import serverInterface.UserService;

public class QuizManager {
	
	private static UserService quizService;
	private static ExamService quizExam ;
	private static QuestionService quizQuestion;
	
	
	public static UserService getQuizService() {
		return quizService;
	}
	
	public static void setQuizService(UserService quizService) {
		QuizManager.quizService = quizService;
	}
	
	public static ExamService getQuizExam() {
		return quizExam;
	}
	
	public static void setQuizExam(ExamService quizExam) {
		QuizManager.quizExam = quizExam;
	}
	
	public static QuestionService getQuizQuestion() {
		return quizQuestion;
	}
	
	public static void setQuizQuestion(QuestionService quizQuestion) {
		QuizManager.quizQuestion = quizQuestion;
	}
	
	
	
	
	


}
