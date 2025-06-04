
package com.mycompany.avanceproyecto.daos;

import com.mycompany.avanceproyecto.model.Usuarios;
import java.util.List;

public interface UsuarioDAO {
    
    void insertar(Usuarios usuario) throws Exception;
    Usuarios obtenerPorNombre(String nombreUsuario) throws Exception;
    List<Usuarios> listar() throws Exception;
}
