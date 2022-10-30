package controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Set;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import model.Customer;
import model.DBConnector;
import model.User;

public class CreateCustomerController extends ShortTermController{
	
	@FXML
	private TextField nameField;
	@FXML
	private TextField addressField;
	@FXML
	private Button backBtn, createBtn;
	
	private boolean newCustomerCreated = false;
	
	private Set<Customer> existedCustomers;

//	SETTERS
	public void setCustomers(Set<Customer> customers) {
		this.existedCustomers = customers;
	}
	
//	LISTENERS
	public void backBtnOnClick(ActionEvent ae) throws IOException {
		if(newCustomerCreated) {
			((FindCustomerController) previousController).populateList();
		} else {//no need to retrieve all users from db
			((FindCustomerController) previousController).populateList(existedCustomers);
		}
		stage.close();
	}
	
	/* get user inputs, clean any useless space and checks if the fields are blank.
	 * 	if one or more fields is/are blank -> show alert message
	 *	otherwise add customer
	 */
	public void createBtnOnClick(ActionEvent ae) throws IOException {
		String givenName = nameField.getText().strip();
		String givenAddress = addressField.getText().strip();
		
		if(!(givenName.isBlank()||givenAddress.isBlank())) {
			addCustomer(givenName, givenAddress);
			//to go back to find customer screen
			backBtnOnClick(ae);
		} else {
			Alert emptyFieldFoundAlert = new Alert(AlertType.ERROR);
			emptyFieldFoundAlert.setHeaderText("FULLFILL ALL THE FIELDS FIRST");
			emptyFieldFoundAlert.showAndWait();
		}
	}
	
	private void addCustomer(String givenName, String givenAddress) {
		DBConnector con = new DBConnector();
		String x = String.format("INSERT INTO customer(name, address) values('%s', '%s')",givenName, givenAddress);
//		System.out.println(x);
		con.updateDatabase(x);
		
		newCustomerCreated = true;
	}
}
