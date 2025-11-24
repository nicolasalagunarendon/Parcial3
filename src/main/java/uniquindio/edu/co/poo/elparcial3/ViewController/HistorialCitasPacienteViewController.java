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
    private TableColumn<Cita, String> colFecha;
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        colHora.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getHora().format(formatter)));

        colFecha.setCellValueFactory(cellData -> {
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

        colMotivo.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getDescripcion()));

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
        cmbFiltroDoctor.setOnAction(e -> aplicarFiltros());

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

        var finalizadas = listaCitas.stream()
                .filter(c -> c.getEstadoCita().getEstado().equalsIgnoreCase("finalizada"))
                .toList();


        var medicos = finalizadas.stream()
                .map(c -> c.getMedico().getNombre())
                .distinct()
                .sorted()
                .collect(Collectors.toList());

        medicos.add(0, "Todos");

        cmbFiltroDoctor.setItems(FXCollections.observableArrayList(medicos));
        cmbFiltroDoctor.setValue("Todos");
    }


    private void aplicarFiltros() {
        if (listaCitas == null) return;

        listaCitasFiltradas.clear(); // Limpiar antes de volver a filtrar

        String periodo = cmbFiltroPeriodo.getValue();
        String medico = cmbFiltroDoctor.getValue();

        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime fechaLimite = switch (periodo) {
            case "Último mes" -> ahora.minusMonths(1);
            case "Últimos 3 meses" -> ahora.minusMonths(3);
            case "Últimos 6 meses" -> ahora.minusMonths(6);
            case "Este año" -> ahora.withDayOfYear(1);
            default -> null; // "Todos"
        };

        for (Cita cita : listaCitas) {
            boolean cumpleEstado = cita.getEstadoCita().getEstado().equalsIgnoreCase("finalizada");
            // Filtro por periodo
            boolean cumplePeriodo = fechaLimite == null || !cita.getFecha().isBefore(ChronoLocalDate.from(fechaLimite));

            // Filtro por médico
            boolean cumpleMedico = medico == null || medico.equals("Todos") ||
                    cita.getMedico().getNombre().equals(medico);

            if (cumplePeriodo && cumpleMedico && cumpleEstado) {
                listaCitasFiltradas.add(cita);
            }
        }

        // Ordenar de más reciente a más antigua
        listaCitasFiltradas.sort((c1, c2) -> c2.getFecha().compareTo(c1.getFecha()));

        tablaHistorial.setItems(listaCitasFiltradas);
    }


    private void actualizarEstadisticas() {

        // ✔️ Solo citas finalizadas
        var finalizadas = listaCitas.stream()
                .filter(c -> c.getEstadoCita().getEstado().equalsIgnoreCase("finalizada"))
                .toList();

        // Total de citas finalizadas
        lblTotalCitas.setText(String.valueOf(finalizadas.size()));

        // Última cita finalizada
        if (!finalizadas.isEmpty()) {
            Cita ultima = finalizadas.stream()
                    .max((c1, c2) -> c1.getFecha().compareTo(c2.getFecha()))
                    .orElse(null);

            if (ultima != null) {
                lblUltimaCita.setText(
                        ultima.getFecha().format(DateTimeFormatter.ofPattern("dd/MMM"))
                );
            }
        } else {
            lblUltimaCita.setText("—");
        }

        // Médicos únicos en citas finalizadas
        long medicosUnicos = finalizadas.stream()
                .map(c -> c.getMedico().getId())
                .distinct()
                .count();
        lblDoctores.setText(String.valueOf(medicosUnicos));

        // Citas finalizadas en el año actual
        LocalDateTime inicioAnio = LocalDateTime.now()
                .withDayOfYear(1)
                .withHour(0)
                .withMinute(0);

        long citasAnio = finalizadas.stream()
                .filter(c -> c.getFecha().isAfter(ChronoLocalDate.from(inicioAnio)))
                .count();
        lblCitasAnio.setText(String.valueOf(citasAnio));
    }



}


