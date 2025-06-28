-- Limpiar tablas si existen (opcional, para desarrollo).
-- Esto borrará todos tus datos. Úsalo solo si quieres empezar de cero.
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE entrega_asistencia; 
TRUNCATE TABLE preparacion;
TRUNCATE TABLE item_receta; 
TRUNCATE TABLE receta;
TRUNCATE TABLE asistido; 
TRUNCATE TABLE familia;
TRUNCATE TABLE producto; 
TRUNCATE TABLE condimento; 
TRUNCATE TABLE ingrediente; 
TRUNCATE TABLE persona; 
TRUNCATE TABLE voluntario; 
SET FOREIGN_KEY_CHECKS = 1;

-- Cargar Ingredientes base (Ahora en la tabla 'ingrediente', y 'producto'/'condimento' para stock/precio)
-- 'Harina 000' es un Producto
INSERT INTO ingrediente (id, nombre, activo, calorias) VALUES (1, 'Harina 000', TRUE, 364);
INSERT INTO producto (id, stock_disponible, precio_actual) VALUES (1, 50.0, 1.20); 

-- 'Azúcar' es un Producto
INSERT INTO ingrediente (id, nombre, activo, calorias) VALUES (2, 'Azúcar', TRUE, 387);
INSERT INTO producto (id, stock_disponible, precio_actual) VALUES (2, 20.0, 0.90);

-- 'Huevos' es un Producto
INSERT INTO ingrediente (id, nombre, activo, calorias) VALUES (3, 'Huevos', TRUE, 155);
INSERT INTO producto (id, stock_disponible, precio_actual) VALUES (3, 10.0, 0.25);

-- 'Leche' es un Producto
INSERT INTO ingrediente (id, nombre, activo, calorias) VALUES (4, 'Leche', TRUE, 61);
INSERT INTO producto (id, stock_disponible, precio_actual) VALUES (4, 15.0, 1.10);

-- 'Manteca' es un Producto
INSERT INTO ingrediente (id, nombre, activo, calorias) VALUES (5, 'Manteca', TRUE, 717);
INSERT INTO producto (id, stock_disponible, precio_actual) VALUES (5, 5.0, 2.50);

-- 'Sal' es un Condimento (sin stock)
INSERT INTO ingrediente (id, nombre, activo, calorias) VALUES (6, 'Sal', TRUE, 0);
INSERT INTO condimento (id) VALUES (6);

-- 'Levadura' es un Producto
INSERT INTO ingrediente (id, nombre, activo, calorias) VALUES (7, 'Levadura', TRUE, 89);
INSERT INTO producto (id, stock_disponible, precio_actual) VALUES (7, 1.0, 0.50);

-- 'Tomate' es un Producto
INSERT INTO ingrediente (id, nombre, activo, calorias) VALUES (8, 'Tomate', TRUE, 18);
INSERT INTO producto (id, stock_disponible, precio_actual) VALUES (8, 30.0, 0.80);

-- 'Cebolla' es un Producto
INSERT INTO ingrediente (id, nombre, activo, calorias) VALUES (9, 'Cebolla', TRUE, 40);
INSERT INTO producto (id, stock_disponible, precio_actual) VALUES (9, 25.0, 0.70);

-- 'Ajo' es un Condimento (sin stock)
INSERT INTO ingrediente (id, nombre, activo, calorias) VALUES (10, 'Ajo', TRUE, 149);
INSERT INTO condimento (id) VALUES (10);

-- 'Carne Picada' es un Producto
INSERT INTO ingrediente (id, nombre, activo, calorias) VALUES (11, 'Carne Picada', TRUE, 250);
INSERT INTO producto (id, stock_disponible, precio_actual) VALUES (11, 40.0, 5.00);


-- Cargar Recetas de ejemplo
INSERT INTO receta (id, nombre, descripcion, activa) VALUES 
(1, 'Pan Casero Simple', 'Mezclar ingredientes, amasar y hornear.', TRUE),
(2, 'Salsa Bolognesa', 'Sofreír carne, cebolla y ajo, añadir tomate y cocinar a fuego lento.', TRUE),
(3, 'Tarta de Manzana (Baja)', 'Receta de tarta clásica, desactivada para pruebas.', FALSE);

-- Cargar Items de Recetas (ItemReceta) para 'Pan Casero Simple' (id=1)
INSERT INTO item_receta (receta_id, ingrediente_id, cantidad, activo) VALUES 
(1, 1, 0.500, TRUE), 
(1, 6, 0.010, TRUE), 
(1, 7, 0.005, TRUE), 
(1, 4, 0.200, TRUE);

-- Cargar Items de Recetas (ItemReceta) para 'Salsa Bolognesa' (id=2)
INSERT INTO item_receta (receta_id, ingrediente_id, cantidad, activo) VALUES
(2, 11, 0.300, TRUE),
(2, 9, 0.150, TRUE), 
(2, 10, 0.020, TRUE),
(2, 8, 0.800, TRUE); 

-- Cargar Items de Recetas (ItemReceta) para 'Tarta de Manzana (Baja)' (id=3)
INSERT INTO item_receta (receta_id, ingrediente_id, cantidad, activo) VALUES
(3, 1, 0.300, TRUE), 
(3, 2, 0.200, TRUE),
(3, 5, 0.100, FALSE); 


-- Cargar una Familia inicial para pruebas (necesaria para Épica 4)
INSERT INTO persona (id, dni, domicilio, apellido, nombre, fecha_nacimiento) VALUES
(1, 12345678, 'Calle Falsa 123', 'Perez', 'Carlos', '1985-01-10'),
(2, 87654321, 'Avenida Siempreviva 742', 'Perez', 'Ana', '1987-03-20');

INSERT INTO asistido (id, ocupacion, fecha_registro, activo, familia_id) VALUES
(1, 'EMPLEADO', CURDATE(), TRUE, NULL), 
(2, 'AMA_DE_CASA', CURDATE(), TRUE, NULL);

INSERT INTO familia (id, nro_familia, nombre, fecha_registro, fecha_ultima_asistencia_recibida, activa) VALUES
(1, 'FAM-INICIAL1', 'Familia Prueba Asistencia', CURDATE(), NULL, TRUE);

-- Asociar asistidos a la familia
UPDATE asistido SET familia_id = 1 WHERE id IN (1, 2);

-- Cargar un Voluntario inicial (solo para demostrar la tabla)
INSERT INTO persona (id, dni, domicilio, apellido, nombre, fecha_nacimiento) VALUES
(3, 11223344, 'Calle Voluntario 50', 'Garcia', 'Laura', '1975-04-01');
INSERT INTO voluntario (id, nro_segu) VALUES (3, 'VOL-9876');
