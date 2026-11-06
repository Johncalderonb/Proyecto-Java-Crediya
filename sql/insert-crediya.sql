-- =====================================================================
-- insert-crediya.sql  |  Datos de prueba para la base crediya_db
-- Ejecutar DESPUES de CrediYa.sql (las tablas ya deben existir)
--
-- ATENCION: la primera parte BORRA todos los datos actuales de las 4 tablas
-- y reinicia los id en 1, para que los préstamos y pagos queden bien enlazados.
--
-- Claves de acceso (se guardan encriptadas con SHA2, igual que en Java):
--   Administradores:  1098555666 / admin123     1098123456 / natalia123
--   Clientes:         documento / nombre en minuscula + 123  (ej: 91234567 / pedro123)
-- =====================================================================

USE crediya_db;

SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE pagos;
TRUNCATE TABLE prestamos;
TRUNCATE TABLE clientes;
TRUNCATE TABLE empleados;
SET FOREIGN_KEY_CHECKS = 1;

-- ---------------------------------------------------------------------
-- EMPLEADOS (10)
-- ---------------------------------------------------------------------
INSERT INTO empleados (nombre, documento, rol, correo, salario, clave) VALUES
('Andrea Diaz', '1098555666', 'ADMINISTRADOR', 'andrea@crediya.com', 4200000, SHA2('admin123', 256)),
('Laura Gomez', '1098111222', 'ASESOR', 'laura@crediya.com', 2500000, SHA2('laura123', 256)),
('Carlos Ruiz', '1098333444', 'COBRADOR', 'carlos@crediya.com', 2200000, SHA2('carlos123', 256)),
('Diana Pardo', '1098777888', 'ASESOR', 'diana@crediya.com', 2600000, SHA2('diana123', 256)),
('Jorge Mantilla', '1098999000', 'COBRADOR', 'jorge@crediya.com', 2100000, SHA2('jorge123', 256)),
('Sofia Rueda', '1098222333', 'ASESOR', 'sofia@crediya.com', 2550000, SHA2('sofia123', 256)),
('Miguel Serrano', '1098444555', 'GERENTE', 'miguel@crediya.com', 6000000, SHA2('miguel123', 256)),
('Paola Castro', '1098666777', 'ASESOR', 'paola@crediya.com', 2450000, SHA2('paola123', 256)),
('Andres Vargas', '1098888999', 'COBRADOR', 'andres@crediya.com', 2150000, SHA2('andres123', 256)),
('Natalia Ortiz', '1098123456', 'ADMINISTRADOR', 'natalia@crediya.com', 4000000, SHA2('natalia123', 256));

-- ---------------------------------------------------------------------
-- CLIENTES (10)
-- ---------------------------------------------------------------------
INSERT INTO clientes (nombre, documento, correo, telefono, clave) VALUES
('Pedro Perez', '91234567', 'pedro@gmail.com', '3001112233', SHA2('pedro123', 256)),
('Maria Lopez', '63456789', 'maria@gmail.com', '3104445566', SHA2('maria123', 256)),
('Juan Torres', '1095777888', 'juan@gmail.com', '3157778899', SHA2('juan123', 256)),
('Camila Rojas', '1005123456', 'camila@gmail.com', '3201234567', SHA2('camila123', 256)),
('Luis Herrera', '91876543', 'luis@gmail.com', '3012345678', SHA2('luis123', 256)),
('Valentina Suarez', '1098765432', 'valentina@gmail.com', '3156789012', SHA2('valentina123', 256)),
('Ricardo Molina', '13567890', 'ricardo@gmail.com', '3175551234', SHA2('ricardo123', 256)),
('Daniela Acosta', '1102345678', 'daniela@gmail.com', '3189876543', SHA2('daniela123', 256)),
('Felipe Cardenas', '1096543210', 'felipe@gmail.com', '3004567890', SHA2('felipe123', 256)),
('Gabriela Nino', '63987654', 'gabriela@gmail.com', '3112223344', SHA2('gabriela123', 256));

