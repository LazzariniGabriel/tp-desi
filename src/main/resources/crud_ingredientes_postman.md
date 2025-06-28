# CRUD INGREDIENTES (CATÁLOGO Y STOCK)

**INICIO: Puedes ejecutar `precarga-receta.sql` para tener algunos ingredientes iniciales con sus tipos (Producto/Condimento).**

---

### **1. Crear un Producto (POST)**
**Objetivo:** Añadir un nuevo ingrediente de tipo Producto al catálogo, con stock y precio.

* **URL:** `http://localhost:8080/ingredientes/producto`
* **Método:** `POST`
* **Headers:** `Content-Type: application/json`
* **Body (raw - JSON):**
    ```json
    {
        "nombre": "Arroz Blanco",
        "calorias": 130,
        "stockDisponible": 25.0,
        "precioActual": 1.50
    }
    ```
* **Respuesta Esperada (201 Created):**
    ```json
    {
        "id": 12, 
        "nombre": "Arroz Blanco",
        "calorias": 130,
        "activo": true,
        "stockDisponible": 25.0,
        "precioActual": 1.50
    }
    ```
    * **Anótate el `id` de este producto.**

---

### **2. Crear un Condimento (POST)**
**Objetivo:** Añadir un nuevo ingrediente de tipo Condimento al catálogo.

* **URL:** `http://localhost:8080/ingredientes/condimento`
* **Método:** `POST`
* **Headers:** `Content-Type: application/json`
* **Body (raw - JSON):**
    ```json
    {
        "nombre": "Pimienta Negra",
        "calorias": 5
    }
    ```
* **Respuesta Esperada (201 Created):**
    ```json
    {
        "id": 13, 
        "nombre": "Pimienta Negra",
        "calorias": 5,
        "activo": true
    }
    ```
    * **Anótate el `id` de este condimento.**

---

### **3. Listar Todos los Ingredientes (GET)**
**Objetivo:** Obtener todos los ingredientes en el catálogo (Productos y Condimentos).

* **URL:** `http://localhost:8080/ingredientes/listar`
* **Método:** `GET`
* **Respuesta Esperada (200 OK):** (Lista de todos los ingredientes con sus atributos específicos de tipo)
    ```json
    [
        { "id": 1, "nombre": "Harina 000", "calorias": 364, "activo": true, "stockDisponible": 50.0, "precioActual": 1.20 },
        { "id": 6, "nombre": "Sal", "calorias": 0, "activo": true }, 
        { "id": 12, "nombre": "Arroz Blanco", "calorias": 130, "activo": true, "stockDisponible": 25.0, "precioActual": 1.50 },
        { "id": 13, "nombre": "Pimienta Negra", "calorias": 5, "activo": true }
    ]
    ```

---

### **4. Obtener Ingrediente por ID (GET)**
**Objetivo:** Obtener los detalles de un ingrediente específico por su ID.

* **URL:** `http://localhost:8080/ingredientes/<ID_INGREDIENTE>`
* **Método:** `GET`
* **Respuesta Esperada (200 OK):**
    ```json
    {
        "id": 12,
        "nombre": "Arroz Blanco",
        "calorias": 130,
        "activo": true,
        "stockDisponible": 25.0,
        "precioActual": 1.50
    }
    ```

---

### **5. Actualizar Stock de un Producto (PUT)**
**Objetivo:** Establecer una nueva cantidad de stock para un **Producto**.

* **URL:** `http://localhost:8080/ingredientes/producto/<ID_PRODUCTO>/actualizar-stock` (reemplaza `<ID_PRODUCTO>`)
* **Método:** `PUT`
* **Headers:** `Content-Type: application/json`
* **Body (raw - JSON):**
    ```json
    {
        "stockDisponible": 30.0
    }
    ```
* **Respuesta Esperada (200 OK):**
    ```json
    {
        "id": <ID_PRODUCTO>,
        "nombre": "Arroz Blanco",
        "calorias": 130,
        "activo": true,
        "stockDisponible": 30.0, 
        "precioActual": 1.50
    }
    ```

---

### **6. Agregar Stock a un Producto (PUT)**
**Objetivo:** Incrementar la cantidad de stock de un **Producto**.

* **URL:** `http://localhost:8080/ingredientes/producto/<ID_PRODUCTO>/agregar-stock`
* **Método:** `PUT`
* **Headers:** `Content-Type: application/json`
* **Body (raw - JSON):**
    ```json
    {
        "cantidad": 5.0
    }
    ```
* **Respuesta Esperada (200 OK):**
    ```json
    {
        "id": <ID_PRODUCTO>,
        "nombre": "Arroz Blanco",
        "calorias": 130,
        "activo": true,
        "stockDisponible": 35.0, 
        "precioActual": 1.50
    }
    ```

---

### **7. Descontar Stock de un Producto (PUT)**
**Objetivo:** Disminuir la cantidad de stock de un **Producto**.

* **URL:** `http://localhost:8080/ingredientes/producto/<ID_PRODUCTO>/descontar-stock`
* **Método:** `PUT`
* **Headers:** `Content-Type: application/json`
* **Body (raw - JSON):**
    ```json
    {
        "cantidad": 2.0
    }
    ```
* **Respuesta Esperada (200 OK):**
    ```json
    {
        "id": <ID_PRODUCTO>,
        "nombre": "Arroz Blanco",
        "calorias": 130,
        "activo": true,
        "stockDisponible": 33.0, 
        "precioActual": 1.50
    }
    ```
* **Escenario con Error: Stock Insuficiente**
    * **Body:** `{"cantidad": 100.0}` (Cantidad mayor a la disponible)
    * **Respuesta Esperada (400 Bad Request):**
        ```
        "Stock insuficiente para Arroz Blanco. Stock actual: 33.0, requerido: 100.0"
        ```

---

### **8. Eliminar un Ingrediente (DELETE - Lógica)**
**Objetivo:** Marcar cualquier ingrediente (Producto o Condimento) como inactivo.

* **URL:** `http://localhost:8080/ingredientes/eliminar/<ID_INGREDIENTE_A_ELIMINAR>`
* **Método:** `DELETE`
* **Respuesta Esperada (204 No Content):** `(No content)`
* **Verificación:**
    1.  Realiza un `GET` a `http://localhost:8080/ingredientes/listar`. El ingrediente eliminado lógicamente **aún aparecerá** en el listado por defecto (porque `findAll()` trae todos), pero su campo `activo` será `false`.
    2.  Confirma en tu base de datos que el campo `activo` de la tabla `ingrediente` para el `ID_INGREDIENTE_A_ELIMINAR` cambió a `false`.