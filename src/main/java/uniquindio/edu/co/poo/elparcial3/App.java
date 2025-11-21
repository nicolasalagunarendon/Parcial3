package uniquindio.edu.co.poo.elparcial3;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import uniquindio.edu.co.poo.elparcial3.ViewController.DoctorDashboardViewController;

import java.io.IOException;

public class App extends Application {
    private Stage primaryStage;
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("InicioSesion.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 600);
                stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }


}
