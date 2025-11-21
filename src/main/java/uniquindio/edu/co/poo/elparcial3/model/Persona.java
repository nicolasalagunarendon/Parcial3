package uniquindio.edu.co.poo.elparcial3.model;

public class Persona{
    private String nombre;
    private String id;
    private String contrasenia;

    public Persona(String nombre, String id, String contrasenia){
        this.nombre = nombre;
        this.id = id;
        this.contrasenia = contrasenia;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContrasenia() {
        return contrasenia;
    }
    public void setContrasenia(String password) {
        this.contrasenia = password;
    }
}
