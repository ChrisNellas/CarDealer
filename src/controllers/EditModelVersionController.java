package controllers;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.CarBrand;
import model.DBConnector;
import model.Engine;
import model.Model;
import model.ModelVersion;
import model.User;
import util.RetrieveEngines;

/*CHECKED
 * 
 * backBtn
 * addColorBtn
 * removeColorBtn
 * logoutBtn
 * removeEngineBtn
 * createNewEngineBtn
 * addEngineBtn
 * saveBtn
 * pickBtn
 * 
 * THIS OK
 */
public class EditModelVersionController extends LongTermController{
	@FXML 
	private Button backBtn, addEngineBtn, removeEngineBtn, pickBtn, saveBtn, removeColorBtn, addColorBtn, createNewEngineBtn;
	@FXML
	private TextField nameField, imageField, colorField;
	@FXML
	private CheckBox threeDoorsAvailCB, fiveDoorsAvailCB;
	@FXML
	private ListView<String> colorList;
	@FXML
	private ListView<Engine> manufacturerEngineList, availableEngineList;
	
	
	
	@FXML
	private TableView<Engine> availEngineTable;
	@FXML
	private TableColumn<Engine, String> availEngineNameCol;
	@FXML
	private TableColumn<Engine, Integer> availEnginePowerCol;
	
	private ObservableList<Engine> engines;
	
	
	private Set<Engine> availEngines, manufacturerEngines;
	
	private CarBrand selectedBrand;
	private Model selectedModel;
	private ModelVersion selectedModelVersion;
	
	private boolean changeMade ;
	
	
	public void init() {
		availEngineNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
		availEnginePowerCol.setCellValueFactory(new PropertyValueFactory<>("power"));
	}
	
	
//	GETERS & SETTER	
	public void setSelectedBrand(CarBrand selectedBrand) {
		this.selectedBrand = selectedBrand;
	}
	
	public void setSelectedModel(Model selectedModel) {
		this.selectedModel = selectedModel;
	}
	
	//populate colors listView and checkBoxes
	public void setSelectedModelVersion(ModelVersion selectedModelVersion) {
		this.selectedModelVersion = selectedModelVersion;
		nameField.setText(selectedModelVersion.getName());
		imageField.setText(selectedModelVersion.getImage());
		colorList.getItems().addAll(selectedModelVersion.getAvailablecolors());
		fiveDoorsAvailCB.setSelected(selectedModelVersion.isAvailableWith5());
		threeDoorsAvailCB.setSelected(selectedModelVersion.isAvailableWith3());
		changeMade = false;
	}

//	 IT WILL BE BETTER IF I HAVE 2 DIFFERENT METHODS TWO POPULATE THE LISTS
	
