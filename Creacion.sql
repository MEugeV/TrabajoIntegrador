CREATE DATABASE contribuyentes;
USE contribuyentes;

CREATE TABLE domicilioFiscal (
id INT PRIMARY KEY AUTO_INCREMENT,
eliminado TINYINT(1) NOT NULL DEFAULT 0,
calle VARCHAR(100) NOT NULL,
numero INT,
ciudad VARCHAR(80) NOT NULL,
provincia ENUM(
'Buenos Aires', 'Catamarca', 'Chaco', 'Chubut', 'Córdoba', 'Corrientes',
'Entre Ríos', 'Formosa', 'Jujuy', 'La Pampa', 'La Rioja', 'Mendoza',
'Misiones', 'Neuquén', 'Río Negro', 'Salta', 'San Juan', 'San Luis',
'Santa Cruz', 'Santa Fe', 'Santiago del Estero', 'Tierra del Fuego',
'Tucumán', 'Ciudad Autonoma de Buenos Aires'
) NOT NULL,
codigoPostal VARCHAR(10),
pais ENUM('Argentina') NOT NULL DEFAULT "Argentina"
);

CREATE TABLE empresa (
id INT PRIMARY KEY AUTO_INCREMENT,
eliminado TINYINT(1) NOT NULL DEFAULT 0,
razonSocial VARCHAR(120) NOT NULL,
cuit VARCHAR(11) NOT NULL UNIQUE,
actividadPrincipal ENUM(
'Actividades administrativas', 'Administracion publica', 'Agricultura', 'Ganadería',
'Servicios deportivos', 'Comercio', 'Construcción', 'Enseñanza', 'Explotación de minas',
'Industria manufacturera', 'Información y comunicaciones', 'Intermediación financiera',
'Salud', 'Servicios artísticos', 'Servicios de alojamiento', 'Servicios de comida',
'Servicios de transporte', 'Servicios inmobiliarios'
) NOT NULL,
email VARCHAR(120),
domicilioFiscalId INT UNIQUE,
FOREIGN KEY (domicilioFiscalId) REFERENCES domicilioFiscal(id)
);