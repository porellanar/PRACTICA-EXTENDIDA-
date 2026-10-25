package modelo;

/**
 * Clase abstracta: concentra los datos comunes (DPI y nombre) de todo empleado.
 * No se puede hacer new Empleado(...); solo se instancian las clases hijas.
 */
public abstract class Empleado {

    // Atributos: estado que pertenece al objeto. Son private (encapsulación).
    private String dpi;
    private String nombre;

    /**
     * Constructor con parámetros.
     * "dpi" y "nombre" son PARÁMETROS; los valores que llegan desde Main son ARGUMENTOS.
     * this.dpi / this.nombre distinguen el atributo del parámetro.
     * Un constructor no declara tipo de retorno.
     */
    public Empleado(String dpi, String nombre) {
        this.dpi = dpi;
        this.nombre = nombre;
    }

    // Getters: métodos de acceso para consultar los datos.
    public String getDpi() {
        return dpi;
    }

    public String getNombre() {
        return nombre;
    }

    // Setters: solo se usan si hay que modificar; conservan las validaciones.
    public void setDpi(String dpi) {
        if (!esDpiValido(dpi)) {
            throw new IllegalArgumentException("El DPI debe tener exactamente 13 dígitos.");
        }
        this.dpi = dpi;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }
        this.nombre = nombre.trim();
    }

    /** Validación reutilizable: exactamente 13 dígitos (no se verifica autenticidad). */
    public static boolean esDpiValido(String dpi) {
        return dpi != null && dpi.matches("\\d{13}");
    }

    /** Método abstracto: cada clase hija define su propia fórmula. */
    public abstract double calcularPago();

    /** Método abstracto: cada clase hija indica su tipo para el reporte. */
    public abstract String obtenerTipo();
}
