
package com.mycompany.avanceproyecto.daos.impl;

import com.mycompany.avanceproyecto.daos.AlojamientoDAO;
import com.mycompany.avanceproyecto.daos.ClienteDAO;
import com.mycompany.avanceproyecto.daos.HabitacionDAO;
import com.mycompany.avanceproyecto.daos.ServicioDAO;
import com.mycompany.avanceproyecto.model.Alojamientos;
import com.mycompany.avanceproyecto.model.Clientes;
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
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AlojamientoDAOImplTest {

    private final AlojamientoDAO alojamientoDAO = new AlojamientoDAOImpl();
    private final ClienteDAO clienteDAO = new ClienteDAOImpl();
    private final HabitacionDAO habitacionDAO = new HabitacionDAOImpl();
    private final ServicioDAO servicioDAO = new ServicioDAOImpl();

    private Clientes cliente;
    private Habitaciones habitacion;
    private Servicios servicio;
    private Alojamientos alojamiento;

    @BeforeAll
    void setup() throws Exception {
        long ts = System.currentTimeMillis();

        // Crear cliente único
        cliente = new Clientes();
        cliente.setNombre("Cliente Test " + ts);
        cliente.setDni((int)(ts % 100_000_000));
        cliente.setTelefono(999999999);
        cliente.setCorreo("cliente" + ts + "@test.com");
        clienteDAO.insertar(cliente);
        cliente = clienteDAO.obtenerPorDni(cliente.getDni());

        // Crear habitación única
        Habitaciones habitacion = new Habitaciones();
        long timestamp = System.currentTimeMillis();
        String numeroUnico = "HAB-" + timestamp;

        habitacion.setNumero(numeroUnico);
        habitacion.setTipo("Doble");
        habitacion.setPrecio(120.0);
        habitacion.setDisponible(true);

        // Insertar habitación
        habitacionDAO.insertar(habitacion);

// Recuperar habitación por número para obtener el ID
Habitaciones habitacionConId = habitacionDAO.listar().stream()
    .filter(h -> h.getNumero().equals(numeroUnico))
    .findFirst()
    .orElseThrow(() -> new IllegalStateException("No se pudo recuperar la habitación insertada"));

habitacion.setId(habitacionConId.getId());

        // Crear servicio único
        servicio = new Servicios();
        servicio.setNombre("Servicio Test " + ts);
        servicio.setPrecio(50.0);
        servicioDAO.insertar(servicio);
        servicio = servicioDAO.listar().stream()
                .filter(s -> s.getNombre().equals(servicio.getNombre()))
                .findFirst().orElseThrow();
    }

    @Test
         @Order(1)
        void testInsertarYObtenerPorId() throws Exception {
    // Crear cliente
    Clientes cliente = new Clientes();
    long timestamp = System.currentTimeMillis();
    cliente.setNombre("Cliente Test " + timestamp);
    cliente.setDni((int)(timestamp % 100_000_000));
    cliente.setTelefono(999999999);
    cliente.setCorreo("cliente" + timestamp + "@mail.com");
    clienteDAO.insertar(cliente);
    cliente = clienteDAO.obtenerPorDni(cliente.getDni());

    // Crear habitación
    Habitaciones habitacion = new Habitaciones();
    String numero = "HAB-" + timestamp;
    habitacion.setNumero(numero);
    habitacion.setTipo("Doble");
    habitacion.setPrecio(150.0);
    habitacion.setDisponible(true);
    habitacionDAO.insertar(habitacion);

    // Recuperar habitación con ID
    Habitaciones habitacionInsertada = habitacionDAO.listar().stream()
        .filter(h -> h.getNumero().equals(numero))
        .findFirst()
        .orElseThrow(() -> new Exception("Habitación no insertada"));

    // Crear alojamiento
    Alojamientos alojamiento = new Alojamientos();
    alojamiento.setCliente(cliente);
    alojamiento.setHabitacion(habitacionInsertada);  // ⚠️ Aquí es donde antes faltaba
    alojamiento.setFechaEntrada(LocalDate.now());
    alojamiento.setFechaSalida(LocalDate.now().plusDays(3));

    alojamientoDAO.insertar(alojamiento);
this.alojamiento = alojamiento;
    // Verificar que se insertó
    Alojamientos obtenido = alojamientoDAO.obtenerPorId(alojamiento.getId());
    assertNotNull(obtenido);
    assertEquals(cliente.getId(), obtenido.getCliente().getId());
    assertEquals(habitacionInsertada.getId(), obtenido.getHabitacion().getId());
}

    @Test
    @Order(2)
    void testListar() throws Exception {
        List<Alojamientos> lista = alojamientoDAO.listar();
        assertNotNull(lista);
        assertTrue(lista.size() > 0);
    }

   
   

    @Test
    @Order(3)
    void testEliminar() throws Exception {
        alojamientoDAO.eliminar(alojamiento.getId());
        Alojamientos eliminado = alojamientoDAO.obtenerPorId(alojamiento.getId());
        assertNull(eliminado);
    }
}