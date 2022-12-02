import controllers.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application{
	
	public static void main(String[] args) throws InterruptedException {
		
		launch(args); // run the start method
		
	}
	
//	shows the loginPanel
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