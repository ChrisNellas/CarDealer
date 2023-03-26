package controllers;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.Mnemonic;
import javafx.stage.Stage;
import model.Role;
import model.User;
import util.RetrieveUsers;

public class LoginController {  //extends Controller
	@FXML
	private TextField usernameField, passwordField;
	@FXML
	private Button loginBtn;
	@FXML 
	private Label errorLabel;
	
	private static Set<User> users;
			
	/* get user inputs, clean any useless space and checks if the fields are blank.
	 * 	if one or more fields is/are blank -> show alert message
	 *	otherwise search for a registered user with the same credentials
	 *
	 *	if a user has been found check his role
	 *		if is just a user then show the main menu screen 
	 *		else the adminPanel screen
	 */
	public void loginBtnOnClick(ActionEvent ae) throws IOException {  // check if the db has return the users 		
		String givenName = usernameField.getText().strip();
		String givenPassword = passwordField.getText().strip(); 
		if(givenName.isBlank()||givenPassword.isBlank()) {
			errorLabel.setText("Please enter your credentials to the fields above");
//			Alert emptyFieldsAlert = new Alert(AlertType.ERROR);
//			emptyFieldsAlert.setHeaderText("PLEASE ENTER YOUR CREDENTIALS TO THE FIELDS ABOVE");
//			emptyFieldsAlert.showAndWait();
		} else {
//			boolean found = users.stream().anyMatch(u->(u.username().equals(usernameField.getText())&&u.password().equals(passwordField.getText())));
			Set<User> filteredUsers = users.stream().filter(u->u.username().equals(givenName)&&u.password().equals(givenPassword)).collect(Collectors.toSet());
			if(filteredUsers.size()!=0) {
				User currentUser = filteredUsers.iterator().next();
				Stage stage = (Stage)((Node)ae.getSource()).getScene().getWindow();
				if(currentUser.role().equals(Role.USER)) {
					FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/MainMenu.fxml"));
					Parent root = loader.load();
					MainMenuController mainMenuController = loader.getController();
					mainMenuController.setUser(currentUser);
					stage.setTitle("MAIN MENU");
					stage.setResizable(false);
					Scene scene = new Scene(root);
					stage.setScene(scene);
					stage.show();
					
//					scene.addMnemonic(new Mnemonic(root, KeyCombination.keyCombination("ENTER")));
				} else {
					FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/AdminPanel.fxml"));
					Parent root = loader.load();
					AdminPanelController adminPanelController = loader.getController();
					adminPanelController.setUser(currentUser);
					adminPanelController.setUsers(users);
					adminPanelController.setBarChartAndBrandList();
					stage.setTitle("Admin Panel");
					stage.setResizable(false);
					Scene scene = new Scene(root);
					stage.setScene(scene);
					stage.show();
				}				
			} else { 
				errorLabel.setText("WRONG CREDENTIALS");
			}
		}
	}
	
	public void getUsers() {
		if(users==null) {
			users = new HashSet<>();
			Thread thr = new Thread(new RetrieveUsers(users));
			thr.start();
		}	
	}
}