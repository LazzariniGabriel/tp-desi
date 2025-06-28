# CRUD FAMILIA

**INICIO: Asegúrate de que las tablas de Persona, Familia y Asistido estén vacías para una prueba limpia, o usa la precarga SQL.**

---

### **1. Alta de una Familia (POST)**
**Objetivo:** Crear una familia con sus integrantes (Asistidos), ahora con los campos de Persona.

* **URL:** `http://localhost:8080/familias`
* **Método:** `POST`
* **Headers:** `Content-Type: application/json`
* **Body (raw - JSON):**
    ```json
    {
        "nombre": "Familia Ramirez y Vecinos",
        "integrantes": [
            {
                "dni": 100000001,
                "domicilio": "Av. Principal 123",
                "apellido": "Ramirez",
                "nombre": "Juan",
                "fechaNacimiento": "1980-05-10",
                "ocupacion": "EMPLEADO"
            },
            {
                "dni": 100000002,
                "domicilio": "Av. Principal 123",
                "apellido": "Ramirez",
                "nombre": "Maria",
                "fechaNacimiento": "1982-11-20",
                "ocupacion": "AMA_DE_CASA"
            },
            {
                "dni": 100000003,
                "domicilio": "Calle Lateral 45",
                "apellido": "Vecino",
                "nombre": "Carlos",
                "fechaNacimiento": "1995-03-01",
                "ocupacion": "ESTUDIANTE"
            }
        ]
    }
    ```
* **Respuesta Esperada (201 Created):**
    ```json
    {
        "id": 1,
        "nroFamilia": "FAM-XXXXXXX",
        "nombre": "Familia Ramirez y Vecinos",
        "fechaRegistro": "2025-06-28", 
        "fechaUltimaAsistenciaRecibida": null,
        "activa": true,
        "integrantes": [
            {
                "id": 1, "dni": 100000001, "domicilio": "Av. Principal 123", "apellido": "Ramirez", "nombre": "Juan", "fechaNacimiento": "1980-05-10", "ocupacion": "EMPLEADO", "fechaRegistro": "2025-06-28", "activo": true
            },
            // ... más integrantes
        ],
        "cantidadIntegrantesActivos": 3
    }
    ```
    * **Anótate el `id` y `nroFamilia` de esta familia creada.**

---

### **2. Escenario con Error: DNI Duplicado en el Sistema (a nivel Persona)**

* **URL:** `http://localhost:8080/familias`
* **Método:** `POST`
* **Headers:** `Content-Type: application/json`
* **Body (raw - JSON):** (Usa un DNI ya existente, ej. `100000001`)
    ```json
    {
        "nombre": "Familia de Prueba Error DNI",
        "integrantes": [
            {
                "dni": 100000001,
                "domicilio": "Calle Error 789",
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
    "Ya existe una persona con DNI: 100000001"
    ```

---

### **3. Escenario con Errores: Datos Faltantes o Inválidos**

* **URL:** `http://localhost:8080/familias`
* **Método:** `POST`
* **Headers:** `Content-Type: application/json`
* **Body (raw - JSON):**
    ```json
    {
        "nombre": "", 
        "integrantes": [
            {
                "dni": -10, 
                "domicilio": "", 
                "apellido": "Apellido",
                "nombre": "",
                "fechaNacimiento": "2030-01-01", 
                "ocupacion": null 
            }
        ]
    }
    ```
