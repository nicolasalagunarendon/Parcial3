package uniquindio.edu.co.poo.elparcial3.model;

import javax.swing.*;
import java.util.ArrayList;

public class Hospital {
    private String nit;
    private String name;
    private ArrayList<Paciente> listapacientes;
    private ArrayList<Medico> listamedicos;
    private ArrayList <Persona> listaPersonas;
    private ArrayList<Cita> listaCitas;
    private Persona usuarioLogueado;

    private Hospital() {
        listapacientes = new ArrayList<>();
        listamedicos = new ArrayList<>();
        listaPersonas = new ArrayList<>();
        listaCitas = new ArrayList<>();
        this.nit= "123455";
        this.name= "Clínica Integral";
        this.usuarioLogueado= null;
    }

    private static  Hospital instance;

    public static Hospital getInstance() {
        if (instance == null) {
            instance = new Hospital();
        }return   instance;
    }

    public ArrayList<Paciente> getAllPacientes() {
        return listapacientes;
    }

    public ArrayList<Medico> getAllMedicos() {
        return listamedicos;
    }


    public Paciente buscarPaciente(String id) {
        for (Paciente paciente : listapacientes) {
            if(paciente.getId().equals(id)){
                return paciente;
            }
        }return null;
    }

    public void agregarPaciente(Paciente paciente) {
        if (paciente == null) {
            throw new IllegalArgumentException("Paciente null");
        }

        for (Paciente p : listapacientes) {
            if (p.getId().equals(paciente.getId())) {
                throw new IllegalStateException("El Paciente ya existe");
            }
        }
        listapacientes.add(paciente);
        listaPersonas.add(paciente);
    }

    public void eliminarPaciente(Paciente paciente) {
        for (Paciente p : listapacientes) {
            if(p.getId().equals(paciente.getId())){
                listapacientes.remove(p);
                listaPersonas.remove(paciente);
                return;
            }
        }throw new IllegalArgumentException("El Paciente no existe");
    }

    public void editarPaciente(Paciente paciente) {
        for (int i=0; i<listapacientes.size(); i++) {
            if(listapacientes.get(i).getId().equals(paciente.getId())){
                for(int j=0;j<listaPersonas.size();j++){
                    if(listaPersonas.get(j).getId().equals(paciente.getId())){
                        listapacientes.set(i,paciente);
                        listaPersonas.set(j,paciente);
                        return;
                    }
                }
            }
        }throw new  IllegalArgumentException("El Paciente no existe");
    }

    public Medico buscarMedico(String id) {
        for (Medico m : listamedicos) {
            if(m.getId().equals(id)){
                return m;
            }
        }return null;
    }

    public void agregarMedico(Medico medico) {
        for (Medico m : listamedicos) {
            if(m.getId().equals(medico.getId())){
                throw new  IllegalStateException("El Medico ya existe");
            }
        }listamedicos.add(medico);
        listaPersonas.add(medico);
    }

    public void eliminarMedico(Medico medico) {
        for (Medico m : listamedicos) {
            if(m.getId().equals(medico.getId())){
                listamedicos.remove(m);
                listaPersonas.remove(medico);
                return;
            }
        }throw new IllegalArgumentException("El Medico no existe");
    }

    public void  editarMedico(Medico medico) {
        for (int i=0; i<listamedicos.size(); i++) {
            if(listamedicos.get(i).getId().equals(medico.getId())){
                for(int j=0;j<listaPersonas.size();j++){
                    listamedicos.set(i,medico);
                    listaPersonas.set(j,medico);
                    return;
                }
            }
        }throw new IllegalArgumentException("El Medico no existe");
    }

    public Persona buscarPersona(String id) {
        for (Persona p : listapacientes) {
            if(p.getId().equals(id)){
                return p;
            }
        }return null;
    }

    public Cita buscarCita(String id) {
        for (Cita c : listaCitas) {
            if(c.getId().equals(id)){
                return c;
            }
        }return null;
    }

    public void agregarCita(Cita cita) {
        for(Cita c:  listaCitas){
            if(c.getId().equals(cita.getId())){
                throw new  IllegalStateException("Esa Cita ya existe");
            }
        }
        listaCitas.add(cita);
        cita.getPaciente().agregarcita(cita);

    }

    public void eliminarCita(Cita cita) {
        for (Cita c : listaCitas) {
            if(c.getId().equals(cita.getId())){
                listaCitas.remove(c);
                return;
            }
        }throw new IllegalArgumentException("Esa Cita no existe");
    }

    public  void  editarCita(Cita cita) {
        for (int i=0; i<listaCitas.size(); i++) {
            if(listaCitas.get(i).equals(cita.getId())){
                listaCitas.set(i, cita);
            }
        }throw new IllegalArgumentException("Esa Cita no existe");
    }

    public ArrayList<Persona> getAllPersonas() {
        return listaPersonas;
    }

    public int numeroPersonas() {
        return listaPersonas.size();
    }

    public boolean personaExiste(String id) {
        return listaPersonas.stream().anyMatch(persona -> persona.getId().equals(id));
    }

    public boolean loginPersona(String personaId, String contrasenia) {
        Persona persona = listaPersonas.stream()
                .filter(p -> p.getId() != null && p.getId().equalsIgnoreCase(personaId))
                .findFirst()
                .orElse(null);

        if (persona == null) {
            JOptionPane.showMessageDialog(null, "El ID es incorrecto o el usuario no existe", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        String personaPass = persona.getContrasenia();

        if (personaPass == null) {
            JOptionPane.showMessageDialog(null, "El usuario no tiene contraseña asignada", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Comparación segura, ignorando mayúsculas/minúsculas y quitando espacios
        if (!personaPass.trim().equalsIgnoreCase(contrasenia.trim())) {
            JOptionPane.showMessageDialog(null, "Contraseña incorrecta", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Login exitoso
        setUsuarioLogueado(persona);
        JOptionPane.showMessageDialog(null, "Login exitoso");
        return true;
    }


    public Persona getUsuarioLogueado() {
        return usuarioLogueado;
    }

    public void setUsuarioLogueado(Persona usuarioLogueado) {
        this.usuarioLogueado = usuarioLogueado;
    }
}
