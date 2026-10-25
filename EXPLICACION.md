# Práctica 11 – Nómina mensual en Java
Programación II · Universidad Mariano Gálvez

## Estructura del proyecto

```
nomina/
├── src/
│   ├── modelo/
│   │   ├── Empleado.java          (abstract)
│   │   ├── EmpleadoFijo.java
│   │   └── EmpleadoPorHora.java
│   └── aplicacion/
│       └── Main.java
└── EXPLICACION.md
```

Compilar y ejecutar (desde la carpeta `nomina`):

```
javac -encoding UTF-8 -d out src/modelo/*.java src/aplicacion/*.java
java -cp out aplicacion.Main
```

En un IDE basta con abrir `src` como carpeta de fuentes y ejecutar `Main`.

---

## 1. Organización y creación de objetos

| Concepto | Dónde | Por qué |
|---|---|---|
| **Proyecto** | Carpeta `nomina` | Proyecto ejecutable con un único punto de entrada: `Main.main`. |
| **package** | `package modelo;` en las tres clases de empleados; `package aplicacion;` en `Main` | Separa los datos (modelo) de la interacción con el usuario (aplicación). |
| **import** | `Main.java`: `ArrayList`, `Locale`, `Scanner`, `Empleado`, `EmpleadoFijo`, `EmpleadoPorHora` | `Main` está en otro paquete, así que necesita importar las clases del modelo y las de `java.util`. |
| **Clase** | Cada `.java` define una: `Empleado`, `EmpleadoFijo`, `EmpleadoPorHora`, `Main` | Una clase por archivo, como pide el enunciado. |
| **Objeto / instancia / instanciación** | `Main.capturarEmpleadoFijo()`: `new EmpleadoFijo(dpi, nombre, sueldo, bonificacion)` | Cada `new` crea un objeto independiente; luego se agrega con `empleados.add(...)`. Ana y Luis son dos instancias distintas. |
| **Constructor** | `EmpleadoFijo(String dpi, String nombre, double sueldoMensual, double bonificacion)` | Recibe los datos iniciales. No lleva tipo de retorno. |
| **Parámetro vs. argumento** | En el constructor de `EmpleadoFijo`, `dpi`, `nombre`, `sueldoMensual` y `bonificacion` son **parámetros** (se declaran en la firma). En `Main`, las variables `dpi`, `nombre`, `sueldo` y `bonificacion` que se pasan en `new EmpleadoFijo(...)` son los **argumentos** (los valores concretos, por ejemplo `"1234567890101"`, `"Ana López"`, `4000`, `300`). | El parámetro es el "hueco" y el argumento es lo que lo llena en cada llamada. |
| **this** | `this.nombre = nombre;` en `Empleado`; `this.sueldoMensual = sueldoMensual;` en `EmpleadoFijo` | Distingue el atributo del objeto (izquierda) del parámetro con el mismo nombre (derecha). |

## 2. Encapsulación, abstracción y herencia

| Concepto | Dónde | Por qué |
|---|---|---|
| **Atributos private** | `dpi`, `nombre`, `sueldoMensual`, `bonificacion`, `pagoPorHora`, `horasTrabajadas`; en `Main`: `meses`, `empleados`, `teclado`, `numeroMes` | Nada se expone como `public` ni `protected`. El arreglo y el `ArrayList` también son `private`. |
| **Getters** | `getDpi()`, `getNombre()`, `getSueldoMensual()`, etc. | `Main` consulta los datos solo mediante métodos de acceso (por ejemplo, `e.getDpi()` en el reporte). |
| **Setters con validación** | `setDpi`, `setNombre`, `setSueldoMensual`, `setBonificacion`, `setPagoPorHora`, `setHorasTrabajadas` | Existen por si se necesita modificar; cada uno lanza `IllegalArgumentException` si el dato no cumple las reglas. |
| **Clase abstracta** | `public abstract class Empleado` | Concentra DPI y nombre. No existe `new Empleado(...)`; solo se crean hijas. |
| **Método abstracto** | `public abstract double calcularPago();` (y `obtenerTipo()`) | Obliga a cada hija a definir su fórmula; la base no sabe cómo se paga. |
| **Herencia** | `EmpleadoFijo extends Empleado`, `EmpleadoPorHora extends Empleado` | Reutiliza DPI y nombre sin duplicarlos. |
| **super(...)** | Primera línea de los constructores hijos: `super(dpi, nombre);` | Inicializa los datos comunes en la base. Las hijas no acceden directamente a esos atributos. |
| **Sobrescritura (@Override)** | `calcularPago()` en `EmpleadoFijo` (`sueldoMensual + bonificacion`) y en `EmpleadoPorHora` (`pagoPorHora * horasTrabajadas`); también `obtenerTipo()` | Cada clase resuelve su propio cálculo. |
| **Polimorfismo** | `Main.imprimirNomina()`: `for (Empleado e : empleados) { e.calcularPago(); e.obtenerTipo(); }` | La referencia es de tipo `Empleado`, pero en ejecución se usa el método de la clase real. El reporte **no** usa `if`, `switch` ni `instanceof` para elegir la fórmula. El único `if` por tipo está en el menú de ingreso, donde se decide qué objeto crear. |

## 3. Arreglo simple y ArrayList

