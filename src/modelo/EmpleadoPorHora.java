package modelo;

/** Empleado que cobra por hora trabajada en el mes. */
public class EmpleadoPorHora extends Empleado {

    private double pagoPorHora;
    private double horasTrabajadas;

    /**
     * Parámetros: dpi, nombre, pagoPorHora, horasTrabajadas.
     * super(...) inicializa los datos comunes en la clase base.
     */
    public EmpleadoPorHora(String dpi, String nombre, double pagoPorHora, double horasTrabajadas) {
        super(dpi, nombre);
        this.pagoPorHora = pagoPorHora;
        this.horasTrabajadas = horasTrabajadas;
    }

    public double getPagoPorHora() {
        return pagoPorHora;
    }

    public double getHorasTrabajadas() {
        return horasTrabajadas;
    }

    public void setPagoPorHora(double pagoPorHora) {
        if (!Double.isFinite(pagoPorHora) || pagoPorHora < 0) {
            throw new IllegalArgumentException("El pago por hora debe ser un número finito mayor o igual a 0.");
        }
        this.pagoPorHora = pagoPorHora;
    }

    public void setHorasTrabajadas(double horasTrabajadas) {
        if (!Double.isFinite(horasTrabajadas) || horasTrabajadas < 0) {
            throw new IllegalArgumentException("Las horas deben ser un número finito mayor o igual a 0.");
        }
        this.horasTrabajadas = horasTrabajadas;
    }

    /** Pago del mes = pago por hora x horas trabajadas. */
    @Override
    public double calcularPago() {
        return pagoPorHora * horasTrabajadas;
    }

    @Override
    public String obtenerTipo() {
        return "Por hora";
    }
}