-- ---------------------------------------------------------------------
-- PRESTAMOS (12)
-- total = monto + monto * interes / 100 ; cuota = total / cuotas
-- saldo = total - lo pagado ; estado PAGADO cuando el saldo llega a 0
-- ---------------------------------------------------------------------
INSERT INTO prestamos (cliente_id, empleado_id, monto, interes, cuotas, fecha_inicio, estado, saldo) VALUES
(1, 2, 1000000, 10, 10, '2025-06-01', 'PENDIENTE', 770000.00),  -- #1 Pedro Perez: vencido y moroso
(2, 2, 2000000, 12, 12, '2026-08-01', 'PENDIENTE', 2240000.00),  -- #2 Maria Lopez: moroso: sin pagos
(3, 4, 500000, 5, 5, '2026-01-10', 'PAGADO', 0.00),  -- #3 Juan Torres: pagado
(4, 6, 3000000, 15, 24, '2026-02-15', 'PENDIENTE', 2300000.00),  -- #4 Camila Rojas: al dia
(5, 2, 1500000, 8, 6, '2025-12-01', 'PENDIENTE', 810000.00),  -- #5 Luis Herrera: vencido y moroso
(6, 8, 800000, 10, 8, '2026-05-20', 'PENDIENTE', 330000.00),  -- #6 Valentina Suarez: al dia
(7, 4, 4800000, 20, 24, '2026-03-01', 'PENDIENTE', 4800000.00),  -- #7 Ricardo Molina: moroso
(8, 6, 1200000, 0, 12, '2026-09-10', 'PENDIENTE', 1100000.00),  -- #8 Daniela Acosta: al dia
(9, 8, 600000, 10, 6, '2025-10-01', 'PAGADO', 0.00),  -- #9 Felipe Cardenas: pagado
(10, 2, 2500000, 12, 10, '2026-10-15', 'PENDIENTE', 2800000.00),  -- #10 Gabriela Nino: al dia: recien creado
(1, 4, 400000, 5, 4, '2026-07-01', 'PAGADO', 0.00),  -- #11 Pedro Perez: pagado
(3, 6, 900000, 10, 9, '2026-04-05', 'PENDIENTE', 770000.00);  -- #12 Juan Torres: moroso

-- ---------------------------------------------------------------------
-- PAGOS (41)  una fila por cuota pagada
-- ---------------------------------------------------------------------
INSERT INTO pagos (prestamo_id, fecha_pago, monto) VALUES
(1, '2025-07-01', 110000.00),
(1, '2025-08-01', 110000.00),
(1, '2025-09-01', 110000.00),
(3, '2026-02-10', 105000.00),
(3, '2026-03-10', 105000.00),
(3, '2026-04-10', 105000.00),
(3, '2026-05-10', 105000.00),
(3, '2026-06-10', 105000.00),
(4, '2026-03-15', 143750.00),
(4, '2026-04-15', 143750.00),
(4, '2026-05-15', 143750.00),
(4, '2026-06-15', 143750.00),
(4, '2026-07-15', 143750.00),
(4, '2026-08-15', 143750.00),
(4, '2026-09-15', 143750.00),
(4, '2026-10-15', 143750.00),
(5, '2026-01-01', 270000.00),
(5, '2026-02-01', 270000.00),
(5, '2026-03-01', 270000.00),
(6, '2026-06-20', 110000.00),
(6, '2026-07-20', 110000.00),
(6, '2026-08-20', 110000.00),
(6, '2026-09-20', 110000.00),
(6, '2026-10-20', 110000.00),
(7, '2026-04-01', 240000.00),
(7, '2026-05-01', 240000.00),
(7, '2026-06-01', 240000.00),
(7, '2026-07-01', 240000.00),
(8, '2026-10-10', 100000.00),
(9, '2025-11-01', 110000.00),
(9, '2025-12-01', 110000.00),
(9, '2026-01-01', 110000.00),
(9, '2026-02-01', 110000.00),
(9, '2026-03-01', 110000.00),
(9, '2026-04-01', 110000.00),
(11, '2026-08-01', 105000.00),
(11, '2026-09-01', 105000.00),
(11, '2026-10-01', 105000.00),
(11, '2026-11-01', 105000.00),
(12, '2026-05-05', 110000.00),
(12, '2026-06-05', 110000.00);

-- ---------------------------------------------------------------------
-- VERIFICACION (ejecuta y revisa)
-- ---------------------------------------------------------------------
SELECT 'empleados' AS tabla, COUNT(*) AS filas FROM empleados
UNION ALL SELECT 'clientes', COUNT(*) FROM clientes
UNION ALL SELECT 'prestamos', COUNT(*) FROM prestamos
UNION ALL SELECT 'pagos', COUNT(*) FROM pagos;

-- El saldo de cada prestamo debe ser igual a: total - suma de sus pagos
SELECT p.id,
       p.monto + p.monto * p.interes / 100 AS total,
       COALESCE(SUM(g.monto), 0) AS pagado,
       p.saldo,
       p.estado
FROM prestamos p
LEFT JOIN pagos g ON g.prestamo_id = p.id
GROUP BY p.id, p.monto, p.interes, p.saldo, p.estado
ORDER BY p.id;