| Elemento | Dónde | Por qué |
|---|---|---|
| **`String[] meses`** | `Main`, atributo `private static final String[] meses = {...}` | Son 12 valores fijos y conocidos: un arreglo simple es lo adecuado. |
| **`meses[numeroMes - 1]`** | `Main.mostrarTitulo()` | Los índices empiezan en 0: el mes 3 → `meses[2]` → "Marzo". Se consulta cada vez que se muestra el mes; no se guarda el nombre en otra variable ni se usa `if`/`switch`. (`toUpperCase()` solo adapta el formato del título, no resuelve el nombre.) |
| **Validación del índice** | `Main.seleccionarMes()` → `leerEntero(..., 1, 12)` | El mes se valida antes de usarse como índice, así que nunca se accede a una posición inexistente. |
| **`ArrayList<Empleado>`** | `Main`, atributo `private static final ArrayList<Empleado> empleados` | La cantidad de empleados crece mientras se ingresan; guarda ambos tipos en una sola colección. |
| **`add()`** | `Main.ingresarNomina()`: `empleados.add(nuevo);` | Se ejecuta solo cuando todos los datos ya fueron válidos, por eso el contador sube únicamente después de guardar. |
| **`size()`** | `ingresarNomina()` (`Trabajadores actuales = ...`) e `imprimirNomina()` (`Total de trabajadores: ...`) | Es el contador oficial; no hay una variable aparte que pueda desincronizarse. |
| **Recorrido** | `for (Empleado e : empleados)` en `imprimirNomina()` | Llama a `calcularPago()` en cada objeto y acumula el total. El reporte no borra ni duplica registros. |

## 4. Métodos, ámbito y alcance

- **`main` como punto de entrada**: solo coordina el ciclo del menú y delega el trabajo.
- **Métodos delegados**: `seleccionarMes`, `mostrarTitulo`, `mostrarMenuPrincipal`, `ingresarNomina`, `capturarEmpleadoFijo`, `capturarEmpleadoPorHora`, `imprimirNomina`, `leerEntero`, `leerDecimal`, `leerDpi`, `leerNombre`, `limpiarPantalla`, `formatearMonto`.
- **Métodos con parámetros**: `leerEntero(String mensaje, int minimo, int maximo)`, `leerDecimal(String mensaje)`, `formatearMonto(double monto)`.
- **Métodos que devuelven un resultado**: `leerEntero` (`int`), `leerDecimal` (`double`), `leerDpi` y `leerNombre` (`String`), `capturarEmpleadoFijo` (`EmpleadoFijo`), `calcularPago` (`double`).

**Atributo, parámetro y variable local (ejemplo en `EmpleadoFijo` / `Main`):**

| Tipo | Ejemplo | Desde dónde se puede usar |
|---|---|---|
| **Atributo** | `sueldoMensual` en `EmpleadoFijo` | Desde todos los métodos de esa clase, mientras el objeto exista. Fuera de la clase solo mediante `getSueldoMensual()`. |
| **Parámetro** | `sueldoMensual` en el constructor `EmpleadoFijo(...)`; `monto` en `formatearMonto` | Solo dentro del constructor/método que lo declara, y solo durante esa llamada. |
| **Variable local** | `double total` dentro de `imprimirNomina()`; `double pago` dentro del `for` | Solo en el bloque donde se declara. `pago` existe únicamente en cada vuelta del ciclo. |

Los métodos se comunican por argumentos y valores de retorno (por ejemplo, `capturarEmpleadoFijo()` devuelve el objeto y `ingresarNomina()` lo agrega a la lista) o mediante getters.

## 5. Comprobación (sección 11 del enunciado)

Entradas: mes `3`; empleado fijo `1234567890101`, Ana López, Q 4000, bonificación Q 300; empleado por hora `9876543210101`, Luis Pérez, Q 25, 120 horas. Salida del reporte:

```
NÓMINA DEL MES: MARZO

DPI             Nombre                    Tipo           Pago (Q)
1234567890101   Ana López                 Fijo            4300.00
9876543210101   Luis Pérez                Por hora        3000.00

Total de trabajadores: 2
Total de nómina: Q 7300.00
```

Otras pruebas realizadas:

- Mes `1` muestra ENERO y mes `12` muestra DICIEMBRE (`meses[0]` y `meses[11]`).
- Mes `0`, `13`, `abc` o vacío: se rechaza y se vuelve a pedir; nunca se usa como índice.
- Reporte sin empleados: muestra "No hay empleados registrados en la nómina de este mes", 0 trabajadores y Q 0.00.
- DPI con menos o más de 13 dígitos, o con letras: se rechaza y se pide de nuevo.
- Nombre vacío o solo espacios: se rechaza.
- Texto en campos numéricos (`abc`) y valores negativos (`-5`): se rechazan sin cerrar el programa.
- Volver al menú principal desde el registro y recuperarse de opciones inválidas en los menús.
- Consultar el reporte varias veces no cambia la cantidad de empleados.

## Nota sobre la limpieza de pantalla

`limpiarPantalla()` imprime 40 líneas en blanco. El enunciado acepta esta simulación y funciona igual en cualquier consola de IDE (los códigos ANSI se muestran como texto extraño en varias de ellas).
