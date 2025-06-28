# CRUD ENTREGA DE ALIMENTOS

**INICIO: Base de datos limpia y la precarga SQL ejecutada. Necesitarás el ID de una Familia activa y el ID de una Preparación activa (que tenga raciones disponibles).**

---

### **1. Registrar una Entrega (POST)**
**Objetivo:** Crear una entrega de asistencia, verificando el descuento de raciones de la preparación y las validaciones de negocio.

* **URL:** `http://localhost:8080/entregas`
* **Método:** `POST`
* **Headers:** `Content-Type: application/x-www-form-urlencoded`
* **Body (x-www-form-urlencoded):**
    * **¡Importante!** Necesitarás el **ID de una Familia activa** (ej. la de la precarga: `id=1`) y el **ID de una Preparación activa** (que tengas raciones disponibles, la que crees en el `CRUD PREPARACIONES`).

    ```
    familiaId=<ID_FAMILIA>
    preparacionId=<ID_PREPARACION>
    cantidadRaciones=2
    ```
    (Reemplaza `<ID_FAMILIA>` y `<ID_PREPARACION>` con los valores reales.)

* **Respuesta Esperada (201 Created):**
    ```json
    {
      "id": 1,
      "familia": {
        "id": <ID_FAMILIA>,
        "nroFamilia": "FAM-XXXXXXX",
        "nombre": "Nombre de la Familia" 
      },
      "preparacion": { 
        "id": <ID_PREPARACION>, 
        "fechaCoccion": "2025-06-28",
        "totalRacionesPreparadas": 2, 
        "stockRacionesRestantes": 0, 
        "receta": { "nombre": "Nombre de la Receta" },
        "activa": true
      },
      "cantidadRaciones": 2,
      "fecha": "2025-06-28", 
      "activo": true
    }
    ```
    * **Verificación Crítica:**
        1.  Comprueba el campo `stockRacionesRestantes` en la respuesta de la `preparacion` dentro de la `entrega`. Debe haber disminuido por la cantidad entregada.
        2.  Realiza un `GET` a `/preparaciones/listar` para verificar el `stockRacionesRestantes` de la preparación en su estado actual en la DB.

---

### **2. Escenario con Error: Entrega repetida el mismo día para la misma familia**

* **URL:** `http://localhost:8080/entregas`
* **Método:** `POST`
* **Headers:** `Content-Type: application/x-www-form-urlencoded`
* **Body (x-www-form-urlencoded):** (Mismos `familiaId` y `fecha` que la anterior)
    ```
    familiaId=<ID_FAMILIA>
    preparacionId=<ID_PREPARACION>
    cantidadRaciones=1
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
* **Body (x-www-form-urlencoded):** (`cantidadRaciones` debe ser mayor que `cantidadIntegrantesActivos` de la familia.)
    ```
    familiaId=<ID_FAMILIA>
    preparacionId=<ID_PREPARACION>
    cantidadRaciones=5 
    ```
* **Respuesta Esperada (400 Bad Request):**
    ```
    "No se pueden entregar más raciones (5) que integrantes activos (<X>) en la familia."
    ```
    (Donde `<X>` es el número real de integrantes activos).

---

### **4. Escenario con Error: Stock de raciones insuficiente en la Preparación**

* **Precondición:** Debes haber creado una Preparación y haber entregado suficientes raciones para que su `stockRacionesRestantes` sea 0 o muy bajo. O crea una Preparación con `totalRacionesPreparadas=1` y luego intenta entregar `cantidadRaciones=2`.

* **URL:** `http://localhost:8080/entregas`
* **Método:** `POST`
* **Headers:** `Content-Type: application/x-www-form-urlencoded`
* **Body (x-www-form-urlencoded):**
    ```
    familiaId=<ID_FAMILIA>
    preparacionId=<ID_PREPARACION>
    cantidadRaciones=1
    ```
* **Respuesta Esperada (400 Bad Request):**
    ```
    "Stock de raciones insuficiente para la preparación. Disponibles: <X>, Requeridas: <Y>"
    ```
    (Donde `<X>` es el stock actual y `<Y>` es lo requerido).

---

### **5. Listar Entregas por Fecha y Filtros (GET)**

* **URL:** `http://localhost:8080/entregas?fecha=2025-06-28` 
* **Método:** `GET`
* **Respuesta Esperada (200 OK):**
    ```json
    [
      {
        "id": <ID_ENTREGA_ASISTENCIA>,
        "familia": {
          "nroFamilia": "FAM-XXXXXXX",
          "nombre": "Nombre de la Familia" 
        },
        "preparacion": {
          "id": <ID_PREPARACION>, 
          "receta": {"nombre": "Nombre de la Receta"},
          "totalRacionesPreparadas": <Cantidad_Preparada> 
          
        },
        "cantidadRaciones": 2,
        "fecha": "2025-06-28", 
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

* **URL:** `http://localhost:8080/entregas/<ID_ENTREGA_ASISTENCIA_A_ELIMINAR>`
* **Método:** `DELETE`
* **Respuesta Esperada (204 No Content):** `(No content)`
* **Verificación Crítica:**
    1.  Después de la eliminación, realiza un `GET` a `/preparaciones/listar` (o directo a la preparación por ID) para verificar el campo `stockRacionesRestantes` de la `Preparacion` original. **Debe haber aumentado** en la cantidad de raciones de la entrega eliminada.
    2.  Realiza un `GET` a `/entregas?fecha=<FECHA_DE_ENTREGA>` y verifica que la entrega eliminada ya no aparezca en el listado activo.
    3.  Confirma en tu base de datos que el campo `activo` de la tabla `entrega_asistencia` para el `ID_ENTREGA_ASISTENCIA_A_ELIMINAR` cambió a `false`.