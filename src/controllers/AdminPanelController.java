package controllers;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.CarBrand;
import model.Order;
import model.User;
import util.RetrieveBrands;
import util.RetrieveOrders;
import util.RetrieveUsers;

public class AdminPanelController extends LongTermController{
	@FXML
	private Button createUserBtn, editUserBtn, addBrandBtn, editBrandBtn;
	@FXML
	private BarChart<String, Double> salesBar;
	@FXML
	private ListView<User> userList;
	@FXML
	private ListView<CarBrand> brandList;
	@FXML
	private LineChart<String, Double> salesLine; // the bottom-left chart (probably disabled and not visible for now)
	
	private Set<Order> orders;
	private Set<CarBrand> brands;
	private Set<User> users;
	
//	 get users from an other class
	public void setUsers(Set<User> users) {			
		this.users = users;
		userList.getItems().addAll(this.users);
	}
	
//	get the users from the database
	public void setUsers() {
		users = new HashSet<>();
		Thread usersThread = new Thread(new RetrieveUsers(users));
		usersThread.start();
		try {
			usersThread.join();
			userList.getItems().addAll(this.users);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void setBrands(Set<CarBrand> brands) {
		this.brands = brands;
	}
		
//	get all orders and brands from database and populate the charts
	public void setBarChartAndBrandList() {
		orders = new HashSet<>();
		Thread ordersThread = new Thread(new RetrieveOrders(orders));
		brands = new HashSet<>();
		Thread brandsThread = new Thread(new RetrieveBrands(brands));
		brandsThread.start();
		ordersThread.start();
		try {
			brandsThread.join();
			brandList.getItems().clear();
			brandList.getItems().addAll(brands); //(CarBrand) brands.stream().map(CarBrand::getName).collect(Collectors.toSet())
			ordersThread.join();
			createSeries();			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
//	get all orders from database and populate the charts
	public void setBarChartAndBrandList(Set<CarBrand> brands) {
		orders = new HashSet<>();
		Thread ordersThread = new Thread(new RetrieveOrders(orders));
		this.brands = brands;
		ordersThread.start();
		brandList.getItems().clear();
		brandList.getItems().addAll(brands);
//		System.out.println("total cost per brand (ADMIN PANEL CONTROLLER)");
		try {
			ordersThread.join();
			createSeries();			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
//	BUTTON LISTENERS
	public void createUserBtnOnClick(ActionEvent ae) throws IOException {
		loader = new FXMLLoader(getClass().getResource("../view/CreateUser.fxml"));
		root = loader.load();
		CreateUserController createUserController = loader.getController();
//		createUserController.setUser(loggedUser);
		createUserController.setUsers(users);
		Stage createUserStage = new Stage();//(Stage) ((Node) ae.getSource()).getScene().getWindow();
		scene = new Scene(root);
		createUserStage.initModality(Modality.APPLICATION_MODAL);
		createUserController.setStage(createUserStage);
		createUserController.setController(this);
		createUserStage.setScene(scene);
		createUserStage.setTitle("CREATE USER");
		createUserStage.show();
	}
	
	public void editUserBtnOnClick(ActionEvent ae) throws IOException {
		User selectedUser = userList.getSelectionModel().getSelectedItem();
		if(selectedUser==null) { //if user does not select any user
			Alert selectUserAlert = new Alert(AlertType.ERROR);
			selectUserAlert.setHeaderText("SELECT THE USER YOU WANT TO EDIT FIRST!");
			selectUserAlert.show();
		} else {
			loader = new FXMLLoader(getClass().getResource("../view/EditUser.fxml"));
			root = loader.load();
			EditUserController editUserController = loader.getController();
			editUserController.setUser(loggedUser);
			editUserController.setUsers(users);
			editUserController.setSelectedUser(selectedUser);
			editUserController.populateRoleComboBox();
			stage = (Stage) ((Node) ae.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setTitle("EDIT USER");
			stage.setScene(scene);
			stage.show();
		}
	}
	
	public void editBrandBtnOnClick(ActionEvent ae) throws IOException {
		CarBrand selectedBrand = brandList.getSelectionModel().getSelectedItem();
		if(selectedBrand==null) { //if user does not select any brand
			Alert selectBrandAlert = new Alert(AlertType.ERROR);
			selectBrandAlert.setHeaderText("SELECT THE BRAND YOU WANT TO EDIT FIRST!");
			selectBrandAlert.show();
		} else {
			loader = new FXMLLoader(getClass().getResource("../view/EditBrand.fxml"));
			root = loader.load();
			EditBrandController editUserController = loader.getController();
			editUserController.setUser(loggedUser);
			editUserController.setUsers(users);
			editUserController.setBrands(brands);
			editUserController.setSelectedBrand(selectedBrand);
			
			stage = (Stage) ((Node) ae.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setTitle("Edit brand");
			stage.setScene(scene);
			stage.show();
		}
	}
	
	public void addBrandOnClick(ActionEvent ae) throws IOException {
		loader = new FXMLLoader(getClass().getResource("../view/AddBrand.fxml"));
		root = loader.load();
		AddBrandController addBrandsController = loader.getController();
		addBrandsController.setUser(loggedUser);
		addBrandsController.setUsers(users);
		addBrandsController.setBrands(brands);
		Stage addBrandStage = new Stage();//(Stage) ((Node)ae.getSource()).getScene().getWindow();
		addBrandsController.setStage(addBrandStage);
		addBrandsController.setController(this);
		addBrandStage.initModality(Modality.APPLICATION_MODAL);
		scene = new Scene(root);
		addBrandStage.setScene(scene);
		addBrandStage.setTitle("Add brand");
		addBrandStage.showAndWait();
	}
	
	//insert the order data into bar chart
	public void createSeries() {
		salesBar.getData().clear(); // clean all previously inserted data
		//for each brand
		brands.forEach(br->{
			Series<String, Double> series = new XYChart.Series<>();
			series.setName(br.getName());
			
			double sum;
			int currentYear = LocalDate.now().getYear();
			
			for(int i=1;i<13;i++) {
				int month = i;		
				// sum the cost of all models of the specific brand that sold 
				sum = orders.stream()
					.filter(ord->ord.getCarBrand().equals(br.getId())&&
							ord.getDate().getMonthValue()==month&&
									ord.getDate().getYear()==currentYear)
					.map(Order::getCost)
					.reduce(0.0, (a,b)->{
						return a+b;
					});
//				System.out.println(String.format("%s (%s): %f", br.getName(),i,sum));			
				series.getData().add(new Data<>(Integer.toString(i),sum));
			}
			salesBar.getData().add(series);
//			salesLine.getData().add(series);
		});		
	}
}