package uniquindio.edu.co.poo.elparcial3;

import uniquindio.edu.co.poo.elparcial3.model.*;
import uniquindio.edu.co.poo.elparcial3.model.EstadosCita.Finalizada;
import uniquindio.edu.co.poo.elparcial3.model.EstadosCita.Pendiente;

import javax.swing.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class Launcher {
    public static void main(String[] args) {

        Hospital hospital=  Hospital.getInstance();
        hospital.agregarMedico(new Medico.MedicoBuilder()
                .id("103844829")
                .nombre("Antonio Velasquez")
                .correo("antonio@gmail.com")
                .contrasenia("12345").build());
        hospital.agregarMedico(
                new Medico.MedicoBuilder()
                        .id("102938475")
                        .nombre("María Fernanda Gómez")
                        .correo("mariafg@gmail.com")
                        .contrasenia("clave001")
                        .build());
        hospital.agregarMedico(
                new Medico.MedicoBuilder()
                        .id("109283746")
                        .nombre("Carlos Eduardo Ruiz")
                        .correo("carlosruiz@gmail.com")
                        .contrasenia("pass789")
                        .build());
        hospital.agregarMedico(
                new Medico.MedicoBuilder()
                        .id("100283645")
                        .nombre("Luisa Martínez")
                        .correo("luisa.mtz@gmail.com")
                        .contrasenia("lm2024")
                        .build());
        hospital.agregarMedico(
                new Medico.MedicoBuilder()
                        .id("107364829")
                        .nombre("Jorge Salazar")
                        .correo("jsalazar@gmail.com")
                        .contrasenia("jorge123")
                        .build());
        hospital.agregarMedico(
                new Medico.MedicoBuilder()
                        .id("105987321")
                        .nombre("Natalia Herrera")
                        .correo("nataliah@gmail.com")
                        .contrasenia("naty987")
                        .build());

        hospital.agregarPaciente(new Paciente.PacienteBuilder()
                .id("1092573310")
                .nombre("Julieta Ramirez")
                .contrasenia("012034")
                .edad((byte)24)
                .altura(1.68F)
                .peso(61)
                .build());
        hospital.agregarPaciente(new Paciente.PacienteBuilder()
                .id("1043458474")
                .nombre("Andrés Castillo")
                .contrasenia("and123")
                .edad((byte)30)
                .altura(1.75F)
                .peso(72)
                .build());

        hospital.agregarPaciente(new Paciente.PacienteBuilder()
                .id("1092846372")
                .nombre("Valentina Rojas")
                .contrasenia("vr2024")
                .edad((byte)21)
                .altura(1.62F)
                .peso(58)
                .build());
        hospital.agregarPaciente(new Paciente.PacienteBuilder()
                .id("1029384756")
                .nombre("Sebastián López")
                .contrasenia("seb555")
                .edad((byte)27)
                .altura(1.80F)
                .peso(80)
                .build());
        hospital.agregarPaciente(new Paciente.PacienteBuilder()
                .id("1083746251")
                .nombre("Camila Torres")
                .contrasenia("ct_001")
                .edad((byte)19)
                .altura(1.66F)
                .peso(55)
                .build());
        hospital.agregarPaciente(new Paciente.PacienteBuilder()
                .id("1048573920")
                .nombre("Diego Hernández")
                .contrasenia("dh890")
                .edad((byte)33)
                .altura(1.78F)
                .peso(76)
                .build());

        hospital.buscarMedico("100283645").agregarCita(new Cita.CitaBuilder()
                .id("dhymbew589")
                .paciente(hospital.buscarPaciente("1048573920"))
                .medico(hospital.buscarMedico("100283645"))
                .fecha(LocalDate.now())
                .hora(LocalTime.now())
                .estadoCita(new Pendiente())
                .precio(25000)
                .descripcion("Tiene gripe")
                .build());
        hospital.buscarMedico("103844829").agregarCita(new Cita.CitaBuilder()
                .id("cita001")
                .paciente(hospital.buscarPaciente("1092573310"))
                .medico(hospital.buscarMedico("103844829"))
                .fecha(LocalDate.now())
                .hora(LocalTime.now())
                .estadoCita(new Finalizada())
                .precio(30000)
                .descripcion("Dolor de cabeza persistente")
                .build());
        hospital.buscarMedico("102938475").agregarCita(new Cita.CitaBuilder()
                .id("cita002")
                .paciente(hospital.buscarPaciente("1029384756"))
                .medico(hospital.buscarMedico("102938475"))
                .fecha(LocalDate.now().plusDays(5))
                .hora(LocalTime.of(10, 30))
                .estadoCita(new Pendiente())
                .precio(45000)
                .descripcion("Revisión general anual")
                .build());
        hospital.buscarMedico("102938475").agregarCita(new Cita.CitaBuilder()
                .id("cita003")
                .paciente(hospital.buscarPaciente("1092846372"))
                .medico(hospital.buscarMedico("102938475"))
                .fecha(LocalDate.now())
                .hora(LocalTime.of(14, 0))
                .estadoCita(new Pendiente())
                .precio(35000)
                .descripcion("Control de presión arterial")
                .build());
        hospital.buscarMedico("102938475").agregarCita(new Cita.CitaBuilder()
                .id("cita004")
                .paciente(hospital.buscarPaciente("1029384756"))
                .medico(hospital.buscarMedico("102938475"))
                .fecha(LocalDate.now())
                .hora(LocalTime.of(9, 15))
                .estadoCita(new Pendiente())
                .precio(28000)
                .descripcion("Lesión en la rodilla")
                .build());
        hospital.buscarMedico("102938475").agregarCita(new Cita.CitaBuilder()
                .id("cita005")
                .paciente(hospital.buscarPaciente("1083746251"))
                .medico(hospital.buscarMedico("102938475"))
                .fecha(LocalDate.now())
                .hora(LocalTime.of(16, 45))
                .estadoCita(new Finalizada())
                .precio(50000)
                .descripcion("Revisión por alergias")
                .build());
        Medico medico = hospital.buscarMedico("102938475");
        hospital.setUsuarioLogueado(hospital.buscarPaciente("1029384756"));
        App.launch(App.class, args);


    }
}