	// retrieve all brand's engines and the currently available for this model version
	public void getAvailableEngines(ModelVersion modelVersion) {
		availEngines = new HashSet<>();
		manufacturerEngines = new HashSet<>();
		
		engines = FXCollections.observableArrayList();
		
		Thread engineThread = new Thread(new RetrieveEngines(engines, modelVersion.getId(), manufacturerEngines, selectedBrand.getId())); //availEngines
		engineThread.start();
		availableEngineList.getItems().clear();
		manufacturerEngineList.getItems().clear();
		try {
			engineThread.join();
			
			
//			System.out.println(engines);
			availEngineTable.setItems(engines);
//			System.out.println("2) "+availEngineTable.getItems());
			
			
			availableEngineList.getItems().addAll(availEngines);
			manufacturerEngineList.getItems().addAll(manufacturerEngines);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
//	LISTENERS
	public void backBtnOnClick(ActionEvent ae) throws IOException {
		loader = new FXMLLoader(getClass().getResource("../view/EditModel.fxml"));
		root = loader.load();
		EditModelController editModelController = loader.getController();
		editModelController.setUser(loggedUser);		
		editModelController.setSelectedBrand(selectedBrand);
		editModelController.setSelectedModel(selectedModel);
		if(changeMade) {
			editModelController.getModelVersions();
		}
		stage = (Stage) ((Node)ae.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.setTitle("edit model");
		stage.show();
	}
	
	public void pickBtnOnClick(ActionEvent ae) throws IOException {
		Engine selectedEngine = manufacturerEngineList.getSelectionModel().getSelectedItem();
		if(selectedEngine==null) {
			Alert noSelectedEngineAlert = new Alert(AlertType.ERROR);
			noSelectedEngineAlert.setHeaderText("SELECT AN ENGINE FIRST(FROM MANUFACTURER ENGINES)");
			noSelectedEngineAlert.showAndWait();
			return ;
		}
		loader = new FXMLLoader(getClass().getResource("../view/EditEngine.fxml"));
		root = loader.load();
		EditEngineController editEngineController = loader.getController();
		editEngineController.setUser(loggedUser);
		editEngineController.setSelectedBrand(selectedBrand);
		editEngineController.setSelectedModel(selectedModel);
		editEngineController.setSelectedModelVersion(selectedModelVersion);
		editEngineController.setSelectedEngine(selectedEngine);
		editEngineController.setGearboxes();
		stage = (Stage)((Node)ae.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.setTitle("EDIT ENGINE");
		stage.show();
	}
	
	/*
	 * pop the InsertGearbox screen in order to create to ePWG table new record
	 */
	public void addEngineBtnOnClick(ActionEvent ae) throws IOException {
		Engine selectedEngine = manufacturerEngineList.getSelectionModel().getSelectedItem();
		if(selectedEngine==null) {
			Alert noSelectedEngineFoundAlert = new Alert(AlertType.ERROR);
			noSelectedEngineFoundAlert.setHeaderText("FIRST SELECT AN ENGINE FROM MANUFACTURER ENGINES LIST");
			noSelectedEngineFoundAlert.showAndWait();
			return ;
		}
		if(availableEngineList.getItems().stream().anyMatch(availEngine->availEngine.getId().equals(selectedEngine.getId()))) {
			Alert alreadyAvailableEngineAlert = new Alert(AlertType.INFORMATION);
			alreadyAvailableEngineAlert.setHeaderText("THIS ENGINE IS ALREADY AVAILABLE FOR THIS MODEL VERSION");
			alreadyAvailableEngineAlert.showAndWait();
			return ;
		} 
		goToInsertGearbox(selectedEngine);
	}
	
	/* if the user has select an engine 
	 * prompt if user is sure he/she wants to delete all records in ePWG table with this engine's id
	 * if the user press the ok btn then change are applied and refresh the engines lists
	 */
	public void removeEngineBtnOnClick(ActionEvent ae) {
		Engine selectedEngine = availableEngineList.getSelectionModel().getSelectedItem();
		if(selectedEngine==null) {
			Alert noSelectedEngineFoundAlert = new Alert(AlertType.ERROR);
			noSelectedEngineFoundAlert.setHeaderText("FIRST SELECT AN ENGINE FROM AVAILABLE ENGINES LIST");
			noSelectedEngineFoundAlert.showAndWait();
			return ;
		}
		Alert confirmationAlert = new Alert(AlertType.CONFIRMATION);
		confirmationAlert.setHeaderText("DELETE THIS ENGINE FROM AVAILABLE ENGINES OF THIS MODEL VERSION?");
		Optional<ButtonType> pressedBtn = confirmationAlert.showAndWait();
		if(pressedBtn.isPresent()&&pressedBtn.get().equals(ButtonType.OK)) {
			deleteEngineFromDB(selectedEngine);
			getAvailableEngines(selectedModelVersion);//reload engines
		}
	}
	
//	remove the selected color from the list
	public void removeColorBtnOnClick(ActionEvent ae) {
		String selectedColor = colorList.getSelectionModel().getSelectedItem();
		if(selectedColor!=null) {
			colorList.getItems().remove(selectedColor);
		} else {
			Alert noColorSelectedAlert = new Alert(AlertType.ERROR);
			noColorSelectedAlert.setHeaderText("please select a color first");
			noColorSelectedAlert.show();
		}
	}
	
	public void createNewEngineBtnOnClick(ActionEvent ae) throws IOException {
		loader = new FXMLLoader(getClass().getResource("../view/AddEngine.fxml"));
		root = loader.load();
		AddEngineController addEngineController = loader.getController();
		addEngineController.setUser(loggedUser);
		addEngineController.setEngines(manufacturerEngines);
		addEngineController.setEngineTypes();
		addEngineController.setSelectedModelVersion(selectedModelVersion);
		Stage addEngineStage = new Stage();
		addEngineStage.initModality(Modality.APPLICATION_MODAL);
		addEngineController.setStage(addEngineStage);
		addEngineController.setController(this);
		scene = new Scene(root);
		addEngineStage.setScene(scene);
		addEngineStage.setTitle("Add engine");
		addEngineStage.showAndWait();
	}
	
	public void addColorBtnOnClick(ActionEvent ae) {
		String givenColor = colorField.getText().strip();
		if(givenColor.isBlank()) {
			Alert emptyColorFieldAlert = new Alert(AlertType.ERROR);
			emptyColorFieldAlert.setHeaderText("please insert a color first into the color field");
			emptyColorFieldAlert.show();
		} else if(colorList.getItems().contains(givenColor)) {
			Alert duplicateColorAlert = new Alert(AlertType.ERROR);
			duplicateColorAlert.setHeaderText("this color is already in the color list");
			duplicateColorAlert.show();
		} else {
			colorList.getItems().add(givenColor);
			colorField.clear();
		}
	}
	
	/* get user inputs, clean any useless space and checks if the fields are blank.
	 *	if not then apply changes to database
	 */
	public void saveBtnOnClick(ActionEvent ae) throws IOException {
		String givenName = nameField.getText().strip();
		String givenImageURL = imageField.getText().strip();
//		ONE OR BOTH TEXTFIELDS IS/ARE BLANK
		if(givenName.isBlank()||givenImageURL.isBlank()) {
			Alert emptyFieldsFoundAlert = new Alert(AlertType.ERROR);
			emptyFieldsFoundAlert.setHeaderText("FIELDS HAVE TO BE WRITTEN");
			emptyFieldsFoundAlert.showAndWait();
			return ;
		}
//		NONE OF CHECKBOXES IS CHECKED
		if(!(threeDoorsAvailCB.isSelected()||fiveDoorsAvailCB.isSelected())) {
			Alert noAvailDoorOptionFoundAlert = new Alert(AlertType.ERROR);
			noAvailDoorOptionFoundAlert.setHeaderText("PLEASE SELECT AT LEAST ONE DOOR OPTION");
			noAvailDoorOptionFoundAlert.showAndWait();
			return ;
		}
//		ONE OR BOTH(AVAILABLE ENGINES - COLORS) LISTS ARE EMPTY
		if(availableEngineList.getItems().size()==0||colorList.getItems().size()==0) {
			Alert emptyListFoundAlert = new Alert(AlertType.ERROR);
			emptyListFoundAlert.setHeaderText("PLEASE INSERT AT LEAST ONE ITEM AT AVAILABLE ENGINES AND COLORS LISTS");
			emptyListFoundAlert.showAndWait();
			return ;
		}
		updateModelVersion(givenName, givenImageURL);
		changeMade = true;
		backBtnOnClick(ae);		
	}
	
	/* apply the changes
	 * in colors column the proper format is: "["....","..."]"
	 */
	private void updateModelVersion(String givenName, String givenImage) {
		DBConnector con = new DBConnector();
		
		String colorsFormat = "[";
		Iterator<String> it = colorList.getItems().iterator();
		while(it.hasNext()) {
			colorsFormat= colorsFormat+"\""+it.next()+"\",";
		}
		colorsFormat = colorsFormat.substring(0, colorsFormat.length()-1);
		colorsFormat = colorsFormat+"]";
		
		con.updateDatabase("""
				UPDATE model_version
				SET version_name='%s', fiveDoorsAvail=%b, threeDoorsAvail=%b, image='%s', colors='%s'
				WHERE version_id = %s
				""".formatted(givenName, fiveDoorsAvailCB.isSelected(), threeDoorsAvailCB.isSelected(), givenImage, colorsFormat, selectedModelVersion.getId()));
	}
	
	private void deleteEngineFromDB(Engine engine) {
		DBConnector con = new DBConnector();
		con.updateDatabase("""
				DELETE FROM engine_provided_with_gearbox
				WHERE engine_id=%s and model_version_id=%s
				""".formatted(engine.getId().toString(), selectedModelVersion.getId().toString()));
	}
	
	
	public void goToInsertGearbox(Engine engine) throws IOException {
		loader = new FXMLLoader(getClass().getResource("../view/InsertGearbox.fxml"));
		root = loader.load();
		InsertGearboxController insertGearboxController = loader.getController();
		insertGearboxController.setSelectedBrandId(selectedBrand.getId());
		insertGearboxController.setBrandsGearboxes();
		insertGearboxController.setSelectedModelVersion(selectedModelVersion);
		insertGearboxController.setSelectedEngine(engine);
		Stage insertGearboxStage = new Stage();
		insertGearboxStage.initModality(Modality.APPLICATION_MODAL);
		insertGearboxController.setStage(insertGearboxStage);
		insertGearboxController.setController(this);
		scene = new Scene(root);
		insertGearboxStage.setScene(scene);
		insertGearboxStage.setTitle("insert gearbox");
		insertGearboxStage.showAndWait();
	}
}
