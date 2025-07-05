
package com.mycompany.avanceproyecto.daos.impl;

import com.mycompany.avanceproyecto.model.Usuarios;
import com.mycompany.avanceproyecto.util.PasswordEncryption;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


class UsuarioDAOImplTest {

    private UsuarioDAOImpl dao;
    private Usuarios testUsuario;

    @BeforeEach
    void setUp() {
        dao = new UsuarioDAOImpl();

        
        String timestamp = String.valueOf(System.currentTimeMillis());
        testUsuario = new Usuarios(0, "usuario_" + timestamp, "clave123", "admin");
    }

    @Test
    void testInsertarYObtenerPorNombre() throws Exception {
        dao.insertar(testUsuario);

        Usuarios usuarioBD = dao.obtenerPorNombre(testUsuario.getNombreUsuario());
        assertNotNull(usuarioBD);
        assertEquals(testUsuario.getNombreUsuario(), usuarioBD.getNombreUsuario());

        // La contraseña debe estar encriptada
        assertNotEquals(testUsuario.getContrasena(), usuarioBD.getContrasena());
        assertTrue(PasswordEncryption.isEncrypted(usuarioBD.getContrasena()));
    }

    @Test
    void testObtenerPorId() throws Exception {
        dao.insertar(testUsuario);
        Usuarios desdeNombre = dao.obtenerPorNombre(testUsuario.getNombreUsuario());
        assertNotNull(desdeNombre);

        Usuarios desdeId = dao.obtenerPorId(desdeNombre.getId());
        assertNotNull(desdeId);
        assertEquals(desdeNombre.getNombreUsuario(), desdeId.getNombreUsuario());
    }

    @Test
    void testListar() throws Exception {
        dao.insertar(testUsuario);
        List<Usuarios> usuarios = dao.listar();
        assertNotNull(usuarios);
        assertTrue(usuarios.size() > 0);
    }

    @Test
    void testActualizar() throws Exception {
        dao.insertar(testUsuario);
        Usuarios usuarioBD = dao.obtenerPorNombre(testUsuario.getNombreUsuario());

        // Cambiar rol y contraseña
        usuarioBD.setRol("recepcionista");
        usuarioBD.setContrasena("nuevaclave123");

        dao.actualizar(usuarioBD);

        Usuarios actualizado = dao.obtenerPorId(usuarioBD.getId());
        assertEquals("recepcionista", actualizado.getRol());
        assertNotEquals("nuevaclave123", actualizado.getContrasena()); // porque está encriptada
        assertTrue(PasswordEncryption.isEncrypted(actualizado.getContrasena()));
    }

    @Test
    void testEliminar() throws Exception {
        dao.insertar(testUsuario);
        Usuarios usuarioBD = dao.obtenerPorNombre(testUsuario.getNombreUsuario());
        assertNotNull(usuarioBD);

        dao.eliminar(usuarioBD.getId());

        Usuarios eliminado = dao.obtenerPorId(usuarioBD.getId());
        assertNull(eliminado);
    }
}
