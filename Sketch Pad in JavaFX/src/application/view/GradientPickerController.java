package application.view;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


public class GradientPickerController {
	@FXML
	private ColorPicker FirstColor;
	@FXML
	private ColorPicker SecondColor;
	@FXML
	private Button Set;
	

	static Color firstcolor;
	static Color secondcolor;
	
	@FXML
	private void set() throws IOException{
	    Stage existingWindow =(Stage) Set.getScene().getWindow();
		firstcolor=FirstColor.getValue();
		secondcolor=SecondColor.getValue();
		existingWindow.close();
	}
	
	
	public static Color getfirst(){
		return firstcolor;
	}
	public static Color getsecond(){
		return secondcolor;
	}
	public GradientPickerController(){
		
	}
}
