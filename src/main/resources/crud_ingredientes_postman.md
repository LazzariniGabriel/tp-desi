# CRUD INGREDIENTES (CATÁLOGO Y STOCK)

**INICIO: Puedes ejecutar `precarga-receta.sql` para tener algunos ingredientes iniciales.**

---

### **1. Crear un Ingrediente (POST)**
**Objetivo:** Añadir un nuevo ingrediente al catálogo.

* **URL:** `http://localhost:8080/ingredientes/agregar`
* **Método:** `POST`
* **Headers:** `Content-Type: application/json`
* **Body (raw - JSON):**
    ```json
    {
        "nombre": "Aceite de Oliva",
        "cantidadEnStock": 10.5
    }
    ```
* **Respuesta Esperada (201 Created):**
    ```json
    {
        "id": 12, // ID autogenerado
        "nombre": "Aceite de Oliva",
        "cantidadEnStock": 10.5,
        "activo": true
    }
    ```
    * **Anótate el `id` de este ingrediente.**

---

### **2. Listar Ingredientes (GET)**
**Objetivo:** Obtener todos los ingredientes en el catálogo.

* **URL:** `http://localhost:8080/ingredientes/listar`
* **Método:** `GET`
* **Respuesta Esperada (200 OK):** (Lista de todos los ingredientes activos y no activos si los hay)
    ```json
    [
        { "id": 1, "nombre": "Harina 000", "cantidadEnStock": 50.0, "activo": true },
        // ... otros ingredientes precargados
        { "id": 12, "nombre": "Aceite de Oliva", "cantidadEnStock": 10.5, "activo": true }
    ]
    ```

---

### **3. Actualizar Stock de un Ingrediente (PUT)**
**Objetivo:** Establecer una nueva cantidad de stock para un ingrediente.

* **URL:** `http://localhost:8080/ingredientes/actualizarstock/<ID_INGREDIENTE>` (reemplaza `<ID_INGREDIENTE>`)
* **Método:** `PUT`
* **Headers:** `Content-Type: application/json`
* **Body (raw - JSON):**
    ```json
    {
        "cantidadEnStock": 15.0
    }
    ```
* **Respuesta Esperada (200 OK):**
    ```json
    {
        "id": <ID_INGREDIENTE>,
        "nombre": "Aceite de Oliva",
        "cantidadEnStock": 15.0, // Cantidad actualizada
        "activo": true
    }
    ```

---

### **4. Agregar Stock a un Ingrediente (PUT)**
**Objetivo:** Incrementar la cantidad de stock de un ingrediente.

* **URL:** `http://localhost:8080/ingredientes/<ID_INGREDIENTE>/agregar-stock` (reemplaza `<ID_INGREDIENTE>`)
* **Método:** `PUT`
* **Headers:** `Content-Type: application/json`
* **Body (raw - JSON):**
    ```json
    {
        "pesoEnKG": 5.0
    }
    ```
* **Respuesta Esperada (200 OK):**
    ```json
    {
        "id": <ID_INGREDIENTE>,
        "nombre": "Aceite de Oliva",
        "cantidadEnStock": 20.0, // Cantidad anterior + 5.0
        "activo": true
    }
    ```

---

### **5. Descontar Stock de un Ingrediente (PUT)**
**Objetivo:** Disminuir la cantidad de stock de un ingrediente.

* **URL:** `http://localhost:8080/ingredientes/<ID_INGREDIENTE>/descontar-stock` (reemplaza `<ID_INGREDIENTE>`)
* **Método:** `PUT`
* **Headers:** `Content-Type: application/json`
* **Body (raw - JSON):**
    ```json
    {
        "pesoEnKG": 2.0
    }
    ```
* **Respuesta Esperada (200 OK):**
    ```json
    {
        "id": <ID_INGREDIENTE>,
        "nombre": "Aceite de Oliva",
        "cantidadEnStock": 18.0, // Cantidad anterior - 2.0
        "activo": true
    }
    ```
* **Escenario con Error: Stock Insuficiente**
    * **Body:** `{"pesoEnKG": 100.0}` (Cantidad mayor a la disponible)
    * **Respuesta Esperada (400 Bad Request):**
        ```
        "Stock insuficiente para Aceite de Oliva. Stock actual: 18.0, requerido: 100.0"
        ```

---

### **6. Eliminar un Ingrediente (DELETE - Lógica)**
**Objetivo:** Marcar un ingrediente del catálogo como inactivo.

* **URL:** `http://localhost:8080/ingredientes/eliminar/<ID_INGREDIENTE_A_ELIMINAR>`
* **Método:** `DELETE`
* **Respuesta Esperada (200 OK):**
    ```
    "Ingrediente eliminado"
    ```
* **Verificación:**
    1.  Realiza un `GET` a `http://localhost:8080/ingredientes/listar`. El ingrediente eliminado lógicamente **aún aparecerá** en el listado por defecto (porque `findAll()` trae todos), pero su campo `activo` será `false`. Si tu lógica de listado solo muestra activos, entonces no aparecerá.
    2.  Confirma en tu base de datos que el campo `activo` de la tabla `ingrediente` para el `ID_INGREDIENTE_A_ELIMINAR` cambió a `false`.