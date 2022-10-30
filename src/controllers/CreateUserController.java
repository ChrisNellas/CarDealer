 package controllers;

import java.io.IOException;
import java.util.Set;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.DBConnector;
import model.Role;
import model.User;

public class CreateUserController extends ShortTermController{
	
	@FXML
	private Label usernameLabel;
	@FXML
	private Button backBtn, createUserBtn;
	@FXML
	private CheckBox adminCB;
	@FXML
	private TextField nameField, usernameField;
	@FXML
	private PasswordField passwordField, confPasswordField;
	
	private String givenName, givenUsername, givenPassword, givenConfPassword;
	
	private Role givenRole;
	
	private Set<User> users;

//	SETTERS	
	public void setUsers(Set<User> users) {
		this.users = users;
	}
	
//	LISTENERS
	public void backBtnOnClick(ActionEvent ae) throws IOException {

		stage.close();
	}
	
	/* get user inputs, clean any useless space and checks if the fields are blank.
	 * 	if one or more fields is/are blank -> show alert message
	 *	otherwise search if this username is unavailable
	 *		unavailable -> show alert message
	 *		available -> create new user to database
	 */
	public void createUserBtnOnClick(ActionEvent ae) throws IOException {
		givenName = nameField.getText().strip();
		givenUsername = usernameField.getText().strip();
		givenPassword = passwordField.getText().strip();
		givenConfPassword = confPasswordField.getText().strip();
		
		if(adminCB.isSelected()) {
			givenRole = Role.ADMIN;
		} 
		else {
			givenRole = Role.USER;
		}
		if(!(givenName.isBlank()||givenUsername.isBlank()||givenPassword.isBlank()||givenConfPassword.isBlank())) {
			if(users.stream().noneMatch(u->givenUsername.equals(u.username()))) {
				if(!givenConfPassword.equals(givenPassword)) {// password and confPassword does not match
//					System.out.println("password and confPassword does not match");
					Alert noMatchingPasswordsAlert = new Alert(AlertType.ERROR);
					noMatchingPasswordsAlert.setHeaderText("PASSWORD AND CONFPASSWORD DOES NOT MATCH");
					noMatchingPasswordsAlert.showAndWait();
				} 
				createUser();
				backBtnOnClick(ae);
			} else { //username reserved
//				System.out.println("username reserved");
				Alert reservedUsernameAlert = new Alert(AlertType.ERROR);
				reservedUsernameAlert.setHeaderText("USERNAME RESERVED");
				reservedUsernameAlert.showAndWait();
			}			
		} else {
//			System.out.println("PLEASE FULFILL ALL THE FIELDS");
			Alert emptyFieldsAlert = new Alert(AlertType.ERROR);
			emptyFieldsAlert.setHeaderText("PLEASE FULFILL ALL THE FIELDS");
			emptyFieldsAlert.showAndWait();
		}
	}
	// create new user into db
	private void createUser() {
		DBConnector con = new DBConnector();
		con.updateDatabase("""
				INSERT INTO users (name,username,password,role) values('%s','%s','%s','%s')
				""".formatted(givenName, givenUsername, givenPassword,givenRole));
	}
}