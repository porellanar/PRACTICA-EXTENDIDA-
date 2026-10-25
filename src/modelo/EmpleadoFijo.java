package modelo;

/** Empleado con sueldo mensual fijo más una bonificación del mes. */
public class EmpleadoFijo extends Empleado {

    private double sueldoMensual;
    private double bonificacion;

    /**
     * Parámetros: dpi, nombre, sueldoMensual, bonificacion.
     * super(...) inicializa los datos comunes en la clase base.
     */
    public EmpleadoFijo(String dpi, String nombre, double sueldoMensual, double bonificacion) {
        super(dpi, nombre);
        this.sueldoMensual = sueldoMensual;
        this.bonificacion = bonificacion;
    }

    public double getSueldoMensual() {
        return sueldoMensual;
    }

    public double getBonificacion() {
        return bonificacion;
    }

    public void setSueldoMensual(double sueldoMensual) {
        if (!Double.isFinite(sueldoMensual) || sueldoMensual < 0) {
            throw new IllegalArgumentException("El sueldo debe ser un número finito mayor o igual a 0.");
        }
        this.sueldoMensual = sueldoMensual;
    }

    public void setBonificacion(double bonificacion) {
        if (!Double.isFinite(bonificacion) || bonificacion < 0) {
            throw new IllegalArgumentException("La bonificación debe ser un número finito mayor o igual a 0.");
        }
        this.bonificacion = bonificacion;
    }

    /** Pago del mes = sueldo mensual + bonificación. */
    @Override
    public double calcularPago() {
        return sueldoMensual + bonificacion;
    }

    @Override
    public String obtenerTipo() {
        return "Fijo";
    }
}
