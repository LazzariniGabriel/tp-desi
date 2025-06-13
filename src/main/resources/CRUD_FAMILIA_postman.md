CRUD FAMILIA


INICIO - BASES DE DATOS CON TABLAS DE FAMILIA E INTEGRANTES VACIA. 

--------------------------------------------------------------------------------------------------------------------------------------------------------------------------

POST:


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







RESPUESTA:

{
    "id": 1,
    "nroFamilia": "FAM-0D9573B2",
    "nombreFamilia": "Familia Gutierrez y Abuela",
    "fechaAlta": "2025-05-29",
    "fechaUltimaAsistenciaRecibida": null,
    "activa": true,
    "integrantes": [
        {
            "id": 1,
            "dni": 123456789,
            "apellido": "Gutierrez",
            "nombre": "Roberto",
            "fechaNacimiento": "1970-01-01",
            "ocupacion": "EMPLEADO",
            "activo": true
        },
        {
            "id": 2,
            "dni": 987654321,
            "apellido": "Gutierrez",
            "nombre": "Alicia",
            "fechaNacimiento": "1972-03-15",
            "ocupacion": "AMA_DE_CASA",
            "activo": true
        },
        {
            "id": 3,
            "dni": 111222333,
            "apellido": "Gutierrez",
            "nombre": "Carmen",
            "fechaNacimiento": "1950-07-20",
            "ocupacion": "OTRO",
            "activo": true
        }
    ],
    "cantidadIntegrantesActivos": 3
}


FUNCIONANDO CORRECTAMENTE.



SEGUNDO INTENTO: Escenario con Errores: DNI Duplicado en el Sistema

POST: 

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

RESPUESTA:

Ya existe un integrante con DNI: 123456789


TERCER ESCENARIO.  Escenario con Errores: Datos Faltantes o Inválidos

POST: 

{
    "nombreFamilia": "",
    "integrantes": [
        {
            "dni": -10,
            "apellido": "Apellido",
            "nombre": "",
            "fechaNacimiento": "2030-01-01",
            "ocupacion": null
        }
    ]
}

RESPUESTA: 



{
    "integrantes[0].ocupacion": "La Ocupación es requerida.",
    "nombreFamilia": "El Nombre de Familia no puede estar vacío.",
    "integrantes[0].nombre": "El Nombre no puede estar vacío.",
    "integrantes[0].dni": "El DNI debe ser un valor numérico positivo.",
    "integrantes[0].fechaNacimiento": "La Fecha de nacimiento no puede ser futura."
}


-------------------------------------------------------------------------------------------------------------------------------------------------------

GET: http://localhost:8080/familias



RESPUESTA:

[
    {
        "id": 1,
        "nroFamilia": "FAM-0D9573B2",
        "nombreFamilia": "Familia Gutierrez y Abuela",
        "fechaAlta": "2025-05-29",
        "fechaUltimaAsistenciaRecibida": null,
        "activa": true,
        "integrantes": [
            {
                "id": 1,
                "dni": 123456789,
                "apellido": "Gutierrez",
                "nombre": "Roberto",
                "fechaNacimiento": "1970-01-01",
                "ocupacion": "EMPLEADO",
                "activo": true
            },
            {
                "id": 2,
                "dni": 987654321,
                "apellido": "Gutierrez",
                "nombre": "Alicia",
                "fechaNacimiento": "1972-03-15",
                "ocupacion": "AMA_DE_CASA",
                "activo": true
            },
            {
                "id": 3,
                "dni": 111222333,
                "apellido": "Gutierrez",
                "nombre": "Carmen",
                "fechaNacimiento": "1950-07-20",
                "ocupacion": "OTRO",
                "activo": true
            }
        ],
        "cantidadIntegrantesActivos": 3
    },
    {
        "id": 2,
        "nroFamilia": "FAM-0BBE530B",
        "nombreFamilia": "Familia de Prueba Error DNI",
        "fechaAlta": "2025-05-29",
        "fechaUltimaAsistenciaRecibida": null,
        "activa": true,
        "integrantes": [
            {
                "id": 4,
                "dni": 12345678,
                "apellido": "Nuevo",
                "nombre": "Integrante",
                "fechaNacimiento": "1995-01-01",
                "ocupacion": "EMPLEADO",
                "activo": true
            }
        ],
        "cantidadIntegrantesActivos": 1
    }
]





-------------------------------------------------------------------------------------------------------------------------

DELETE:

http://localhost:8080/familias/1

NO DA RESPUESTA POR POSTMAN.

REVISAR LA BASE EN LA TABLA FAMILIA Y VER LA BAJA LOGICA EN LA COLUMNA 'ACTIVA' PASA DE '1' A '0' Y CUANDO SE HACE GET DE LISTA DE FAMILIAS YA NO SE OBTIENE ESTA MISMA, SI NO EL RESTO.

--------------------------------------------------------------------------------------------------------------------------------------------

UPDATE: 

PUT: 

{
    "id": 2,
    "nroFamilia": "FAM-ABC123",  
    "nombreFamilia": "Los Gomez Actualizados",
    "fechaAlta": "2024-02-20", 
    "fechaUltimaAsistenciaRecibida": "2025-05-29",
    "activa": true, 
    "integrantes": [
        {
            "id": 5,
            "dni": 98765432,
            "apellido": "Gomez",
            "nombre": "Pedro Juan", 
            "fechaNacimiento": "1968-11-05",
            "ocupacion": "EMPLEADO", 
            "activo": true
        },
        {
            "id": 6,
            "dni": 45678901,
            "apellido": "Gomez",
            "nombre": "Rosa Maria",
            "fechaNacimiento": "1970-01-25",
            "ocupacion": "AMA_DE_CASA",
            "activo": false 
        },
        {
            "dni": 77788899,
            "apellido": "Gomez",
            "nombre": "Nuevo Integrante",
            "fechaNacimiento": "2000-01-01",
            "ocupacion": "ESTUDIANTE"
        }
    ]
}

(PROBAR ENVIARLO DOS VECES SI A LA PRIMERA NO DETECTA LOS NUEVOS DOS INTEGRANTES)


RESPUESTA: 


{
    "id": 2,
    "nroFamilia": "FAM-0BBE530B",
    "nombreFamilia": "Los Gomez Actualizados",
    "fechaAlta": "2025-05-29",
    "fechaUltimaAsistenciaRecibida": "2025-05-29",
    "activa": true,
    "integrantes": [
        {
            "id": 4,
            "dni": 12345678,
            "apellido": "Nuevo",
            "nombre": "Integrante",
            "fechaNacimiento": "1995-01-01",
            "ocupacion": "EMPLEADO",
            "activo": false
        },
        {
            "id": 5,
            "dni": 98765432,
            "apellido": "Gomez",
            "nombre": "Pedro Juan",
            "fechaNacimiento": "1968-11-05",
            "ocupacion": "EMPLEADO",
            "activo": true
        },
        {
            "id": 6,
            "dni": 77788899,
            "apellido": "Gomez",
            "nombre": "Nuevo Integrante",
            "fechaNacimiento": "2000-01-01",
            "ocupacion": "ESTUDIANTE",
            "activo": true
        }
    ],
    "cantidadIntegrantesActivos": 2
}


