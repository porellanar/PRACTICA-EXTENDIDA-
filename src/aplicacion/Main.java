package aplicacion;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

import modelo.Empleado;
import modelo.EmpleadoFijo;
import modelo.EmpleadoPorHora;

/**
 * Controla la selección del mes, los menús, la captura de datos y el reporte.
 * Los atributos son private (incluidos el arreglo y la colección).
 */
public class Main {

    // Arreglo simple: la cantidad de meses es fija (12) y está predefinida.
    private static final String[] meses = {
            "Enero", "Febrero", "Marzo", "Abril",
            "Mayo", "Junio", "Julio", "Agosto",
            "Septiembre", "Octubre", "Noviembre", "Diciembre"
    };

    // ArrayList: la cantidad de empleados crece conforme se ingresan.
    private static final ArrayList<Empleado> empleados = new ArrayList<>();

    private static final Scanner teclado = new Scanner(System.in);

    // Solo se guarda el número; el nombre se consulta siempre con meses[numeroMes - 1].
    private static int numeroMes;

    public static void main(String[] args) {
        numeroMes = seleccionarMes();

        int opcion;
        do {
            limpiarPantalla();
            mostrarMenuPrincipal();
            opcion = leerEntero("Seleccione una opción: ", 1, 3);

            if (opcion == 1) {
                ingresarNomina();
            } else if (opcion == 2) {
                imprimirNomina();
            }
        } while (opcion != 3);

        limpiarPantalla();
        System.out.println("Gracias por usar el programa de nómina. ¡Hasta pronto!");
    }

    // ---------------------------------------------------------------
    // Mes
    // ---------------------------------------------------------------

    /** Pide el mes y lo valida (1-12) ANTES de usarlo como índice. Devuelve el número. */
    private static int seleccionarMes() {
        System.out.println("CÁLCULO DE NÓMINA");
        return leerEntero("Ingrese el mes a trabajar (1-12): ", 1, 12);
    }

    /** Encabezado común: usa meses[numeroMes - 1] cada vez que se muestra el mes. */
    private static void mostrarTitulo() {
        System.out.println("NÓMINA DEL MES: " + meses[numeroMes - 1].toUpperCase());
    }

    // ---------------------------------------------------------------
    // Menús
    // ---------------------------------------------------------------

    private static void mostrarMenuPrincipal() {
        mostrarTitulo();
        System.out.println("1 - Ingreso de nómina");
        System.out.println("2 - Impresión de nómina");
        System.out.println("3 - Salir");
    }

    /** Opción 1: repite el ingreso mientras el usuario quiera agregar más empleados. */
    private static void ingresarNomina() {
        int continuar;
        do {
            limpiarPantalla();
            mostrarTitulo();
            System.out.println("INGRESO DE EMPLEADO A NÓMINA");
            System.out.println("Trabajadores actuales = " + empleados.size());
            System.out.println("1 - Empleado fijo");
            System.out.println("2 - Empleado por hora");
            int tipo = leerEntero("Seleccione el tipo de empleado: ", 1, 2);

            limpiarPantalla();
            Empleado nuevo;
            if (tipo == 1) {
                nuevo = capturarEmpleadoFijo();
            } else {
                nuevo = capturarEmpleadoPorHora();
            }

            // Solo se agrega cuando todos los datos fueron válidos.
            empleados.add(nuevo);

            System.out.println();
            System.out.println("Empleado agregado correctamente.");
            System.out.println("Trabajadores actuales = " + empleados.size());
            System.out.println("¿Desea ingresar otro empleado?");
            System.out.println("1 - Sí, ingresar otro empleado");
            System.out.println("2 - Volver al menú principal");
            continuar = leerEntero("Seleccione una opción: ", 1, 2);
        } while (continuar == 1);
    }

    // ---------------------------------------------------------------
    // Captura de datos
    // ---------------------------------------------------------------

    private static EmpleadoFijo capturarEmpleadoFijo() {
        mostrarTitulo();
        System.out.println("INGRESO DE EMPLEADO FIJO");
        String dpi = leerDpi();
        String nombre = leerNombre();
        double sueldo = leerDecimal("Sueldo mensual (Q): ");
        double bonificacion = leerDecimal("Bonificación de este mes (Q): ");

        // Instanciación: new crea un objeto independiente por cada empleado.
        // dpi, nombre, sueldo y bonificacion son los ARGUMENTOS del constructor.
        EmpleadoFijo empleado = new EmpleadoFijo(dpi, nombre, sueldo, bonificacion);

        System.out.println("Pago del mes = sueldo mensual + bonificación");
        System.out.println("Pago de " + empleado.getNombre() + " = Q " + formatearMonto(empleado.calcularPago()));
        return empleado;
    }

