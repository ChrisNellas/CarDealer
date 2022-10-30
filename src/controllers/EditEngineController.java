package controllers;

import java.io.IOException;
import java.util.HashSet;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.CarBrand;
import model.DBConnector;
import model.Engine;
import model.Gearbox;
import model.Model;
import model.ModelVersion;
import model.Type;
import model.User;
import util.RetrieveGearboxes;

/*
 * CHECKED
 * saveBtn
 * backBtn
 * logoutBtn
 * addGearboxBtn
 * removeGearboxBtn
 * addNewGearboxBtn
 *  
 * NOT DONE
 * editGearboxBtn
 */

public class EditEngineController extends LongTermController{

	@FXML
	private Button backBtn, saveBtn, addGearboxBtn, removeGearboxBtn, editGearboxBtn, addNewGearboxBtn;
	@FXML
	private TextField nameField, powerField;
	@FXML 
	private ListView<Gearbox> availableGearboxList, manufacturerGearboxList;
	@FXML 
	private ComboBox<Type> typeComboBox;
		
	private CarBrand selectedBrand;
	private Model selectedModel;
	private ModelVersion selectedModelVersion;
	private Engine selectedEngine;
	private Set<Engine> engines;
	private Set<Gearbox> brandsGearboxes, availableGearboxes;
	
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
	
//	set engine's data into fields
	public void setSelectedEngine(Engine selectedEngine) {
		this.selectedEngine = selectedEngine;	
		nameField.setText(selectedEngine.getName());
		powerField.setText(String.valueOf(selectedEngine.getPower()));
		populateTypeComboBox(selectedEngine.getType());
		this.selectedEngine.getAvailableGearboxes().forEach((gb)->{
			availableGearboxList.getItems().add(gb);
		});
	}
	
	public void populateTypeComboBox(Type selectedType) {
		typeComboBox.getItems().add(Type.DIESEL);
		typeComboBox.getItems().add(Type.ELECTRIC);
		typeComboBox.getItems().add(Type.HYBRID);
		typeComboBox.getItems().add(Type.HYDROGEN);
		typeComboBox.getItems().add(Type.LPG);
		typeComboBox.getItems().add(Type.LPG);
		typeComboBox.getItems().add(Type.PETROL);
		typeComboBox.getItems().add(Type.PLUG_IN_HYBRID);

		typeComboBox.getSelectionModel().select(selectedType);		
	}
	
