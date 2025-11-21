package uniquindio.edu.co.poo.elparcial3.ViewController;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import uniquindio.edu.co.poo.elparcial3.model.Cita;
import uniquindio.edu.co.poo.elparcial3.model.Hospital;
import uniquindio.edu.co.poo.elparcial3.model.Medico;

import java.time.LocalDateTime;
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
    private Button btnActualizar;

    @FXML
    private TableView<Cita> tablaCitas;
    @FXML
    private TableColumn<Cita, LocalDateTime> colFecha;
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
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colFecha.setCellFactory(column -> new TableCell<Cita, LocalDateTime>() {
            private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            @Override
            protected void updateItem(LocalDateTime fecha, boolean empty) {
                super.updateItem(fecha, empty);
                if (empty || fecha == null) {
                    setText(null);
                } else {
                    setText(fecha.format(formatter));
                }
            }
        });

        colPaciente.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getPaciente().getNombre()));

        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));

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
                        case "en proceso":
                            emoji = "🩺";
                            color = "-fx-text-fill: #3498db;";
                            break;
                        case "finalizada":
                            emoji = "✅";
                            color = "-fx-text-fill: #27ae60;";
                            break;
                        case "cancelada":
                            emoji = "❌";
                            color = "-fx-text-fill: #e74c3c;";
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
                case "proceso":
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


    private void iniciarCita(Cita cita) {
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Iniciar Cita");
        confirmacion.setHeaderText("¿Deseas iniciar esta cita?");
        confirmacion.setContentText("Paciente: " + cita.getPaciente().getNombre() + "\n" +
                "Descripción: " + cita.getDescripcion());

        confirmacion.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                   // cita.iniciar(); // Patrón State: cambia a EnProcesoState
                    tablaCitas.refresh();
                    actualizarEstadisticas();

                    mostrarNotificacion("Cita iniciada exitosamente", Alert.AlertType.INFORMATION);
                } catch (Exception e) {
                    mostrarNotificacion("Error: " + e.getMessage(), Alert.AlertType.ERROR);
                }
            }
        });
    }

    private void finalizarCita(Cita cita) {
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Finalizar Cita");
        confirmacion.setHeaderText("¿Deseas finalizar esta cita?");
        confirmacion.setContentText("Paciente: " + cita.getPaciente().getNombre());

        confirmacion.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    //cita.finalizar(); // Patrón State: cambia a FinalizadaState
                    tablaCitas.refresh();
                    actualizarEstadisticas();

                    mostrarNotificacion("Cita finalizada exitosamente", Alert.AlertType.INFORMATION);
                } catch (Exception e) {
                    mostrarNotificacion("Error: " + e.getMessage(), Alert.AlertType.ERROR);
                }
            }
        });
    }

    private void mostrarNotificacion(String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(tipo == Alert.AlertType.ERROR ? "Error" : "Éxito");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
