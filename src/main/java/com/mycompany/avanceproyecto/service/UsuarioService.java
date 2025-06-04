package com.mycompany.avanceproyecto.service;

import com.mycompany.avanceproyecto.model.Usuarios;
import com.mycompany.avanceproyecto.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UsuarioService {
    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);
    private final UsuarioRepository repository;
    
    public UsuarioService() {
        this.repository = new UsuarioRepository();
    }
    
    public Usuarios autenticarUsuario(String username, String password) {
        return repository.findByUsernameAndPassword(username, password);
    }
}
