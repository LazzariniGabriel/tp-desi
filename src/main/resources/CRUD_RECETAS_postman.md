CRUD RECETAS


INICIO : EJECUTAR LA PRE CARGA DE DATOS, ARCHIVO "precarga-receta.sql" .


------------------------------------------------------------------------------
ESCENARIO 1 : CREACION

POST: http://localhost:8080/recetas
{
    "nombre": "Curry de Garbanzos y Espinacas",
    "descripcionPreparacion": "Un curry vegano y nutritivo, perfecto para una comida rápida entre semana. Se cocina a fuego lento con especias aromáticas.",
    "ingredientes": [
        {
            "ingrediente": {"id": 9},   // ID del ingrediente "Cebolla" de tu catálogo (data.sql)
            "cantidad": 0.200,          // 200 gramos
            "calorias": 80              // 80 calorías
        },
        {
            "ingrediente": {"id": 10},  // ID del ingrediente "Ajo" de tu catálogo (data.sql)
            "cantidad": 0.010,          // 10 gramos
            "calorias": 15              // 15 calorías
        },
        {
            "ingrediente": {"id": 8},   // ID del ingrediente "Tomate" de tu catálogo (data.sql)
            "cantidad": 0.400,          // 400 gramos
            "calorias": 90              // 90 calorías
        },
        {
            "ingrediente": {"id": 1},   // ID del ingrediente "Harina 000" (usémoslo para espesar el curry, aunque no sea típico)
            "cantidad": 0.030,          // 30 gramos
            "calorias": 100             // 100 calorías
        }
        // Nota: Los ingredientes que no tienen un ID asignado (solo {"id": X} para el catálogo)
        // son los "nuevos" ingredientes para esta receta en particular.
        // El sistema les asignará un ID de IngredienteReceta automáticamente.
    ]
}




RESPUESTA : 
{
    "id": 5,
    "nombre": "Curry de Garbanzos y Espinacas",
    "descripcionPreparacion": "Un curry vegano y nutritivo, perfecto para una comida rápida entre semana. Se cocina a fuego lento con especias aromáticas.",
    "activa": true,
    "ingredientes": [
        {
            "id": 13,
            "ingrediente": {
                "id": 9,
                "nombre": "Cebolla",
                "activo": true
            },
            "cantidad": 0.2,
            "calorias": 80,
            "activo": true
        },
        {
            "id": 14,
            "ingrediente": {
                "id": 10,
                "nombre": "Ajo",
                "activo": true
            },
            "cantidad": 0.01,
            "calorias": 15,
            "activo": true
        },
        {
            "id": 15,
            "ingrediente": {
                "id": 8,
                "nombre": "Tomate",
                "activo": true
            },
            "cantidad": 0.4,
            "calorias": 90,
            "activo": true
        },
        {
            "id": 16,
            "ingrediente": {
                "id": 1,
                "nombre": "Harina 000",
                "activo": true
            },
            "cantidad": 0.03,
            "calorias": 100,
            "activo": true
        }
    ],
    "caloriasTotales": 285
}


------------------------------------------------------------------------------

ESCENARIO 2 : 


POST : http://localhost:8080/recetas

{
    "nombre": "Ensalada Completa",
    "descripcionPreparacion": "Mezclar vegetales frescos, proteína y aderezo ligero. Ideal para una comida rápida y nutritiva.",
    "integrantes": [
        {
            "ingrediente": {"id": 8},   // Tomate
            "cantidad": 0.300,
            "calorias": 50
        },
        {
            "ingrediente": {"id": 9},   // Cebolla
            "cantidad": 0.100,
            "calorias": 30
        },
        {
            "ingrediente": {"id": 4},   // Leche (ejemplo: para un aderezo cremoso, aunque sea ensalada)
            "cantidad": 0.050,
            "calorias": 25
        }
    ]
}

RESPUESTA: 

Ya existe una receta con el nombre: Ensalada Completa.

--------------------------------------------------------------------------------------------------------------------------------------

ESCENARIO 3: 


POST: http://localhost:8080/recetas

{
    "nombre": "", // Vacío
    "descripcionPreparacion": null, // Nulo
    "integrantes": [
        {
            "ingrediente": {"id": 999}, // Ingrediente de catálogo inexistente
            "cantidad": -5.0, // Negativo
            "calorias": 0 // Cero o negativo
        },
        {
            "ingrediente": null, // Ingrediente nulo
            "cantidad": 10.0,
            "calorias": 100
        }
    ]
}

RESPUESTA: 

{
    "descripcionPreparacion": "La Descripción de la preparación es requerida.",
    "nombre": "El Nombre de la receta es requerido."
}


--------------------------------------------------------------------------------------

GET: http://localhost:8080/recetas


