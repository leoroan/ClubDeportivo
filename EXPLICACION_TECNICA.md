# Explicación Técnica: Arquitectura y Flujo de Datos

Este documento resume cómo funciona la aplicación "Club Deportivo" desde una perspectiva técnica, utilizando conceptos de Programación Orientada a Objetos (POO) y las herramientas nativas de Android.

---

## 1. El Modelo de Datos (POO)
En POO, representamos entidades del mundo real como objetos. En esta app, usamos `data classes` en Kotlin para definir qué es un **Socio**, un **Usuario** o una **Persona**.

**Ejemplo:**
```kotlin
data class Socio(
    val carnetNumero: Long,
    val nombre: String,
    val apellido: String,
    // ... otros atributos
)
```
Esto nos permite agrupar datos relacionados en una sola unidad que podemos manipular y transportar fácilmente por toda la app.

---

## 2. El Corazón de los Datos: `DatabaseHelper`
El `DatabaseHelper` actúa como un **intermediario** (Capa de Datos) entre la lógica de la aplicación y la base de datos SQLite (almacenamiento persistente).

- **Persistencia:** Cuando cierras la app, los datos no se borran porque están en un archivo de base de datos gestionado por este Helper.
- **Responsabilidad:** Se encarga de las operaciones CRUD (Crear, Leer, Actualizar, Borrar).
- **Relaciones:** Maneja cómo se vincula una `persona` con un `socio` o un `nosocio` mediante IDs (Primary Keys y Foreign Keys).

---

## 3. El Viaje de la Información entre Activities
En Android, las pantallas (Activities) son independientes. Para que la data "viaje" de una a otra, usamos un objeto llamado **Intent**.

### El proceso paso a paso:

1.  **Captura de datos:** El usuario escribe en un formulario (ej. `RegisterActivity`).
2.  **Creación del Objeto:** Creamos una instancia de nuestra clase (ej. `Socio`).
3.  **Guardado en BD:** Llamamos al `dbHelper.registrarSocio(socio)`. La base de datos nos devuelve un ID único.
4.  **Transporte (The "Bridge"):** Queremos ir a la pantalla de Pagos. Usamos el Intent y le "adjuntamos" el objeto.
    ```kotlin
    val intent = Intent(this, PagosActivity::class.java)
    intent.putExtra("socio", socioObjeto) // El socio viaja aquí
    startActivity(intent)
    ```
5.  **Recepción:** En `PagosActivity`, extraemos el objeto del Intent para mostrar el nombre y el monto a pagar.

---

## 4. Ejemplo Práctico: Registro de No Socio y Pago
Este es el flujo que corregimos recientemente:

1.  **Formulario:** Ingresas datos para un "Pase Diario".
2.  **Lógica del Helper:** 
    - Primero se guarda en la tabla `persona`.
    - Luego se guarda en la tabla `nosocio` vinculándolo a esa persona.
    - Se genera un registro en `pago_actividad` con el monto de la actividad elegida.
    - El Helper devuelve el `carnetTemporal`.
3.  **Navegación:** `RegisterActivity` crea un objeto `Socio` temporal con ese ID y lo envía a `PagosActivity`.
4.  **Actualización UI:** Al volver a la lista de búsqueda, el método `onResume()` fuerza a la app a preguntar de nuevo al Helper: *"¿Quiénes deben todavía?"*, y como el pago ya se registró, esa persona ya no aparece en la lista.

---

## Conceptos Clave para Recordar:
- **Activities:** Pantallas de la app.
- **Intents:** Mensajes o sobres que transportan datos entre pantallas.
- **SQLite:** La caja fuerte donde guardamos la información.
- **Modelos (Models):** Los moldes que definen cómo es nuestra información.

---

## 5. Los Adapters: Los Intérpretes del Listado
En Android, cuando tenemos una lista de datos (como 100 socios) y queremos mostrarla en pantalla, no podemos simplemente "tirar" los datos al diseño. Necesitamos un **Adapter** (ej. `PersonaAdapter`).

### ¿Qué hace un Adapter?
Imagina que el **Adapter** es como un "Traductor" o un "Mesero":
1.  **Recibe la materia prima:** Una lista de objetos (ej. `List<Socio>`).
2.  **Usa un molde (Layout):** Toma un archivo XML (como `item_persona.xml`) que define cómo se ve una sola fila.
3.  **Crea la fila:** Por cada socio en la lista, el adapter "infla" el molde, pone el nombre del socio en el `TextView` correspondiente y entrega esa fila terminada al **RecyclerView** (el componente que desliza).

**En resumen:** El `RecyclerView` es el estante vacío, y el `Adapter` es quien fabrica y coloca cada producto en su lugar.

