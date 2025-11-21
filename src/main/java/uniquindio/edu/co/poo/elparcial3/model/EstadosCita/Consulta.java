package uniquindio.edu.co.poo.elparcial3.model.EstadosCita;

import uniquindio.edu.co.poo.elparcial3.model.Cita;

public class Consulta implements EstadoCita{

    @Override
    public void manejar(Cita cita) {
        System.out.println("La cita está en consulta. Ahora pasará a finalizada.");
        cita.setEstadoCita(new Finalizada());
    }

    @Override
    public String getEstado() {
        return "Consulta";
    }


}
