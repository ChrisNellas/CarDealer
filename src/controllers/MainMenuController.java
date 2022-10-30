package controllers;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import model.User;

public class MainMenuController extends LongTermController{ 
	@FXML
	private Button newOrderBtn, findCustomerBtn; //, logoutBtn
	
	public void newOrderBtnOnClick(ActionEvent ae) throws IOException {
		loader = new FXMLLoader(getClass().getResource("../view/FindCustomer.fxml"));
		root = loader.load();
		FindCustomerController findCustomerController = loader.getController();
		findCustomerController.setDestination("CreateNewCarOrder.fxml");
		findCustomerController.setUser(loggedUser);
		findCustomerController.populateList();
		scene = new Scene(root);
		stage = (Stage) ((Node)ae.getSource()).getScene().getWindow();
		stage.setScene(scene);
		stage.setTitle("Find customer");
		stage.show();		
	}
	
	public void findCustomerBtnOnClick(ActionEvent ae) throws IOException {
		loader = new FXMLLoader(getClass().getResource("../view/FindCustomer.fxml"));
		root = loader.load();
		FindCustomerController findCustomerController = loader.getController();
		findCustomerController.setDestination("CustomerPage.fxml");
		findCustomerController.setUser(loggedUser);
		findCustomerController.populateList();
		scene = new Scene(root);
		stage = (Stage) ((Node)ae.getSource()).getScene().getWindow();
		stage.setScene(scene);
		stage.setTitle("Find customer");
		stage.show();
	}
}
