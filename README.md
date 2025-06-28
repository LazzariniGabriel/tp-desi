# Proyecto DESI - Centro de Asistencia Social (UTN Santa Fe)

Este proyecto implementa un sistema de información para un centro de asistencia social, gestionando la entrega de alimentos a familias. El desarrollo se ha estructurado en 4 Épicas principales, abordando las necesidades de ABMC (Alta, Baja, Modificación y Consulta) de las entidades clave y la gestión de stock de alimentos.

---

## **Visión General de las Épicas Resueltas**

A continuación, se detalla una descripción general de cada épica y cómo ha sido implementada:

### **1. Épica 1: ABMC de Familias **

Esta épica permite al personal del centro gestionar la información de las familias asistidas.

* **Funcionalidades Clave:**
    * **Alta:** Registro de nuevas familias con sus integrantes (DNI, apellido, nombre, fecha de nacimiento, ocupación). Validación de DNI único por integrante y campos requeridos. Generación automática de un "Número de Familia Asistida".
    * **Baja (Lógica):** Las familias no se eliminan físicamente, se marcan como inactivas (`activa=false`). No aparecen en listados ni pueden recibir asistencia, pero su historial se mantiene.
    * **Modificación:** Permite actualizar datos de la familia y de sus integrantes. La eliminación de integrantes es lógica (`activo=false`) si no son incluidos en la lista de actualización. El número de familia es de solo lectura.
    * **Consulta/Listado:** Listado de familias activas con filtros por número de familia y nombre.

---

### **2. Épica 2: ABMC de Recetas **

Gestiona la creación y el mantenimiento de las recetas que se pueden preparar en el centro.

* **Funcionalidades Clave:**
    * **Alta:** Registro de recetas con nombre, descripción y una lista de ingredientes (con cantidad y calorías). Validación de nombre de receta único y campos requeridos. Los ingredientes se asocian a un catálogo de ingredientes existente.
    * **Baja (Lógica):** Las recetas se marcan como inactivas (`activa=false`). No se pueden seleccionar para preparaciones, pero su historial de uso se conserva.
    * **Modificación:** Permite actualizar la descripción y los ingredientes. El nombre de la receta es de solo lectura. La eliminación de ingredientes dentro de una receta también es lógica (`activo=false`).
    * **Consulta/Listado:** Listado de recetas activas, mostrando el nombre y las calorías totales. Permite filtrar por nombre y por rango de calorías.

---

### **3. Épica 3: ABMC de Preparaciones **

Esta épica registra la preparación de una receta específica en un día determinado, controlando el stock de ingredientes y raciones.

* **Funcionalidades Clave:**
    * **Registro:** Creación de una preparación indicando fecha, receta y cantidad de raciones.
        * **Validaciones Críticas:** La fecha no puede ser futura. No puede haber dos preparaciones de la misma receta en el mismo día. **Verifica stock de ingredientes antes de la preparación.**
        * **Control de Stock:** Automáticamente **descuenta los ingredientes** del catálogo al registrar una preparación.
        * **Stock de Raciones:** La preparación ahora tiene un campo `racionesDisponibles` que se inicializa con la cantidad de raciones preparadas.
    * **Baja (Lógica):** La preparación se marca como inactiva (`activa=false`).
        * **Control de Stock (CRÍTICO):** Al dar de baja una preparación, el sistema **reintegra el stock de ingredientes** consumidos a la fecha de creación.
    * **Modificación:** Solo se permite modificar la fecha de la preparación (la receta y cantidad de raciones son fijas porque ya afectaron el stock).
    * **Consulta/Listado:** Listado de preparaciones activas, mostrando fecha, nombre de receta, número de raciones preparadas y calorías por plato. Filtros por fecha y nombre de receta.

---

### **4. Épica 4: ABMC de Entrega de Alimentos**

Registra la entrega de raciones de alimentos a una familia específica, controlando el stock de raciones disponibles de las preparaciones.

* **Funcionalidades Clave:**
    * **Registro:** Permite registrar una entrega a una familia de un plato (preparación) específico con una cantidad de raciones.
        * **Validaciones Críticas:** La fecha de entrega se guarda automáticamente (hoy). No puede haber dos entregas a la misma familia en el mismo día. La cantidad de raciones no puede superar el número de integrantes activos de la familia. **Verifica stock de raciones de la preparación antes de la entrega.**
        * **Control de Stock (CRÍTICO):** Automáticamente **descuenta las raciones** del stock de la preparación correspondiente.
    * **Baja (Lógica):** La entrega se marca como inactiva (`activa=false`).
        * **Control de Stock (CRÍTICO):** Al dar de baja una entrega, las raciones entregadas **vuelven a estar disponibles** en el stock de la preparación original.
    * **Consulta/Listado:** Listado de entregas realizadas, mostrando número de familia, nombre de familia, plato entregado y número de raciones. Filtros por fecha, número de familia y nombre de familia.

