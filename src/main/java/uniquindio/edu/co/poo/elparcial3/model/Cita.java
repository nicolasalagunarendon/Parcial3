package uniquindio.edu.co.poo.elparcial3.model;

import uniquindio.edu.co.poo.elparcial3.model.EstadosCita.EstadoCita;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Cita {
    private String id;
    private Paciente paciente;
    private Medico medico;
    private LocalDate fecha;
    private LocalTime hora;
    private EstadoCita estadoCita;
    private float precio;
    private String descripcion;

    public Cita(CitaBuilder build){
        this.id = build.id;
        this.paciente = build.paciente;
        this.medico = build.medico;
        this.fecha = build.fecha;
        this.hora = build.hora;
        this.estadoCita = build.estadoCita;
        this.precio = build.precio;
        this.descripcion= build.descripcion;
    }

    public static class  CitaBuilder{
        private String id;
        private Paciente paciente;
        private Medico medico;
        private LocalDate fecha;
        private LocalTime hora;
        private EstadoCita estadoCita;
        private float precio;
        private String descripcion;

        public CitaBuilder id(String id){
            this.id = id;
            return this;
        }

        public CitaBuilder paciente(Paciente paciente){
            this.paciente = paciente;
            return this;
        }

        public CitaBuilder medico(Medico medico){
            this.medico = medico;
            return this;
        }

        public CitaBuilder fecha(LocalDate fecha){
            this.fecha = fecha;
            return this;
        }

        public CitaBuilder hora(LocalTime hora){
            this.hora = hora;
            return this;
        }

        public CitaBuilder estadoCita(EstadoCita estado){
            this.estadoCita = estado;
            return this;
        }

        public CitaBuilder precio(float precio){
            this.precio = precio;
            return this;
        }

        public  CitaBuilder descripcion(String descripcion){
            this.descripcion= descripcion;
            return this;
        }

        public Cita build(){
            return new Cita(this);
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public  LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public EstadoCita getEstadoCita() {
        return estadoCita;
    }

    public void setEstadoCita(EstadoCita estadoCita) {
        this.estadoCita = estadoCita;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
