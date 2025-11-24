package uniquindio.edu.co.poo.elparcial3.ViewController;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import uniquindio.edu.co.poo.elparcial3.model.Cita;
import uniquindio.edu.co.poo.elparcial3.model.EstadosCita.Pendiente;
import uniquindio.edu.co.poo.elparcial3.model.Hospital;
import uniquindio.edu.co.poo.elparcial3.model.Medico;
import uniquindio.edu.co.poo.elparcial3.model.Paciente;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

public class SolicitarCitaPacienteViewController {
    @FXML
    private ComboBox<Medico> cmbMedico;
    
    @FXML
    private Label lblInfoMedico;

    @FXML
    private DatePicker dpFecha;

    @FXML
    private FlowPane flowHorarios;

    @FXML
    private TextArea txtMotivo;

    @FXML
    private Label lblResumenMedico;
    @FXML
    private Label lblResumenFecha;


    @FXML
    private Button btnCancelar;
    @FXML
    private Button btnConfirmar;

    private Paciente pacienteActual;
    private Medico medicoSeleccionado;
    private LocalTime horaSeleccionada;
    private ObservableList<Medico> listaDoctores;
    private ObservableList<String> listaHoras;

    /*
    @FXML
    public void initialize() {
        configurarCombos();
        listaDoctores = FXCollections.observableArrayList(Hospital.getInstance().getAllMedicos());
        cmbMedico.setItems(listaDoctores);
        configurarDatePicker();
        cargarHorariosDisponibles();
    }
     */

    @FXML
    public void initialize() {
        configurarCombos();
        listaDoctores = FXCollections.observableArrayList(Hospital.getInstance().getAllMedicos());
        cmbMedico.setItems(listaDoctores);
        configurarDatePicker();

        configurarEventos(); // <-- IMPORTANTÍSIMO

        // Al iniciar, no cargues horarios sin un médico seleccionado
    }


    public void setPaciente(Paciente paciente) {
        this.pacienteActual = paciente;
    }

    private void configurarCombos() {
        // ComboBox de médico con formato personalizado
        cmbMedico.setCellFactory(param -> new ListCell<Medico>() {
            @Override
            protected void updateItem(Medico medico, boolean empty) {
                super.updateItem(medico, empty);
                if (empty || medico == null) {
                    setText(null);
                } else {
                    setText(medico.getNombre());
                }
            }
        });

        cmbMedico.setButtonCell(new ListCell<Medico>() {
            @Override
            protected void updateItem(Medico medico, boolean empty) {
                super.updateItem(medico, empty);
                if (empty || medico == null) {
                    setText(null);
                } else {
                    setText(medico.getNombre());
                }
            }
        });
    }

