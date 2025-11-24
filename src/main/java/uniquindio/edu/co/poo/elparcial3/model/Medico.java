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
        inicializarHorasDisponibles();
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
    }

    public void eliminarCita(Cita cita){
        for(Cita c:listaCitas){
            if(c.getId().equalsIgnoreCase(cita.getId())){
                listaCitas.remove(c);
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

    private void inicializarHorasDisponibles() {
        for (int h = 9; h <= 17; h++) {

            if (h == 12 || h == 13) continue;

            horasDisponibles.add(LocalTime.of(h, 0));
        }
    }

    public ArrayList<LocalTime> getHorasDisponibles() {
        return horasDisponibles;
    }

    public boolean estaHoraOcupada(LocalDate fecha, LocalTime hora) {
        for (Cita cita : listaCitas) {
            if (cita.getFecha().equals(fecha) && cita.getHora().equals(hora)) {
                return true; // Ya está usada
            }
        }
        return false;
    }

}
