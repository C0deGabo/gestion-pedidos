IF DB_ID('gestion_pedidos') IS NULL
BEGIN
    CREATE DATABASE gestion_pedidos;
END
GO

USE gestion_pedidos;
GO

IF OBJECT_ID('dbo.productos', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.productos (
        codigo VARCHAR(20) NOT NULL PRIMARY KEY,
        nombre VARCHAR(100) NOT NULL,
        precio DECIMAL(10,2) NOT NULL,
        stock INT NOT NULL,
        activo BIT NOT NULL
    );
END
GO