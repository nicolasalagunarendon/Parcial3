package uniquindio.edu.co.poo.elparcial3.model.EstadosCita;

import uniquindio.edu.co.poo.elparcial3.model.Cita;

public class Pendiente implements EstadoCita {
    @Override
    public void manejar(Cita cita) {
        System.out.println("La cita está pendiente. Ahora pasará a consulta.");
        cita.setEstadoCita(new Consulta());
    }

    @Override
    public String getEstado() {
        return "Pendiente";
    }
}
