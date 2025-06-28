# CRUD RECETAS

**INICIO: Asegúrate de que tu base de datos tenga los ingredientes base cargados (usa `precarga-receta.sql`).**

---

### **1. Creación de Receta (POST)**

* **URL:** `http://localhost:8080/recetas/crear`
* **Método:** `POST`
* **Headers:** `Content-Type: application/json`
* **Body (raw - JSON):** (Asegúrate de que los IDs de ingredientes existan en tu precarga. Por ejemplo, `id: 9` para "Cebolla", etc.)
    ```json
    {
        "nombre": "Curry de Garbanzos y Espinacas",
        "descripcionPreparacion": "Un curry vegano y nutritivo, perfecto para una comida rápida entre semana. Se cocina a fuego lento con especias aromáticas.",
        "ingredientes": [
            { "ingrediente": {"id": 9}, "cantidad": 0.200, "calorias": 80 },
            { "ingrediente": {"id": 10}, "cantidad": 0.010, "calorias": 15 },
            { "ingrediente": {"id": 8}, "cantidad": 0.400, "calorias": 90 },
            { "ingrediente": {"id": 1}, "cantidad": 0.030, "calorias": 100 }
        ]
    }
    ```
* **Respuesta Esperada (201 Created):**
    ```json
    {
        "id": 5, // ID autogenerado
        "nombre": "Curry de Garbanzos y Espinacas",
        "descripcionPreparacion": "Un curry vegano y nutritivo, perfecto para una comida rápida entre semana. Se cocina a fuego lento con especias aromáticas.",
        "activa": true,
        "ingredientes": [
            { "id": 13, "ingrediente": { "id": 9, "nombre": "Cebolla", "activo": true }, "cantidad": 0.2, "calorias": 80, "activo": true },
            { "id": 14, "ingrediente": { "id": 10, "nombre": "Ajo", "activo": true }, "cantidad": 0.01, "calorias": 15, "activo": true },
            { "id": 15, "ingrediente": { "id": 8, "nombre": "Tomate", "activo": true }, "cantidad": 0.4, "calorias": 90, "activo": true },
            { "id": 16, "ingrediente": { "id": 1, "nombre": "Harina 000", "activo": true }, "cantidad": 0.03, "calorias": 100, "activo": true }
        ],
        "caloriasTotales": 285 // Suma de calorías de los ingredientes
    }
    ```
    * **Anótate el `id` de esta receta y los `id` de sus `ingredientes` (IngredienteReceta).**

---

### **2. Escenario con Error: Nombre de Receta Duplicado**

* **URL:** `http://localhost:8080/recetas/crear`
* **Método:** `POST`
* **Headers:** `Content-Type: application/json`
* **Body (raw - JSON):** (Usa un nombre ya existente, ej. "Curry de Garbanzos y Espinacas")
    ```json
    {
        "nombre": "Curry de Garbanzos y Espinacas",
        "descripcionPreparacion": "Otro intento de crear el mismo curry.",
        "ingredientes": []
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
        "nombre": "", // Vacío
        "descripcionPreparacion": null, // Nulo
        "ingredientes": [
            { "ingrediente": {"id": 999}, "cantidad": -5.0, "calorias": 0 }, // Ingrediente inexistente, cantidad negativa, calorías no positivas
            { "ingrediente": null, "cantidad": 10.0, "calorias": 100 } // Ingrediente nulo
        ]
    }
    ```
