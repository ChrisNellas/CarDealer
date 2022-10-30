package controllers;

import java.io.IOException;
import java.time.LocalDate;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import model.CarBrand;
import model.Customer;
import model.DBConnector;
import model.Engine;
import model.Gearbox;
import model.Model;
import model.ModelVersion;
import model.Order;
import model.Type;
import model.User;
import util.RetrieveBrands;
import util.RetrieveEngines;
import util.RetrieveGearboxes;
import util.RetrieveModels;
import util.RetrieveModelVersions;
import util.RetrievePrice;

public class CreateNewCarOrderController extends LongTermController{

	@FXML
	private ComboBox<String> brandComboBox, modelComboBox, colorComboBox, 
					carVersionComboBox, engineComboBox, gearboxComboBox;
	@FXML
	private Button backBtn, createOrderBtn;
	@FXML
	private Label custNameLabel, custAddressLabel;
	@FXML
	private Label carTypeLabel, priceLabel;
	
	private boolean found = false;
	
	private Customer selectedCustomer;
	
	private Set<CarBrand> brands;
	private Set<String> colors;
	private Set<Engine> engines;
	private Set<ModelVersion> modelVersions;
	private Set<Model> models;
	private Set<Gearbox> gearboxes;
	
	private String selectedColor= null; 
	
//	OPTIONALS
	private Optional<Gearbox> optSelectedGearbox;
	private Optional<Engine> optSelectedEngine;
	private Optional<ModelVersion> optSelectedmodelVersion;
	private Optional<Model> optSelectedmodel;
	private Optional<CarBrand> optSelectedBrand;
	
	
//	SETTERS AND GETTERS
	public void setModelVersions(Set<ModelVersion> modelVersions) {
		this.modelVersions = modelVersions;
		modelVersions.forEach(mV->modelComboBox.getItems().add(mV.getName()));
	}
	
//	insert the customer's data into labels
	public void getCustomer(Customer cust) {
		selectedCustomer = cust;
		custNameLabel.setText(cust.getName());
		custAddressLabel.setText(cust.getAddress());
	}
	
	public boolean isFound() {
		return found;
	}
	
	public void setFoundTrue() {
		this.found = true;
		brandComboBox.setDisable(false);
	}
	
	public void getBrands() {
		brands = new HashSet<>();
		Thread thread = new Thread(new RetrieveBrands(brands));
		thread.start();
		try {
			thread.join();
			brands.forEach(brand->brandComboBox.getItems().add(brand.getName()));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
//	return to main menu
	public void goToMainMenu(ActionEvent ae) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/MainMenu.fxml"));
		Parent root = loader.load();
		MainMenuController controller = loader.getController();
		controller.setUser(loggedUser);
		Stage stage = (Stage) ((Node) ae.getSource()).getScene().getWindow();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.setTitle("MAIN MENU");
		stage.show();
	}
	
//  LISTENERS
	public void backBtnOnClick(ActionEvent ae) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/FindCustomer.fxml"));
		Parent root = loader.load();	
		FindCustomerController findCustomerController = loader.getController();
		findCustomerController.setUser(loggedUser);
		findCustomerController.populateList();
		findCustomerController.setDestination("CreateNewCarOrder.fxml");
		Scene scene = new Scene(root);
		Stage stage = (Stage) ((Node) ae.getSource()).getScene().getWindow();
		stage.setTitle("FIND CUSTOMER");
		stage.setScene(scene);
		stage.show();
	}
	
