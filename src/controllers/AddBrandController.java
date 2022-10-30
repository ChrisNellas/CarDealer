package controllers;

import java.io.IOException;
import java.util.Set;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import model.CarBrand;
import model.DBConnector;
import model.User;

public class AddBrandController extends ShortTermController{
	@FXML
	private Button backBtn, createBrandBtn;
	@FXML 
	private TextField nameField, logoField;
	
	private Set<User> users;
	private Set<CarBrand> brands;
	
	private boolean newBrandCreated = false;
		
	//SETTERS		
	public void setUsers(Set<User> users) {
		this.users = users;
	}
	
	public void setBrands(Set<CarBrand> brands) {
		this.brands = brands;
	}
		
//	LISTENERS
	
//	check if a new brand has created (get data from db) or just return the brands that have been passed to it
	public void backBtnOnClick(ActionEvent ae) throws IOException {
		if(newBrandCreated) {
			((AdminPanelController) previousController).setBarChartAndBrandList();
		} else {
			((AdminPanelController) previousController).setBarChartAndBrandList(brands);
		}
		stage.close();
	}
	
	/*	get user's inputs, clean any useless space and checks if the fields are blank.
	 *		if one or more fields is/are blank -> show alert message
	 *		otherwise search if this brand name is unavailable
	 *			unavailable -> show alert message
	 *			available -> create new brand to database
	 */
	public void createBrandBtnOnClick(ActionEvent ae) throws IOException {	
		String givenBrandName = nameField.getText().strip();
		String givenBrandLogoURL = logoField.getText().strip();
		
		if(!(givenBrandName.isBlank()||givenBrandLogoURL.isBlank())) {
			if(brands.stream().noneMatch((brand)->brand.getName().equals(givenBrandName))){ // if this brand name isn't taken
				addBrand(givenBrandName, givenBrandLogoURL);
				backBtnOnClick(ae);
			} else { // brand name taken
				Alert duplicateBrandAlert = new Alert(AlertType.ERROR);
				duplicateBrandAlert.setHeaderText("THIS brand name already exists in db");
				duplicateBrandAlert.showAndWait();
			}
		} else { // empty fields
			Alert emptyFieldsAlert = new Alert(AlertType.ERROR);
			emptyFieldsAlert.setHeaderText("FULFILL ALL THE FIELDS");
			emptyFieldsAlert.showAndWait();
		}
	}
	
//	insert brand record to db with the given values
	private void addBrand(String givenName, String givenLogo) {
		DBConnector con = new DBConnector();
		con.updateDatabase("""
				INSERT INTO brands (name, logo) VALUES('%s', '%s')
		""".formatted(givenName, givenLogo));
		newBrandCreated = true;
	} 
}
