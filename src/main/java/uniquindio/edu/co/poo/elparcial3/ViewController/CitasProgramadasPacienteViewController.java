package uniquindio.edu.co.poo.elparcial3.ViewController;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import uniquindio.edu.co.poo.elparcial3.model.Cita;
import uniquindio.edu.co.poo.elparcial3.model.Hospital;
import uniquindio.edu.co.poo.elparcial3.model.Paciente;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;


public class CitasProgramadasPacienteViewController {
    @FXML private Label lblProximaCita;
    @FXML private Label lblProximoDoctor;
    @FXML private Label lblCitasPendientes;
    @FXML private Label lblCitasMes;

    @FXML private ComboBox<String> cmbFiltroEstado;
    @FXML private ComboBox<String> cmbFiltroMes;
    @FXML private CheckBox chkSoloProximas;

    @FXML private VBox vboxCitas;

    private ObservableList<Cita> listaCitas;
    private Paciente pacienteActual;
    private Hospital hospitalActual = Hospital.getInstance();

    @FXML
    public void initialize() {
        configurarFiltros();

        listaCitas = FXCollections.observableArrayList();
        setPaciente((Paciente) hospitalActual.getUsuarioLogueado());
        cargarCitas();
    }

    public void setPaciente(Paciente paciente) {
        this.pacienteActual = paciente;
        cargarCitas();
    }

    private void configurarFiltros() {
        cmbFiltroEstado.setItems(FXCollections.observableArrayList(
                "Todas", "Pendiente", "consulta", "Finalizada"
        ));
        cmbFiltroEstado.setValue("Todas");

        cmbFiltroMes.setItems(FXCollections.observableArrayList(
                "Este mes", "Próximo mes", "Últimos 30 días"
        ));
        cmbFiltroMes.setValue("Este mes");

        cmbFiltroEstado.setOnAction(e -> aplicarFiltros());
        cmbFiltroMes.setOnAction(e -> aplicarFiltros());
        chkSoloProximas.setOnAction(e -> aplicarFiltros());
    }

    private void cargarCitas() {
        if (pacienteActual == null) return;

        listaCitas.clear();

        listaCitas.addAll(pacienteActual.getCitasProgramadas());

        aplicarFiltros();
    }

    private void aplicarFiltros() {
        vboxCitas.getChildren().clear();

        String estadoFiltro = cmbFiltroEstado.getValue();
        String mesFiltro = cmbFiltroMes.getValue();
        boolean soloProximas = chkSoloProximas.isSelected();

        LocalDate ahora = LocalDate.now();
        LocalDate fechaLimite = switch (mesFiltro) {
            case "Este mes" -> ahora.plusMonths(1);
            case "Próximo mes" -> ahora.plusMonths(2);
            case "Últimos 30 días" -> ahora.minusDays(30);
            default -> ahora.plusYears(1);
        };

        ObservableList<Cita> citasFiltradas = FXCollections.observableArrayList();

        for (Cita cita : listaCitas) {
            boolean cumpleEstado = estadoFiltro.equals("Todas") ||
                    cita.getEstadoCita().getEstado().equalsIgnoreCase(estadoFiltro);

            boolean cumpleFecha = cita.getFecha().isBefore(fechaLimite);

            boolean cumpleProximas = !soloProximas || cita.getFecha().isAfter(ahora);

            if (cumpleEstado && cumpleFecha && cumpleProximas) {
                citasFiltradas.add(cita);
            }
        }

        citasFiltradas.sort((c1, c2) -> c1.getFecha().compareTo(c2.getFecha()));

        // Crear tarjetas de citas
        if (citasFiltradas.isEmpty()) {
            mostrarPlaceholderVacio();
        } else {
            for (Cita cita : citasFiltradas) {
                vboxCitas.getChildren().add(crearTarjetaCita(cita));
            }
        }

        actualizarEstadisticas(citasFiltradas);
    }

    private HBox crearTarjetaCita(Cita cita) {
        HBox tarjeta = new HBox(20);
        tarjeta.setAlignment(Pos.CENTER_LEFT);
        tarjeta.setStyle("-fx-background-color: white; -fx-background-radius: 12; " +
                "-fx-padding: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");

        // Fecha destacada
        VBox vboxFecha = crearCajaFecha(cita.getFecha().atStartOfDay());

        // Información de la cita
        VBox vboxInfo = crearInfoCita(cita);
        HBox.setHgrow(vboxInfo, Priority.ALWAYS);

        // Botones de acción
        VBox vboxAcciones = crearBotonesAccion(cita);

        tarjeta.getChildren().addAll(vboxFecha, vboxInfo, vboxAcciones);

        return tarjeta;
    }

