package controllers;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Set;
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
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import model.DBConnector;
import model.Engine;
import model.ModelVersion;
import model.Type;
import model.User;

/* CHECKED 
 * 
 * logoutBtn
 * backBtn
 * createEngineBtn
 */
public class AddEngineController extends ShortTermController{
	@FXML
	private Button backBtn, createEngineBtn;
	@FXML
	private TextField nameField, powerField;
	@FXML 
	private ComboBox<Type> typeComboBox;
	
	private ModelVersion selectedModelVersion;
	private Set<Engine> engines;
	
	private Engine createdEngine;
	
	
//	GETERS & SETTERS
	public void setSelectedModelVersion(ModelVersion selectedModelVersion) {
		this.selectedModelVersion = selectedModelVersion;
	}
	
	public void setEngines(Set<Engine> engines) {
		this.engines = engines;  
	}
	
	public void setEngineTypes() {
		typeComboBox.getItems().add(Type.DIESEL);
		typeComboBox.getItems().add(Type.ELECTRIC);
		typeComboBox.getItems().add(Type.HYDROGEN);
		typeComboBox.getItems().add(Type.HYBRID);
		typeComboBox.getItems().add(Type.LPG);
		typeComboBox.getItems().add(Type.PETROL);
		typeComboBox.getItems().add(Type.PLUG_IN_HYBRID);
	}

//	LISTENERS
	public void backBtnOnClick(ActionEvent ae) throws IOException {
		stage.close();
	}
		
	/* get user's inputs, clean any useless space and checks if the fields are blank.
	 * if one or more fields is/are blank -> show alert message
	 * Otherwise search if this engine name is unavailable
	 * 	unavailable -> show alert message
	 * 	available ->
	 *		power's field has no numeric char -> show alert message
	 *		else create engine record to database and new engine object
	 *
	 *If new engine record has been added to db then open new window (Insert gearbox)
	*/	
	public void createEngineBtnOnClick(ActionEvent ae) throws IOException {
		String givenName = nameField.getText().strip();
		String givenPower = powerField.getText().strip();
		Type givenType = typeComboBox.getSelectionModel().getSelectedItem();
		if(!(givenName.isBlank()||givenPower.isBlank()||givenType==null)) {
			if(engines.stream().noneMatch(ngn->ngn.getName().equals(givenName))) {
				insertNewEngine(givenName, Integer.parseInt(givenPower), givenType);
				try {
					//create new engine object with the given data
					createdEngine = new Engine(
							getEngineId(givenName,Integer.parseInt(givenPower), givenType), 
							givenName, 
							Integer.parseInt(givenPower), 
							givenType
							);
//					update the editModelVersionController's list in order to get the new engine 
					((EditModelVersionController)previousController).getAvailableEngines(selectedModelVersion); // maybe useless cause no ePWG record is related to the new engine
//					shows the InsertGearbox screen to create new record(s) to ePWG table
					((EditModelVersionController)previousController).goToInsertGearbox(createdEngine);
					backBtnOnClick(ae);
				} catch (NumberFormatException e) { // if givenPower field has a no numeric character
					Alert powerFieldNotNumber = new Alert(AlertType.ERROR);
					powerFieldNotNumber.setHeaderText("POWER FIELD MUST CONTAIN AN INTEGER");
					powerFieldNotNumber.showAndWait();
				}
			} else {
				Alert duplicateEngineAlert = new Alert(AlertType.ERROR);
				duplicateEngineAlert.setHeaderText("AN EXISTING ENGINE HAS THIS NAME.");
				duplicateEngineAlert.showAndWait();
			}
		} else {
			Alert emptyFieldsAlert = new Alert(AlertType.ERROR);
			emptyFieldsAlert.setHeaderText("PLEASE FILL ALL THE FIELDS");
			emptyFieldsAlert.showAndWait();
		}	
	}
	
//	create new engine record in database
	private void insertNewEngine(String givenName, int givenPower, Type givenType) {
		DBConnector con = new DBConnector(); //GIVEN POWER HAS %s and givenPower is int SOSOSOSOSOSOSOS
		con.updateDatabase("""
				INSERT INTO engine(name, power, type) VALUES('%s', %s, '%s') 
				""".formatted(givenName, givenPower, givenType));
	}
	
	/* retrieves the freshly inserted engine record's id
	 * 
	 * get all engines (probably only one) that have the same attributes with the last inserted 
	 * engine record with descending order (the first result is what i searching for)
	*/
	private Long getEngineId(String name, int power, Type type) {
		DBConnector con = new DBConnector();
		con.sendQuery("""
				SELECT id FROM engine where name='%s' and power=%s and type='%s' 
				ORDER BY id DESC
				""".formatted(name, power, type));
		Optional<ResultSet> optRs = con.getResults();
		if(optRs.isPresent()) {
			ResultSet rs = optRs.get();
			try {
				if(rs.next()) {
					return rs.getLong("id");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return -1L;
	}
}
