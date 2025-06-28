# CRUD FAMILIA

**INICIO: Asegúrate de que las tablas de Familia e Integrantes estén vacías para una prueba limpia.**

---

### **1. Alta de una Familia (POST)**
**Objetivo:** Crear una familia con sus integrantes.

* **URL:** `http://localhost:8080/familias`
* **Método:** `POST`
* **Headers:** `Content-Type: application/json`
* **Body (raw - JSON):**
    ```json
    {
        "nombreFamilia": "Familia Gutierrez y Abuela",
        "integrantes": [
            {
                "dni": 123456789,
                "apellido": "Gutierrez",
                "nombre": "Roberto",
                "fechaNacimiento": "1970-01-01",
                "ocupacion": "EMPLEADO"
            },
            {
                "dni": 987654321,
                "apellido": "Gutierrez",
                "nombre": "Alicia",
                "fechaNacimiento": "1972-03-15",
                "ocupacion": "AMA_DE_CASA"
            },
            {
                "dni": 111222333,
                "apellido": "Gutierrez",
                "nombre": "Carmen",
                "fechaNacimiento": "1950-07-20",
                "ocupacion": "OTRO"
            }
        ]
    }
    ```
* **Respuesta Esperada (201 Created):**
    ```json
    {
        "id": 1,
        "nroFamilia": "FAM-XXXXXXX", // Este será autogenerado
        "nombreFamilia": "Familia Gutierrez y Abuela",
        "fechaAlta": "2025-06-28", // Fecha actual
        "fechaUltimaAsistenciaRecibida": null,
        "activa": true,
        "integrantes": [
            {
                "id": 1, "dni": 123456789, "apellido": "Gutierrez", "nombre": "Roberto", "fechaNacimiento": "1970-01-01", "ocupacion": "EMPLEADO", "activo": true
            },
            {
                "id": 2, "dni": 987654321, "apellido": "Gutierrez", "nombre": "Alicia", "fechaNacimiento": "1972-03-15", "ocupacion": "AMA_DE_CASA", "activo": true
            },
            {
                "id": 3, "dni": 111222333, "apellido": "Gutierrez", "nombre": "Carmen", "fechaNacimiento": "1950-07-20", "ocupacion": "OTRO", "activo": true
            }
        ],
        "cantidadIntegrantesActivos": 3
    }
    ```
    * **Anótate el `id` y `nroFamilia` de esta familia creada.**

---

### **2. Escenario con Error: DNI Duplicado en el Sistema**

* **URL:** `http://localhost:8080/familias`
* **Método:** `POST`
* **Headers:** `Content-Type: application/json`
* **Body (raw - JSON):** (Usa un DNI ya existente, ej. `123456789`)
    ```json
    {
        "nombreFamilia": "Familia de Prueba Error DNI",
        "integrantes": [
            {
                "dni": 123456789,
                "apellido": "Nuevo",
                "nombre": "Integrante",
                "fechaNacimiento": "1995-01-01",
                "ocupacion": "EMPLEADO"
            }
        ]
    }
    ```
* **Respuesta Esperada (400 Bad Request):**
    ```
    "Ya existe un integrante con DNI: 123456789"
    ```

---

### **3. Escenario con Errores: Datos Faltantes o Inválidos**

* **URL:** `http://localhost:8080/familias`
* **Método:** `POST`
* **Headers:** `Content-Type: application/json`
* **Body (raw - JSON):**
    ```json
    {
        "nombreFamilia": "", // Vacío
        "integrantes": [
            {
                "dni": -10, // Negativo
                "apellido": "Apellido",
                "nombre": "", // Vacío
                "fechaNacimiento": "2030-01-01", // Futura
                "ocupacion": null // Nulo
            }
        ]
    }
    ```
* **Respuesta Esperada (400 Bad Request):**
    ```json
    {
        "integrantes[0].ocupacion": "La Ocupación es requerida.",
        "nombreFamilia": "El Nombre de Familia no puede estar vacío.",
        "integrantes[0].nombre": "El Nombre no puede estar vacío.",
        "integrantes[0].dni": "El DNI debe ser un valor numérico positivo.",
        "integrantes[0].fechaNacimiento": "La Fecha de nacimiento no puede ser futura."
    }
    ```

---

### **4. Listar Familias Activas (GET)**

