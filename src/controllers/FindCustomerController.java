package controllers;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

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
import model.Customer;
import model.User;
import util.RetrieveCustomers;

// MEXRI EDW DOKIMASE TO 

public class FindCustomerController extends LongTermController{
	
	private final String ERROR_MESSAGE = "Please select a user from above";
	@FXML
	private Button backBtn, getCustomerBtn, filterBtn, createCustomerBtn;
	@FXML
	private ListView<Customer> listView;
	@FXML
	private Label errorLabel;
	@FXML 
	private TextField idField, nameField, addressField;
	
	private Set<Customer> customers;
	private String givenName, givenAddress;
	private Long givenId;
	
	private String destination;
	
	private Customer selectedCustomer;
	
//	SETTERS
	public void setDestination(String dest) {
		destination = dest;
	}
	
	// set all the customers into the list (from db)
	public void populateList()  {
		customers = new HashSet<>();
		Thread thread = new Thread(new RetrieveCustomers(customers));
		thread.start();
		listView.getItems().clear();
		try {
			thread.join();
			customers.forEach(listView.getItems()::add);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	// set all the customers into the list 
	// if user try to add new user but he/she press the backBtn
		public void populateList(Set<Customer> customers)  {
			listView.getItems().clear();
			this.customers = customers;
		}
	

//	LISTENERS
	public void backBtnOnClick(ActionEvent ae) throws IOException {
		loader = new FXMLLoader(getClass().getResource("../view/MainMenu.fxml"));
		root = loader.load();
		MainMenuController controller = loader.getController();
		controller.setUser(loggedUser);
		scene = new Scene(root);
		stage = (Stage) ((Node)ae.getSource()).getScene().getWindow();
		stage.setScene(scene);
		stage.setTitle("Create new order");
		stage.show();
	}
	
	public void getCustomerOnClick(ActionEvent ae) throws IOException {
		selectedCustomer = listView.getSelectionModel().getSelectedItem();
		if(selectedCustomer!=null) {
			stage = (Stage) ((Node)ae.getSource()).getScene().getWindow();
			loader = new FXMLLoader(getClass().getResource("../view/"+destination));
			root = loader.load();
			if(destination.equals("CreateNewCarOrder.fxml")) {
				CreateNewCarOrderController createNewCarOrderController = loader.getController();
				createNewCarOrderController.getBrands();
				createNewCarOrderController.getCustomer(selectedCustomer);
				createNewCarOrderController.setFoundTrue();
				createNewCarOrderController.setUser(loggedUser);
				stage.setTitle("CREATE NEW ORDER");
			} else {
				CustomerPageController customerPageController = loader.getController();
				customerPageController.getCustomer(selectedCustomer);
				customerPageController.setUser(loggedUser);
				stage.setTitle("CUSTOMER");
			}
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		} else {
			errorLabel.setText(ERROR_MESSAGE);
		}
	}
	
	/* filter the list with this order: 
	 * 	id (if id >=0 then i use only this)
	 * 	name
	 * 	address
	 */
	public void filterBtOnClick(ActionEvent ae) {
		String givenIdText = idField.getText().strip();
		givenId = -1L;
		if(!givenIdText.isBlank()) {
			try {
				givenId = Long.parseLong(givenIdText);
			} catch(NumberFormatException nFEx) {
				Alert idNotNumberFormatAlert = new Alert(AlertType.ERROR);
				idNotNumberFormatAlert.setHeaderText("id must be a number. Please insert a number to id field");
				idNotNumberFormatAlert.showAndWait();
			}	
		}
		givenName = nameField.getText().strip();
		givenAddress = addressField.getText().strip();
		
		Stream<Customer> filteredSet = customers.stream();
		if(givenId<=0) {
			if(!givenName.isBlank()) {
				filteredSet = filteredSet
						.filter(cust->cust.getName().contains(givenName));
			}
			
			if(!givenAddress.isBlank()) {
				filteredSet = filteredSet
						.filter(cust->cust.getAddress().contains(givenAddress));
			}
		} else {
			filteredSet = filteredSet.filter(cust->cust.getId()==givenId);
		}
		listView.getItems().clear();
		filteredSet.forEach(listView.getItems()::add);
	}
	
	public void createCustomerBtnOnClick(ActionEvent ae) throws IOException {
		loader = new FXMLLoader(getClass().getResource("../view/CreateCustomer.fxml"));
		root = loader.load();
		CreateCustomerController createCustController = loader.getController();
		createCustController.setUser(loggedUser);
		createCustController.setCustomers(customers);
		Stage createCustomerStage = new Stage();//(Stage) ((Node) ae.getSource()).getScene().getWindow();
		createCustController.setStage(createCustomerStage);
		createCustController.setController(this);
		createCustomerStage.initModality(Modality.APPLICATION_MODAL);
		scene = new Scene(root);		
		createCustomerStage.setTitle("NEW CUSTOMER");
		createCustomerStage.setScene(scene);
		createCustomerStage.showAndWait();
	}
}