* **Respuesta Esperada (400 Bad Request):**
    ```json
    {
        "integrantes[0].ocupacion": "La Ocupación es requerida.",
        "integrantes[0].domicilio": "El domicilio no puede estar vacío.",
        "integrantes[0].nombre": "El Nombre no puede estar vacío.",
        "integrantes[0].dni": "El DNI debe ser un valor numérico positivo.",
        "integrantes[0].fechaNacimiento": "La Fecha de nacimiento no puede ser futura.",
        "nombre": "El Nombre de Familia no puede estar vacío."
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
            "nroFamilia": "FAM-INICIAL1", 
            "nombre": "Familia Prueba Asistencia",
            "fechaRegistro": "2025-06-28",
            "fechaUltimaAsistenciaRecibida": null,
            "activa": true,
            "integrantes": [
                {
                    "id": 1, "dni": 12345678, "domicilio": "Calle Falsa 123", "apellido": "Perez", "nombre": "Carlos", "fechaNacimiento": "1985-01-10", "ocupacion": "EMPLEADO", "fechaRegistro": "2025-06-28", "activo": true
                },
                {
                    "id": 2, "dni": 87654321, "domicilio": "Avenida Siempreviva 742", "apellido": "Perez", "nombre": "Ana", "fechaNacimiento": "1987-03-20", "ocupacion": "AMA_DE_CASA", "fechaRegistro": "2025-06-28", "activo": true
                }
            ],
            "cantidadIntegrantesActivos": 2
        },
        {
            "id": 2, 
            "nroFamilia": "FAM-XXXXXXX",
            "nombre": "Familia Ramirez y Vecinos",
            "fechaRegistro": "2025-06-28",
            "fechaUltimaAsistenciaRecibida": null,
            "activa": true,
            "integrantes": [
                {
                    "id": 3, "dni": 100000001, "domicilio": "Av. Principal 123", "apellido": "Ramirez", "nombre": "Juan", "fechaNacimiento": "1980-05-10", "ocupacion": "EMPLEADO", "fechaRegistro": "2025-06-28", "activo": true
                },
                {
                    "id": 4, "dni": 100000002, "domicilio": "Av. Principal 123", "apellido": "Ramirez", "nombre": "Maria", "fechaNacimiento": "1982-11-20", "ocupacion": "AMA_DE_CASA", "fechaRegistro": "2025-06-28", "activo": true
                },
                {
                    "id": 5, "dni": 100000003, "domicilio": "Calle Lateral 45", "apellido": "Vecino", "nombre": "Carlos", "fechaNacimiento": "1995-03-01", "ocupacion": "ESTUDIANTE", "fechaRegistro": "2025-06-28", "activo": true
                }
            ],
            "cantidadIntegrantesActivos": 3
        }
    ]
    ```

---

### **5. Modificar una Familia (PUT)**
**Objetivo:** Actualizar nombre, fecha de última asistencia, agregar nuevos integrantes, modificar existentes y eliminar lógicamente otros.

* **URL:** `http://localhost:8080/familias/<ID_FAMILIA>` (reemplaza `<ID_FAMILIA>` con el ID de la familia que creaste, ej. `2`)
* **Método:** `PUT`
* **Headers:** `Content-Type: application/json`
* **Body (raw - JSON):** Modifica la familia que creaste. Elimina un integrante existente no incluyéndolo, modifica otro, y agrega uno nuevo. 
    ```json
    {
        "nombre": "Los Ramirez Actualizados", 
        "fechaUltimaAsistenciaRecibida": "2025-06-28",
        "integrantes": [
            {
                "id": <ID_ASISTIDO_JUAN>, 
                "dni": 100000001,
                "domicilio": "Av. Principal 123",
                "apellido": "Ramirez",
                "nombre": "Juan Carlos", 
                "fechaNacimiento": "1980-05-10",
                "ocupacion": "DESEMPLEADO" 
            },
            {
                "dni": 100000004, 
                "domicilio": "Calle Nueva 77",
                "apellido": "Nuevo",
                "nombre": "Pilar",
                "fechaNacimiento": "2010-01-01",
                "ocupacion": "ESTUDIANTE"
            }
        ]
    }
    ```
* **Respuesta Esperada (200 OK):**
    ```json
    {
        "id": <ID_FAMILIA>,
        "nroFamilia": "FAM-XXXXXXX",
        "nombre": "Los Ramirez Actualizados",
        "fechaRegistro": "2025-06-28",
        "fechaUltimaAsistenciaRecibida": "2025-06-28",
        "activa": true,
        "integrantes": [
            {
                "id": <ID_ASISTIDO_JUAN>, "dni": 100000001, "domicilio": "Av. Principal 123", "apellido": "Ramirez", "nombre": "Juan Carlos", "fechaNacimiento": "1980-05-10", "ocupacion": "DESEMPLEADO", "fechaRegistro": "2025-06-28", "activo": true
            },
            {
                "id": <ID_ASISTIDO_MARIA>, "dni": 100000002, "domicilio": "Av. Principal 123", "apellido": "Ramirez", "nombre": "Maria", "fechaNacimiento": "1982-11-20", "ocupacion": "AMA_DE_CASA", "fechaRegistro": "2025-06-28", "activo": false 
            },
            {
                "id": <ID_ASISTIDO_CARLOS>, "dni": 100000003, "domicilio": "Calle Lateral 45", "apellido": "Vecino", "nombre": "Carlos", "fechaNacimiento": "1995-03-01", "ocupacion": "ESTUDIANTE", "fechaRegistro": "2025-06-28", "activo": false 
            },
            {
                "id": <ID_NUEVO_ASISTIDO>, "dni": 100000004, "domicilio": "Calle Nueva 77", "apellido": "Nuevo", "nombre": "Pilar", "fechaNacimiento": "2010-01-01", "ocupacion": "ESTUDIANTE", "fechaRegistro": "2025-06-28", "activo": true
            }
        ],
        "cantidadIntegrantesActivos": 2 
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