package uniquindio.edu.co.poo.elparcial3.model.EstadosCita;

import uniquindio.edu.co.poo.elparcial3.model.Cita;

public interface EstadoCita {
    void manejar(Cita cita);
    String getEstado();

    //Esto es un State men
}
