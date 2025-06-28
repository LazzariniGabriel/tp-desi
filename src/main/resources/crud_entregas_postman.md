# CRUD ENTREGA DE ALIMENTOS

**INICIO: Asegúrate de tener la base de datos limpia y la precarga SQL ejecutada. Necesitarás el ID de una Familia activa y el ID de una Preparación activa (que tenga raciones disponibles).**

---

### **1. Registrar una Entrega (POST)**
**Objetivo:** Crear una entrega, verificando el descuento de raciones de la preparación y las validaciones de negocio.

* **URL:** `http://localhost:8080/entregas`
* **Método:** `POST`
* **Headers:** `Content-Type: application/x-www-form-urlencoded`
* **Body (x-www-form-urlencoded):**
    * **¡Importante!** Necesitarás el **ID de una Familia activa** (ej. la que crees en el `CRUD FAMILIA`) y el **ID de una Preparación activa** (que tengas raciones disponibles, la que crees en el `CRUD PREPARACIONES`).

    ```
    familiaId=<ID_FAMILIA>
    preparacionId=<ID_PREPARACION>
    raciones=2 
    ```
    (Reemplaza `<ID_FAMILIA>` y `<ID_PREPARACION>` con los valores reales.)

* **Respuesta Esperada (201 Created):**
    ```json
    {
      "id": 1,
      "familia": {
        "id": <ID_FAMILIA>,
        "nroFamilia": "FAM-XXXXXXX",
        "nombreFamilia": "Nombre de la Familia"
      },
      "preparacion": {
        "idPreparacion": <ID_PREPARACION>,
        "fechaPreparacion": "2025-06-28",
        "cantidadDeRacionesPreparar": 2, // Cantidad original preparada
        "racionesDisponibles": 0 // Aquí debe reflejar el descuento
      },
      "cantidadRaciones": 2,
      "fechaEntrega": "2025-06-28", // Fecha actual
      "activo": true
    }
    ```
    * **Verificación Crítica:**
        1.  Comprueba el campo `racionesDisponibles` en la respuesta de la `preparacion` dentro de la `entrega`. Debe haber disminuido por la cantidad entregada.
        2.  Realiza un `GET` a `/preparaciones/listar` para verificar el `racionesDisponibles` de la preparación en su estado actual en la DB.

---

### **2. Escenario con Error: Entrega repetida el mismo día para la misma familia**

* **URL:** `http://localhost:8080/entregas`
* **Método:** `POST`
* **Headers:** `Content-Type: application/x-www-form-urlencoded`
* **Body (x-www-form-urlencoded):** (Mismos `familiaId` y `fecha` que la anterior)
    ```
    familiaId=<ID_FAMILIA>
    preparacionId=<ID_PREPARACION>
    raciones=1
    ```
* **Respuesta Esperada (400 Bad Request):**
    ```
    "Ya se realizó una entrega hoy para esta familia."
    ```

---

### **3. Escenario con Error: Raciones a entregar mayores a los integrantes activos de la familia**

* **URL:** `http://localhost:8080/entregas`
* **Método:** `POST`
* **Headers:** `Content-Type: application/x-www-form-urlencoded`
* **Body (x-www-form-urlencoded):** (Asegúrate de que `raciones` sea mayor que `cantidadIntegrantesActivos` de la familia.)
    ```
    familiaId=<ID_FAMILIA>
    preparacionId=<ID_PREPARACION>
    raciones=5 
    ```
* **Respuesta Esperada (400 Bad Request):**
    ```
    "No se pueden entregar más raciones (5) que integrantes activos (<X>) en la familia."
    ```
    (Donde `<X>` es el número real de integrantes activos).

---

### **4. Escenario con Error: Stock de raciones insuficiente en la Preparación**

* **Precondición:** Debes haber creado una Preparación y haber entregado suficientes raciones para que su `racionesDisponibles` sea 0 o muy bajo. O crea una Preparación con `cantidad=1` y luego intenta entregar `raciones=2`.
* **URL:** `http://localhost:8080/entregas`
* **Método:** `POST`
* **Headers:** `Content-Type: application/x-www-form-urlencoded`
* **Body (x-www-form-urlencoded):**
    ```
    familiaId=<ID_FAMILIA>
    preparacionId=<ID_PREPARACION>
    raciones=1 
    ```
* **Respuesta Esperada (400 Bad Request):**
    ```
    "Stock de raciones insuficiente para la preparación. Disponibles: <X>, Requeridas: <Y>"
    ```
    (Donde `<X>` es el stock actual y `<Y>` es lo requerido).

---

### **5. Listar Entregas por Fecha y Filtros (GET)**

* **URL:** `http://localhost:8080/entregas?fecha=2025-06-28` (Usa la fecha actual)
* **Método:** `GET`
* **Respuesta Esperada (200 OK):**
    ```json
    [
      {
        "id": <ID_ENTREGA>,
        "familia": {
          "nroFamilia": "FAM-XXXXXXX",
          "nombreFamilia": "Nombre de la Familia"
        },
        "preparacion": { // Ahora devuelve la preparación completa o un DTO de la preparación
          "idPreparacion": <ID_PREPARACION>,
          "receta": {"nombre": "Nombre de la Receta"},
          "cantidadDeRacionesPreparar": <Cantidad_Preparada>
          // ... otros campos de Preparacion
        },
        "cantidadRaciones": 2,
        "fechaEntrega": "2025-06-28",
        "activo": true
      }
    ]
    ```
* **Filtrar por número de familia:**
    * **URL:** `http://localhost:8080/entregas?fecha=2025-06-28&nroFamilia=FAM-XXXXXXX`
* **Filtrar por nombre de familia:**
    * **URL:** `http://localhost:8080/entregas?fecha=2025-06-28&nombreFamilia=Ramirez`

---

### **6. Eliminar una Entrega (DELETE - Lógica)**
**Objetivo:** Marcar la entrega como inactiva y **reintegrar las raciones a la preparación**.

* **URL:** `http://localhost:8080/entregas/<ID_ENTREGA_A_ELIMINAR>`
* **Método:** `DELETE`
* **Respuesta Esperada (204 No Content):** `(No content)`
* **Verificación Crítica:**
    1.  Después de la eliminación, realiza un `GET` a `/preparaciones/listar` (o directo a la preparación por ID) para verificar el campo `racionesDisponibles` de la `Preparacion` original. **Debe haber aumentado** en la cantidad de raciones de la entrega eliminada.
    2.  Realiza un `GET` a `/entregas?fecha=<FECHA_DE_ENTREGA>` y verifica que la entrega eliminada ya no aparezca en el listado activo.
    3.  Confirma en tu base de datos que el campo `activo` de la tabla `entrega` para el `ID_ENTREGA_A_ELIMINAR` cambió a `false`.