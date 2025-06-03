# Sistema de Gestión Hotelera - Hotel Casa Andina

Sistema de gestión hotelera desarrollado en Java con arquitectura MVC y base de datos SQLite.

## Requisitos Previos

- Java JDK 21
- Maven 3.9.0 o superior
- IDE compatible con Maven (NetBeans/VSCode)

## Instalación

1. Clonar el repositorio:
```bash
git clone [url-repositorio]
cd ServicioHotelCA
```

2. Instalar dependencias:
```bash
mvn clean install
```

3. Ejecutar la aplicación:
```bash
mvn exec:java
```

## Estructura del Proyecto

```
ServicioHotelCA/
├── src/main/java/com/mycompany/avanceproyecto/
│   ├── config/           # Configuración de BD
│   ├── controller/       # Controladores MVC
│   ├── model/           # Entidades
│   ├── repository/      # Acceso a datos
│   ├── service/         # Lógica de negocio
│   └── view/            # Interfaces de usuario
```

## Tecnologías Utilizadas

- Java 21
- Maven
- SQLite
- Swing (GUI)
- Logback (Logging)
- Google Guava
- Apache Commons Lang

## Modelo de Datos

### Entidades Principales

1. **Usuario**
   - Gestión de acceso al sistema
   - Roles y permisos
   - Autenticación

2. **Cliente**
   - Información personal
   - Historial de alojamientos
   - Gestión de contacto

3. **Habitación**
   - Tipos de habitación
   - Estado de disponibilidad
   - Precios y características

4. **Alojamiento**
   - Registro de estadías
   - Relación cliente-habitación
   - Control de fechas

5. **Servicio**
   - Servicios disponibles
   - Precios
   - Categorías

6. **ConsumoServicio**
   - Registro de consumos
   - Tracking de servicios
   - Facturación

7. **Factura**
   - Generación de comprobantes
   - Cálculo de totales
   - Historial de pagos

## Características

- Autenticación de usuarios
- Gestión de habitaciones
- Registro de clientes
- Control de alojamientos
- Facturación de servicios
- Reportes y estadísticas

## Base de Datos

El sistema utiliza SQLite para almacenamiento de datos:
- Archivo: `hotel.db`
- Creación automática en primer inicio
- Datos iniciales precargados

## Seguridad

- Autenticación basada en roles
- Validación de inputs
- Logging de eventos
- Manejo de excepciones

## Desarrollo

Para contribuir al proyecto:
1. Fork del repositorio
2. Crear rama feature
3. Commit cambios
4. Push a la rama
5. Crear Pull Request

## Licencia

Este proyecto es parte del curso Integrador I: Sistemas Software- Sección 44647.
Universidad UTP Lima Sur.

## Contacto

- Desarrollador: Nelson, Victor, Yuli
