
package com.mycompany.avanceproyecto.daos.impl;

import com.mycompany.avanceproyecto.config.DatabaseInitializer;
import com.mycompany.avanceproyecto.daos.ServicioDAO;
import com.mycompany.avanceproyecto.model.Servicios;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ServicioDAOImplTest {

    private static ServicioDAO servicioDAO;

    @BeforeAll
    static void setUpClass() {
        // Reiniciar la base de datos antes de todas las pruebas
        DatabaseInitializer.initializeDatabase();
        servicioDAO = new ServicioDAOImpl();
    }

    @Test
    @Order(1)
    void testInsertarYObtenerPorId() throws Exception {
        Servicios nuevo = new Servicios(0, "Lavandería", 35.0);
        servicioDAO.insertar(nuevo);

        List<Servicios> todos = servicioDAO.listar();
        Servicios guardado = todos.stream()
                .filter(s -> "Lavandería".equals(s.getNombre()))
                .findFirst()
                .orElse(null);

        assertNotNull(guardado);
        assertEquals("Lavandería", guardado.getNombre());
        assertEquals(35.0, guardado.getPrecio());
    }

    @Test
    @Order(2)
    void testActualizar() throws Exception {
        Servicios servicio = servicioDAO.listar().stream()
                .filter(s -> s.getNombre().equals("Lavandería"))
                .findFirst()
                .orElseThrow();

        servicio.setPrecio(40.0);
        servicioDAO.actualizar(servicio);

        Servicios actualizado = servicioDAO.obtenerPorId(servicio.getId());
        assertEquals(40.0, actualizado.getPrecio());
    }

    @Test
    @Order(3)
    void testListar() throws Exception {
        List<Servicios> lista = servicioDAO.listar();
        assertNotNull(lista);
        assertTrue(lista.size() >= 3); 
    }

    @Test
    @Order(4)
    void testListarPorAlojamiento() throws Exception {
        List<Servicios> servicios = servicioDAO.listarPorAlojamiento(1);
        assertNotNull(servicios);
        assertTrue(servicios.stream().anyMatch(s -> s.getNombre().equals("Desayuno")));
        assertTrue(servicios.stream().anyMatch(s -> s.getNombre().equals("Wi-Fi premium")));
    }

    @Test
    @Order(5)
    void testEliminar() throws Exception {
        Servicios servicio = servicioDAO.listar().stream()
                .filter(s -> s.getNombre().equals("Lavandería"))
                .findFirst()
                .orElseThrow();

        servicioDAO.eliminar(servicio.getId());

        Servicios eliminado = servicioDAO.obtenerPorId(servicio.getId());
        assertNull(eliminado);
    }
}