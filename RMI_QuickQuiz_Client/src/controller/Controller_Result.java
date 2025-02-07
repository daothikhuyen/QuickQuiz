package controller;

import java.text.DecimalFormat;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Controller_Result {
	
    @FXML
    private AnchorPane root;

    @FXML
    private Label lb_NumCorrect;

    @FXML
    private Label lb_NumIncorrect;

    @FXML
    private Label lb_NumScore;

    @FXML
    private Label lb_NumTimeComplete;

    @FXML
    private Label lb_level;

    @FXML
    void btnExit(ActionEvent event) {
    	Stage primaryStage = (Stage) root.getScene().getWindow();
		primaryStage.close();
    }

	public void loadData(float score, int total_numCorrect, int total_numIncorrect, String time, String rank) {
    	DecimalFormat df = new DecimalFormat("0.00");
    	
		lb_NumCorrect.setText(String.valueOf(total_numCorrect));
		lb_NumIncorrect.setText(String.valueOf(total_numIncorrect));
		lb_level.setText(rank);
		lb_NumScore.setText(df.format(score));
		lb_NumTimeComplete.setText(time);
		
		
	}

}

