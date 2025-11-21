package uniquindio.edu.co.poo.elparcial3.ViewController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.Node;
import uniquindio.edu.co.poo.elparcial3.App;
import uniquindio.edu.co.poo.elparcial3.model.Hospital;
import uniquindio.edu.co.poo.elparcial3.model.Medico;

import javax.swing.*;
import java.io.IOException;
public class InicioSesionViewController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private TextField passwordTextField;
    @FXML private Button togglePasswordButton;
    @FXML private Label errorLabel;
    @FXML private Button loginButton;
    @FXML private CheckBox rememberMeCheckbox;



    private boolean passwordVisible = false;

    // Toggle de visibilidad de la contraseña
    @FXML
    private void togglePasswordVisibility(ActionEvent event) {
        if (passwordVisible) {
            passwordField.setText(passwordTextField.getText());
            passwordField.setVisible(true);
            passwordField.setManaged(true);

            passwordTextField.setVisible(false);
            passwordTextField.setManaged(false);
            passwordVisible = false;
        } else {
            passwordTextField.setText(passwordField.getText());
            passwordTextField.setVisible(true);
            passwordTextField.setManaged(true);

            passwordField.setVisible(false);
            passwordField.setManaged(false);
            passwordVisible = true;
        }
    }



    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordVisible ? passwordTextField.getText() : passwordField.getText();


        if (Hospital.getInstance().loginPersona(username, password)) {

            String fxmlPath = null;

            if (Hospital.getInstance().buscarMedico(username) != null) {
                // Es médico
                fxmlPath = "/uniquindio/edu/co/poo/elparcial3/DoctorDashboard.fxml";
            } else if (Hospital.getInstance().buscarPaciente(username) != null) {
                // Es paciente
                fxmlPath = "/uniquindio/edu/co/poo/elparcial3/PacienteDashboard.fxml";
            } else {
                showError("No se pudo determinar el tipo de usuario.");
                return;
            }

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
                Scene scene = new Scene(loader.load());

                // Obtener el Stage actual
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
                showError("Error al cargar el dashboard.");
            }

        } else {
            showError("Usuario o contraseña incorrectos.");
        }


    }





    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
        errorLabel.setManaged(true);
    }

}