    private static EmpleadoPorHora capturarEmpleadoPorHora() {
        mostrarTitulo();
        System.out.println("INGRESO DE EMPLEADO POR HORA");
        String dpi = leerDpi();
        String nombre = leerNombre();
        double pagoPorHora = leerDecimal("Pago por hora (Q): ");
        double horas = leerDecimal("Horas trabajadas este mes: ");

        EmpleadoPorHora empleado = new EmpleadoPorHora(dpi, nombre, pagoPorHora, horas);

        System.out.println("Pago del mes = pago por hora x horas trabajadas");
        System.out.println("Pago de " + empleado.getNombre() + " = Q " + formatearMonto(empleado.calcularPago()));
        return empleado;
    }

    // ---------------------------------------------------------------
    // Reporte
    // ---------------------------------------------------------------

    /** Opción 2: solo consulta; no modifica la colección. */
    private static void imprimirNomina() {
        limpiarPantalla();
        mostrarTitulo();
        System.out.println();

        double total = 0;
        if (empleados.isEmpty()) {
            System.out.println("No hay empleados registrados en la nómina de este mes");
        } else {
            System.out.printf(Locale.US, "%-15s %-25s %-10s %12s%n", "DPI", "Nombre", "Tipo", "Pago (Q)");
            // Polimorfismo: la referencia es de tipo Empleado; cada objeto
            // ejecuta SU versión de calcularPago() y obtenerTipo().
            for (Empleado e : empleados) {
                double pago = e.calcularPago();
                total += pago;
                System.out.printf(Locale.US, "%-15s %-25s %-10s %12s%n",
                        e.getDpi(), e.getNombre(), e.obtenerTipo(), formatearMonto(pago));
            }
        }

        System.out.println();
        System.out.println("Total de trabajadores: " + empleados.size());
        System.out.println("Total de nómina: Q " + formatearMonto(total));
        System.out.println();
        System.out.print("Presione Enter para volver al menú principal...");
        teclado.nextLine();
    }

    // ---------------------------------------------------------------
    // Entrada y validación
    // ---------------------------------------------------------------

    /** Lee un entero entre minimo y maximo; repite hasta que sea válido. */
    private static int leerEntero(String mensaje, int minimo, int maximo) {
        while (true) {
            System.out.print(mensaje);
            String linea = teclado.nextLine().trim();
            try {
                int valor = Integer.parseInt(linea);
                if (valor >= minimo && valor <= maximo) {
                    return valor;
                }
                System.out.println("Error: ingrese un número entre " + minimo + " y " + maximo + ".");
            } catch (NumberFormatException ex) {
                System.out.println("Error: ingrese un número entero entre " + minimo + " y " + maximo + ".");
            }
        }
    }

    /** Lee un decimal finito mayor o igual a 0. */
    private static double leerDecimal(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String linea = teclado.nextLine().trim().replace(',', '.');
            try {
                double valor = Double.parseDouble(linea);
                if (Double.isFinite(valor) && valor >= 0) {
                    return valor;
                }
                System.out.println("Error: el valor debe ser un número mayor o igual a 0.");
            } catch (NumberFormatException ex) {
                System.out.println("Error: ingrese un valor numérico (por ejemplo 25 o 120.5).");
            }
        }
    }

    /** Lee el DPI: exactamente 13 dígitos, guardado como String. */
    private static String leerDpi() {
        while (true) {
            System.out.print("DPI: ");
            String dpi = teclado.nextLine().trim();
            if (Empleado.esDpiValido(dpi)) {
                return dpi;
            }
            System.out.println("Error: el DPI debe tener exactamente 13 dígitos numéricos.");
        }
    }

    /** Lee el nombre: no puede estar vacío ni tener solo espacios. */
    private static String leerNombre() {
        while (true) {
            System.out.print("Nombre: ");
            String nombre = teclado.nextLine().trim();
            if (!nombre.isEmpty()) {
                return nombre;
            }
            System.out.println("Error: el nombre no puede estar vacío.");
        }
    }

    // ---------------------------------------------------------------
    // Utilidades
    // ---------------------------------------------------------------

    /** Simula la limpieza de pantalla imprimiendo líneas en blanco (válido en cualquier consola de IDE). */
    private static void limpiarPantalla() {
        for (int i = 0; i < 40; i++) {
            System.out.println();
        }
    }

    /** Devuelve el monto con dos decimales (punto decimal). */
    private static String formatearMonto(double monto) {
        return String.format(Locale.US, "%.2f", monto);
    }
}
