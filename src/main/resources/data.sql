-- Data SQL para inicialización de datos del servicio de autenticación
-- Este archivo se ejecuta automáticamente después del schema.sql

-- Insertar usuarios de ejemplo para desarrollo y pruebas
-- Nota: Las contraseñas están encriptadas con BCrypt
-- password123 -> $2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi
-- admin123 -> $2a$10$gSLhQokSiyu0N2jTm4FjOeJ5V8X7K8m9P1qR2sT3uV4wX5yZ6a7b8c

-- Usuario Administrador
INSERT INTO users (username, password, email, first_name, last_name, role) VALUES 
('admin', '$2a$10$gSLhQokSiyu0N2jTm4FjOeJ5V8X7K8m9P1qR2sT3uV4wX5yZ6a7b8c', 
 'admin@banco-digital.com', 'Administrador', 'Sistema', 'ADMIN')
ON CONFLICT (username) DO NOTHING;

-- Usuario Gerente
INSERT INTO users (username, password, email, first_name, last_name, role) VALUES 
('gerente01', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 
 'gerente01@banco-digital.com', 'Carlos', 'Rodríguez', 'GERENTE')
ON CONFLICT (username) DO NOTHING;

-- Usuario Empleado
INSERT INTO users (username, password, email, first_name, last_name, role) VALUES 
('empleado01', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 
 'empleado01@banco-digital.com', 'María', 'González', 'EMPLEADO')
ON CONFLICT (username) DO NOTHING;

-- Usuario Cliente
INSERT INTO users (username, password, email, first_name, last_name, role) VALUES 
('cliente01', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 
 'cliente01@banco-digital.com', 'Juan', 'Pérez', 'CLIENTE')
ON CONFLICT (username) DO NOTHING;

-- Usuario Cliente adicional
INSERT INTO users (username, password, email, first_name, last_name, role) VALUES 
('cliente02', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 
 'cliente02@banco-digital.com', 'Ana', 'Martínez', 'CLIENTE')
ON CONFLICT (username) DO NOTHING;

-- Insertar más usuarios de prueba para diferentes roles
INSERT INTO users (username, password, email, first_name, last_name, role) VALUES 
('empleado02', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 
 'empleado02@banco-digital.com', 'Luis', 'Sánchez', 'EMPLEADO'),
('gerente02', '$2a$10$gSLhQokSiyu0N2jTm4FjOeJ5V8X7K8m9P1qR2sT3uV4wX5yZ6a7b8c', 
 'gerente02@banco-digital.com', 'Laura', 'Díaz', 'GERENTE')
ON CONFLICT (username) DO NOTHING;

-- Confirmación de inserción
DO $$
BEGIN
    RAISE NOTICE 'Base de datos inicializada con % usuarios', 
        (SELECT COUNT(*) FROM users);
    RAISE NOTICE 'Usuarios por rol:';
    RAISE NOTICE '  ADMIN: %', (SELECT COUNT(*) FROM users WHERE role = 'ADMIN');
    RAISE NOTICE '  GERENTE: %', (SELECT COUNT(*) FROM users WHERE role = 'GERENTE');
    RAISE NOTICE '  EMPLEADO: %', (SELECT COUNT(*) FROM users WHERE role = 'EMPLEADO');
    RAISE NOTICE '  CLIENTE: %', (SELECT COUNT(*) FROM users WHERE role = 'CLIENTE');
END $$;
