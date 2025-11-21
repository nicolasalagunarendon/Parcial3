package uniquindio.edu.co.poo.elparcial3.ViewController;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import uniquindio.edu.co.poo.elparcial3.App;
import uniquindio.edu.co.poo.elparcial3.model.Hospital;
import uniquindio.edu.co.poo.elparcial3.model.Medico;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

public class DoctorDashboardViewController {
    @FXML
    private BorderPane mainBorderPane;

    @FXML
    private Button btnSalir;


    @FXML
    private StackPane centerStackPane;


    @FXML
    public void initialize() {
    }



    @FXML
    private void onCitasProgramadas() {
        loadView("CitasProgramadasDoctor.fxml");
    }

    private void loadView(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/uniquindio/edu/co/poo/elparcial3/" + fxml));

            Node node = loader.load();

            Object controller = loader.getController();

            if (controller instanceof CitasProgramadasDoctorViewController) {
                ((CitasProgramadasDoctorViewController) controller)
                        .setDoctor((Medico) Hospital.getInstance().getUsuarioLogueado());
            }

            centerStackPane.getChildren().setAll(node);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