    private void configurarDatePicker() {
        // Deshabilitar fechas pasadas
        dpFecha.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(LocalDate.now()));
            }
        });
    }

    private void configurarEventos() {

        cmbMedico.setOnAction(e -> {
            medicoSeleccionado = cmbMedico.getValue();
            if (medicoSeleccionado != null) {
                actualizarResumenMedico(medicoSeleccionado);

                if (dpFecha.getValue() != null) {
                    cargarHorariosDisponibles();
                }
            }
        });

        dpFecha.setOnAction(e -> {
            if (medicoSeleccionado != null && dpFecha.getValue() != null) {
                cargarHorariosDisponibles();
                actualizarResumenFecha();
            }
        });

        btnCancelar.setOnAction(e -> cancelar());
        btnConfirmar.setOnAction(e -> confirmarCita());

        txtMotivo.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.length() > 500) {
                txtMotivo.setText(oldVal);
            }
            validarFormulario();
        });
    }


    private void configurarValidaciones() {
        // Inicialmente el botón confirmar está deshabilitado
        btnConfirmar.setDisable(true);

        // Validar que todos los campos estén completos
        txtMotivo.textProperty().addListener((obs, oldVal, newVal) -> validarFormulario());
    }

    private void validarFormulario() {
        boolean valido = medicoSeleccionado != null &&
                dpFecha.getValue() != null &&
                horaSeleccionada != null &&
                txtMotivo.getText() != null && !txtMotivo.getText().trim().isEmpty();

        btnConfirmar.setDisable(!valido);
    }



    private void cargarHorariosDisponibles() {
        flowHorarios.getChildren().clear();

        if (medicoSeleccionado == null || dpFecha.getValue() == null) {
            return;
        }

        LocalDate fecha = dpFecha.getValue();

        // 1. HORAS BASE DEL MÉDICO
        List<LocalTime> horasBase = medicoSeleccionado.getHorasDisponibles();

        // 2. HORAS YA OCUPADAS (sacadas del hospital, NO del médico)
        List<LocalTime> horasOcupadas = Hospital.getInstance()
                .getListaCitas()
                .stream()
                .filter(c -> c.getMedico().equals(medicoSeleccionado)
                        && c.getFecha().equals(fecha))
                .map(Cita::getHora)
                .toList();

        // 3. HORAS FILTRADAS
        List<LocalTime> horasDisponibles = horasBase.stream()
                .filter(h -> !horasOcupadas.contains(h))
                .toList();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        for (LocalTime hora : horasDisponibles) {
            Button btnHora = new Button(hora.format(formatter));
            btnHora.setStyle("-fx-background-color: #ecf0f1; -fx-text-fill: #2c3e50; -fx-background-radius: 6; -fx-padding: 8 15; -fx-cursor: hand;");
            btnHora.setOnAction(e -> seleccionarHorario(hora, btnHora));
            flowHorarios.getChildren().add(btnHora);
        }
    }


    private void seleccionarHorario(LocalTime hora, Button botonSeleccionado) {
        horaSeleccionada = hora;

        // Resetear estilo de todos los botones
        flowHorarios.getChildren().forEach(node -> {
            if (node instanceof Button) {
                ((Button) node).setStyle("-fx-background-color: #ecf0f1; -fx-text-fill: #2c3e50; " +
                        "-fx-background-radius: 6; -fx-padding: 8 15; -fx-cursor: hand;");
            }
        });

        // Resaltar botón seleccionado
        botonSeleccionado.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; " +
                "-fx-background-radius: 6; -fx-padding: 8 15; -fx-cursor: hand; " +
                "-fx-font-weight: bold;");

        actualizarResumenFecha();
        validarFormulario();
    }

    private void actualizarResumenMedico(Medico doctor) {
        lblResumenMedico.setText(doctor.getNombre());
    }

    private void actualizarResumenFecha() {
        if (dpFecha.getValue() != null && horaSeleccionada != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy",
                    new java.util.Locale("es"));
            LocalDateTime fechaHora = LocalDateTime.of(dpFecha.getValue(), horaSeleccionada);
            lblResumenFecha.setText(fechaHora.format(formatter));
        }
    }

    @FXML
    private void confirmarCita() {
        // Validación final
        if (!validarDatos()) {
            return;
        }

        // Crear alerta de confirmación
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar Cita");
        confirmacion.setHeaderText("¿Deseas confirmar esta cita?");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        LocalDateTime fechaHora = LocalDateTime.of(dpFecha.getValue(), horaSeleccionada);

        String detalles = String.format(
                "Médico: \n" + medicoSeleccionado.getNombre() + "\n" +
                        "Fecha: \n" + fechaHora.format(formatter) + "\n" +
                        "Motivo: \n" + txtMotivo.getText()
        );

        confirmacion.setContentText(detalles);

        confirmacion.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                guardarCita();
            }
        });
    }

    private boolean validarDatos() {
        if (cmbMedico.getSelectionModel().getSelectedItem() == null) {
            mostrarError("Debes seleccionar un médico");
            return false;
        }

        if (dpFecha.getValue() == null) {
            mostrarError("Debes seleccionar una fecha");
            return false;
        }

        if (horaSeleccionada == null) {
            mostrarError("Debes seleccionar una hora");
            return false;
        }

        if (txtMotivo.getText() == null || txtMotivo.getText().trim().isEmpty()) {
            mostrarError("Debes ingresar el motivo de la consulta");
            return false;
        }

        return true;
    }

    private void guardarCita() {
        try {
            LocalDateTime fechaHora = LocalDateTime.of(dpFecha.getValue(), horaSeleccionada);
            String descripcion = txtMotivo.getText().trim();
            Cita nuevaCita = new Cita.CitaBuilder().id(UUID.randomUUID().toString()).medico(medicoSeleccionado).paciente(pacienteActual)
                    .descripcion(descripcion).fecha(dpFecha.getValue()).hora(horaSeleccionada)
                    .estadoCita(new Pendiente()).build();
            Hospital.getInstance().agregarCita(nuevaCita);
            Alert exito = new Alert(Alert.AlertType.INFORMATION);
            exito.setTitle("Cita Agendada");
            exito.setHeaderText("Tu cita ha sido agendada exitosamente");

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            exito.setContentText(String.format(
                    "Fecha: %s\n" +
                            "Médico: %s\n\n" +
                            "Recuerda llegar 10 minutos antes.",
                    fechaHora.format(formatter),
                    medicoSeleccionado.getNombre()
            ));

            exito.showAndWait();

            // Limpiar formulario
            limpiarFormulario();

        } catch (Exception e) {
            mostrarError("Error al agendar la cita: " + e.getMessage());
        }
    }

    private void limpiarFormulario() {
        cmbMedico.setValue(null);
        dpFecha.setValue(null);
        txtMotivo.clear();

        medicoSeleccionado = null;
        horaSeleccionada = null;

        lblResumenMedico.setText("No seleccionado");
        lblResumenFecha.setText("No seleccionada");

    }

    @FXML
    private void cancelar() {
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Cancelar");
        confirmacion.setHeaderText("¿Deseas cancelar la solicitud?");
        confirmacion.setContentText("Se perderán los datos ingresados.");
        confirmacion.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                limpiarFormulario();
            }
        });
    }

    private void mostrarError(String mensaje) {
        Alert error = new Alert(Alert.AlertType.ERROR);
        error.setTitle("Error");
        error.setHeaderText("Error en el formulario");
        error.setContentText(mensaje);
        error.showAndWait();
    }
}
