-- Limpiar tablas si existen (opcional, para desarrollo)
-- SET FOREIGN_KEY_CHECKS = 0;
-- TRUNCATE TABLE ingrediente_receta;
-- TRUNCATE TABLE receta;
-- TRUNCATE TABLE ingrediente;
-- SET FOREIGN_KEY_CHECKS = 1;

-- Cargar Ingredientes base (catálogo)
INSERT INTO ingrediente (id, nombre, activo) VALUES
(1, 'Harina 000', TRUE),
(2, 'Azúcar', TRUE),
(3, 'Huevos', TRUE),
(4, 'Leche', TRUE),
(5, 'Manteca', TRUE),
(6, 'Sal', TRUE),
(7, 'Levadura', TRUE),
(8, 'Tomate', TRUE),
(9, 'Cebolla', TRUE),
(10, 'Ajo', TRUE),
(11, 'Carne Picada', TRUE);

-- Cargar Recetas de ejemplo
INSERT INTO receta (id, nombre, descripcion_preparacion, activa) VALUES
(1, 'Pan Casero Simple', 'Mezclar ingredientes, amasar y hornear.', TRUE),
(2, 'Salsa Bolognesa', 'Sofreír carne, cebolla y ajo, añadir tomate y cocinar a fuego lento.', TRUE),
(3, 'Tarta de Manzana (Baja)', 'Receta de tarta clásica, desactivada para pruebas.', FALSE); -- Receta de baja lógica

-- Cargar Ingredientes de Recetas (IngredienteReceta) para 'Pan Casero Simple' (id=1)
INSERT INTO ingrediente_receta (receta_id, ingrediente_id, cantidad, calorias, activo) VALUES
(1, 1, 0.500, 1800, TRUE), -- Harina 000 (0.5 kg, 1800 cal)
(1, 6, 0.010, 5, TRUE),   -- Sal (0.010 kg, 5 cal)
(1, 7, 0.005, 10, TRUE),  -- Levadura (0.005 kg, 10 cal)
(1, 4, 0.200, 100, TRUE); -- Leche (0.2 kg, 100 cal)

-- Cargar Ingredientes de Recetas (IngredienteReceta) para 'Salsa Bolognesa' (id=2)
INSERT INTO ingrediente_receta (receta_id, ingrediente_id, cantidad, calorias, activo) VALUES
(2, 11, 0.300, 750, TRUE),  -- Carne Picada (0.3 kg, 750 cal)
(2, 9, 0.150, 60, TRUE),   -- Cebolla (0.15 kg, 60 cal)
(2, 10, 0.020, 30, TRUE),  -- Ajo (0.02 kg, 30 cal)
(2, 8, 0.800, 200, TRUE);  -- Tomate (0.8 kg, 200 cal)

-- Cargar Ingredientes de Recetas (IngredienteReceta) para 'Tarta de Manzana (Baja)' (id=3)
INSERT INTO ingrediente_receta (receta_id, ingrediente_id, cantidad, calorias, activo) VALUES
(3, 1, 0.300, 1080, TRUE), -- Harina 000
(3, 2, 0.200, 800, TRUE),  -- Azúcar
(3, 5, 0.100, 700, FALSE); -- Manteca (marcada como inactiva para pruebas de eliminación lógica)