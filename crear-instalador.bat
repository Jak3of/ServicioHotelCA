@echo off
echo ====================================
echo   Creando ejecutables ServicioHotelCA
echo ====================================

echo.
echo 1. Compilando proyecto...
call mvn clean package -DskipTests

echo.
echo 2. Creando version portable (APP_IMAGE)...
call mvn jpackage:jpackage

echo.
echo 3. Verificando si WiX esta instalado...
where candle.exe >nul 2>nul
if %errorlevel% == 0 (
    echo    ✓ WiX encontrado! Creando instalador MSI...
    call mvn jpackage:jpackage@installer-msi
    echo    ✓ Instalador MSI creado en: dist-installer\
) else (
    echo    ✗ WiX no encontrado
    echo    Para crear instalador MSI:
    echo    1. Descarga WiX Toolset desde: https://wixtoolset.org/
    echo    2. Instala WiX 3.11.2 o superior
    echo    3. Ejecuta este script nuevamente
)

echo.
echo ====================================
echo   Proceso completado!
echo ====================================
echo.
echo Archivos creados:
echo   - Version portable: dist\ServicioHotelCA\
echo   - JAR ejecutable: target\ServicioHotelCA-ejecutable.jar
if exist dist-installer echo   - Instalador MSI: dist-installer\ServicioHotelCA-1.0.msi
echo.
pause
