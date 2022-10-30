package controllers;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import model.User;

public non-sealed abstract class LongTermController extends Controller {

	@FXML
	protected Label usernameLabel;
	@FXML
	protected Button logoutBtn;
	
//	protected User loggedUser;
	
	protected void setUser(User user) {
		super.setUser(user);
		usernameLabel.setText(loggedUser.username());
	}
	
//	LOGOUT BUTTON LISTENER
	public void logoutBtnOnClick(ActionEvent ae) throws IOException {
		loader = new FXMLLoader(getClass().getResource("../view/LoginPanel.fxml"));
		root = loader.load();
		LoginController loginController = loader.getController();
		loginController.getUsers();
		stage = (Stage) ((Node)ae.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.setTitle("LOGIN");
		stage.show();
	}	
}