-- Script de inicialización para PostgreSQL en Docker
-- Este archivo se usa en docker-compose.yml para inicializar la base de datos

-- PostgreSQL crea automáticamente la base de datos especificada en POSTGRES_DB
-- No necesitamos CREATE DATABASE aquí ya que se gestiona via variables de entorno

-- Conectarse a la base de datos (automático en PostgreSQL Docker)
-- La base de datos banco_auth ya está creada por la variable POSTGRES_DB

-- Mostrar mensaje de confirmación
SELECT 'Base de datos banco_auth lista para usar' AS status;
