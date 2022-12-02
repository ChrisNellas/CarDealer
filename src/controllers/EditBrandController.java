package controllers;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.imageio.ImageWriter;

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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.CarBrand;
import model.DBConnector;
import model.Model;
import model.User;
import util.RetrieveBrands;
import util.RetrieveModels;

public class EditBrandController extends LongTermController{
	@FXML
	private Button backBtn, pickBtn, addModelBtn, saveBtn;
	@FXML
	private TextField nameField, logoField;
	@FXML
	private ListView<Model> modelList;
	@FXML
	private ImageView logoImageView;
		
	private CarBrand selectedBrand;
	
	private Set<User> users;
	private Set<CarBrand> brands;
	
//	SETTERS	
	public void setUsers(Set<User> users) {
		this.users = users;
	}
	
	public void setSelectedBrand(CarBrand brand) {
		selectedBrand = brand;
		modelList.getItems().clear();
		if(selectedBrand.getModels().size()==0) {
			getBrandsModels(brand);
		} else {
			modelList.getItems().addAll(selectedBrand.getModels());
		}
		nameField.setText(brand.getName());
		logoField.setText(brand.getLogo());
		logoImageView.setImage(new Image(selectedBrand.getLogo()));
	}
	
//	gets brands from adminPanel
	public void setBrands(Set<CarBrand> brands) {
		this.brands = brands;
	}
	
//	gets brands from db
	public void setBrands() {
		brands = new HashSet<>();
		Thread brandsThread = new Thread(new RetrieveBrands(brands));
		brandsThread.start();
		try {
			brandsThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

//	LISTENERS
	public void backBtnOnClick(ActionEvent ae) throws IOException {
		loader = new FXMLLoader(getClass().getResource("../view/AdminPanel.fxml"));
		root = loader.load();
		AdminPanelController adminPanelController = loader.getController();
		adminPanelController.setUser(loggedUser);
		if(users!=null) {
			adminPanelController.setUsers(users);	
		} else {
			adminPanelController.setUsers();
		}	
//		sendUsers(adminPanelController);		
		adminPanelController.setBarChartAndBrandList(brands);
		stage = (Stage) ((Node)ae.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.setTitle("ADMIN PANEL");
		stage.show();
	}
	
//	go to edit model screen
	public void pickBtnOnClick(ActionEvent ae) throws IOException {
		Model selectedModel = modelList.getSelectionModel().getSelectedItem();
		if(selectedModel!=null) {
			loader = new FXMLLoader(getClass().getResource("../view/EditModel.fxml"));
			root = loader.load();
			EditModelController editModelController = loader.getController();
			editModelController.setUser(loggedUser);
			editModelController.setSelectedBrand(selectedBrand);
			editModelController.setSelectedModel(selectedModel);
			stage = (Stage) ((Node)ae.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.setTitle("EDIT MODEL");
			stage.show();
		} else {
			Alert noSelectedModelAlert = new Alert(AlertType.ERROR);
			noSelectedModelAlert.setHeaderText("PLEASE FIRST SELECT A MODEL");
			noSelectedModelAlert.showAndWait();
		}
	}
	
//	go to add model screen
	public void addModelBtnOnClick(ActionEvent ae) throws IOException {
		loader = new FXMLLoader(getClass().getResource("../view/AddModel.fxml"));
		root = loader.load();
		AddModelController addModelController = loader.getController();
		addModelController.setUser(loggedUser);
		addModelController.setSelectedBrand(selectedBrand);
		Stage addModelStage = new Stage(); //(Stage) ((Node)ae.getSource()).getScene().getWindow();
		addModelController.setStage(addModelStage);
		addModelController.setController(this);
		addModelStage.initModality(Modality.APPLICATION_MODAL);		
		scene = new Scene(root);
		addModelStage.setScene(scene);
		addModelStage.setTitle("Add model");
		addModelStage.showAndWait();
	}
	
	/* get user inputs, clean any useless space and checks if the fields are blank.
	 * 	if one or more fields is/are blank -> show alert message
	 *	otherwise search if this brand name is unavailable
	 *		unavailable -> show alert message
	 *		available -> apply changes to database
	 */
	public void saveBtnOnClick(ActionEvent ae) throws IOException {
		String givenBrandName = nameField.getText().strip();
		String givenBrandLogoURL = logoField.getText().strip();
		Set<Model> givenModels = modelList.getItems().stream().collect(Collectors.toSet());
		if(!(givenBrandName.isBlank()||givenBrandLogoURL.isBlank()||givenModels.size()==0)) {
			if(brands.stream().noneMatch((brand)->brand.getName().equals(givenBrandName))){
				updateBrand(givenBrandName, givenBrandLogoURL);
				backBtnOnClick(ae);
			} else {
				Alert duplicateBrandAlert = new Alert(AlertType.ERROR);
				duplicateBrandAlert.setHeaderText("THIS brand name already exists in db");
				duplicateBrandAlert.showAndWait();
			}
		} else {
			Alert emptyFieldsOrModelListAlert = new Alert(AlertType.ERROR);
			emptyFieldsOrModelListAlert.setHeaderText("FULFILL ALL THE FIELDS AND INSERT AT LEAST ONE MODEL");
			emptyFieldsOrModelListAlert.showAndWait();
		}
	}
	
//	UPDATE THE DATABASE AND THE SELECTED BRAND 
	private void updateBrand(String givenName, String givenLogo) {
		DBConnector con = new DBConnector();
		con.updateDatabase("""
				UPDATE brands
				SET name='%s', logo='%s'
				WHERE brand_id=%s
				""".formatted(givenName, givenLogo, selectedBrand.getId()));
		this.selectedBrand.setName(givenName);
		this.selectedBrand.setLogo(givenLogo);
		updateBrandList(selectedBrand);
	}
	
//	get from db the models of the selected brand
	private void getBrandsModels(CarBrand selectedBrand) {
		Set<Model> models = new HashSet<>();
		Thread modelThread = new Thread(new RetrieveModels(models, selectedBrand.getId()));
		modelThread.start();
		try {
			modelThread.join();
			modelList.getItems().addAll(models);
			models.forEach(selectedBrand::addNewModel);
//			selectedBrand.setModels(models); // also good 
			updateBrandList(selectedBrand);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	// replace the selectedBrand in brandsList with the selectedBrand after the last get its models 
	private void updateBrandList(CarBrand selectedBrand) {
		this.brands.forEach(brand->{
			if(brand.getId()==selectedBrand.getId()) {
				brand = selectedBrand;
			}
		});
	}
}