* **Respuesta Esperada (400 Bad Request):**
    ```json
    {
        "descripcionPreparacion": "La Descripción de la preparación es requerida.",
        "nombre": "El Nombre de la receta es requerido."
    }
    ```
    (Nota: Las validaciones de los ingredientes de receta se capturan en el servicio y pueden dar otro error si no se validan directamente con `@Valid` en el `ingredientes` de la receta)

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
            "descripcionPreparacion": "Mezclar ingredientes, amasar y hornear.",
            "activa": true,
            "ingredientes": [ /* ... */ ],
            "caloriasTotales": 1915
        },
        {
            "id": 2,
            "nombre": "Salsa Bolognesa",
            "descripcionPreparacion": "Sofreír carne, cebolla y ajo, añadir tomate y cocinar a fuego lento.",
            "activa": true,
            "ingredientes": [ /* ... */ ],
            "caloriasTotales": 1040
        },
        {
            "id": 5,
            "nombre": "Curry de Garbanzos y Espinacas",
            "descripcionPreparacion": "Un curry vegano y nutritivo...",
            "activa": true,
            "ingredientes": [ /* ... */ ],
            "caloriasTotales": 285
        }
    ]
    ```

---

### **5. Modificar Receta (PUT)**
**Objetivo:** Actualizar descripción, cantidad y calorías de ingredientes, y eliminar lógicamente un ingrediente de receta existente.

* **URL:** `http://localhost:8080/recetas/<ID_RECETA>` (reemplaza `<ID_RECETA>` con el ID de la receta que quieres modificar, ej. `1` para "Pan Casero Simple")
* **Método:** `PUT`
* **Headers:** `Content-Type: application/json`
* **Body (raw - JSON):**
    * **¡Importante!** Usa los IDs de los `ingredientes` (IngredienteReceta) que anotaste al crear la receta, o los que obtengas de un GET.

    ```json
    {
        "id": 1, // ID de la receta (se ignora el nombre)
        "nombre": "Pan Casero Simple", // El nombre se envía, pero el backend lo ignora (es de solo lectura)
        "descripcionPreparacion": "Versión mejorada: Receta de pan fácil y rápida, ideal para principiantes. Requiere poco amasado y un toque de manteca para mayor suavidad.",
        "activa": true,
        "ingredientes": [
            {
                "id": <ID_INGREDIENTE_RECETA_HARINA>, // ID de IngredienteReceta de Harina
                "ingrediente": {"id": 1},
                "cantidad": 0.600, // Cantidad modificada
                "calorias": 2160, // Calorías modificadas
                "activo": true
            },
            {
                "id": <ID_INGREDIENTE_RECETA_SAL>, // ID de IngredienteReceta de Sal
                "ingrediente": {"id": 6},
                "cantidad": 0.015,
                "calorias": 7,
                "activo": true
            },
            // Ingrediente "Levadura" (si existía) NO se incluye para eliminarlo lógicamente.
            {
                "id": <ID_INGREDIENTE_RECETA_LECHE>, // ID de IngredienteReceta de Leche
                "ingrediente": {"id": 4},
                "cantidad": 0.200,
                "calorias": 100,
                "activo": true
            },
            {
                "ingrediente": {"id": 5}, // Nuevo Ingrediente "Manteca" (sin ID de IngredienteReceta)
                "cantidad": 0.050,
                "calorias": 350
            }
        ]
    }
    ```
* **Respuesta Esperada (200 OK):**
    ```json
    {
        "id": 1,
        "nombre": "Pan Casero Simple",
        "descripcionPreparacion": "Versión mejorada: Receta de pan fácil y rápida, ideal para principiantes. Requiere poco amasado y un toque de manteca para mayor suavidad.",
        "activa": true,
        "ingredientes": [
            // Ingredientes actualizados y nuevos
            { /* ... Harina (cantidad y calorías actualizadas) ... */ },
            { /* ... Sal (cantidad y calorías actualizadas) ... */ },
            { /* ... Leche ... */ },
            { /* ... Manteca (nuevo, con ID de IngredienteReceta autogenerado) ... */ },
            { /* ... Levadura (si existía, ahora con activo: false) ... */ } // Fíjate en este
        ],
        "caloriasTotales": 2617 // Nueva suma de calorías de los ingredientes *activos*
    }
    ```
    * **Verificación:** Consulta la receta por GET de nuevo y en tu base de datos para confirmar los estados `activo` de los ingredientes de receta.

---

### **6. Eliminar Receta (DELETE - Lógica)**

* **URL:** `http://localhost:8080/recetas/<ID_RECETA_A_ELIMINAR>`
* **Método:** `DELETE`
* **Respuesta Esperada (204 No Content):** `(No content)`
* **Verificación:**
    1.  Realiza un `GET` a `http://localhost:8080/recetas`. La receta eliminada lógicamente no debería aparecer.
    2.  Confirma en tu base de datos que el campo `activa` de la tabla `receta` para el `ID_RECETA_A_ELIMINAR` cambió a `false`.