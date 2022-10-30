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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import model.CarBrand;
import model.DBConnector;
import model.Model;
import model.User;

public class AddModelController extends ShortTermController{
	
	@FXML
	private Button createModelBtn, backBtn;
	@FXML
	private TextField nameField;
	
	private CarBrand selectedBrand;
	
	
//	SETTERS	
	public void setSelectedBrand(CarBrand selectedBrand) {
		this.selectedBrand = selectedBrand;
	}	
	
//	LISTENERS
	public void backBtnOnClick(ActionEvent ae) throws IOException {
		((EditBrandController) previousController).setSelectedBrand(selectedBrand);
		stage.close();
	}
	
	/* get user's inputs, clean any useless space and checks if the fields are blank.
	 * if one or more fields is/are blank -> show alert message
	 * otherwise search if this model name is unavailable
	 * 		unavailable -> show alert message
	 *		available ->
	 *			create model record to database 
	*/
	public void createModelBtnOnClick(ActionEvent ae) throws IOException {
		String givenName = nameField.getText().strip();
		Set<Model> models = selectedBrand.getModels();
		if(!givenName.isBlank()) {
			if(models.stream().noneMatch(model->model.getName().equals(givenName))) {
				insertModel(givenName, selectedBrand.getId());
				backBtnOnClick(ae);
			} else {
				Alert duplicateModelNameAlert = new Alert(AlertType.ERROR);
				duplicateModelNameAlert.setHeaderText("this model of this brand name already exists in db");
				duplicateModelNameAlert.showAndWait();
			}
		} else {
			Alert emptyFieldsAlert = new Alert(AlertType.ERROR);
			emptyFieldsAlert.setHeaderText("FULFILL ALL THE FIELDS");
			emptyFieldsAlert.showAndWait();
		}
	}
	
	// create new model to database
	private void insertModel(String givenName, Long brandId) {
		DBConnector con = new DBConnector();
		con.updateDatabase("""
				INSERT INTO car_model(model_name, brand_id) VALUES ('%s', %s)
				""".formatted(givenName, brandId));
		//clear the model set of the selected brand in order to get all models again in editBrandController
		selectedBrand.getModels().clear();
	}
}
