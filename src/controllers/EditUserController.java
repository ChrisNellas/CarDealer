// why after a user has been changed to retrive all users from db 
// and not update locally the list and update the db

package controllers;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Stream;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import model.DBConnector;
import model.Role;
import model.User;
import util.RetrieveUsers;

public class EditUserController extends LongTermController{
	@FXML
	private Button backBtn, saveBtn;
	@FXML
	private TextField nameField, usernameField;
	@FXML
	private PasswordField passwordField, confPasswordField;
	@FXML
	private ComboBox<Role> roleComboBox;
	
	private User selectedUser;
	private String givenName, givenUsername, givenPassword, givenConfPassword;
	private Role givenRole;
	private boolean updateExists;
	private Set<User> users;
	
	private Stream<User> otherUsersStream;
	
//	SETTERS
	public void setUsers(Set<User> users) {
		this.users = users;
	}
	
//	always call this AFTER the setUsers method (in order to init the users)
	public void setSelectedUser(User selecteduser) {
		this.selectedUser = selecteduser;
		displayUserData();
		updateExists=false;
		otherUsersStream = users.stream().filter(user->!user.username().equals(selecteduser.username()));
	}
	
	public void populateRoleComboBox() {
		roleComboBox.getItems().add(Role.ADMIN);
		roleComboBox.getItems().add(Role.USER);
	}
	
//	set the user's data into fields
	public void displayUserData() {
		nameField.setText(selectedUser.name());
		usernameField.setText(selectedUser.username());
		nameField.setText(selectedUser.name());
		nameField.setText(selectedUser.name());
//		select user's assigned role
		roleComboBox.getSelectionModel().select(selectedUser.role()); 
	}
	
//	LISTENERS
	/* get user inputs, clean any useless space and checks if the fields are blank.
	 * 	if one or more fields is/are blank -> show alert message
	 *	otherwise search if this username is unavailable
	 *		unavailable -> show alert message
	 *		available -> apply changes to database 
	 */
	public void saveBtnOnClick(ActionEvent ae) throws IOException {
		givenName = nameField.getText().strip();
		givenUsername = usernameField.getText().strip();
		givenPassword = passwordField.getText().strip();
		givenConfPassword = confPasswordField.getText().strip();
		givenRole = roleComboBox.getSelectionModel().getSelectedItem();
	
		if(!(givenName.isBlank()||givenUsername.isBlank())) {
//			search if he givenusername is already taken from other user 
//			(the stream does not contain the selected user look at setSelecteduser method)
			if(otherUsersStream.noneMatch(u->givenUsername.equals(u.username()))) {
				if(givenPassword.equals(givenConfPassword)) {
					updateUser();
					backBtnOnClick(ae);
				} else { // 2 password differ
					Alert noMatchingPasswordFieldsAlert = new Alert(AlertType.ERROR);
					noMatchingPasswordFieldsAlert.setHeaderText("password fields does not matching");
					noMatchingPasswordFieldsAlert.showAndWait();
				}
			} else { // username not available
				Alert emptyFieldsAlert = new Alert(AlertType.ERROR);
				emptyFieldsAlert.setHeaderText("this username is not available");
				emptyFieldsAlert.showAndWait();
			}
		} else { // empty field found
			Alert emptyFieldAlert = new Alert(AlertType.ERROR);
			emptyFieldAlert.setHeaderText("FULFILL ALL THE FIELDS FIRST");
			emptyFieldAlert.showAndWait();
		}
	}
	
	public void backBtnOnClick(ActionEvent ae) throws IOException {
		loader = new FXMLLoader(getClass().getResource("../view/AdminPanel.fxml"));
		root = loader.load();	
		AdminPanelController adminPanelController = loader.getController();
		adminPanelController.setUser(loggedUser);
		adminPanelController.setBarChartAndBrandList();
		if(updateExists) { //
			users.clear();
			Thread userThread = new Thread(new RetrieveUsers(this.users));
			userThread.start();
			try {
				userThread.join();
				adminPanelController.setUsers(users);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}			
		} else {
			adminPanelController.setUsers(users);
		}
		scene = new Scene(root);
		stage = (Stage) ((Node)ae.getSource()).getScene().getWindow();
		stage.setTitle("Admin panel");
		stage.setScene(scene);
		stage.show();
	}
	
	public void updateUser() {
		DBConnector con = new DBConnector();
		String query = """
				UPDATE users
				set name='%s', username='%s', role='%s' %s
				where id=%s
				""".formatted(givenName, givenUsername, givenRole, 
						givenPassword.isBlank()? "": String.format(", password='%s'", givenPassword),
								selectedUser.id());
//		System.out.println(query);
		con.updateDatabase(query);
		updateExists=true; 
	}
}