    private VBox crearCajaFecha(LocalDateTime fecha) {
        VBox vbox = new VBox(5);
        vbox.setAlignment(Pos.CENTER);
        vbox.setStyle("-fx-background-color: #3498db; -fx-background-radius: 10; " +
                "-fx-padding: 15; -fx-min-width: 90;");

        Label lblMes = new Label(fecha.getMonth().getDisplayName(TextStyle.SHORT, new Locale("es")).toUpperCase());
        lblMes.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;");

        Label lblDia = new Label(String.valueOf(fecha.getDayOfMonth()));
        lblDia.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 32px;");

        Label lblDiaSemana = new Label(fecha.getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("es")));
        lblDiaSemana.setStyle("-fx-text-fill: #ecf0f1; -fx-font-size: 11px;");

        vbox.getChildren().addAll(lblMes, lblDia, lblDiaSemana);
        return vbox;
    }

    private VBox crearInfoCita(Cita cita) {
        VBox vbox = new VBox(8);

        // Título con descripción y hora
        HBox hboxTitulo = new HBox(10);
        hboxTitulo.setAlignment(Pos.CENTER_LEFT);

        Label lblDescripcion = new Label(cita.getDescripcion());
        lblDescripcion.setStyle("-fx-text-fill: #2c3e50; -fx-font-weight: bold; -fx-font-size: 16px;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        Label lblHora = new Label("⏰ " + cita.getHora().format(formatter));
        lblHora.setStyle("-fx-text-fill: #7f8c8d; -fx-font-weight: bold; -fx-font-size: 12px; " +
                "-fx-background-color: #ecf0f1; -fx-background-radius: 5; -fx-padding: 5 10;");

        hboxTitulo.getChildren().addAll(lblDescripcion, spacer, lblHora);

        // Médico
        HBox hboxMedico = new HBox(5);
        hboxMedico.setAlignment(Pos.CENTER_LEFT);
        Label lblMedico = new Label("‍⚕ Dr." + cita.getMedico().getNombre() + " - ");
        lblMedico.setStyle("-fx-text-fill: #7f8c8d; -fx-font-size: 13px;");
        hboxMedico.getChildren().add(lblMedico);

        // Estado
        HBox hboxEstado = new HBox(5);
        hboxEstado.setAlignment(Pos.CENTER_LEFT);
        Label lblEstado = new Label(obtenerEmojiEstado(cita) + " Estado: " +
                cita.getEstadoCita().getEstado());
        lblEstado.setStyle("-fx-text-fill: #7f8c8d; -fx-font-size: 13px;");
        hboxEstado.getChildren().add(lblEstado);

        HBox hboxPrecio = new HBox(5);
        hboxPrecio.setAlignment(Pos.CENTER_LEFT);
        Label lblPrecio = new Label("💲 " + cita.getPrecio());
        lblPrecio.setStyle("-fx-text-fill: #7f8c8d; -fx-font-size: 13px;");
        hboxPrecio.getChildren().add(lblPrecio);

        vbox.getChildren().addAll(hboxTitulo, hboxMedico, hboxEstado, hboxPrecio);
        return vbox;
    }

    private VBox crearBotonesAccion(Cita cita) {
        VBox vbox = new VBox(10);
        vbox.setAlignment(Pos.CENTER);

        String estadoActual = cita.getEstadoCita().getEstado().toLowerCase();

        if (estadoActual.equals("pendiente")) {
            Button btnVer = new Button("Ver Detalles");
            btnVer.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; " +
                    "-fx-background-radius: 6; -fx-padding: 8 20; -fx-cursor: hand; " +
                    "-fx-min-width: 130; -fx-font-weight: bold; -fx-font-size: 12px;");
            btnVer.setOnAction(e -> verDetallesCita(cita));


            Button btnCancelar = new Button("Cancelar");
            btnCancelar.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; " +
                    "-fx-background-radius: 6; -fx-padding: 8 20; -fx-cursor: hand; " +
                    "-fx-min-width: 130; -fx-font-weight: bold; -fx-font-size: 12px;");
            btnCancelar.setOnAction(e -> cancelarCita(cita));

            vbox.getChildren().addAll(btnVer, btnCancelar);
        } else {
            Button btnVer = new Button("Ver Detalles");
            btnVer.setStyle("-fx-background-color: #95a5a6; -fx-text-fill: white; " +
                    "-fx-background-radius: 6; -fx-padding: 8 20; -fx-cursor: hand; " +
                    "-fx-min-width: 130; -fx-font-weight: bold; -fx-font-size: 12px;");
            btnVer.setOnAction(e -> verDetallesCita(cita));

            vbox.getChildren().add(btnVer);
        }

        return vbox;
    }

    private void mostrarPlaceholderVacio() {
        VBox placeholder = new VBox(15);
        placeholder.setAlignment(Pos.CENTER);
        placeholder.setStyle("-fx-padding: 60; -fx-background-color: white; -fx-background-radius: 10;");

        Label lblEmoji = new Label("📅");
        lblEmoji.setStyle("-fx-font-size: 64px;");

        Label lblTitulo = new Label("No tienes citas programadas");
        lblTitulo.setStyle("-fx-text-fill: #7f8c8d; -fx-font-weight: bold; -fx-font-size: 18px;");

        Label lblSubtitulo = new Label("Solicita una nueva cita para comenzar");
        lblSubtitulo.setStyle("-fx-text-fill: #95a5a6; -fx-font-size: 14px;");

        placeholder.getChildren().addAll(lblEmoji, lblTitulo, lblSubtitulo);
        vboxCitas.getChildren().add(placeholder);
    }

    private void actualizarEstadisticas(ObservableList<Cita> citas) {
        LocalDate ahora = LocalDate.now();

        // Próxima cita
        Cita proxima = citas.stream()
                .filter(c -> c.getFecha().isAfter(ahora))
                .findFirst()
                .orElse(null);

        if (proxima != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM", new Locale("es"));
            lblProximaCita.setText(proxima.getFecha().format(formatter));
            lblProximoDoctor.setText(proxima.getMedico().getNombre());
        } else {
            lblProximaCita.setText("Sin citas próximas");
            lblProximoDoctor.setText("-");
        }

        // Citas pendientes
        long pendientes = citas.stream()
                .filter(c -> c.getEstadoCita().getEstado().equalsIgnoreCase("Pendiente"))
                .count();
        lblCitasPendientes.setText(String.valueOf(pendientes));

        // Citas este mes
        LocalDate inicioMes = LocalDate.from(ahora.withDayOfMonth(1));
        LocalDate finMes = LocalDate.from(ahora.plusMonths(1).withDayOfMonth(1));
        long citasMes = citas.stream()
                .filter(c -> c.getFecha().isAfter(inicioMes) && c.getFecha().isBefore(finMes))
                .count();
        lblCitasMes.setText(String.valueOf(citasMes));
    }

    private String obtenerEmojiEstado(Cita cita) {
        return switch (cita.getEstadoCita().getEstado().toLowerCase()) {
            case "pendiente" -> "⏰";
            case "consulta" -> "🩺";
            case "finalizada" -> "✅";
            default -> "📋";
        };
    }

    private void verDetallesCita(Cita cita) {
        Alert detalles = new Alert(Alert.AlertType.INFORMATION);
        detalles.setTitle("Detalles de la Cita");
        detalles.setHeaderText("Motivo: \n\n" + cita.getDescripcion());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter formatterHora = DateTimeFormatter.ofPattern("HH:mm");
        String contenido = String.format(
                "📅 Fecha: \n\n" + cita.getFecha().format(formatter) + "\n\n" +
                        "⏰ Hora: \n\n" + cita.getHora().format(formatterHora) + "\n\n" +
                        "👨‍⚕ Doctor: \n" +"Dr."+ cita.getMedico().getNombre() + "\n\n" +
                        "📋 Estado: \n\n" + cita.getEstadoCita().getEstado() + "\n\n" +
                        "💡 Recuerda llegar 10 minutos antes"
        );

        detalles.setContentText(contenido);
        detalles.showAndWait();
    }


    private void cancelarCita(Cita cita) {
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Cancelar Cita");
        confirmacion.setHeaderText("¿Estás seguro de cancelar esta cita?");
        confirmacion.setContentText(String.format(
                "Fecha: %s\n" +
                        "Médico: %s\n\n" +
                        "Esta acción no se puede deshacer.",
                cita.getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                cita.getMedico().getNombre()
        ));

        confirmacion.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    pacienteActual.cancelarCita(cita);
                    listaCitas.remove(cita);


                    aplicarFiltros();

                    Alert exito = new Alert(Alert.AlertType.INFORMATION);
                    exito.setTitle("Cita Cancelada");
                    exito.setContentText("Tu cita ha sido cancelada exitosamente.");
                    exito.showAndWait();
                } catch (Exception e) {
                    Alert error = new Alert(Alert.AlertType.ERROR);
                    error.setTitle("Error");
                    error.setContentText("No se pudo cancelar la cita: " + e.getMessage());
                    error.showAndWait();
                }
            }
        });
    }




}
