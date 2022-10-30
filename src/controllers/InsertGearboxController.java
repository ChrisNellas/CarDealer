package controllers;

import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.DBConnector;
import model.Engine;
import model.Gearbox;
import model.ModelVersion;
import util.RetrieveGearboxes;

/*
 * CHECKED 
 * 
 * cancelBtn
 * finishBtn
 * cancelBtn
 * addGearboxBtn
 * removeGearboxBtn
 * addNewGearboxBtn
 * 
 * I THINK IS DONE
 * */

public class InsertGearboxController extends ShortTermController{

	@FXML
	private Button finishBtn, cancelBtn, addGearboxBtn, removeGearboxBtn, addNewGearboxBtn;
	@FXML
	private ListView<Gearbox> brandsGearboxesList, selectedGearboxesList;
		
	private ModelVersion selectedModelVersion;
	private Engine selectedEngine;
	
	private Set<Gearbox> brandsGearboxSet, selectedGearboxSet, createdGearboxesSet; 
	
	private Long selectedBrandId;
		
//	SETTERS
	public void setSelectedBrandId(Long brandId) {
		this.selectedBrandId = brandId;
	}
	
	public void setSelectedModelVersion(ModelVersion selectedModelVersion) {
		this.selectedModelVersion = selectedModelVersion;
	}
	
	public void setSelectedEngine(Engine selectedEngine) {
		this.selectedEngine = selectedEngine;
	}
	
//	clean the set and list and then get gearboxes from db  
	public void setBrandsGearboxes() {
		if(brandsGearboxSet==null) {
			brandsGearboxSet = new HashSet<>();
		}
		brandsGearboxSet.clear();
		brandsGearboxesList.getItems().clear();
		Thread gearboxesThread = new Thread(new RetrieveGearboxes(brandsGearboxSet, selectedBrandId));
		gearboxesThread.start();
		createdGearboxesSet = new HashSet<>();
		try {
			gearboxesThread.join();
			brandsGearboxesList.getItems().addAll(brandsGearboxSet);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void setSelectedGearboxes() {
		if(selectedGearboxSet==null) {
			selectedGearboxSet = new HashSet<>();
		}
		selectedGearboxSet.clear();
		Thread gearboxesThread = new Thread(new RetrieveGearboxes(selectedGearboxSet, selectedModelVersion.getId(), selectedEngine.getId()));
		gearboxesThread.start();
		selectedGearboxesList.getItems().clear();
		try {
			gearboxesThread.join();
			selectedGearboxesList.getItems().addAll(selectedGearboxSet);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
//	trigger the insertPrice screen
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
	public void cancelBtnOnClick(ActionEvent ae) {
		Alert deleteEngineWarningAlert = new Alert(AlertType.CONFIRMATION);
		deleteEngineWarningAlert.setHeaderText("DO YOU REALLY WANT GO ABORT YOUR PROGRESS?");
		Optional<ButtonType> alertsPressedBtn = deleteEngineWarningAlert.showAndWait();
		
		if(alertsPressedBtn.isPresent()&&alertsPressedBtn.get().equals(ButtonType.OK)) {
			selectedGearboxesList.getItems().forEach(gb->setUnavailableGearbox(gb));//delete all records from engineProv.... with the selected gearboxes, engine and model version
			stage.close();
		}
	}
	
//	remove from viewList and delete from db the proper record
	public void removeBtnOnClick(ActionEvent ae) {
		Gearbox selectedGearbox = selectedGearboxesList.getSelectionModel().getSelectedItem();
		if(selectedGearbox==null) {
			Alert noSelectedGearboxAlert = new Alert(AlertType.ERROR);
			noSelectedGearboxAlert.setHeaderText("PLEASE SELECT THE GEARBOX YOU WANT TO REMOVE FROM SELECTED GEARBOXES LIST");
			noSelectedGearboxAlert.show();
			return ;
		}
		Alert confirmation = new Alert(AlertType.CONFIRMATION);
		confirmation.setHeaderText("REMOVE THIS GEARBOX FROM THE SELECTED GEARBOXES?");
		Optional<ButtonType> pressedBtn = confirmation.showAndWait();
		if(pressedBtn.isPresent()&&pressedBtn.get().equals(ButtonType.OK)) {
			selectedGearboxesList.getItems().remove(selectedGearbox);
			setUnavailableGearbox(selectedGearbox);
		}
	}
	
// add the selected gearbox to the listview and to selectedGearboxesMap
	public void addGearboxBtnOnClick(ActionEvent ae) {
		Gearbox selectedGearbox = brandsGearboxesList.getSelectionModel().getSelectedItem();
		if(selectedGearbox==null) {
			Alert noSelectedGearboxAlert = new Alert(AlertType.ERROR);
			noSelectedGearboxAlert.setHeaderText("PLEASE SELECT THE GEARBOX YOU WANT TO REMOVE FROM BRANDS GEARBOXES LIST");
			noSelectedGearboxAlert.show();
			return ;
		}
		if(selectedGearboxesList.getItems().stream().anyMatch(gb->gb.id().equals(selectedGearbox.id()))) {
			Alert duplicateGearboxAlert = new Alert(AlertType.ERROR);
			duplicateGearboxAlert.setHeaderText("THIS GEARBOX IS ALREADY IN SELECTED GEARBOXES LIST");
			duplicateGearboxAlert.show();
			return ;
		}
		addSelectedGearbox(selectedGearbox);
	}	
	
//	go to add gearbox screen
	public void addNewGearboxBtnOnClick(ActionEvent ae) throws IOException {
		loader = new FXMLLoader(getClass().getResource("../view/AddGearbox.fxml"));
		root = loader.load();
		AddGearboxController addGearboxController = loader.getController();
		addGearboxController.setGearboxTypes();
		addGearboxController.setGearboxes(brandsGearboxSet);
		Stage addGearboxStage = new Stage();
		scene = new Scene(root);
		addGearboxStage.setScene(scene);
		addGearboxStage.setTitle("ADD GEARBOX");
		addGearboxStage.initModality(Modality.APPLICATION_MODAL);
		addGearboxController.setStage(addGearboxStage);
		addGearboxController.setController(this);
		addGearboxStage.showAndWait();
	}
//	return to editModelVersion screen
//	call editModelVersionController.getAvailableEngines method
	public void finishBtnOnClick(ActionEvent ae) {
		((EditModelVersionController)previousController).getAvailableEngines(selectedModelVersion); // if i create both engine & gearbox then i think i need it
		stage.close();
	}
	
//	delete all records from ePWG with the specific attribute values
	private void setUnavailableGearbox(Gearbox selectedGearbox) {
		DBConnector con = new DBConnector();
		con.updateDatabase("""
				DELETE FROM engine_provided_with_gearbox
				WHERE engine_id=%s and gearbox_id=%s and model_version_id=%s
				""".formatted(selectedEngine.getId(), selectedGearbox.id(), selectedModelVersion.getId()));
		this.setBrandsGearboxes();
	}
}
