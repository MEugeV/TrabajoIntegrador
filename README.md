## Trabajo Final Integrador - Programación II

### UTN

### Tecnicatura Universitaria en Programación a Distancia

#### Alumna: María Eugenia Vogt - DNI: 30231758


## Gestión de Contribuyentes - Empresa => Domicilio Fiscal

### Objetivo

Este Trabajo Final Integrador tiene como objetivo la aplicación de JAVA en una relación unidireccional 1 a 1, Patrón DAO y MySQL.

### Consignas

- Se desarolla una aplicación en JAVA modelando dos clases relacionadas con una relación unidirecional 1 a 1 donde una clase referenia a otra
- Se persisten datos en una base relacional con JDBC sin ORM. 
- Aplicación del patrón DAO y capa Service
- Manejo de excepciones en todas las capas
- Menú de consola para operaciones CRUD
- Desarro de operaciones transaccionales (commit/rollback)
- Eliminación lógica de registros
- Uso de PreparedStatements para prevenir SQL Injection
- Uso del patrón try-with-resources para gestión automática de recursos JDBC

### Arquitectura
- Conexión a base de datos MySQL mediante JDBC
- Capa de Modelo (Models): Representación de entidades con baja lógica
- Capa de Acceso a Datos (DAO): Operaciones de persistencia con preparedStatements
- Capa de Lógica de Negocio (Service): Validaciones y reglas de negocio y orquestación de transacciones
- Capa de Presentación (UI): Arranque de la aplicación e interacción con el usuario mediante consola

### Requisitos del Sistema

| Componente | Versión
|------------|-------------------|
| Java JDK | 21.0.8
| MySQL | 8.4.7

## Instalación y ejecución

### 1. Configurar Base de Datos

Ejecutar el siguiente script SQL en MySQL:

```sql
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
```

Cargar datos de prueba ejecutando el siguiente script SQL en MySQL:


```sql
INSERT INTO domicilioFiscal (calle, numero, ciudad, provincia, codigoPostal, pais) VALUES
("Rivadavia", 23, "Córdoba", "Córdoba", "5000", "Argentina"),
("Colón", 1000, "Córdoba", "Córdoba", "5000", "Argentina"),
("Sucre", 502, "Chilecito", "La Rioja", "5360", "Argentina");


INSERT INTO empresa (razonSocial, cuit, actividadPrincipal, email, domicilioFiscalId) VALUES
("El Tano", "30669425787", "Servicios de comida", "eltano@gmail.com", 1),
("El Mercadito", "30549425711", "Comercio", "elmercado@gmail.com", 2),
("El Bus", "30224125484", "Servicios de transporte", "elbus@gmail.com", 3);
```


### 2. Compilar el Proyecto

Desde Apache Net Beans ejecutar en el proyecto el comando "Clean and Build"

### 3. Configurar Conexión

Conexión default:
- **Host**: localhost:3306
- **Base de datos**: contribuyentes
- **Usuario**: root
- **Contraseña**: (vacía)

Configuración personalizada:

Incluir las variables deseadas en archivo de configuración config.properties:

```
TrabajoIntegrador/
└── config.properties

db.url=jdbc:mysql://localhost:3306/db
db.user=root
db.password=password
```

## Ejecución

### Desde IDE
1. Abrir proyecto en Apache Netbeans
2. Ejecutar clase `Main.Main` o `Main.AppMenu` 

### Verificar Conexión
2. Ejecutar clase `Main.TestConection` 


Salida esperada:
```
Conectado a URL: jdbc:mysql://localhost:3306/contribuyentes
Base de datos: contribuyentes
Schema: null
Conexión a mysql exitosa
Id 1
Id 2
Id 3
```
### Flujo de uso

- Su menú permite gestionar empresas y sus domicilios fiscales con una relación uno a uno exclusivo:

```
========= MENÚ =========
1. Crear empresa
2. Listar empresas
3. Actualizar empresa
4. Eliminar empresa
5. Recuperar empresa
6. Listar domicilios fiscales
0. Salir de la aplicación
```
 

 1. Creación de una empresa y opcionalmente su domicilio fiscal asociado (transacción)
 2. Listado de empresas activas (sin borrado lógico) con su respectivo domicilio fiscal, en cuatro modalidades:
    1. Listar todas las empresas
    2. Buscar empresa por razón social
    3. Buscar empresa por CUIT 
    4. Buscar empresa por ID 
 3. Actualización de campos de una empresa y su domicilio fiscal asociado (sólo se actualizan los campos que sobreescribe el usuario) (transacción)
 4. Borrado lógido de la empresa y su domicilio fiscal asociado (transacción)
 5. Recupero de borrado lógido de la empresa y su domicilio fiscal asociado (transacción)
 6. Listado de domicilios fiscales activos

### Enlace al video