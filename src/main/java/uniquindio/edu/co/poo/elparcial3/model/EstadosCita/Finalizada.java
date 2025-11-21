package uniquindio.edu.co.poo.elparcial3.model.EstadosCita;

import uniquindio.edu.co.poo.elparcial3.model.Cita;

public class Finalizada implements EstadoCita{
    @Override
    public void manejar(Cita cita) {
        System.out.println("La cita ya está finalizada. No puede cambiar a otro estado.");
    }

    @Override
    public String getEstado() {
        return "Finalizada";
    }
}
