
package com.mycompany.avanceproyecto.daos.impl;

import com.mycompany.avanceproyecto.daos.ClienteDAO;
import com.mycompany.avanceproyecto.model.Clientes;
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
class ClienteDAOImplTest {

    private final ClienteDAO clienteDAO = new ClienteDAOImpl();
    private Clientes clientePrueba;

    @BeforeAll
    void insertarCliente() throws Exception {
        long timestamp = System.currentTimeMillis();
        clientePrueba = new Clientes();
        clientePrueba.setNombre("Cliente Test " + timestamp);
        clientePrueba.setDni((int)(timestamp % 100_000_000));  // Único
        clientePrueba.setTelefono(987654321);
        clientePrueba.setCorreo("cliente" + timestamp + "@test.com");

        clienteDAO.insertar(clientePrueba);

        // Obtener por DNI (NO usar obtenerPorId)
        clientePrueba = clienteDAO.obtenerPorDni(clientePrueba.getDni());
    }

    @Test
    @Order(1)
    void testObtenerPorDni() throws Exception {
        assertNotNull(clientePrueba);
        Clientes obtenido = clienteDAO.obtenerPorDni(clientePrueba.getDni());
        assertNotNull(obtenido);
        assertEquals(clientePrueba.getDni(), obtenido.getDni());
        assertEquals(clientePrueba.getNombre(), obtenido.getNombre());
    }

    @Test
    @Order(2)
    void testListar() throws Exception {
        List<Clientes> clientes = clienteDAO.listar();
        assertNotNull(clientes);
        assertTrue(clientes.size() > 0);
    }

    @Test
    @Order(3)
    void testActualizar() throws Exception {
        clientePrueba.setNombre("Cliente Actualizado");
        clienteDAO.actualizar(clientePrueba);

        Clientes actualizado = clienteDAO.obtenerPorDni(clientePrueba.getDni());
        assertEquals("Cliente Actualizado", actualizado.getNombre());
    }

    @Test
    @Order(4)
    void testEliminar() throws Exception {
        clienteDAO.eliminar(clientePrueba.getId());

        Clientes eliminado = clienteDAO.obtenerPorDni(clientePrueba.getDni());
        assertNull(eliminado);
    }
}