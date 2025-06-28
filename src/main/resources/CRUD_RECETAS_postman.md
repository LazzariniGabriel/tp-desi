# CRUD RECETAS

**INICIO: Asegúrate de que tu base de datos tenga los ingredientes base cargados (usa `precarga-receta.sql`).**

---

### **1. Creación de Receta (POST)**

* **URL:** `http://localhost:8080/recetas/crear`
* **Método:** `POST`
* **Headers:** `Content-Type: application/json`
* **Body (raw - JSON):**
    * **¡Importante!** Las calorías ahora están en el `Ingrediente` (Producto/Condimento), no en `items`. Solo envías `ingrediente_id` y `cantidad` para el `item`.

    ```json
    {
        "nombre": "Curry de Garbanzos y Espinacas",
        "descripcion": "Un curry vegano y nutritivo, perfecto para una comida rápida entre semana. Se cocina a fuego lento con especias aromáticas.",
        "items": [ 
            { "ingrediente": {"id": 9}, "cantidad": 0.200 }, 
            { "ingrediente": {"id": 10}, "cantidad": 0.010 }, 
            { "ingrediente": {"id": 8}, "cantidad": 0.400 }, 
            { "ingrediente": {"id": 1}, "cantidad": 0.030 } 
        ]
    }
    ```
* **Respuesta Esperada (201 Created):**
    ```json
    {
        "id": 5, 
        "nombre": "Curry de Garbanzos y Espinacas",
        "descripcion": "Un curry vegano y nutritivo, perfecto para una comida rápida entre semana. Se cocina a fuego lento con especias aromáticas.",
        "activa": true,
        "items": [ 
            { "id": 13, "ingrediente": { "id": 9, "nombre": "Cebolla", "calorias": 40, "activo": true }, "cantidad": 0.2, "activo": true }, 
            { "id": 14, "ingrediente": { "id": 10, "nombre": "Ajo", "calorias": 149, "activo": true }, "cantidad": 0.01, "activo": true },
            { "id": 15, "ingrediente": { "id": 8, "nombre": "Tomate", "calorias": 18, "activo": true }, "cantidad": 0.4, "activo": true },
            { "id": 16, "ingrediente": { "id": 1, "nombre": "Harina 000", "calorias": 364, "activo": true }, "cantidad": 0.03, "activo": true }
        ],
        "caloriasTotales": 285 
    }
    ```
    * **Anótate el `id` de esta receta y los `id` de sus `items` (ItemReceta).**

---

### **2. Escenario con Error: Nombre de Receta Duplicado**

* **URL:** `http://localhost:8080/recetas/crear`
* **Método:** `POST`
* **Headers:** `Content-Type: application/json`
* **Body (raw - JSON):** (Usa un nombre ya existente, ej. "Curry de Garbanzos y Espinacas")
    ```json
    {
        "nombre": "Curry de Garbanzos y Espinacas",
        "descripcion": "Otro intento de crear el mismo curry.",
        "items": []
    }
    ```
* **Respuesta Esperada (400 Bad Request):**
    ```
    "Ya existe una receta con el nombre: Curry de Garbanzos y Espinacas."
    ```

---

### **3. Escenario con Errores: Datos Faltantes o Inválidos**

* **URL:** `http://localhost:8080/recetas/crear`
* **Método:** `POST`
* **Headers:** `Content-Type: application/json`
* **Body (raw - JSON):**
    ```json
    {
        "nombre": "", 
        "descripcion": null, 
        "items": [
            { "ingrediente": {"id": 999}, "cantidad": -5.0 }, 
            { "ingrediente": null, "cantidad": 10.0 } 
        ]
    }
    ```
* **Respuesta Esperada (400 Bad Request):**
    ```json
    {
        "descripcion": "La Descripción es requerida.",
        "nombre": "El Nombre de la receta es requerido."
    }
    ```

---

### **4. Listar Recetas Activas (GET)**

