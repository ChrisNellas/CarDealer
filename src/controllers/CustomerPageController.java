package controllers;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Customer;
import model.DBConnector;
import model.User;

public class CustomerPageController extends LongTermController{

	@FXML
	private TextField nameField, addressField, deptField;
	@FXML
	private Button backBtn, saveBtn, editPersonalDataBtn;
	@FXML
	private Label idLabel;
	
	private Customer selectedCustomer;
		
	private DBConnector con;
	
//	SETTERS AND GETTERS
	public void getCustomer(Customer customer) {
		selectedCustomer = customer;
		idLabel.setText(selectedCustomer.getId().toString());
		nameField.setText(selectedCustomer.getName());
		addressField.setText(selectedCustomer.getAddress());
		deptField.setText(String.valueOf(selectedCustomer.getDept()));
	}
	
//	LISTENERS
	public void backBtnOnClick(ActionEvent ae) throws IOException {
		loader = new FXMLLoader(getClass().getResource("../view/FindCustomer.fxml"));
		root = loader.load();	
		FindCustomerController findCustomerController = loader.getController();
		findCustomerController.setDestination("CustomerPage.fxml");
		findCustomerController.setUser(loggedUser);
		findCustomerController.populateList();
		scene = new Scene(root);
		stage = (Stage) ((Node) ae.getSource()).getScene().getWindow();
		stage.setTitle("Find customer");
		stage.setScene(scene);
		stage.show();
	}
	
	/* get user inputs, clean any useless space and checks if the fields are blank.
	 * 	if one or more fields is/are blank -> show alert message
	 *	otherwise change customer's data
	 */
	public void saveBtnOnClick(ActionEvent ae) throws IOException {
		String newName = nameField.getText().strip();
		String newAddress = addressField.getText().strip();
		String newDeptText =  deptField.getText().strip();
		if(!(newName.isBlank()||newAddress.isBlank()||newDeptText.isBlank())) {
//			double newDept = Double.parseDouble(newDeptText);
			con = new DBConnector();
			con.updateDatabase("""
					update customer
					set name ='%s', address='%s', dept=%s
					where id =%s
					""".formatted(newName, newAddress, newDeptText, idLabel.getText()));
			
			Alert notification = new Alert(AlertType.INFORMATION);
			notification.setHeaderText("Customer's data change successully");
//			notification.setContentText("");
			notification.show();
		}
	}
//	allow user to change the fields
	public void editPersonalDataBtnOnClick(ActionEvent ae) throws IOException {
		activateFields();
		activateSaveBtn();
	}
	
	public void activateFields() {
		nameField.setDisable(false);
		addressField.setDisable(false);
		deptField.setDisable(false);
	}
	
	public void activateSaveBtn() {
		saveBtn.setDisable(false);
	}
}
