package controllers;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public non-sealed abstract class ShortTermController extends Controller {

	
	protected void setStage(Stage stage) {
		this.stage = stage;
	}
	
	protected void setController(Controller controller) { //T properController
		this.previousController = controller;
	}
	
// check if the passed controller is instance of an specific controller class
	protected <T> boolean isProperController(T controller) {
//			System.out.println(previousController.getClass());
		System.out.println(controller);
//			System.out.println(previousController.getClass().equals(controller));
		return previousController.getClass().equals(controller);
	}
	
//	NO USE AT ALL	
//		show an error and stop the program
//	protected void wrongControllerPassed(Controller controller) {
//		Alert wrongControllerHasPassedAlert = new Alert(AlertType.ERROR);
//		wrongControllerHasPassedAlert.setHeaderText(String.format("Wrong controller has passed to %s", controller));
//		wrongControllerHasPassedAlert.showAndWait();
//		System.exit(29);
//	}
}
