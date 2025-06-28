# Proyecto DESI - Centro de Asistencia Social (UTN - FR Santa Fe)

Este proyecto implementa un sistema de información para un centro de asistencia social, gestionando la entrega de alimentos a familias. El desarrollo se ha estructurado en 4 Épicas principales, abordando las necesidades de ABMC (Alta, Baja, Modificación y Consulta) de las entidades clave y la gestión de stock de alimentos.

---

## **Visión General de las Épicas Resueltas**

A continuación, se detalla una descripción general de cada épica y cómo ha sido implementada, reflejando el diseño del diagrama de clases UML proporcionado:

### **1. Épica 1: ABMC de Familias**

Esta épica permite al personal del centro gestionar la información de las familias asistidas.

* **Entidades Centrales:** `Familia`, `Asistido` (hereda de `Persona`).
* **Funcionalidades Clave:**
    * **Alta:** Registro de nuevas `Familia`s con sus `Asistido`s (que son un tipo de `Persona` con DNI, domicilio, apellido, nombre, fecha de nacimiento, y ocupación). Validación de DNI único (a nivel de `Persona`) y campos requeridos. Generación automática de un "Número de Familia Asistida". La `fechaRegistro` del `Asistido` se guarda al crearlo.
    * **Baja (Lógica):** Las `Familia`s no se eliminan físicamente, se marcan como inactivas (`activa=false`). No aparecen en listados ni pueden recibir asistencia, pero su historial se mantiene.
    * **Modificación:** Permite actualizar `nombre` de la `Familia` y `fechaUltimaAsistenciaRecibida`, así como modificar y agregar `Asistido`s existentes o nuevos. La eliminación de `Asistido`s de la `Familia` es lógica (`activo=false`) si no son incluidos en la lista de actualización. El `nroFamilia` es de solo lectura.
    * **Consulta/Listado:** Listado de `Familia`s activas con filtros por `nroFamilia` y `nombre`.

---

### **2. Épica 2: ABMC de Recetas**

Gestiona la creación y el mantenimiento de las `Receta`s que se pueden preparar en el centro.

* **Entidades Centrales:** `Receta`, `ItemReceta`, `Ingrediente` (abstracta), `Producto` (hereda de `Ingrediente`), `Condimento` (hereda de `Ingrediente`).
* **Funcionalidades Clave:**
    * **Alta:** Registro de `Receta`s con `nombre`, `descripcion` y una lista de `ItemReceta`s. Cada `ItemReceta` especifica una `cantidad` y se asocia a un `Ingrediente` existente (que puede ser un `Producto` o un `Condimento`). La `calorias` de cada `Ingrediente` se define en la clase `Ingrediente` misma. Validación de `nombre` de `Receta` único y campos requeridos.
    * **Baja (Lógica):** Las `Receta`s se marcan como inactivas (`activa=false`). No se pueden seleccionar para `Preparacion`es, pero su historial de uso se conserva.
    * **Modificación:** Permite actualizar la `descripcion` de la `Receta` y sus `ItemReceta`s. El `nombre` de la `Receta` es de solo lectura. La eliminación de `ItemReceta`s dentro de una `Receta` también es lógica (`activo=false`).
    * **Consulta/Listado:** Listado de `Receta`s activas, mostrando el `nombre` y las `caloriasTotales` (calculadas dinámicamente sumando `cantidad * calorias` de cada `ItemReceta` a través de su `Ingrediente` asociado). Permite filtrar por `nombre` y por rango de `caloriasTotales`.

---

### **3. Épica 3: ABMC de Preparaciones**

Esta épica registra la `Preparacion` de una `Receta` específica en un día determinado, controlando el stock de `Ingrediente`s (tipo `Producto`) y `totalRacionesPreparadas`.

