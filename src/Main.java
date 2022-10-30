import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import controllers.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.DBConnector;

public class Main extends Application{
	
	public static void main(String[] args) throws InterruptedException {
//		DBConnector con = new DBConnector();
//		con.sendQuery("select * from model_version");
//		ResultSet rs = con.getResults().get();
//		try {
//			while(rs.next()) {
//				System.out.println(rs.getString("colors"));
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
		launch(args);
		
//		DBConnector con = new DBConnector();
//		con.sendQuery("SELECT colors from model_version where version_id=6");
//		Optional<ResultSet> optRs =  con.getResults();
//		if(optRs.isPresent()) {
//			ResultSet rs = optRs.get();
//			try {
//				while(rs.next()) {
//					System.out.println(rs.getString(1));
//				}
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		}
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("view/LoginPanel.fxml"));
		Parent root = loader.load();
		LoginController controller = loader.getController();
		controller.getUsers();
		Scene scene = new Scene(root);
//		primaryStage.initStyle(StageStyle.TRANSPARENT);
		primaryStage.setScene(scene);
		primaryStage.setTitle("LOGIN");
		primaryStage.show();	
	}
}