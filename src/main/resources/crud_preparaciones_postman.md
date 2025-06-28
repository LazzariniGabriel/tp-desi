# CRUD PREPARACIONES

**INICIO: Asegúrate de tener una Receta activa y, si vas a probar la creación, suficiente stock de sus ingredientes.**

---

### **1. Registrar una Preparación (POST)**
**Objetivo:** Crear una preparación, verificar descuento de stock de ingredientes, validaciones de fecha y duplicidad.

* **URL:** `http://localhost:8080/preparaciones/registrar`
* **Método:** `POST`
* **Headers:** `Content-Type: application/x-www-form-urlencoded`
* **Body (x-www-form-urlencoded):**
    * **¡Importante!** Usa el `idReceta` de una receta que tengas activa (ej. "Pan Casero Simple" `id=1`).
    * **Asegúrate de tener stock suficiente** de los ingredientes de esa receta (usa `/ingredientes/agregar-stock` si es necesario).

    ```
    fecha=2025-06-28 // Fecha actual
    idReceta=1     // ID de una receta activa (ej. Pan Casero Simple)
    cantidad=2     // Cantidad de raciones a preparar
    ```
* **Respuesta Esperada (201 Created):**
    ```json
    {
      "mensaje": "Preparacion registrada con exito",
      "datos": {
        "idPreparacion": 1, // Autogenerado
        "fechaPreparacion": "2025-06-28",
        "receta": { // Objeto de Receta asociada
          "id": 1,
          "nombre": "Pan Casero Simple",
          "descripcionPreparacion": "Mezclar ingredientes, amasar y hornear.",
          "activa": true
          // ... otros campos de Receta
        },
        "cantidadDeRacionesPreparar": 2,
        "racionesDisponibles": 2, // Inicialmente igual a cantidadDeRacionesPreparar
        "estadoPreparacion": "FINALIZADA",
        "activa": true // Ahora se llama 'activa'
      }
    }
    ```
    * **Anótate el `idPreparacion` de esta preparación.**
    * **Verificación Crítica:**
        1.  Comprueba el stock de los ingredientes usados en la receta (usa `GET /ingredientes/listar` o tu DB). Debería haber **disminuido** por la cantidad correspondiente.

---

### **2. Escenario con Error: Fecha Futura**

* **URL:** `http://localhost:8080/preparaciones/registrar`
* **Método:** `POST`
* **Headers:** `Content-Type: application/x-www-form-urlencoded`
* **Body (x-www-form-urlencoded):**
    ```
    fecha=2025-07-01 // Fecha futura
    idReceta=1
    cantidad=1
    ```
* **Respuesta Esperada (400 Bad Request):**
    ```
    "La fecha de preparación no puede ser futura."
    ```

---

### **3. Escenario con Error: Preparación Duplicada para la misma Receta y Fecha**

* **Precondición:** Ya has creado una preparación para `idReceta=1` y `fecha=2025-06-28`.
* **URL:** `http://localhost:8080/preparaciones/registrar`
* **Método:** `POST`
* **Headers:** `Content-Type: application/x-www-form-urlencoded`
* **Body (x-www-form-urlencoded):**
    ```
    fecha=2025-06-28
    idReceta=1
    cantidad=1
    ```
* **Respuesta Esperada (400 Bad Request):**
    ```
    "Ya existe una preparación con esta receta para la fecha indicada."
    ```

---

### **4. Escenario con Error: Stock de Ingredientes Insuficiente**

* **Precondición:** Asegúrate de que los ingredientes de la receta elegida tengan stock bajo o cero.
* **URL:** `http://localhost:8080/preparaciones/registrar`
* **Método:** `POST`
* **Headers:** `Content-Type: application/x-www-form-urlencoded`
* **Body (x-www-form-urlencoded):**
    ```
    fecha=2025-06-28
    idReceta=<ID_RECETA_CON_POCO_STOCK_DE_INGREDIENTES>
    cantidad=10 // Una cantidad que agote el stock
    ```
* **Respuesta Esperada (400 Bad Request):**
    ```
    "Stock insuficiente para: <Nombre_Ingrediente>. Se necesitan X KG, hay Y KG. Faltan Z KG."
    ```

---

### **5. Listar Preparaciones Activas (GET)**
**Objetivo:** Obtener un listado de preparaciones con filtros.

* **URL:** `http://localhost:8080/preparaciones/listar`
* **Método:** `GET`
* **Respuesta Esperada (200 OK):**
    ```json
    {
      "mensaje": "Preparaciones activas obtenidas correctamente",
      "datos": [
        {
          "nombreReceta": "Pan Casero Simple",
          "fechaDePreparacion": "2025-06-28",
          "numeroDeRacionesPreparadas": 2,
          "caloriasPorPlato": 1915 // Calorías totales de la receta asociada
        }
      ]
    }
    ```
* **Filtrar por fecha:**
    * **URL:** `http://localhost:8080/preparaciones/listar?fecha=2025-06-28`
* **Filtrar por nombre de receta:**
    * **URL:** `http://localhost:8080/preparaciones/listar?nombreReceta=Pan`
* **Filtrar por fecha Y nombre de receta:**
    * **URL:** `http://localhost:8080/preparaciones/listar?fecha=2025-06-28&nombreReceta=Pan`

---

### **6. Modificar Fecha de Preparación (PUT)**
**Objetivo:** Actualizar la fecha de una preparación (sin cambiar la receta ni cantidad).

* **URL:** `http://localhost:8080/preparaciones/modificar-fecha`
* **Método:** `PUT`
* **Headers:** `Content-Type: application/x-www-form-urlencoded`
* **Body (x-www-form-urlencoded):** (Usa el `idPreparacion` que anotaste al crearla.)
    ```
    id=<ID_PREPARACION>
    nuevaFecha=2025-06-27 // Una fecha pasada (pero no futura respecto a hoy)
    ```
* **Respuesta Esperada (200 OK):**
    ```json
    {
      "mensaje": "Fecha de la preparacion modificada con exito",
      "datos": {
        "idPreparacion": <ID_PREPARACION>,
        "fechaPreparacion": "2025-06-27", // Fecha actualizada
        // ... otros campos
      }
    }
    ```
* **Escenario con Error: Nueva Fecha Futura**
    * **Body:** `id=<ID_PREPARACION>&nuevaFecha=2025-07-01`
    * **Respuesta Esperada (400 Bad Request):**
        ```
        "La nueva fecha de preparación no puede ser futura."
        ```

---

### **7. Dar de Baja una Preparación (PUT - Lógica)**
**Objetivo:** Marcar la preparación como inactiva y **reintegrar el stock de ingredientes**.

* **URL:** `http://localhost:8080/preparaciones/dar-baja/<ID_PREPARACION_A_ELIMINAR>`
* **Método:** `PUT`
* **Respuesta Esperada (200 OK):**
    ```json
    {
      "mensaje": "Preparacion dada de baja correctamente. Stock reintegrado.",
      "datos": null
    }
    ```
* **Verificación Crítica:**
    1.  **¡CRÍTICO!** Después de la eliminación, realiza un `GET` a `/ingredientes/listar` (o directamente a los IDs de ingredientes afectados en tu DB). **El stock de los ingredientes usados en esa preparación debería haber AUMENTADO** por la cantidad correspondiente.
    2.  Realiza un `GET` a `http://localhost:8080/preparaciones/listar`. La preparación eliminada lógicamente no debería aparecer en el listado.
    3.  Confirma en tu base de datos que el campo `activa` de la tabla `preparacion` para el `ID_PREPARACION_A_ELIMINAR` cambió a `false`.