* **Entidades Centrales:** `Preparacion` (asociada a una `Receta`).
* **Funcionalidades Clave:**
    * **Registro:** Creación de una `Preparacion` indicando `fechaCoccion`, `Receta` asociada y `totalRacionesPreparadas`.
        * **Validaciones Críticas:** La `fechaCoccion` no puede ser futura. No puede haber dos `Preparacion`es de la misma `Receta` en el mismo día. **Verifica el `stockDisponible` de los `Producto`s** (ingredientes con stock) antes de la `Preparacion`.
        * **Control de Stock de `Producto`s:** Automáticamente **descuenta los `Producto`s** del catálogo al registrar una `Preparacion`.
        * **Stock de Raciones:** La `Preparacion` ahora tiene un campo `stockRacionesRestantes` que se inicializa con el `totalRacionesPreparadas`.
    * **Baja (Lógica):** La `Preparacion` se marca como inactiva (`activa=false`).
        * **Control de Stock (CRÍTICO):** Al dar de baja una `Preparacion`, el sistema **reintegra el stock de `Producto`s** consumidos a la fecha de creación.
    * **Modificación:** Solo se permite modificar la `fechaCoccion` de la `Preparacion` (la `Receta` y `totalRacionesPreparadas` son fijas porque ya afectaron el stock).
    * **Consulta/Listado:** Listado de `Preparacion`es activas, mostrando `fechaCoccion`, `nombre` de la `Receta`, `totalRacionesPreparadas` y `caloriasPorPlato`. Filtros por `fechaCoccion` y `nombre` de `Receta`.

---

### **4. Épica 4: ABMC de Entrega de Alimentos**

Registra la `EntregaAsistencia` de `totalRacionesPreparadas` a una `Familia` específica, controlando el stock de `stockRacionesRestantes` de las `Preparacion`es.

* **Entidades Centrales:** `EntregaAsistencia` (asociada a una `Familia` y una `Preparacion`).
* **Funcionalidades Clave:**
    * **Registro:** Permite registrar una `EntregaAsistencia` a una `Familia` de un plato (`Preparacion`) específico con una `cantidadRaciones`.
        * **Validaciones Críticas:** La `fecha` de `EntregaAsistencia` se guarda automáticamente (hoy). No puede haber dos `EntregaAsistencia`s a la misma `Familia` en el mismo día. La `cantidadRaciones` no puede superar el número de `Asistido`s activos de la `Familia`. **Verifica `stockRacionesRestantes` de la `Preparacion` antes de la `EntregaAsistencia`.**
        * **Control de Stock (CRÍTICO):** Automáticamente **descuenta las raciones** del `stockRacionesRestantes` de la `Preparacion` correspondiente.
    * **Baja (Lógica):** La `EntregaAsistencia` se marca como inactiva (`activo=false`).
        * **Control de Stock (CRÍTICO):** Al dar de baja una `EntregaAsistencia`, las raciones entregadas **vuelven a estar disponibles** en el `stockRacionesRestantes` de la `Preparacion` original.
    * **Consulta/Listado:** Listado de `EntregaAsistencia`s realizadas, mostrando `nroFamilia`, `nombre` de `Familia`, plato (`Preparacion`) entregado y `cantidadRaciones`. Filtros por `fecha`, `nroFamilia` y `nombre` de `Familia`.

---

## **Guía Rápida de Prueba (Una acción por Épica)**

Para verificar rápidamente la funcionalidad central de cada épica después de iniciar la aplicación (y asegurarte de que tu base de datos esté limpia y la `precarga-receta.sql` ejecutada), puedes seguir estos pasos.

**Asume:**
* La aplicación está corriendo en `http://localhost:8080`.
* Tienes un cliente HTTP como Postman o Insomnia.
* Tu `precarga-receta.sql` incluye los ingredientes (como `Producto`s y `Condimento`s) y una familia inicial con `id=1` y dos `Asistido`s activos.
* Las fechas se ajustarán automáticamente a la fecha actual si no se especifican.

