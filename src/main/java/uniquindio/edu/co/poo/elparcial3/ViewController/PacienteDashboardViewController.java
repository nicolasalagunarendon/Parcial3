package uniquindio.edu.co.poo.elparcial3.ViewController;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import uniquindio.edu.co.poo.elparcial3.model.Hospital;
import uniquindio.edu.co.poo.elparcial3.model.Medico;
import uniquindio.edu.co.poo.elparcial3.model.Paciente;

import java.awt.event.ActionEvent;
import java.io.IOException;

public class PacienteDashboardViewController {
    @FXML
    private BorderPane mainBorderPane;

    @FXML
    private StackPane centerStackPane;

    @FXML
    public void initialize() {
    }

    @FXML
    private void onHistorialCitas() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/uniquindio/edu/co/poo/elparcial3/HistorialCitasPaciente.fxml"));
            Node node = loader.load();

            HistorialCitasPacienteViewController controller = loader.getController();
            controller.setPaciente((Paciente)Hospital.getInstance().getUsuarioLogueado());

            centerStackPane.getChildren().setAll(node);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void onCitasProgramadas() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/uniquindio/edu/co/poo/elparcial3/CitasProgramadasPaciente.fxml"));
            Node node = loader.load();

            CitasProgramadasPacienteViewController controller = loader.getController();
            controller.setPaciente((Paciente)Hospital.getInstance().getUsuarioLogueado());

            centerStackPane.getChildren().setAll(node);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void onSolicitarCita() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/uniquindio/edu/co/poo/elparcial3/SolicitarCitaPaciente.fxml"));
            Node node = loader.load();

            SolicitarCitaPacienteViewController controller = loader.getController();
            controller.setPaciente((Paciente)Hospital.getInstance().getUsuarioLogueado());

            centerStackPane.getChildren().setAll(node);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}

