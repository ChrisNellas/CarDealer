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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import model.CarBrand;
import model.DBConnector;
import model.Engine;
import model.Gearbox;
import model.GearboxType;
import model.Model;
import model.ModelVersion;
import model.User;

public class EditGearboxController extends LongTermController{
	@FXML
	private Button backBtn, addGearboxBtn, saveGearboxBtn;
	@FXML
	private TextField nameField, gearsField;
	@FXML
	private ComboBox<GearboxType> typeComboBox;
		
	private CarBrand selectedBrand;
	private Model selectedModel;
	private ModelVersion selectedModelVersion;
	private Gearbox selectedGearbox;
	private Set<Engine> engines;
	private Set<Gearbox> otherGearboxes;
	private Engine selectedEngine;
	
//	SETTERS	
	public void setSelectedBrand(CarBrand selectedBrand) {
		this.selectedBrand = selectedBrand;
	}
	
	public void setSelectedModel(Model selectedModel) {
		this.selectedModel = selectedModel;
	}
	
	public void setSelectedModelVersion(ModelVersion selectedModelVersion) {
		this.selectedModelVersion = selectedModelVersion;
	}
	
	public void setSelectedEngine(Engine selectedEngine) {
		this.selectedEngine = selectedEngine;
	}
	
	public void setEngines(Set<Engine> engines) {
		this.engines = engines;  
	}
	
//	set the gearboxes values into fields
	public void setSelectedGearbox(Gearbox selectedGearbox) {
		this.selectedGearbox = selectedGearbox;
		nameField.setText(selectedGearbox.name());
		gearsField.setText(String.valueOf(selectedGearbox.gears()));
		populateGearboxTypeComboBox(selectedGearbox.type());
	}
	
	private void populateGearboxTypeComboBox(GearboxType type) {
		typeComboBox.getItems().add(GearboxType.AUTOMATIC);
		typeComboBox.getItems().add(GearboxType.MANUAL);
		typeComboBox.getSelectionModel().select(type);
	}
	
	/*gets all the gearboxes of the brand and removes the selected gearbox 
	 * The selected gearbox removed in order to be possible to change gearbox attributes 
	 * with the gearbox name unchanged (otherwise duplicateGearboxNameAlert will always be triggered)
	 */
	public void setGearboxes(Set<Gearbox> gearboxes) {
		this.otherGearboxes = gearboxes;
		this.otherGearboxes.remove(selectedGearbox); 
	}
	
//	LISTENERS
	public void backBtnOnClick(ActionEvent ae) throws IOException {
		loader = new FXMLLoader(getClass().getResource("../view/EditEngine.fxml"));
		root = loader.load();
		EditEngineController editEngineController = loader.getController();
		editEngineController.setUser(loggedUser);		
		editEngineController.setSelectedBrand(selectedBrand);
		editEngineController.setSelectedModel(selectedModel);
		editEngineController.setSelectedModelVersion(selectedModelVersion);
		editEngineController.setSelectedEngine(selectedEngine);
		editEngineController.setEngines(engines);
		editEngineController.setGearboxes();
		stage = (Stage) ((Node)ae.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.setTitle("Edit engine");
		stage.show();
	}
	
	/* get user inputs, clean any useless space and checks if the fields are blank.
	 * 	if one or more fields is/are blank -> show alert message
	 *	otherwise search if this gearbox name is unavailable
	 *		unavailable -> show alert message
	 *		available -> apply changes to database
	 */
	public void saveGearboxBtnOnClick(ActionEvent ae) throws IOException {
		String givenName = nameField.getText().strip();
		String givenGears = gearsField.getText().strip();
		GearboxType givenType = typeComboBox.getSelectionModel().getSelectedItem();
		if(!(givenName.isBlank()||givenType==null)) {
			if(otherGearboxes.stream().noneMatch(gb->gb.name().equals(givenName))) {
				saveGearbox(givenName, givenType, Integer.parseInt(givenGears));
				backBtnOnClick(ae);
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
	
	private void saveGearbox(String givenName, GearboxType givenType, int givenGears) {
		DBConnector con = new DBConnector();
		con.updateDatabase("""
				UPDATE gearbox
				SET name='%s', type='%s', gears=%s
				where id=%s
				""".formatted(givenName, givenType, givenGears, selectedGearbox.id()));
	}
}
