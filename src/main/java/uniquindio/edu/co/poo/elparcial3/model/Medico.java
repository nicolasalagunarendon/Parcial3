package uniquindio.edu.co.poo.elparcial3.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class Medico extends Persona{
    private String nombre;
    private String id;
    private String correo;
    private String contrasenia;
    private ArrayList<Cita> listaCitas;
    private ArrayList<LocalTime> horasDisponibles;

    public  Medico(MedicoBuilder build) {
        super(build.nombre, build.id, build.contrasenia);
        this.correo = build.correo;
        this.listaCitas= new ArrayList<>();
        this.horasDisponibles = new ArrayList<>();
    }

    public static class MedicoBuilder{
        private String nombre;
        private String id;
        private String correo;
        private String contrasenia;

        public MedicoBuilder nombre(String nombre) {
            this.nombre = nombre;
            return this;
        }

        public MedicoBuilder id(String id) {
            this.id = id;
            return this;
        }

        public MedicoBuilder correo(String correo) {
            this.correo = correo;
            return this;
        }

        public MedicoBuilder contrasenia(String contrasenia) {
            this.contrasenia = contrasenia;
            return this;
        }

        public Medico build(){
            return new Medico(this);
        }
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public ArrayList<Cita> getListaCitas() {
        return listaCitas;
    }

    public void setListaCitas(ArrayList<Cita> listaCitas) {
        this.listaCitas = listaCitas;
    }

    public void agregarCita(Cita cita){
        for(Cita c:listaCitas){
            if(c.getId().equals(cita.getId())){
                throw new  IllegalStateException("La Cita ya existente");
            }
        }
        listaCitas.add(cita);
        Hospital.getInstance().agregarCita(cita);
    }

    public void eliminarCita(Cita cita){
        for(Cita c:listaCitas){
            if(c.getId()==cita.getId()){
                listaCitas.remove(c);
                Hospital.getInstance().eliminarCita(cita);
                return;
            }
        }throw new IllegalArgumentException("La Cita no existe");
    }

    public void editarCita(Cita cita){
        for(int i=0; i<listaCitas.size();i++){
            if(listaCitas.get(i).getId()==cita.getId()){
                listaCitas.set(i,cita);
                Hospital.getInstance().editarCita(cita);
                return;
            }
        }throw new IllegalArgumentException("La Cita no existe");
    }

    public ArrayList<Cita> getCitasHoy(){
        ArrayList<Cita> citasHoy= new ArrayList<>();
        for(Cita c:listaCitas){
            if(c.getFecha().isEqual(LocalDate.now())){
                citasHoy.add(c);
            }
        }return citasHoy;
    }

    public void agregarHorasDisponibilidad(LocalTime horasDisponible){
        for(LocalTime d:horasDisponibles){
            if(d.isBefore(horasDisponible)){
                throw new IllegalStateException("Esa hora de disponibilidad ya está registrada");
            }
        }horasDisponibles.add(horasDisponible);
    }

    public void  eliminarHorasDisponibilidad(LocalTime horasDisponible){
        for(LocalTime d:horasDisponibles){
            if(d.isBefore(horasDisponible)){
                horasDisponibles.remove(d);
                return;
            }
        }throw new IllegalArgumentException("Esa hora de disponibilidad no está registrada");
    }

    public void editarHorasDisponibilidad(LocalTime horasDisponible){
        for(int  i=0; i<horasDisponibles.size();i++){
            if(horasDisponibles.get(i).isBefore(horasDisponible)){
                horasDisponibles.set(i,horasDisponible);
                return;
            }
        }throw new IllegalArgumentException("Esa hora de disponibilidad no existe");
    }

    public ArrayList<LocalTime> getHorasDisponibles() {
        return horasDisponibles;
    }
}