---

## **Guía Rápida de Prueba (Una acción por Épica)**

Para verificar rápidamente la funcionalidad central de cada épica después de iniciar la aplicación (y asegurarte de que tu base de datos esté limpia y la `precarga-receta.sql` ejecutada), puedes seguir estos pasos.

**Asume:**
* La aplicación está corriendo en `http://localhost:8080`.
* Tienes un cliente HTTP como Postman o Insomnia.
* Tu `precarga-receta.sql` incluye los ingredientes y una familia con ID `1` y dos integrantes activos.
* Las fechas se ajustarán automáticamente a la fecha actual si no se especifican.

---

### **Paso 0: Preparación Inicial (SQL)**

Abre tu cliente de base de datos (MySQL Workbench, DBeaver, etc.) y ejecuta el script `precarga-receta.sql`. Esto cargará ingredientes y una familia inicial.

---

### **1. Probar Épica 1: ABMC de Familias (Alta)**

* **Objetivo:** Crear una nueva familia.
* **Endpoint:** `POST http://localhost:8080/familias`
* **Body (raw - JSON):**
    ```json
    {
        "nombreFamilia": "Familia Testeando ABMC",
        "integrantes": [
            {
                "dni": 200000001,
                "apellido": "Tester",
                "nombre": "Ana",
                "fechaNacimiento": "1990-01-01",
                "ocupacion": "EMPLEADO"
            },
            {
                "dni": 200000002,
                "apellido": "Tester",
                "nombre": "Pedro",
                "fechaNacimiento": "1992-02-02",
                "ocupacion": "ESTUDIANTE"
            }
        ]
    }
    ```
* **Verifica:** Deberías obtener un `201 Created` y ver los datos de la familia creada. **Anótate el `id` de esta familia (ej. `familiaId=2`).**

---

### **2. Probar Épica 2: ABMC de Recetas (Alta)**

* **Objetivo:** Crear una nueva receta.
* **Endpoint:** `POST http://localhost:8080/recetas/crear`
* **Body (raw - JSON):** (Asume que el ingrediente con `id=1` (Harina 000) existe de la precarga).
    ```json
    {
        "nombre": "Receta De Prueba ABMC",
        "descripcionPreparacion": "Una receta sencilla para verificar el ABMC.",
        "ingredientes": [
            { "ingrediente": {"id": 1}, "cantidad": 0.300, "calorias": 1000 }
        ]
    }
    ```
* **Verifica:** Deberías obtener un `201 Created`. **Anótate el `id` de esta receta (ej. `recetaId=4`).**

---

### **3. Probar Épica 3: ABMC de Preparaciones (Registro y Descuento de Ingredientes)**

* **Objetivo:** Registrar una preparación y verificar que descuenta ingredientes.
* **Endpoint:** `POST http://localhost:8080/preparaciones/registrar`
* **Body (x-www-form-urlencoded):**
    ```
    fecha=<FECHA_ACTUAL_EN_FORMATO_AAAA-MM-DD>
    idReceta=<ID_RECETA_DE_PRUEBA_ABMC> (ej. 4)
    cantidad=1
    ```
    (Ejemplo: `fecha=2025-06-28&idReceta=4&cantidad=1`)
* **Verifica:**
    1.  Deberías obtener un `201 Created` con los detalles de la preparación. **Anótate el `idPreparacion` (ej. `preparacionId=1`).**
    2.  Realiza un `GET` a `http://localhost:8080/ingredientes/listar`. El `cantidadEnStock` del ingrediente (`Harina 000`) debería haber disminuido (0.300 kg en este caso).

---

### **4. Probar Épica 4: ABMC de Entrega de Alimentos (Registro y Descuento de Raciones)**

* **Objetivo:** Registrar una entrega y verificar que descuenta raciones de la preparación.
* **Endpoint:** `POST http://localhost:8080/entregas`
* **Body (x-www-form-urlencoded):**
    ```
    familiaId=<ID_FAMILIA_DE_LA_PREC_CARGA> (ej. 1)
    preparacionId=<ID_PREPARACION> (ej. 1, de la prueba anterior)
    raciones=1
    ```
    (Ejemplo: `familiaId=1&preparacionId=1&raciones=1`)
* **Verifica:**
    1.  Deberías obtener un `201 Created` con los detalles de la entrega.
    2.  Realiza un `GET` a `http://localhost:8080/preparaciones/listar?fecha=<FECHA_ACTUAL>`. La preparación usada (`idPreparacion=1`) debería tener su `racionesDisponibles` disminuido (en este caso, de 1 a 0, si preparaste 1 y entregaste 1).

