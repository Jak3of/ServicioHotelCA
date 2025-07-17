/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.mycompany.avanceproyecto.daos.impl;

import com.mycompany.avanceproyecto.daos.AlojamientoDAO;
import com.mycompany.avanceproyecto.daos.ClienteDAO;
import com.mycompany.avanceproyecto.daos.FacturaDAO;
import com.mycompany.avanceproyecto.daos.HabitacionDAO;
import com.mycompany.avanceproyecto.model.Alojamientos;
import com.mycompany.avanceproyecto.model.Clientes;
import com.mycompany.avanceproyecto.model.Facturas;
import com.mycompany.avanceproyecto.model.Habitaciones;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FacturaDAOImplTest {

    private final FacturaDAO facturaDAO = new FacturaDAOImpl();
    private final ClienteDAO clienteDAO = new ClienteDAOImpl();
    private final HabitacionDAO habitacionDAO = new HabitacionDAOImpl();
    private final AlojamientoDAO alojamientoDAO = new AlojamientoDAOImpl();

    private Clientes cliente;
    private Habitaciones habitacion;
    private Alojamientos alojamiento;
    private Facturas factura;

    @BeforeAll
    static void setup() throws Exception {
        // Inicializar la base de datos antes de todos los tests
        com.mycompany.avanceproyecto.config.DatabaseInitializer.initializeDatabase();
    }
    
    @BeforeEach
    void setupEach() throws Exception {
        long ts = System.currentTimeMillis();

        // Cliente único
        cliente = new Clientes();
        cliente.setNombre("Cliente Test " + ts);
        cliente.setDni((int)(ts % 100_000_000));
        cliente.setTelefono(987654321);
        cliente.setCorreo("cliente" + ts + "@mail.com");
        clienteDAO.insertar(cliente);
        cliente = clienteDAO.obtenerPorDni(cliente.getDni());

        // Habitación única
        String numero = "HAB-" + ts;
        habitacion = new Habitaciones();
        habitacion.setNumero(numero);
        habitacion.setTipo("Suite");
        habitacion.setPrecio(250.0);
        habitacion.setDisponible(true);
        habitacionDAO.insertar(habitacion);
        habitacion = habitacionDAO.listar().stream()
            .filter(h -> h.getNumero().equals(numero))
            .findFirst().orElseThrow();

        // Alojamiento
        alojamiento = new Alojamientos();
        alojamiento.setCliente(cliente);
        alojamiento.setHabitacion(habitacion);
        alojamiento.setFechaEntrada(LocalDate.now());
        alojamiento.setFechaSalida(LocalDate.now().plusDays(2));
        alojamientoDAO.insertar(alojamiento);
        
        // Factura para todos los tests
        factura = new Facturas();
        factura.setFecha(LocalDate.now());
        factura.setCliente(cliente);
        factura.setAlojamiento(alojamiento);
        factura.setTotal(500.0);
        facturaDAO.insertar(factura);
    }

    @Test
    @Order(1)
    void testInsertarYObtenerPorId() throws Exception {
        // La factura ya está creada en setupEach, solo verificamos que funciona
        Facturas obtenida = facturaDAO.obtenerPorId(factura.getId());
        assertNotNull(obtenida);
        assertEquals(factura.getCliente().getId(), obtenida.getCliente().getId());
        assertEquals(factura.getTotal(), obtenida.getTotal(), 0.01);
    }

    @Test
    @Order(2)
    void testListar() throws Exception {
        List<Facturas> lista = facturaDAO.listar();
        assertNotNull(lista);
        assertTrue(lista.size() > 0);
    }

    @Test
    @Order(3)
    void testActualizar() throws Exception {
        factura.setTotal(999.99);
        facturaDAO.actualizar(factura);

        Facturas actualizada = facturaDAO.obtenerPorId(factura.getId());
        assertEquals(999.99, actualizada.getTotal(), 0.01);
    }

    @Test
    @Order(4)
    void testBuscarPorCliente() throws Exception {
        List<Facturas> facturas = facturaDAO.buscarPorCliente(cliente.getId());
        assertNotNull(facturas);
        assertTrue(facturas.stream().anyMatch(f -> f.getId() == factura.getId()));
    }

    @Test
    @Order(5)
    void testBuscarPorFecha() throws Exception {
        String fecha = LocalDate.now().toString();
        List<Facturas> facturas = facturaDAO.buscarPorFecha(fecha);
        assertNotNull(facturas);
        assertTrue(facturas.stream().anyMatch(f -> f.getId() == factura.getId()));
    }

    @Test
    @Order(6)
    void testEliminar() throws Exception {
        facturaDAO.eliminar(factura.getId());
        Facturas eliminada = facturaDAO.obtenerPorId(factura.getId());
        assertNull(eliminada);
    }
}
