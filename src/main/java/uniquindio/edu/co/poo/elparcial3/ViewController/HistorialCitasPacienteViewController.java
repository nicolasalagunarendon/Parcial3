package uniquindio.edu.co.poo.elparcial3.ViewController;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import uniquindio.edu.co.poo.elparcial3.model.Cita;
import uniquindio.edu.co.poo.elparcial3.model.Paciente;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.chrono.ChronoLocalDate;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

public class HistorialCitasPacienteViewController {

    @FXML
    private Label lblTotalCitas;
    @FXML
    private Label lblUltimaCita;
    @FXML
    private Label lblDoctores; // ID se respeta
    @FXML
    private Label lblCitasAnio;

    @FXML
    private ComboBox<String> cmbFiltroPeriodo;
    @FXML
    private ComboBox<String> cmbFiltroDoctor; // ID se respeta

    @FXML
    private TableView<Cita> tablaHistorial;
    @FXML
    private TableColumn<Cita, LocalDateTime> colFecha;
    @FXML
    private TableColumn<Cita, String> colHora;
    @FXML
    private TableColumn<Cita, String> colMedico;

    @FXML
    private TableColumn<Cita, String> colMotivo;

    @FXML
    private TableColumn<Cita, String> colEstado;


    private ObservableList<Cita> listaCitas;
    private ObservableList<Cita> listaCitasFiltradas;
    private Paciente pacienteActual;

    @FXML
    public void initialize() {
        listaCitasFiltradas = FXCollections.observableArrayList();
        configurarTabla();
        configurarFiltros();

        listaCitas = FXCollections.observableArrayList();

    }

    public void setPaciente(Paciente paciente) {
        this.pacienteActual = paciente;
        cargarHistorial();
    }

    private void configurarTabla() {
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

        colHora.setCellValueFactory(cellData -> {
            LocalDate fecha = cellData.getValue().getFecha();
            return new javafx.beans.property.SimpleStringProperty(
                    fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            );
        });

        // Columna Médico (ya estaba bien)
        colMedico.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getMedico().getNombre()
                )
        );

        colMotivo.setCellValueFactory(new PropertyValueFactory<>("descripcion"));

        colEstado.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getEstadoCita().getEstado()));
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
                        case "finalizada":
                            emoji = "✅";
                            color = "-fx-text-fill: #27ae60;";
                            break;
                        case "cancelada":
                            emoji = "❌";
                            color = "-fx-text-fill: #e74c3c;";
                            break;
                        default:
                            emoji = "⏰";
                            color = "-fx-text-fill: #95a5a6;";
                    }

                    setText(emoji + " " + estado);
                    setStyle(color + " -fx-font-weight: bold;");
                }
            }
        });

        //

    }

    private void configurarFiltros() {
        cmbFiltroPeriodo.setItems(FXCollections.observableArrayList(
                "Todos", "Último mes", "Últimos 3 meses", "Últimos 6 meses", "Este año"
        ));
        cmbFiltroPeriodo.setValue("Todos");

        cmbFiltroPeriodo.setOnAction(e -> aplicarFiltros());
        cmbFiltroDoctor.setOnAction(e -> aplicarFiltros()); // ID respetado

    }

    private void cargarHistorial() {
        if (pacienteActual == null) return;

        listaCitas.clear();
        listaCitas.addAll(pacienteActual.getHistorialMedico());
        cargarFiltrosDinamicos();
        aplicarFiltros();
        actualizarEstadisticas();
    }

    private void cargarFiltrosDinamicos() {
        // Cargar médicos únicos
        var medicos = listaCitas.stream()
                .map(c -> c.getMedico().getNombre())
                .distinct()
                .sorted()
                .collect(Collectors.toList());
        medicos.add(0, "Todos");
        cmbFiltroDoctor.setItems(FXCollections.observableArrayList(medicos)); // ID respetado
        cmbFiltroDoctor.setValue("Todos");

    }

    private void aplicarFiltros() {
        if (listaCitas == null) return;

        listaCitasFiltradas.clear(); // Limpiar antes de volver a filtrar

        String periodo = cmbFiltroPeriodo.getValue();
        String medico = cmbFiltroDoctor.getValue(); // ID respetado

        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime fechaLimite = switch (periodo) {
            case "Último mes" -> ahora.minusMonths(1);
            case "Últimos 3 meses" -> ahora.minusMonths(3);
            case "Últimos 6 meses" -> ahora.minusMonths(6);
            case "Este año" -> ahora.withDayOfYear(1);
            default -> null; // "Todos"
        };

        for (Cita cita : listaCitas) {
            // Filtro por periodo
            boolean cumplePeriodo = fechaLimite == null || !cita.getFecha().isBefore(ChronoLocalDate.from(fechaLimite));

            // Filtro por médico
            boolean cumpleMedico = medico == null || medico.equals("Todos") ||
                    cita.getMedico().getNombre().equals(medico);

            if (cumplePeriodo && cumpleMedico) {
                listaCitasFiltradas.add(cita);
            }
        }

        // Ordenar de más reciente a más antigua
        listaCitasFiltradas.sort((c1, c2) -> c2.getFecha().compareTo(c1.getFecha()));

        tablaHistorial.setItems(listaCitasFiltradas);
    }


    private void actualizarEstadisticas() {
        lblTotalCitas.setText(String.valueOf(listaCitas.size()));

        if (!listaCitas.isEmpty()) {
            Cita ultima = listaCitas.stream()
                    .max((c1, c2) -> c1.getFecha().compareTo(c2.getFecha()))
                    .orElse(null);

            if (ultima != null) {
                lblUltimaCita.setText(ultima.getFecha().format(
                        DateTimeFormatter.ofPattern("dd/MMM")
                ));
            }
        }

        // Médicos únicos (ID lblDoctores se respeta)
        long medicosUnicos = listaCitas.stream()
                .map(c -> c.getMedico().getId())
                .distinct()
                .count();
        lblDoctores.setText(String.valueOf(medicosUnicos));

        LocalDateTime inicioAnio = LocalDateTime.now().withDayOfYear(1).withHour(0).withMinute(0);
        long citasAnio = listaCitas.stream()
                .filter(c -> c.getFecha().isAfter(ChronoLocalDate.from(inicioAnio)))
                .count();
        lblCitasAnio.setText(String.valueOf(citasAnio));
    }

    private void verDetallesCita(Cita cita) {
        Alert detalles = new Alert(Alert.AlertType.INFORMATION);
        detalles.setTitle("Detalles de la Cita");
        detalles.setHeaderText("Información Completa");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy ");

        String contenido = String.format(
                "Fecha: %s\n" +
                        "Médico: %s\n" +
                        "Especialidad: %s\n" +
                        "Descripción: %s\n" +
                        "Estado: %s",
                cita.getFecha().format(formatter),
                cita.getMedico().getNombre(),
                cita.getDescripcion(),
                cita.getEstadoCita().getEstado()
        );

        detalles.setContentText(contenido);
        detalles.showAndWait();
    }

    @FXML
    private void exportarPDF() {
        Alert info = new Alert(Alert.AlertType.INFORMATION);
        info.setTitle("Exportar Historial");
        info.setHeaderText("Funcionalidad en desarrollo");
        info.setContentText("La exportación a PDF estará disponible próximamente.");
        info.showAndWait();
    }
}


