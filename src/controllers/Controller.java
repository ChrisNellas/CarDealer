package controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.User;

public abstract sealed class Controller permits LongTermController, ShortTermController{
		
	protected User loggedUser;
	
	protected FXMLLoader loader;
	protected Parent root;
	protected Stage stage;
	protected Scene scene;
		
	protected Controller previousController, nextController;
	
	protected void setUser(User user) {
		loggedUser = user;
	}
	
	protected <T extends Controller> void CreateNewController(T controller) {
		nextController = controller;
	}
}