* **URL:** `http://localhost:8080/recetas`
* **Método:** `GET`
* **Respuesta Esperada (200 OK):** (Debería listar las recetas activas, incluyendo la que creaste.)
    ```json
    [
        {
            "id": 1,
            "nombre": "Pan Casero Simple",
            "descripcion": "Mezclar ingredientes, amasar y hornear.",
            "activa": true,
            "items": [],
            "caloriasTotales": 2045 
        },
        {
            "id": 2,
            "nombre": "Salsa Bolognesa",
            "descripcion": "Sofreír carne, cebolla y ajo, añadir tomate y cocinar a fuego lento.",
            "activa": true,
            "items": [],
            "caloriasTotales": 3226 
        },
        {
            "id": 5,
            "nombre": "Curry de Garbanzos y Espinacas",
            "descripcion": "Un curry vegano y nutritivo...",
            "activa": true,
            "items": [],
            "caloriasTotales": 285 
        }
    ]
    ```

---

### **5. Modificar Receta (PUT)**
**Objetivo:** Actualizar descripción, cantidad de items, y eliminar lógicamente un item de receta existente.

* **URL:** `http://localhost:8080/recetas/<ID_RECETA>` (reemplaza `<ID_RECETA>` con el ID de la receta que quieres modificar, ej. `1` para "Pan Casero Simple")
* **Método:** `PUT`
* **Headers:** `Content-Type: application/json`
* **Body (raw - JSON):**
    * **¡Importante!** Usa los IDs de los `items` (ItemReceta) que anotaste al crear la receta, o los que obtengas de un GET.
    * Las calorías ya no se envían en el item, se calculan del Ingrediente.

    ```json
    {
        "id": 1, 
        "nombre": "Pan Casero Simple", 
        "descripcion": "Versión mejorada: Receta de pan fácil y rápida, ideal para principiantes. Requiere poco amasado y un toque de manteca para mayor suavidad.",
        "activa": true,
        "items": [ 
            {
                "id": <ID_ITEM_RECETA_HARINA>, 
                "ingrediente": {"id": 1},
                "cantidad": 0.600, 
                "activo": true
            },
            {
                "id": <ID_ITEM_RECETA_SAL>, 
                "ingrediente": {"id": 6},
                "cantidad": 0.015,
                "activo": true
            },
            {
                "id": <ID_ITEM_RECETA_LECHE>, 
                "ingrediente": {"id": 4},
                "cantidad": 0.200,
                "activo": true
            },
            {
                "ingrediente": {"id": 5}, 
                "cantidad": 0.050
            }
        ]
    }
    ```
* **Respuesta Esperada (200 OK):**
    ```json
    {
        "id": 1,
        "nombre": "Pan Casero Simple",
        "descripcion": "Versión mejorada: Receta de pan fácil y rápida, ideal para principiantes. Requiere poco amasado y un toque de manteca para mayor suavidad.",
        "activa": true,
        "items": [
            { "id": <ID_ITEM_RECETA_HARINA>, "ingrediente": { "id": 1, "nombre": "Harina 000", "calorias": 364, "activo": true }, "cantidad": 0.6, "activo": true },
            { "id": <ID_ITEM_RECETA_SAL>, "ingrediente": { "id": 6, "nombre": "Sal", "calorias": 0, "activo": true }, "cantidad": 0.015, "activo": true },
            { "id": <ID_ITEM_RECETA_LECHE>, "ingrediente": { "id": 4, "nombre": "Leche", "calorias": 61, "activo": true }, "cantidad": 0.2, "activo": true },
            { "id": <NUEVO_ID_ITEM_RECETA_MANTECA>, "ingrediente": { "id": 5, "nombre": "Manteca", "calorias": 717, "activo": true }, "cantidad": 0.05, "activo": true },
            { "id": <ID_ITEM_RECETA_LEVADURA>, "ingrediente": { "id": 7, "nombre": "Levadura", "calorias": 89, "activo": true }, "cantidad": 0.005, "activo": false } 
        ],
        "caloriasTotales": 2617 
    }
    ```
    * **Verificación:** Consulta la receta por GET de nuevo y en tu base de datos para confirmar los estados `activo` de los `items`.

---

### **6. Eliminar Receta (DELETE - Lógica)**

* **URL:** `http://localhost:8080/recetas/<ID_RECETA_A_ELIMINAR>`
* **Método:** `DELETE`
* **Respuesta Esperada (204 No Content):** `(No content)`
* **Verificación:**
    1.  Realiza un `GET` a `http://localhost:8080/recetas`. La receta eliminada lógicamente no debería aparecer.
    2.  Confirma en tu base de datos que el campo `activa` de la tabla `receta` para el `ID_RECETA_A_ELIMINAR` cambió a `false`.