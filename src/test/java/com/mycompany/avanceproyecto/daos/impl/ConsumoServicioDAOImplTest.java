
package com.mycompany.avanceproyecto.daos.impl;

import com.mycompany.avanceproyecto.config.DatabaseInitializer;
import com.mycompany.avanceproyecto.daos.AlojamientoDAO;
import com.mycompany.avanceproyecto.daos.ClienteDAO;
import com.mycompany.avanceproyecto.daos.ConsumoServicioDAO;
import com.mycompany.avanceproyecto.daos.FacturaDAO;
import com.mycompany.avanceproyecto.daos.HabitacionDAO;
import com.mycompany.avanceproyecto.daos.ServicioDAO;
import com.mycompany.avanceproyecto.model.Alojamientos;
import com.mycompany.avanceproyecto.model.Clientes;
import com.mycompany.avanceproyecto.model.ConsumoServicio;
import com.mycompany.avanceproyecto.model.Facturas;
import com.mycompany.avanceproyecto.model.Habitaciones;
import com.mycompany.avanceproyecto.model.Servicios;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


class ConsumoServicioDAOImplTest {

    private ConsumoServicioDAOImpl consumoDAO;
    private AlojamientoDAOImpl alojamientoDAO;
    private ClienteDAOImpl clienteDAO;
    private ServicioDAOImpl servicioDAO;
    private HabitacionDAOImpl habitacionDAO;

    private Clientes cliente;
    private Habitaciones habitacion;
    private Alojamientos alojamiento;
    private Servicios servicio;

    @BeforeEach
    void setUp() throws Exception {
        // Inicializar la base de datos antes de cada test
        DatabaseInitializer.initializeDatabase();
        
        consumoDAO = new ConsumoServicioDAOImpl();
        alojamientoDAO = new AlojamientoDAOImpl();
        clienteDAO = new ClienteDAOImpl();
        servicioDAO = new ServicioDAOImpl();
        habitacionDAO = new HabitacionDAOImpl();

        // Cliente único con timestamp más preciso
        long timestamp = System.currentTimeMillis();
        cliente = new Clientes();
        cliente.setNombre("Test Cliente");
        cliente.setDni((int) (10000000 + (timestamp % 1000000))); // Usar los últimos 6 dígitos
        cliente.setTelefono(900000000);
        cliente.setCorreo("test" + timestamp + "@mail.com");
        clienteDAO.insertar(cliente);
        cliente = clienteDAO.obtenerPorDni(cliente.getDni());

        // Habitación única con timestamp
        habitacion = new Habitaciones();
        habitacion.setNumero("H" + timestamp);
        habitacion.setTipo("Simple");
        habitacion.setDisponible(true);
        habitacion.setPrecio(100.0);
        habitacionDAO.insertar(habitacion);
        habitacion = habitacionDAO.listar().stream()
            .filter(h -> h.getNumero().equals(habitacion.getNumero()))
            .findFirst().orElseThrow();

        // Alojamiento
        alojamiento = new Alojamientos();
        alojamiento.setCliente(cliente);
        alojamiento.setHabitacion(habitacion);
        alojamiento.setFechaEntrada(LocalDate.now());
        alojamiento.setFechaSalida(LocalDate.now().plusDays(2));
        alojamientoDAO.insertar(alojamiento);

        // Servicio con nombre único
        servicio = new Servicios();
        servicio.setNombre("TV_" + timestamp);
        servicio.setPrecio(10.0);
        servicioDAO.insertar(servicio);
        servicio = servicioDAO.listar().stream()
            .filter(s -> s.getNombre().equals("TV_" + timestamp))
            .findFirst().orElseThrow();
    }
    
    @AfterEach
    void tearDown() throws Exception {
        // Limpiar datos después de cada test
        try (var conn = com.mycompany.avanceproyecto.config.DatabaseConfig.getConnection();
             var stmt = conn.createStatement()) {
            
            // Desactivar foreign keys temporalmente
            stmt.execute("PRAGMA foreign_keys = OFF");
            
            // Limpiar tablas en orden inverso de dependencias
            stmt.execute("DELETE FROM consumo_servicio");
            stmt.execute("DELETE FROM alojamiento_servicio");
            stmt.execute("DELETE FROM facturas");
            stmt.execute("DELETE FROM alojamientos");
            stmt.execute("DELETE FROM clientes WHERE id > 2"); // Mantener los clientes iniciales
            stmt.execute("DELETE FROM habitaciones WHERE id > 3"); // Mantener las habitaciones iniciales
            stmt.execute("DELETE FROM servicios WHERE id > 3"); // Mantener los servicios iniciales
            
            // Reactivar foreign keys
            stmt.execute("PRAGMA foreign_keys = ON");
        }
    }
    
    @AfterAll
    static void cleanupTestDatabase() {
        // Limpiar completamente la base de datos de test al final
        com.mycompany.avanceproyecto.config.DatabaseConfig.cleanTestDatabase();
    }

    @Test
    void testRegistrarYObtenerPorId() throws Exception {
        ConsumoServicio consumo = new ConsumoServicio();
        consumo.setAlojamiento(alojamiento);
        consumo.setServicio(servicio);
        consumo.setCantidad(2);

        consumoDAO.registrar(consumo);

        List<ConsumoServicio> consumos = consumoDAO.listarPorAlojamiento(alojamiento.getId());
        assertFalse(consumos.isEmpty());

        ConsumoServicio obtenido = consumoDAO.obtenerPorId(consumos.get(0).getId());
        assertNotNull(obtenido);
        assertEquals(2, obtenido.getCantidad());
        assertEquals(servicio.getId(), obtenido.getServicio().getId());
    }

    @Test
    void testListarPorAlojamiento() throws Exception {
        ConsumoServicio consumo = new ConsumoServicio();
        consumo.setAlojamiento(alojamiento);
        consumo.setServicio(servicio);
        consumo.setCantidad(1);
        consumoDAO.registrar(consumo);

        List<ConsumoServicio> lista = consumoDAO.listarPorAlojamiento(alojamiento.getId());
        assertNotNull(lista);
        assertTrue(lista.stream().anyMatch(cs -> cs.getServicio().getId() == servicio.getId()));
    }

    @Test
    void testListarTodos() throws Exception {
        ConsumoServicio consumo = new ConsumoServicio();
        consumo.setAlojamiento(alojamiento);
        consumo.setServicio(servicio);
        consumo.setCantidad(3);
        consumoDAO.registrar(consumo);

        List<ConsumoServicio> lista = consumoDAO.listarTodos();
        assertNotNull(lista);
        assertTrue(lista.size() > 0);
    }

    @Test
    void testEliminar() throws Exception {
        ConsumoServicio consumo = new ConsumoServicio();
        consumo.setAlojamiento(alojamiento);
        consumo.setServicio(servicio);
        consumo.setCantidad(1);
        consumoDAO.registrar(consumo);

        List<ConsumoServicio> consumos = consumoDAO.listarPorAlojamiento(alojamiento.getId());
        assertFalse(consumos.isEmpty());

        int idEliminar = consumos.get(0).getId();
        consumoDAO.eliminar(idEliminar);

        ConsumoServicio eliminado = consumoDAO.obtenerPorId(idEliminar);
        assertNull(eliminado);
    }
}