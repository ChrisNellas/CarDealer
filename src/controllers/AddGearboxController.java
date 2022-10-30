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
import model.Gearbox;
import model.GearboxType;
import model.User;

public class AddGearboxController extends ShortTermController {

	@FXML
	private Button backBtn, createGearboxBtn;
	@FXML
	private TextField nameField, gearsField;
	@FXML
	private ComboBox<GearboxType> typeComboBox;
	
	private Set<Gearbox> gearboxes;
	
//	SETTERS
	public void setGearboxes(Set<Gearbox> gearboxes) {
		this.gearboxes = gearboxes;
	}
	
	public void setGearboxTypes() {
		typeComboBox.getItems().add(GearboxType.AUTOMATIC);
		typeComboBox.getItems().add(GearboxType.MANUAL);
	}
	
//	LISTENERS
	public void backBtnOnClick(ActionEvent ae) throws IOException {
		stage.close();
	}

	/* get user's inputs, clean any useless space and checks if the fields are blank.
	 * 	if one or more fields is/are blank -> show alert message
	 *	otherwise search if this gearbox name is unavailable
	 *		unavailable -> show alert message
	 *		available ->
	 *			gears's field has no numeric char -> show alert message
	 *			else create gearbox record to database and new gearbox object
	*/
	public void createGearboxBtnOnClick(ActionEvent ae) throws IOException {
		String givenName = nameField.getText().strip();
		String givenGears = gearsField.getText().strip();
		GearboxType givenType = typeComboBox.getSelectionModel().getSelectedItem();
		if(!(givenName.isBlank()||givenType==null||givenGears.isBlank())) {
			if(gearboxes.stream().noneMatch(gb->gb.name().equals(givenName))) {
				try{
					insertGearbox(givenName, givenType, Integer.parseInt(givenGears));
					//create new gearbox object with the given data
					Gearbox createdGearbox = new Gearbox(getGearboxId(givenName,Integer.parseInt(givenGears), givenType), givenName, givenType, Integer.parseInt(givenGears));
					
					if(isProperController(InsertGearboxController.class)) { //insertGearboxController!=null
						((InsertGearboxController)previousController).addSelectedGearbox(createdGearbox);
					} else {
						((EditEngineController)previousController).addSelectedGearbox(createdGearbox);
					}
					backBtnOnClick(ae);
				} catch(NumberFormatException nFE) {
					System.out.println("THE PRICE AND GEAR FIELDS MUST BE NUMERIC");
				}
			} else {
				Alert duplicateGearboxNameAlert = new Alert(AlertType.ERROR);
				duplicateGearboxNameAlert.setHeaderText("THIS NAME IS TAKEN");
				duplicateGearboxNameAlert.showAndWait();
			}
		} else {
			Alert emptyFieldsAlert = new Alert(AlertType.ERROR);
			emptyFieldsAlert.setHeaderText("PLEASE FILL THE FIELDS");
			emptyFieldsAlert.showAndWait();
		}
	}
	
	//create new gearbox record in database
	private void insertGearbox(String givenName, GearboxType givenType, int givenGears) {
		DBConnector con = new DBConnector();
		
		con.updateDatabase("""
				INSERT INTO gearbox(name, type, gears) VALUES('%s', '%s', %s)
				""".formatted(givenName, givenType, givenGears));
	}
	
	/* retrieves the freshly inserted gearbox record's id 
	 * 
	 * get all gearboxes (probably only one) that have the same attributes with the last inserted
	 * gearbox record with descending order (the first result is what i searching for)
	 */	
	private Long getGearboxId(String name, int gears, GearboxType type) {
		DBConnector con = new DBConnector();
		con.sendQuery("""
				SELECT id FROM gearbox WHERE name='%s' and gears=%s and type='%s'
				ORDER BY id DESC
				""".formatted(name, gears, type));
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
