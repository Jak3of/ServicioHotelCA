
package com.mycompany.avanceproyecto.daos.impl;

import com.mycompany.avanceproyecto.config.DatabaseInitializer;
import com.mycompany.avanceproyecto.model.Habitaciones;
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
public class HabitacionDAOImplTest {

    private static HabitacionDAOImpl dao;
    private static int idGenerado;

    @BeforeAll
    public static void setUp() throws Exception {
        DatabaseInitializer.initializeDatabase(); 
        dao = new HabitacionDAOImpl();
    }

    @Test
    @Order(1)
    public void testInsertar() throws Exception {
        Habitaciones h = new Habitaciones();
        h.setNumero("H-" + System.currentTimeMillis());
        h.setTipo("DOBLE");
        h.setPrecio(150.0);
        h.setDisponible(true);

        dao.insertar(h);

        List<Habitaciones> habitaciones = dao.listar();
        Habitaciones insertada = habitaciones.stream()
                .filter(x -> x.getNumero().equals(h.getNumero()))
                .findFirst()
                .orElse(null);

        assertNotNull(insertada);
        idGenerado = insertada.getId(); // guardamos para otros tests
    }

    @Test
    @Order(2)
    public void testObtenerPorId() throws Exception {
        Habitaciones h = dao.obtenerPorId(idGenerado);
        assertNotNull(h);
        assertEquals(idGenerado, h.getId());
    }

    @Test
    @Order(3)
    public void testActualizar() throws Exception {
        Habitaciones h = dao.obtenerPorId(idGenerado);
        assertNotNull(h);

        h.setPrecio(199.99);
        h.setDisponible(false);
        dao.actualizar(h);

        Habitaciones actualizado = dao.obtenerPorId(idGenerado);
        assertEquals(199.99, actualizado.getPrecio());
        assertFalse(actualizado.isDisponible());
    }

    @Test
    @Order(4)
    public void testListar() throws Exception {
        List<Habitaciones> lista = dao.listar();
        assertNotNull(lista);
        assertTrue(lista.size() > 0);
    }

    @Test
    @Order(5)
    public void testListarDisponibles() throws Exception {
        List<Habitaciones> disponibles = dao.listarDisponibles();
        for (Habitaciones h : disponibles) {
            assertTrue(h.isDisponible());
        }
    }

    @Test
    @Order(6)
    public void testEliminar() throws Exception {
        dao.eliminar(idGenerado);
        Habitaciones h = dao.obtenerPorId(idGenerado);
        assertNull(h);
    }
}