	public void setEngines(Set<Engine> engines) {
		this.engines = engines;
	}
	
//	retrieve gearboxes from database
	public void setGearboxes() { 
		brandsGearboxes = new HashSet<>();
		availableGearboxes = new HashSet<>();
//		gets the available gearboxes for this model version and engine
		Thread availableGearboxThread = new Thread(new RetrieveGearboxes(availableGearboxes, selectedModelVersion.getId(), selectedEngine.getId()));
		availableGearboxThread.start();
//		gets all the gearboxes of this brand
		Thread brandsGearboxThread = new Thread(new RetrieveGearboxes(brandsGearboxes, selectedBrand.getId()));
		brandsGearboxThread.start();
		availableGearboxList.getItems().clear();
		manufacturerGearboxList.getItems().clear();
		try {
			availableGearboxThread.join();
//			populate the available list 
			availableGearboxList.getItems().addAll(availableGearboxes);
//			save the gearboxes into gearbox set at engine's object 
			availableGearboxes.forEach(gearbox->selectedEngine.addGearbox(gearbox));
			brandsGearboxThread.join();
//			populate the manufacturer list 
			manufacturerGearboxList.getItems().addAll(brandsGearboxes);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
/* pop a screen that request from user to insert the price of the 
 * specific combination of engine gearbox and model version.
 * 
 * Does NOT insert the selected gearbox into available gearboxes list
 */
	public void addSelectedGearbox(Gearbox selectedGearbox) {
		loader = new FXMLLoader(getClass().getResource("../view/InsertPrice.fxml"));
		try {
			root = loader.load();
			InsertPriceController insertPriceController = loader.getController();
			insertPriceController.setSelectedModelVersion(selectedModelVersion);
			insertPriceController.setSelectedEngine(selectedEngine);
			insertPriceController.setSelectedGearbox(selectedGearbox);
			Stage insertPriceStage = new Stage();
			insertPriceStage.initModality(Modality.APPLICATION_MODAL);
			insertPriceController.setStage(insertPriceStage);
			insertPriceController.setController(this);
			scene = new Scene(root);
			insertPriceStage.setScene(scene);
			insertPriceStage.setTitle("INSERT PRICE");
			insertPriceStage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
//	LISTENERS
	public void backBtnOnClick(ActionEvent ae) throws IOException {
		loader = new FXMLLoader(getClass().getResource("../view/EditModelVersion.fxml"));
		root = loader.load();
		EditModelVersionController editModelVersionController = loader.getController();
		editModelVersionController.setUser(loggedUser);		
		editModelVersionController.setSelectedBrand(selectedBrand);
		editModelVersionController.setSelectedModel(selectedModel);
		editModelVersionController.setSelectedModelVersion(selectedModelVersion);
		editModelVersionController.getAvailableEngines(selectedModelVersion);
		stage = (Stage) ((Node)ae.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.setTitle("Edit model");
		stage.show();
	}
	
/*	allow to create new engine without a gearbox selected
 * 
 *  get user inputs, clean any useless space and checks if the fields are blank.
 * 	if one or more fields is/are blank -> show alert message
 *	otherwise notify the user for the meaning of his/her changes and check his/hers answer 
 *		pressed OK btn -> update engine
 *		anything else -> nothing happens
 */
	public void saveBtnOnClick(ActionEvent ae) throws IOException {
		String givenName = nameField.getText().strip();
		String givenPower = powerField.getText().strip();
		Type givenType = typeComboBox.getSelectionModel().getSelectedItem();
		if(!(givenName.isBlank()||givenPower.isBlank()||givenType==null)) {
			Alert confirmationAlert = new Alert(AlertType.CONFIRMATION);
			confirmationAlert.setHeaderText("ANY CHANGE TO ENGINE'S PRIMARY DATA WILL TAKE AFFECT TO ALL MODEL THAT USE THIS ENGINE");
			Optional<ButtonType> pressedBtn = confirmationAlert.showAndWait();
			if(pressedBtn.isPresent()&&pressedBtn.get().equals(ButtonType.OK)) {
				updateEngine(givenName, givenPower, givenType);
				backBtnOnClick(ae);
			}
		} else {
			Alert emptyFieldsOrGearboxListAlert = new Alert(AlertType.ERROR);
			emptyFieldsOrGearboxListAlert.setHeaderText("PLEASE FILL ALL THE FIELDS AND INSERT AT LEAST ONE GEARBOX TO THE SELECTED GEARBOXES");
			emptyFieldsOrGearboxListAlert.showAndWait();
		}
		
	}
	
//	use the selected Gearbox list 
//	go to screen that user can change the selected gearbox
	public void editGearboxBtnOnClick(ActionEvent ae) throws IOException {
		Gearbox selectedGearbox = manufacturerGearboxList.getSelectionModel().getSelectedItem();
		if(selectedGearbox==null) {
			Alert noSelectedGearbox = new Alert(AlertType.ERROR);
			noSelectedGearbox.setHeaderText("SELECT A GEARBOX FIRST (FROM MANUFACTURER LIST)");
			noSelectedGearbox.showAndWait();
			return ;
		} 
		loader = new FXMLLoader(getClass().getResource("../view/EditGearbox.fxml"));
		root = loader.load();
		EditGearboxController editGearboxController = loader.getController();
		editGearboxController.setUser(loggedUser);
		editGearboxController.setSelectedBrand(selectedBrand);
		editGearboxController.setSelectedModel(selectedModel);
		editGearboxController.setSelectedModelVersion(selectedModelVersion);
		editGearboxController.setEngines(engines);
		editGearboxController.setSelectedEngine(selectedEngine);
		editGearboxController.setSelectedGearbox(selectedGearbox);
		editGearboxController.setGearboxes(brandsGearboxes);
		stage = (Stage) ((Node)ae.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.setTitle("Edit gearbox");
		stage.show();
	}
	
//	pop a new window in order to create a new gearbox
	public void addNewGearboxBtnOnClick(ActionEvent ae) throws IOException {
		loader = new FXMLLoader(getClass().getResource("../view/AddGearbox.fxml"));
		root = loader.load();
		AddGearboxController addGearboxController = loader.getController();
		addGearboxController.setGearboxTypes();
		addGearboxController.setGearboxes(brandsGearboxes);
		Stage addGearboxStage = new Stage();
		scene = new Scene(root);
		addGearboxStage.setScene(scene);
		addGearboxStage.setTitle("ADD GEARBOX");
		addGearboxStage.initModality(Modality.APPLICATION_MODAL);
		addGearboxController.setStage(addGearboxStage);
		addGearboxController.setController(this);
		addGearboxStage.showAndWait();
	}
	
//	activates if user want to add an existed gearbox to the available gearboxes
//	leads to Insert price screen
	public void addGearboxBtnOnClick(ActionEvent ae) {
		Gearbox selectedGearbox = manufacturerGearboxList.getSelectionModel().getSelectedItem();
		if(selectedGearbox==null) {
			Alert noSelectedGearbox = new Alert(AlertType.ERROR);
			noSelectedGearbox.setHeaderText("PLEASE SELECT A GEARBOX FIRST FROM AVAILABLE GEARBOX LIST");
			noSelectedGearbox.showAndWait();
			return ;
		} 
		if(availableGearboxList.getItems().stream().anyMatch(gb->gb.id()==selectedGearbox.id())) {
			Alert duplicateGearboxAlert = new Alert(AlertType.ERROR);
			duplicateGearboxAlert.setHeaderText("THIS GEARBOX IS ALREADY IN SELECTED GEARBOXES LIST");
			duplicateGearboxAlert.showAndWait();
			return ;
		}
		addSelectedGearbox(selectedGearbox);
	}	
	
//	activates if the user want to remove a gearbox from the available gearboxes
//	pops a message that notify the user about his/hers action and make them only if the user press the ok btn
	public void removeGearboxBtnOnClick(ActionEvent ae) {
		Gearbox selectedGearbox = availableGearboxList.getSelectionModel().getSelectedItem();
		if(selectedGearbox==null) {
			Alert noSelectedGearbox = new Alert(AlertType.ERROR);
			noSelectedGearbox.setHeaderText("PLEASE SELECT A GEARBOX FIRST FROM SELECTED GEARBOX LIST");
			noSelectedGearbox.showAndWait();
			return ;
		} 
		Alert confirmation = new Alert(AlertType.CONFIRMATION);
		confirmation.setHeaderText("REMOVE THIS GEARBOX FROM THE AVAILABLE GEARBOXES?");
		Optional<ButtonType> pressedBtn = confirmation.showAndWait();
		if(pressedBtn.isPresent()&&pressedBtn.get().equals(ButtonType.OK)) {
			availableGearboxList.getItems().remove(selectedGearbox);
			setUnavailableGearbox(selectedGearbox);
		}
	}
	
	private void updateEngine(String givenName, String givenPower, Type givenType) {
		DBConnector con = new DBConnector();
		con.updateDatabase("""
				UPDATE engine
				set name='%s', power='%s', type='%s'
				where id=%s
				""".formatted(givenName, givenPower, givenType, selectedEngine.getId()));
		System.out.println("engine table successufully updated");
	}	
	
//	delete from ePWG table all records that have the specific attribute values
	private void setUnavailableGearbox(Gearbox selectedGearbox) {
		DBConnector con = new DBConnector();
		con.updateDatabase("""
				DELETE FROM engine_provided_with_gearbox
				WHERE engine_id=%s and gearbox_id=%s and model_version_id=%s
				""".formatted(selectedEngine.getId(), selectedGearbox.id(), selectedModelVersion.getId()));
		this.setGearboxes();
	}
}
