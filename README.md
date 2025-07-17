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
   - Estados: ACTIVO/PAGADO/FINALIZADO
   - Gestión automática de estados

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
   - Validación de pagos duplicados
   - Búsqueda por DNI de cliente

## Características

- Autenticación de usuarios
- Gestión de habitaciones con filtros de disponibilidad
- Registro de clientes con autocompletado
- Control de alojamientos con estados automáticos
- Facturación de servicios con validación de duplicados
- Búsqueda rápida por DNI y número de habitación
- Integración entre módulos (navegación contextual)
- Reportes y estadísticas

## Base de Datos

El sistema utiliza SQLite para almacenamiento de datos:
- Separación automática entre entornos de desarrollo y producción
- Datos de test isolados en ubicaciones temporales
- Creación automática en primer inicio
- Datos iniciales precargados
- Configuración centralizada de conexiones

## Distribución

El sistema soporta múltiples métodos de distribución:

### Proceso de Build Completo
```bash
# 1. Compilar y crear JAR con dependencias
mvn clean package

# 2. Crear aplicación portable
mvn jpackage:jpackage@app-image

# 3. Crear instalador MSI (requiere WiX)
mvn jpackage:jpackage@msi
```

### Aplicación Portable
```bash
mvn clean package
mvn jpackage:jpackage@app-image
```
- Ubicación: `dist/ServicioHotelCA/`
- Autocontenida con JRE incluido
- No requiere instalación de Java

### Instalador MSI (Windows)
```bash
mvn jpackage:jpackage@msi
```
- Requiere WiX Toolset instalado
- Instalación tradicional en Windows
- Registro en Programas y características
- Ubicación: `dist-installer/`

### Ejecución desde JAR
```bash
java -jar target/ServicioHotelCA-1.0.jar
```
- Requiere Java 21+ instalado
- Todas las dependencias incluidas

### Script Automatizado
```bash
crear-instalador.bat
```
- Ejecuta todo el proceso automáticamente
- Detecta si WiX está instalado
- Crea ambos: aplicación portable e instalador MSI

## Seguridad

- Autenticación basada en roles
- Validación de inputs y formatos
- Prevención de operaciones duplicadas
- Logging de eventos del sistema
- Manejo robusto de excepciones

## Desarrollo

### Configuración de Desarrollo
- Configuración automática de entorno de desarrollo
- Base de datos de test separada automáticamente
- Logging detallado para debugging
- Suite de tests automatizada (39 tests)

### Contribución al Proyecto
Para contribuir al proyecto:
1. Fork del repositorio
2. Crear rama feature
3. Commit cambios
4. Push a la rama
5. Crear Pull Request

### Testing
```bash
mvn test
```
- Tests unitarios con cobertura completa
- Aislamiento de datos de test
- Verificación automática de funcionalidades críticas

## Licencia

Este proyecto es parte del curso Integrador I: Sistemas Software- Sección 44647.
Universidad UTP Lima Sur.

## Contacto

- Desarrollador: Nelson, Victor, Yuli