* **URL:** `http://localhost:8080/familias`
* **Método:** `GET`
* **Respuesta Esperada (200 OK):** (Debería listar solo la familia creada y activa)
    ```json
    [
        {
            "id": 1,
            "nroFamilia": "FAM-XXXXXXX",
            "nombreFamilia": "Familia Gutierrez y Abuela",
            "fechaAlta": "2025-06-28",
            "fechaUltimaAsistenciaRecibida": null,
            "activa": true,
            "integrantes": [
                {
                    "id": 1, "dni": 123456789, "apellido": "Gutierrez", "nombre": "Roberto", "fechaNacimiento": "1970-01-01", "ocupacion": "EMPLEADO", "activo": true
                },
                // ... más integrantes
            ],
            "cantidadIntegrantesActivos": 3
        }
    ]
    ```

---

### **5. Modificar una Familia (PUT)**
**Objetivo:** Actualizar nombre, fecha de última asistencia, agregar nuevos integrantes, modificar existentes y eliminar lógicamente otros.

* **URL:** `http://localhost:8080/familias/<ID_FAMILIA>` (reemplaza `<ID_FAMILIA>` con el ID real)
* **Método:** `PUT`
* **Headers:** `Content-Type: application/json`
* **Body (raw - JSON):**
    * **¡Importante!** Reemplaza los IDs de los integrantes con los IDs reales de tu base de datos.
    * No incluyas los integrantes que quieres dar de baja lógicamente.
    * Para agregar un nuevo integrante, no le pongas `id`.

    ```json
    {
        "nombreFamilia": "Los Gutierrez Actualizados",
        "fechaUltimaAsistenciaRecibida": "2025-06-28", // Fecha actual
        "integrantes": [
            {
                "id": <ID_INTEGRANTE_ROBERTO>, // ID del integrante Roberto
                "dni": 123456789,
                "apellido": "Gutierrez",
                "nombre": "Roberto Carlos", // Nombre modificado
                "fechaNacimiento": "1970-01-01",
                "ocupacion": "EMPLEADO",
                "activo": true
            },
            {
                "dni": 444555666, // Nuevo integrante sin ID
                "apellido": "Nuevo",
                "nombre": "Fernanda",
                "fechaNacimiento": "2000-01-01",
                "ocupacion": "ESTUDIANTE"
            }
        ]
    }
    ```
* **Respuesta Esperada (200 OK):**
    ```json
    {
        "id": <ID_FAMILIA>,
        "nroFamilia": "FAM-XXXXXXX", // Nro. Familia no se modifica
        "nombreFamilia": "Los Gutierrez Actualizados",
        "fechaAlta": "2025-06-28",
        "fechaUltimaAsistenciaRecibida": "2025-06-28",
        "activa": true,
        "integrantes": [
            {
                "id": <ID_INTEGRANTE_ROBERTO>, "dni": 123456789, "apellido": "Gutierrez", "nombre": "Roberto Carlos", "fechaNacimiento": "1970-01-01", "ocupacion": "EMPLEADO", "activo": true
            },
            {
                "id": <ID_INTEGRANTE_MARIA>, "dni": 987654321, "apellido": "Gutierrez", "nombre": "Alicia", "fechaNacimiento": "1972-03-15", "ocupacion": "AMA_DE_CASA", "activo": false // Este debería estar en false
            },
            {
                "id": <ID_INTEGRANTE_CARMEN>, "dni": 111222333, "apellido": "Gutierrez", "nombre": "Carmen", "fechaNacimiento": "1950-07-20", "ocupacion": "OTRO", "activo": false // Este también
            },
            {
                "id": <ID_NUEVO_INTEGRANTE>, "dni": 444555666, "apellido": "Nuevo", "nombre": "Fernanda", "fechaNacimiento": "2000-01-01", "ocupacion": "ESTUDIANTE", "activo": true
            }
        ],
        "cantidadIntegrantesActivos": 2 // Debería reflejar solo los activos
    }
    ```
    * **Verificación:** Consulta la familia por GET de nuevo y en tu base de datos para confirmar los estados `activo` de los integrantes.

---

### **6. Eliminar una Familia (DELETE - Lógica)**

* **URL:** `http://localhost:8080/familias/<ID_FAMILIA>` (reemplaza `<ID_FAMILIA>`)
* **Método:** `DELETE`
* **Respuesta Esperada (204 No Content):** `(No content)`
* **Verificación:**
    1.  Realiza un `GET` a `http://localhost:8080/familias`. La familia eliminada lógicamente no debería aparecer en el listado.
    2.  Confirma en tu base de datos que el campo `activa` de la tabla `familia` para el `ID_FAMILIA` cambió a `false`.