RESPUESTA:
[
    {
        "id": 1,
        "nombre": "Pan Casero Simple",
        "descripcionPreparacion": "Mezclar ingredientes, amasar y hornear.",
        "activa": true,
        "ingredientes": [
            {
                "id": 1,
                "ingrediente": {
                    "id": 1,
                    "nombre": "Harina 000",
                    "activo": true
                },
                "cantidad": 0.5,
                "calorias": 1800,
                "activo": true
            },
            {
                "id": 2,
                "ingrediente": {
                    "id": 6,
                    "nombre": "Sal",
                    "activo": true
                },
                "cantidad": 0.01,
                "calorias": 5,
                "activo": true
            },
            {
                "id": 3,
                "ingrediente": {
                    "id": 7,
                    "nombre": "Levadura",
                    "activo": true
                },
                "cantidad": 0.005,
                "calorias": 10,
                "activo": true
            },
            {
                "id": 4,
                "ingrediente": {
                    "id": 4,
                    "nombre": "Leche",
                    "activo": true
                },
                "cantidad": 0.2,
                "calorias": 100,
                "activo": true
            }
        ],
        "caloriasTotales": 1915
    },
    {
        "id": 2,
        "nombre": "Salsa Bolognesa",
        "descripcionPreparacion": "Sofreír carne, cebolla y ajo, añadir tomate y cocinar a fuego lento.",
        "activa": true,
        "ingredientes": [
            {
                "id": 5,
                "ingrediente": {
                    "id": 11,
                    "nombre": "Carne Picada",
                    "activo": true
                },
                "cantidad": 0.3,
                "calorias": 750,
                "activo": true
            },
            {
                "id": 6,
                "ingrediente": {
                    "id": 9,
                    "nombre": "Cebolla",
                    "activo": true
                },
                "cantidad": 0.15,
                "calorias": 60,
                "activo": true
            },
            {
                "id": 7,
                "ingrediente": {
                    "id": 10,
                    "nombre": "Ajo",
                    "activo": true
                },
                "cantidad": 0.02,
                "calorias": 30,
                "activo": true
            },
            {
                "id": 8,
                "ingrediente": {
                    "id": 8,
                    "nombre": "Tomate",
                    "activo": true
                },
                "cantidad": 0.8,
                "calorias": 200,
                "activo": true
            }
        ],
        "caloriasTotales": 1040
    },
    {
        "id": 4,
        "nombre": "Ensalada Completa",
        "descripcionPreparacion": "Mezclar vegetales frescos, proteína y aderezo ligero. Ideal para una comida rápida y nutritiva.",
        "activa": true,
        "ingredientes": [],
        "caloriasTotales": 0
    }
]


---------------------------------------------------------------------------------------------------------------------------------------

PUT: http://localhost:8080/recetas/1

{
    "id": 1,
    "nombre": "Pan Casero Simple", // El nombre se envía, pero el backend lo ignora (es de solo lectura)
    "descripcionPreparacion": "Versión mejorada: Receta de pan fácil y rápida, ideal para principiantes. Requiere poco amasado y un toque de manteca para mayor suavidad.",
    "activa": true, // La receta sigue activa
    "ingredientes": [
        {
            "id": 1, // ID del ingrediente de receta existente (ej. 1 de tu data.sql)
            "ingrediente": {"id": 1},   // Harina 000 (ID del catálogo de ingredientes)
            "cantidad": 0.600,          // **Cantidad modificada** (de 0.500 a 0.600)
            "calorias": 2160,           // **Calorías modificadas** (de 1800 a 2160)
            "activo": true
        },
        {
            "id": 2, // ID del ingrediente de receta existente (ej. 2 de tu data.sql)
            "ingrediente": {"id": 6},   // Sal (ID del catálogo de ingredientes)
            "cantidad": 0.015,          // **Cantidad modificada**
            "calorias": 7,              // **Calorías modificadas**
            "activo": true
        },
        // **Ingrediente "Levadura" (originalmente con ID_IngredienteReceta_Levadura) NO se incluye aquí.**
        // Al no estar en la lista que enviamos, el servicio lo marcará como activo: false (eliminación lógica).
        {
            "id": 3, // ID del ingrediente de receta existente (ej. 4 de tu data.sql)
            "ingrediente": {"id": 4},   // Leche (ID del catálogo de ingredientes)
            "cantidad": 0.200,
            "calorias": 100,
            "activo": true
        },
        {
            "ingrediente": {"id": 5},   // **Nuevo Ingrediente "Manteca"** (ID del catálogo de ingredientes)
            "cantidad": 0.050,
            "calorias": 350
        }
    ]
}


RESPUESTA: 

{
    "id": 1,
    "nombre": "Pan Casero Simple",
    "descripcionPreparacion": "Versión mejorada: Receta de pan fácil y rápida, ideal para principiantes. Requiere poco amasado y un toque de manteca para mayor suavidad.",
    "activa": true,
    "ingredientes": [
        {
            "id": 1,
            "ingrediente": {
                "id": 1,
                "nombre": "Harina 000",
                "activo": true
            },
            "cantidad": 0.6,
            "calorias": 2160,
            "activo": true
        },
        {
            "id": 2,
            "ingrediente": {
                "id": 6,
                "nombre": "Sal",
                "activo": true
            },
            "cantidad": 0.015,
            "calorias": 7,
            "activo": true
        },
        {
            "id": 3,
            "ingrediente": {
                "id": 4,
                "nombre": "Leche",
                "activo": true
            },
            "cantidad": 0.2,
            "calorias": 100,
            "activo": true
        },
        {
            "id": 4,
            "ingrediente": {
                "id": 4,
                "nombre": "Leche",
                "activo": true
            },
            "cantidad": 0.2,
            "calorias": 100,
            "activo": false
        },
        {
            "id": 12,
            "ingrediente": {
                "id": 5,
                "nombre": "Manteca",
                "activo": true
            },
            "cantidad": 0.05,
            "calorias": 350,
            "activo": true
        }
    ],
    "caloriasTotales": 2617
}







ESCENARIO 2 DE PUT:  (ID de receta que no existe)

{
    "id": 999,
    "nombre": "Receta Inexistente",
    "descripcionPreparacion": "Esta no existe."
}

RESPUESTA:
Receta no encontrada con ID: 999


---------------------------------------------------------------------------------------------------

DELETE: http://localhost:8080/recetas/2

VER COMO EL REGISTRO EN LA TABLA DE LA BASE DE DATOS CAMBIA LA COLUMNA DE ACTIVO DE 1 A 0 POR ENDE CUANDO RECUPERAMOS TODAS LAS RECETAS ESTA YA NO APARECE. 


ESCENARIO 2 DELETE: http://localhost:8080/recetas/999

RESPUESTA:
Receta no encontrada con ID: 999