---

### **Paso 0: Preparación Inicial (SQL)**

Abre tu cliente de base de datos (MySQL Workbench, DBeaver, etc.) y ejecuta el script `precarga-receta.sql`. Este script está diseñado para limpiar tus tablas y precargar los datos iniciales (ingredientes con sus tipos, recetas, y una familia con asistidos) necesarios para las pruebas.

---

### **1. Probar Épica 1: ABMC de Familias (Alta)**

* **Objetivo:** Crear una nueva familia con `Asistido`s.
* **Endpoint:** `POST http://localhost:8080/familias`
* **Body (raw - JSON):**
    ```json
    {
        "nombre": "Familia Testeando ABMC",
        "integrantes": [
            {
                "dni": 200000001,
                "domicilio": "Calle Prueba 100",
                "apellido": "Tester",
                "nombre": "Ana",
                "fechaNacimiento": "1990-01-01",
                "ocupacion": "EMPLEADO"
            },
            {
                "dni": 200000002,
                "domicilio": "Calle Prueba 100",
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
* **Body (raw - JSON):** (Asume que el `Ingrediente` con `id=1` (Harina 000) existe como `Producto` de la precarga).
    ```json
    {
        "nombre": "Receta De Prueba ABMC",
        "descripcion": "Una receta sencilla para verificar el ABMC.",
        "items": [
            { "ingrediente": {"id": 1}, "cantidad": 0.300 } // Sin calorias aquí, se toman del Ingrediente
        ]
    }
    ```
* **Verifica:** Deberías obtener un `201 Created`. **Anótate el `id` de esta receta (ej. `recetaId=4`).**

---

### **3. Probar Épica 3: ABMC de Preparaciones (Registro y Descuento de Productos)**

* **Objetivo:** Registrar una `Preparacion` y verificar que descuenta `Producto`s.
* **Endpoint:** `POST http://localhost:8080/preparaciones/registrar`
* **Body (x-www-form-urlencoded):**
    ```
    fechaCoccion=<FECHA_ACTUAL_EN_FORMATO_AAAA-MM-DD>
    idReceta=<ID_RECETA_DE_PRUEBA_ABMC> (ej. 4)
    totalRacionesPreparadas=1
    ```
    (Ejemplo: `fechaCoccion=2025-06-28&idReceta=4&totalRacionesPreparadas=1`)
* **Verifica:**
    1.  Deberías obtener un `201 Created` con los detalles de la `Preparacion`. **Anótate el `id` de esta `Preparacion` (ej. `preparacionId=1`).**
    2.  Realiza un `GET` a `http://localhost:8080/ingredientes/listar`. El `stockDisponible` del `Producto` (`Harina 000`) debería haber disminuido (0.300 kg en este caso).

---

### **4. Probar Épica 4: ABMC de Entrega de Alimentos (Registro y Descuento de Raciones)**

* **Objetivo:** Registrar una `EntregaAsistencia` y verificar que descuenta `stockRacionesRestantes` de la `Preparacion`.
* **Endpoint:** `POST http://localhost:8080/entregas`
* **Body (x-www-form-urlencoded):**
    ```
    familiaId=<ID_FAMILIA_DE_LA_PREC_CARGA> (ej. 1)
    preparacionId=<ID_PREPARACION> (ej. 1, de la prueba anterior)
    cantidadRaciones=1
    ```
    (Ejemplo: `familiaId=1&preparacionId=1&cantidadRaciones=1`)
* **Verifica:**
    1.  Deberías obtener un `201 Created` con los detalles de la `EntregaAsistencia`.
    2.  Realiza un `GET` a `http://localhost:8080/preparaciones/listar?fecha=<FECHA_ACTUAL>`. La `Preparacion` usada (`id=1`) debería tener su `stockRacionesRestantes` disminuido (en este caso, de 1 a 0, si se preparó 1 y se entregó 1).

---