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

public class SolicitarCitaPacienteViewController {
    @FXML
    private ComboBox<Medico> cmbMedico;
    @FXML
    private Label lblInfoMedico;

    @FXML
    private DatePicker dpFecha;
    @FXML
    private ComboBox<String> cmbHora;
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

    @FXML
    public void initialize() {
        configurarCombos();

        listaDoctores = FXCollections.observableArrayList();
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
            Medico medico = cmbMedico.getValue();
            if (medico != null) {
                medicoSeleccionado = medico;
                actualizarInfoMedico(medico);
                actualizarResumenMedico(medico);
                if (dpFecha.getValue() != null) {
                    cargarHorariosDisponibles();
                }
            }
        });

        // Cuando se selecciona fecha, cargar horarios
        dpFecha.setOnAction(e -> {
            if (medicoSeleccionado != null && dpFecha.getValue() != null) {
                cargarHorariosDisponibles();
                actualizarResumenFecha();
            }
        });

        // Botones
        btnCancelar.setOnAction(e -> cancelar());
        btnConfirmar.setOnAction(e -> confirmarCita());

        // Motivo con contador de caracteres
        txtMotivo.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.length() > 500) {
                txtMotivo.setText(oldVal);
            }
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

    private void cargarEspecialidades() {
        // Aquí cargarías las especialidades desde la BD
        ObservableList<String> especialidades = FXCollections.observableArrayList(
                "Medicina General",
                "Cardiología",
                "Pediatría",
                "Dermatología",
                "Oftalmología",
                "Traumatología"
        );

    }

    private void cargarMedicosPorEspecialidad(String especialidad) {
        listaDoctores.clear();
        cmbMedico.getItems().clear();
        cmbMedico.setItems(listaDoctores);
        cmbMedico.setPromptText("Selecciona un médico");
        lblInfoMedico.setText("ℹ️ Selecciona un médico para ver su disponibilidad");
    }

    private void actualizarInfoMedico(Medico doctor) {
        lblInfoMedico.setText(String.format(
                "ℹ️ %s tiene disponibilidad de lunes a viernes",
                doctor.getNombre()));
    }

    private void cargarHorariosDisponibles() {
        flowHorarios.getChildren().clear();
        if (medicoSeleccionado == null || dpFecha.getValue() == null) {
            return;
        }

        // Obtener horarios disponibles del médico
        List<LocalTime> horariosDisponibles = medicoSeleccionado.getHorasDisponibles();

        // Filtrar horarios ya ocupados (esto lo harías consultando la BD)
        LocalDate fechaSeleccionada = dpFecha.getValue();


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        for (LocalTime hora : horariosDisponibles) {
            Button btnHora = new Button(hora.format(formatter));
            btnHora.setStyle("-fx-background-color: #ecf0f1; -fx-text-fill: #2c3e50; " +
                    "-fx-background-radius: 6; -fx-padding: 8 15; -fx-cursor: hand;");
            btnHora.setOnAction(e -> {
                seleccionarHorario(hora, btnHora);
            });

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
                "Médico: %s\n" +
                        "Especialidad: %s\n" +
                        "Fecha: %s\n" +
                        "Motivo: %s",
                medicoSeleccionado.getNombre(),
                fechaHora.format(formatter),
                txtMotivo.getText()
        );

        confirmacion.setContentText(detalles);

        confirmacion.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                guardarCita();
            }
        });
    }

    private boolean validarDatos() {
        if (medicoSeleccionado == null) {
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
            Cita nuevaCita = new Cita.CitaBuilder().medico(medicoSeleccionado).paciente(pacienteActual).descripcion(descripcion).fecha(LocalDate.now()).hora(horaSeleccionada).estadoCita(new Pendiente()).build();
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
        cmbMedico.getItems().clear();
        cmbMedico.setValue(null);
        dpFecha.setValue(null);
        cmbHora.setValue(null);
        txtMotivo.clear();
        flowHorarios.getChildren().clear();

        medicoSeleccionado = null;
        horaSeleccionada = null;

        lblResumenMedico.setText("No seleccionado");
        lblResumenFecha.setText("No seleccionada");

        btnConfirmar.setDisable(true);
    }

    private void cancelar() {
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Cancelar");
        confirmacion.setHeaderText("¿Deseas cancelar la solicitud?");
        confirmacion.setContentText("Se perderán los datos ingresados.");

        confirmacion.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                limpiarFormulario();
                // navegarACitasProgramadas();
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
