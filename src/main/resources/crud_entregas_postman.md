CRUD ENTREGA DE ALIMENTOS

INICIO - BASE DE DATOS VACÍA, FAMILIA Y RECETA/PREPARACIÓN YA REGISTRADAS.

POST:

http://localhost:8080/entregas

BODY (x-www-form-urlencoded):

familiaId=1
recetaId=1
raciones=2

RESPUESTA:

{
  "id": 1,
  "familia": {
    "id": 1,
    "nroFamilia": "FAM-0D9573B2",
    "nombreFamilia": "Familia Gutierrez y Abuela"
  },
  "receta": {
    "id": 1,
    "nombre": "Guiso de lentejas"
  },
  "cantidadRaciones": 2,
  "fechaEntrega": "2025-06-15",
  "activo": true
}

FUNCIONANDO CORRECTAMENTE.

SEGUNDO INTENTO: Escenario con error por entrega repetida el mismo día

POST:

familiaId=1
recetaId=1
raciones=1

RESPUESTA:

"Ya se realizó una entrega hoy para esta familia."

TERCER ESCENARIO: Raciones mayores a los integrantes activos

POST:

familiaId=1
recetaId=1
raciones=5

RESPUESTA:

"No se pueden entregar más raciones que integrantes activos."

CUARTO ESCENARIO: No hay stock suficiente

(Suponiendo que stockRacionesRestantes = 0)

POST:

familiaId=1
recetaId=1
raciones=1

RESPUESTA:

"No hay suficientes raciones disponibles para esta receta."

GET:

http://localhost:8080/entregas?fecha=2025-06-15

RESPUESTA:

[
  {
    "id": 1,
    "familia": {
      "nroFamilia": "FAM-0D9573B2",
      "nombreFamilia": "Familia Gutierrez y Abuela"
    },
    "receta": {
      "nombre": "Guiso de lentejas"
    },
    "cantidadRaciones": 2,
    "fechaEntrega": "2025-06-15"
  }
]

DELETE:

http://localhost:8080/entregas/1

RESPUESTA:

204 No Content

REVISAR EN LA BASE: LA ENTREGA CAMBIA SU CAMPO 'ACTIVO' A FALSE Y LAS RACIONES VUELVEN AL STOCK DISPONIBLE EN LA RECETA/PREPARACIÓN.

