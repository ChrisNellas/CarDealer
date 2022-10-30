package controllers;

import java.io.IOException;
import java.util.Iterator;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import model.CarBrand;
import model.DBConnector;
import model.Model;
import model.ModelVersion;
import model.User;

public class AddModelVersionController extends ShortTermController{
	@FXML 
	private TextField nameField, imageField, colorField;
	@FXML
	private Button logoutBtn, backBtn, addColorBtn, addModelVersionBtn;
	@FXML
	private CheckBox fiveDoorsCB, threeDoorsCB;
	@FXML
	private ListView<String> colorList;
	@FXML 
	private Label usernameLabel;
	
	private CarBrand selectedBrand;
	private Model selectedModel;
	boolean newModelVersionCreated;
	
//	SETTERS	
	public void setSelectedBrand(CarBrand selectedBrand) {
		this.selectedBrand = selectedBrand;
	}
	
	public void setSelectedModel(Model selectedModel) {
		this.selectedModel = selectedModel;
		newModelVersionCreated = false;
	}

//	LISTENERS
	public void backBtnOnClick(ActionEvent ae) throws IOException {
		if(newModelVersionCreated) {
			((EditModelController) previousController).getModelVersions();
		} else {
//			editModelController.setModelVersions(selectedModel.getModelVersions());
			((EditModelController) previousController).setModelVersions(selectedModel.getModelVersions());
		}
		stage.close();
	}
	
	/*get user's input, clean any useless space and checks if the field are blank.
	 * if the field is blank -> show alert message
	 * otherwise check if this color is already inside the list
	 * 		exist -> show alert message
	 *		don't exist -> add color to list 
	*/
	public void addColorBtnOnClick(ActionEvent ae) {
		String givenColor = colorField.getText().strip();
		if(givenColor.isBlank()) {
			Alert emptyColorFieldAlert = new Alert(AlertType.ERROR);
			emptyColorFieldAlert.setHeaderText("please write a available color");
			emptyColorFieldAlert.show();
		} else if(colorList.getItems().contains(givenColor)){
			Alert duplicateColorAlert = new Alert(AlertType.ERROR);
			duplicateColorAlert.setHeaderText("you already have insert this color");
			duplicateColorAlert.show();
		} else {
			colorList.getItems().add(givenColor);
			colorField.clear();
		}
	}

	/* get user's inputs, clean any useless space and checks if the fields are blank. 
	 * if one or more fields is/are blank -> show alert message 
	 * otherwise search if this model version's name is unavailable 
	 * 	unavailable -> show alert message
	 * 	available -> create model version record to database
	 */ 
	public void addModelVersionBtnOnClick(ActionEvent ae) throws IOException {
		String givenName = nameField.getText().strip();
		String givenImage = imageField.getText().strip();
		Set<String> colors = colorList.getItems().stream().collect(Collectors.toSet());
		boolean fiveDoorAvail = fiveDoorsCB.isSelected();
		boolean threeDoorAvail = threeDoorsCB.isSelected();
		Set<ModelVersion> modelVersions = selectedModel.getModelVersions();
		if(!(givenName.isBlank()||givenImage.isBlank()||colors.isEmpty())) {
			if(modelVersions.stream().noneMatch(model->model.getImage().equals(givenName))) {
				insertModelVersionToDB(givenName, givenImage, fiveDoorAvail, threeDoorAvail, colors);
				backBtnOnClick(ae);
			} else {
				Alert duplicateModelVersionAlert = new Alert(AlertType.ERROR);
				duplicateModelVersionAlert.setHeaderText("This model version already exists");
				duplicateModelVersionAlert.showAndWait();
			}
		} else {
			Alert emptyFieldsOrColorListAlert = new Alert(AlertType.ERROR);
			emptyFieldsOrColorListAlert.setHeaderText("fulfill all the nameField, imageField and populate color list with at least 1 color");
			emptyFieldsOrColorListAlert.showAndWait();
		}
	}

//	INSERT MODEL VERSION TO DB 
	public void insertModelVersionToDB(String givenName, String givenImage, boolean fDAvail, boolean tDAvail, Set<String> colors) {
		DBConnector con = new DBConnector();
		String colorsFormat = "[";
		Iterator<String> it = colors.iterator();
		while(it.hasNext()) {
			colorsFormat= colorsFormat+"\""+it.next()+"\",";
		}
		colorsFormat = colorsFormat.substring(0, colorsFormat.length()-1);
		colorsFormat = colorsFormat+"]";
		
		String query = """
				INSERT INTO model_version(version_name, model_id, fiveDoorsAvail, threeDoorsAvail, image, colors) 
				VALUES('%s', %s, %b, %b, '%s', '%s')
				""".formatted(givenName, selectedModel.getId(), fDAvail, tDAvail, givenImage, colorsFormat);		
		con.updateDatabase(query);
		newModelVersionCreated = true;
	}
}
