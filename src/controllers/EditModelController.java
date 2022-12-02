/*CHECKED
 * 
 * backBtn
 * saveBtn 
 * addModelVersionBtn
 * pickBtn
 * logoutBtn
*/
package controllers;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.CarBrand;
import model.DBConnector;
import model.Model;
import model.ModelVersion;
import model.User;
import util.RetrieveModelVersions;

public class EditModelController extends LongTermController{
	@FXML
	private TextField nameField;
	@FXML 
	private Button backBtn, pickBtn, addModelVersionBtn, saveBtn;
	@FXML
	private ListView<ModelVersion> modelVersionList;
		
	Set<ModelVersion> modelVersions;
	private CarBrand selectedBrand;
	private Model selectedModel;
	
//	GETERS & SETTERS	
	public void setSelectedBrand(CarBrand selectedBrand) {
		this.selectedBrand = selectedBrand;
	}
	// get the selected model and modelVersions
	public void setSelectedModel(Model selectedModel) {
		this.selectedModel = selectedModel;
		nameField.setText(selectedModel.getName());
		if(selectedModel.getModelVersions().size()==0) {
			getModelVersions();
		} else {
			modelVersionList.getItems().addAll(selectedModel.getModelVersions());
		}
	}
	
	public void setModelVersions(Set<ModelVersion> modelVersions) { 
		this.modelVersions = modelVersions;
	}
	
//	retrieve model versions from db
	public void getModelVersions() { 
		modelVersions = new HashSet<>();
		Thread modelVersionThread = new Thread(new RetrieveModelVersions(modelVersions, selectedModel.getId()));
		modelVersionThread.start();
		modelVersionList.getItems().clear();
		try {
			modelVersionThread.join();
			modelVersionList.getItems().addAll(modelVersions);
			modelVersions.forEach(selectedModel::addModelVersion);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

//	LISTENERS
	public void backBtnOnClick(ActionEvent ae) throws IOException {
		loader = new FXMLLoader(getClass().getResource("../view/EditBrand.fxml"));
		root = loader.load();
		EditBrandController editBrandController = loader.getController();
		editBrandController.setUser(loggedUser);		
		editBrandController.setSelectedBrand(selectedBrand);
		editBrandController.setBrands();
		stage = (Stage) ((Node)ae.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.setTitle("EDIT BRAND");
		stage.show();
	}
	
	public void pickBtnOnClick(ActionEvent ae) throws IOException {
		ModelVersion selectedModelVersion = modelVersionList.getSelectionModel().getSelectedItem();
		if(selectedModelVersion==null) {
			Alert noModelVersionSelectedAlert = new Alert(AlertType.ERROR);
			noModelVersionSelectedAlert.setHeaderText("PLEASE CHOOSE THE MODEL VERSION YOY WANT TO EDIT FIRST");
			noModelVersionSelectedAlert.show();
		} else {
			loader = new FXMLLoader(getClass().getResource("../view/EditModelVersion.fxml"));
			root = loader.load();
			EditModelVersionController editModelVersionController = loader.getController();
			editModelVersionController.setUser(loggedUser);
			
			editModelVersionController.init();
			
			editModelVersionController.setSelectedBrand(selectedBrand);
			editModelVersionController.setSelectedModel(selectedModel);
			editModelVersionController.setSelectedModelVersion(selectedModelVersion);
			editModelVersionController.getAvailableEngines(selectedModelVersion);
			stage = (Stage) ((Node)ae.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.setTitle("EDIT MODEL VERSION");
			stage.show();
		}
	}
	
	public void addModelVersionBtnOnClick(ActionEvent ae) throws IOException {
		loader = new FXMLLoader(getClass().getResource("../view/AddModelVersion.fxml"));
		root = loader.load();
		AddModelVersionController addModelVersionController = loader.getController();
		addModelVersionController.setUser(loggedUser);
		addModelVersionController.setSelectedModel(selectedModel);
		addModelVersionController.setSelectedBrand(selectedBrand);
		Stage addModelVersionStage = new Stage();//(Stage) ((Node)ae.getSource()).getScene().getWindow();
		addModelVersionController.setStage(addModelVersionStage);
		addModelVersionController.setController(this);
		addModelVersionStage.initModality(Modality.APPLICATION_MODAL);
		scene = new Scene(root);
		addModelVersionStage.setScene(scene);
		addModelVersionStage.setTitle("Add model version");
		addModelVersionStage.showAndWait();
	}
	
	/*	get user inputs, clean any useless space and checks if the fields are blank.
	 * 	if one or more fields is/are blank -> show alert message
	 *	otherwise search if this model name is unavailable
	 *		unavailable -> show alert message
	 *		available -> apply changes to database
	 */
	public void saveBtnOnClick(ActionEvent ae) throws IOException {
		String givenName = nameField.getText().strip();
		Set<ModelVersion> modelVersions = modelVersionList.getItems().stream().collect(Collectors.toSet());
		if(!(givenName.isBlank()||modelVersions.size()==0)) {
			if(selectedBrand.getModels().stream().noneMatch(model->model.getName().equals(givenName))) {
				updateModel(givenName, modelVersions);
				backBtnOnClick(ae);
			} else {
				Alert duplicatedNameAlert = new Alert(AlertType.ERROR);
				duplicatedNameAlert.setHeaderText("THIS MODEL ALREADY EXISTS");
				duplicatedNameAlert.showAndWait();
			}
		} else {
			Alert noAcceptedFieldsAlert = new Alert(AlertType.ERROR);
			noAcceptedFieldsAlert.setHeaderText("PROVIDE A NAME TO THE FIELD AND AT LEAST ONE MODEL VERSION IN THE LIST");
			noAcceptedFieldsAlert.showAndWait();
		}
	}
	
//	modelVersions are useless
//	UPDATE THE car_model TABLE (NOT THE model_version TABLE)
	public void updateModel(String givenName, Set<ModelVersion> modelVersions) {
		DBConnector con = new DBConnector();
		con.updateDatabase("""
				UPDATE car_model
				SET model_name='%s'
				WHERE model_id = %s
				""".formatted(givenName, selectedModel.getId()));
		selectedModel.setName(givenName);//useless because i don't pass it back
		updatebrandsModelList();
	}
	
	public void updatebrandsModelList() {
		selectedBrand.getModels().stream()
		.filter(model->model.getId()==selectedModel.getId())
		.forEach(model->model=selectedModel);
	}
}
