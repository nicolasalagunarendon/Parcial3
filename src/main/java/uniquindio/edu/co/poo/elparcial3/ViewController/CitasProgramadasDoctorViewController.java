package uniquindio.edu.co.poo.elparcial3.ViewController;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import uniquindio.edu.co.poo.elparcial3.model.Cita;
import uniquindio.edu.co.poo.elparcial3.model.EstadosCita.Consulta;
import uniquindio.edu.co.poo.elparcial3.model.EstadosCita.Pendiente;
import uniquindio.edu.co.poo.elparcial3.model.Hospital;
import uniquindio.edu.co.poo.elparcial3.model.Medico;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class CitasProgramadasDoctorViewController {

    @FXML
    private Label lblFechaActual;
    @FXML
    private Label lblTotalCitas;
    @FXML
    private Label lblCitasPendientes;
    @FXML
    private Label lblCitasEnProceso;
    @FXML
    private Label lblCitasFinalizadas;


    @FXML
    private Button btnIniciarCita;

    @FXML
    private Button btnFinalizarCita;

    @FXML
    private TableView<Cita> tablaCitas;
    @FXML
    private TableColumn<Cita, String> colFecha;
    @FXML
    private TableColumn<Cita, String> colPaciente;
    @FXML
    private TableColumn<Cita, String> colDescripcion;
    @FXML
    private TableColumn<Cita, String> colEstado;

    private ObservableList<Cita> listaCitas;
    private Medico medicoActual;
    private Hospital hospitalActual = Hospital.getInstance();

    @FXML
    public void initialize(){
        configurarTabla();
        actualizarFechaActual();

        listaCitas = FXCollections.observableArrayList();
        tablaCitas.setItems(listaCitas);

        btnIniciarCita.setOnAction(e -> {
            Cita seleccionada = tablaCitas.getSelectionModel().getSelectedItem();
            if (seleccionada != null) {
                iniciarCita(seleccionada);
            } else {
                mostrarNotificacion("Por favor selecciona una cita.", Alert.AlertType.WARNING);
            }
        });

        btnFinalizarCita.setOnAction(e -> {
            Cita seleccionada = tablaCitas.getSelectionModel().getSelectedItem();
            if (seleccionada != null) {
                finalizarCita(seleccionada);
            } else {
                mostrarNotificacion("Por favor selecciona una cita.", Alert.AlertType.WARNING);
            }
        });
    }

    private void iniciarCita(Cita cita) {
        if (!(cita.getEstadoCita() instanceof Pendiente)) {
            mostrarNotificacion("Solo puedes iniciar citas que estén Pendientes.", Alert.AlertType.WARNING);
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Iniciar Cita");
        confirmacion.setHeaderText("¿Deseas iniciar esta cita?");
        confirmacion.setContentText("Paciente: " + cita.getPaciente().getNombre());

        confirmacion.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {

                cita.getEstadoCita().manejar(cita);

                tablaCitas.refresh();
                actualizarEstadisticas();
                mostrarNotificacion("La cita ha iniciado (ahora está en Consulta).", Alert.AlertType.INFORMATION);
            }
        });
    }

    private void finalizarCita(Cita cita) {
        if (!(cita.getEstadoCita() instanceof Consulta)) {
            mostrarNotificacion("Solo puedes finalizar citas que estén en Consulta.", Alert.AlertType.WARNING);
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Finalizar Cita");
        confirmacion.setHeaderText("¿Deseas finalizar esta cita?");
        confirmacion.setContentText("Paciente: " + cita.getPaciente().getNombre());

        confirmacion.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {

                cita.getEstadoCita().manejar(cita);
                cita.getPaciente().eliminarCita(cita);
                tablaCitas.refresh();
                actualizarEstadisticas();
                mostrarNotificacion("La cita ha sido finalizada.", Alert.AlertType.INFORMATION);
            }
        });

    }

    public void setDoctor(Medico medico) {
        System.out.println("Llamando al método setDoctor");
        this.medicoActual = medico;
        cargarCitas();
        actualizarEstadisticas();
    }

    private void cargarCitas() {
        if (medicoActual == null) return;

        listaCitas.clear();
        listaCitas.addAll(medicoActual.getCitasHoy());
        tablaCitas.refresh();

    }

    private void configurarTabla() {
        // Configurar columnas
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        colFecha.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getHora().format(formatter)));

        colPaciente.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getPaciente().getNombre()));

        colDescripcion.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDescripcion()));

        colEstado.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getEstadoCita().getEstado()));
        colEstado.setCellFactory(column -> new TableCell<Cita, String>() {
            @Override
            protected void updateItem(String estado, boolean empty) {
                super.updateItem(estado, empty);
                if (empty || estado == null) {
                    setText(null);
                    setStyle("");
                } else {
                    String emoji = "";
                    String color = "";

                    switch (estado.toLowerCase()) {
                        case "pendiente":
                            emoji = "⏰";
                            color = "-fx-text-fill: #e67e22;";
                            break;
                        case "consulta":
                            emoji = "🩺";
                            color = "-fx-text-fill: #3498db;";
                            break;
                        case "finalizada":
                            emoji = "✅";
                            color = "-fx-text-fill: #27ae60;";
                            break;
                    }

                    setText(emoji + " " + estado);
                    setStyle(color + " -fx-font-weight: bold;");
                }
            }




    });
    }
    private void actualizarEstadisticas() {
        int total = listaCitas.size();
        int pendientes = 0;
        int Proceso = 0;
        int finalizadas = 0;
        for(Cita cita: medicoActual.getCitasHoy()) {
            String estado = cita.getEstadoCita().getEstado().toLowerCase();

            switch (estado) {
                case "pendiente":
                    pendientes++;
                    break;
                case "consulta":
                    Proceso++;
                    break;
                case "finalizada":
                    finalizadas++;
                    break;
            }
        }

        lblTotalCitas.setText(String.valueOf(total));
        lblCitasPendientes.setText(String.valueOf(pendientes));
        lblCitasEnProceso.setText(String.valueOf(Proceso));
        lblCitasFinalizadas.setText(String.valueOf(finalizadas));
    }

    private void actualizarFechaActual() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, dd 'de' MMMM yyyy",
                new java.util.Locale("es", "ES"));
        lblFechaActual.setText(LocalDateTime.now().format(formatter));
    }




    private void mostrarNotificacion(String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(tipo == Alert.AlertType.ERROR ? "Error" : "Éxito");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
