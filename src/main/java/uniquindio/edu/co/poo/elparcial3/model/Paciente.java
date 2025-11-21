package uniquindio.edu.co.poo.elparcial3.model;

import java.util.ArrayList;

public class Paciente extends Persona{
    private byte edad;
    private float altura;
    private float peso;
    private ArrayList<Cita> citasProgramadas;
    private ArrayList<Cita> historialMedico;

    public Paciente(PacienteBuilder build){
        super(build.nombre, build.id, build.contrasenia);
        this.edad = build.edad;
        this.altura = build.altura;
        this.peso = build.peso;
        this.historialMedico = new ArrayList<>();
        this.citasProgramadas= new ArrayList<>();
    }

    public static class PacienteBuilder{
        private String nombre;
        private String id;
        private String contrasenia;
        private byte edad;
        private float altura;
        private float peso;

        public PacienteBuilder nombre(String nombre){
            this.nombre = nombre;
            return this;
        }

        public PacienteBuilder id(String id){
            this.id = id;
            return this;
        }

        public PacienteBuilder contrasenia(String contrasenia){
            this.contrasenia = contrasenia;
            return this;
        }

        public PacienteBuilder edad(byte edad){
            this.edad = edad;
            return this;
        }

        public PacienteBuilder altura(float altura){
            this.altura = altura;
            return this;
        }

        public PacienteBuilder peso(float peso){
            this.peso = peso;
            return this;
        }

        public Paciente build(){
            return new Paciente(this);
        }
    }

    public byte getEdad() {
        return edad;}

    public void setEdad(byte edad) {
        this.edad = edad;}

    public float getAltura() {
        return altura;}

    public void setAltura(float altura) {
        this.altura = altura;}

    public float getPeso() {
        return peso;}

    public void setPeso(float peso) {
        this.peso = peso;}




    public ArrayList<Cita> getHistorialMedico() {
        return historialMedico;
    }


    public void agregarcita(Cita cita){
        for(Cita c: citasProgramadas){
            if(c.getId().equals(cita.getId())){
                throw new IllegalArgumentException("La Cita ya existe");
            }
        }
        historialMedico.add(cita);
        citasProgramadas.add(cita);
    }

    public void eliminarcita(Cita cita){
        for(Cita c: citasProgramadas){
            if(c.getId().equals(cita.getId())){
                citasProgramadas.remove(c);
                historialMedico.remove(c);
                return;
            }
        }throw new IllegalArgumentException("La Cita no existe");
    }

    public void editarCita(Cita cita){
        for(int i = 0; i< historialMedico.size(); i++){
            if(historialMedico.get(i).getId().equals(cita.getId())){
                historialMedico.set(i, historialMedico.get(i));
                return;
            }
        }throw new IllegalArgumentException("La Cita no existe");
    }

    public ArrayList<Cita> getCitasProgramadas() {
        return citasProgramadas;
    }

    @Override
    public String toString() {
        return "Paciente{" +
                "edad=" + edad +
                ", altura=" + altura +
                ", peso=" + peso +
                ", citasProgramadas=" + citasProgramadas +
                ", historialMedico=" + historialMedico +
                '}';
    }
}
