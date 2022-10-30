package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import model.DBConnector;
import model.Engine;
import model.Gearbox;
import model.ModelVersion;

public class InsertPriceController extends ShortTermController{
	
	@FXML
	private TextField priceField;
	@FXML
	private Button cancelBtn, insertPriceBtn;
	@FXML 
	private Label modelVersionNameLabel, engineNameLabel, gearboxNameLabel;
		
	private ModelVersion selectedModelVersion;
	private Engine selectedEngine;
	private Gearbox selectedGearbox;

	
//	SETTERS
	public void setSelectedModelVersion(ModelVersion modelVersion) {
		selectedModelVersion = modelVersion;
		modelVersionNameLabel.setText(selectedModelVersion.getName());
	}
	
	public void setSelectedEngine(Engine engine) {
		selectedEngine = engine;
		engineNameLabel.setText(selectedEngine.getName());
	}
	
	public void setSelectedGearbox(Gearbox gearbox) {
		selectedGearbox = gearbox;
		gearboxNameLabel.setText(selectedGearbox.name());
	}
	
//	LISTENERS
	public void cancelBtnOnClick(ActionEvent ae) {
		stage.close();
	}

	/* get user inputs, clean any useless space and checks if the fields are blank.
	 * 	if one or more fields is/are blank -> show alert message
	 *	otherwise insert the combination engine gearbox and modelversion to database
	 *		if user's input is not numeric then show alert message 
	 */
	public void insertPriceBtnOnClick(ActionEvent ae) {
		String givenPriceStr = priceField.getText().strip();
		if(givenPriceStr.isBlank()) {
			Alert noPriceFoundAlert = new Alert(AlertType.ERROR);
			noPriceFoundAlert.setHeaderText("PLEASE GIVE THE PRICE OF THIS COMPINATION OF ENGINE, GEARBOX AND MODEL VERSION");
			noPriceFoundAlert.showAndWait();
			return ;
		}
		try {
			double givenPrice = Double.parseDouble(givenPriceStr);
			insertToDB(givenPrice);
			if(isProperController(InsertGearboxController.class)) { //insertGearboxController!=null
				((InsertGearboxController)previousController).setBrandsGearboxes();
				((InsertGearboxController)previousController).setSelectedGearboxes();
			} else { //editGearbox
				((EditEngineController)previousController).setGearboxes();
			}
			stage.close();
		} catch (NumberFormatException nFE) {
			Alert noNumberInputAlert = new Alert(AlertType.ERROR);
			noNumberInputAlert.setHeaderText("PRICE FIELD MUST CONTAIN A NUMBER");
			noNumberInputAlert.showAndWait();
		}
	}
	
//	create a record to engineProvidedWithGearbox table
	private void insertToDB(double price) {
//		System.out.println("engine "+ selectedEngine.getId()+" gearbox "+ selectedGearbox.id()+" model version "+ selectedModelVersion.getId());
		DBConnector con = new DBConnector();
		con.updateDatabase("""
				INSERT INTO engine_provided_with_gearbox(engine_id, gearbox_id, model_version_id, price) 
				VALUES(%s,%s,%s,%s)
				""".formatted(selectedEngine.getId().toString(), selectedGearbox.id().toString(), selectedModelVersion.getId().toString(), String.valueOf(price)));	
	}
}