	public void createOrderBtnOnClick(ActionEvent ae) throws IOException {
		if(optSelectedBrand.isPresent()&&optSelectedmodel.isPresent()&&optSelectedmodelVersion.isPresent()
				&&optSelectedEngine.isPresent()&&optSelectedGearbox.isPresent()&&selectedColor!=null) {
			Long brandId = optSelectedBrand.get().getId();
			Long modelId = optSelectedmodel.get().getId();
			Long modelVersionId = optSelectedmodelVersion.get().getId();
			Long engineId = optSelectedEngine.get().getId();
			Long gearboxId = optSelectedGearbox.get().id();
			
			LocalDate date = LocalDate.now();
			
			DBConnector con = new DBConnector();
			con.updateDatabase("""
					INSERT INTO orders(cost,brand_id,model_id,version_id,engine_id,type,gearbox_id,customer_id,color, date) 
					values(%s,'%s','%s','%s','%s','%s','%s','%s','%s','%s')
					""".formatted(priceLabel.getText(), brandId, modelId, modelVersionId, engineId, carTypeLabel.getText(),
							gearboxId, selectedCustomer.getId(), colorComboBox.getSelectionModel().getSelectedItem(), date));
			Type type = null;
			switch(carTypeLabel.getText()){
				case "DIESEL"-> type=Type.DIESEL;
				case "PETROL"-> type=Type.PETROL;
				case "ELECTRIC"-> type=Type.ELECTRIC;
				case "HYBRID"-> type=Type.HYBRID;
				case "HIDROGEN"-> type=Type.HYDROGEN;
				case "PLUG_IN_HYBRID"-> type=Type.PLUG_IN_HYBRID;
				case "LPG" -> type=Type.LPG;
			}
			
			selectedCustomer.addNewOrder(new Order(Double.parseDouble(priceLabel.getText()), brandId, modelId, selectedColor, modelVersionId, type, engineId, gearboxId));
			Alert notification = new Alert(AlertType.INFORMATION);
			notification.setHeaderText("New order has successfully created!");
			notification.setContentText("""
					date: %s
					customer: %s
					brand: %s
					model: %s
					modelVersion: %s
					engine: %s
					gearbox: %s
					color: %S
					cost: %s
					""".formatted(date, custNameLabel.getText(), optSelectedBrand.get().getName(), optSelectedmodel.get().getName(), optSelectedmodelVersion.get().getName(),
							optSelectedEngine.get().getName(), optSelectedGearbox.get().name(), selectedColor, priceLabel.getText()));
			notification.show();
			//go to the main menu screen
			goToMainMenu(ae);			
		}
	}
	
/*	COMBOBOX HANDLERS
 * when a combo box gets new value unlock the next combo box(es)
 */
	public void brandComboBoxGetValue(ActionEvent ae) {
		String selectedBrandName = brandComboBox.getSelectionModel().getSelectedItem();
		optSelectedBrand = brands.stream().filter(brand->brand.getName().equals(selectedBrandName)).findFirst();
		if(optSelectedBrand.isPresent()) {
			models = new HashSet<>();
			Thread modelThread = new Thread(new RetrieveModels(models, optSelectedBrand.get().getId()));
			modelThread.start();
			
			//clear the related to this combo boxes and labels			
			clearColors();
			clearEngines();
			clearGearboxes();
			clearModelVersions();
			clearModels();
			
			try {
				modelThread.join();
				//set the models to the brand's set
				models.stream().forEach((model->optSelectedBrand.get().addNewModel(model)));	
				models.stream().forEach(model->modelComboBox.getItems().add(model.getName()));
				modelComboBox.setDisable(false);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	public void modelComboBoxGetValue(ActionEvent ae) {
		String selectedModel = modelComboBox.getSelectionModel().getSelectedItem();
		optSelectedmodel = models.stream().filter(model->model.getName().equals(selectedModel)).findFirst();
		if(optSelectedmodel.isPresent()) {
			modelVersions = new HashSet<>();
			Thread modelVersionThread = new Thread(new RetrieveModelVersions(modelVersions, optSelectedmodel.get().getId()));
			modelVersionThread.start();
			
			//clear the related to this combo boxes and labels			
			clearColors();
			clearEngines();
			clearGearboxes();
			clearModelVersions();
	
			try {
				modelVersionThread.join();
				//set the modelVersions to the model's set
				modelVersions.stream().forEach(mV->optSelectedmodel.get().addModelVersion(mV));
				modelVersions.stream().forEach(mV->carVersionComboBox.getItems().add(mV.getName()));
				carVersionComboBox.setDisable(false);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} 		
	}
	
	public void carVersionComboBoxGetValue(ActionEvent ae) {
		String selectedModelVersion = carVersionComboBox.getSelectionModel().getSelectedItem();
		optSelectedmodelVersion = modelVersions.stream().filter(mV->mV.getName().equals(selectedModelVersion)).findFirst();
		if(optSelectedmodelVersion.isPresent()) {
			//initialize engine combo box
			engines = new HashSet<>();
			Thread engineThread = new Thread(new RetrieveEngines(engines, optSelectedmodelVersion.get().getId()));
			engineThread.start();
			
			//clear the related to this combo boxes and labels
			clearGearboxes();
			clearEngines();
			clearColors();
			
			//initialize color combo box
			colors = optSelectedmodelVersion.get().getAvailablecolors();
			colors.stream().forEach(colorComboBox.getItems()::add);
			colorComboBox.setDisable(false);
			
			try {
				engineThread.join();
				engines.stream().forEach(engine->optSelectedmodelVersion.get().addEngineVersion(engine));
				engines.stream().forEach(engine->engineComboBox.getItems().add(engine.toString()));
				engineComboBox.setDisable(false);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void engineComboBoxGetValue(ActionEvent ae) {
		String selectedEngine = engineComboBox.getSelectionModel().getSelectedItem();
		optSelectedEngine = engines.stream().filter(eng->eng.toString().equals(selectedEngine)).findFirst();
		if(optSelectedEngine.isPresent()) {
			
			//initialize gearbox combo box
			gearboxes = new HashSet<>();
//			if(optSelectedmodelVersion.isPresent()) {
			Thread gearboxThread = new Thread(new RetrieveGearboxes(gearboxes, optSelectedmodelVersion.get().getId(), optSelectedEngine.get().getId()));
			gearboxThread.start();
//				clear all the related to this combo boxes and labels
			clearGearboxes();
			//insert the type of the engine in type label
			carTypeLabel.setText(optSelectedEngine.get().getType().toString());
			try {
				gearboxThread.join();
				gearboxes.forEach(optSelectedEngine.get()::addGearbox);
				gearboxes.stream().forEach(gb->gearboxComboBox.getItems().add(gb.name()));
				gearboxComboBox.setDisable(false);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
//			}
		}
	}
	
	public void colorComboBoxGetValue(ActionEvent ae) {
		selectedColor = colorComboBox.getSelectionModel().getSelectedItem();
		System.out.println(selectedColor);
	}
	
	public void gearboxComboBoxGetValue(ActionEvent ae) {
		String selectedGearbox = gearboxComboBox.getSelectionModel().getSelectedItem();
		optSelectedGearbox = gearboxes.stream().filter(gb->gb.name().equals(selectedGearbox)).findFirst();
		if(optSelectedGearbox.isPresent()) {
			double price = -1;
			RetrievePrice rPrice = new RetrievePrice(price, optSelectedmodelVersion.get().getId(), optSelectedEngine.get().getId(), optSelectedGearbox.get().id());
			Thread priceThread = new Thread(rPrice);
			priceThread.start();
			try {
				priceThread.join();
//			    insert the price into the price label
				priceLabel.setText(Double.toString(rPrice.getPrice()));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
/*	CLEANERS
 * clean all data from the related combo box and make the related optional empty
 */
	public void clearModels() {
		optSelectedmodel = Optional.empty();
		modelComboBox.getItems().clear();
	}
	public void clearModelVersions() {
		optSelectedmodelVersion = Optional.empty();
		carVersionComboBox.getItems().clear();
	}
	public void clearEngines() {
		optSelectedEngine = Optional.empty();
		engineComboBox.getItems().clear();
		carTypeLabel.setText("");
	}
	public void clearGearboxes() {
		optSelectedGearbox = Optional.empty();
		gearboxComboBox.getItems().clear();
		priceLabel.setText("");
	}	
	public void clearColors() {
		colorComboBox.getItems().clear();
	}
}