package com.mycompany.avanceproyecto.controller;

import com.mycompany.avanceproyecto.model.Usuarios;
import com.mycompany.avanceproyecto.service.UsuarioService;
import com.mycompany.avanceproyecto.view.Usuario;
import javax.swing.JOptionPane;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UsuarioController {
    private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);
    private final Usuario view;
    private final UsuarioService service;
    
    public UsuarioController(Usuario view) {
        this.view = view;
        this.service = new UsuarioService();
        inicializarControlador();
    }
    
    private void inicializarControlador() {
        view.getBtnNuevo().addActionListener(e -> limpiarFormulario());
        view.getBtnGuardar().addActionListener(e -> guardarUsuario());
        view.getBtnEliminar().addActionListener(e -> eliminarUsuario());
        view.getBtnBuscar().addActionListener(e -> buscarUsuarios());
        cargarUsuarios();
    }
    
    private void guardarUsuario() {
        try {
            logger.debug("Iniciando proceso de guardado de usuario");
            
            // Validar campos
            String nombreUsuario = view.getTxtNombreUsuario().getText().trim();
            String contrasena = view.getTxtContrasena().getText().trim();
            String rol = view.getCboRol().getSelectedItem().toString();
            
            if (nombreUsuario.isEmpty()) {
                JOptionPane.showMessageDialog(view, "El nombre de usuario es obligatorio");
                return;
            }
            
            if (contrasena.isEmpty()) {
                JOptionPane.showMessageDialog(view, "La contraseña es obligatoria");
                return;
            }
            
            if (contrasena.length() < 4) {
                JOptionPane.showMessageDialog(view, "La contraseña debe tener al menos 4 caracteres");
                return;
            }
            
            Usuarios usuario = new Usuarios();
            
            // Si hay ID, es una actualización
            String idText = view.getTxtIdUsuario().getText().trim();
            if (!idText.isEmpty()) {
                usuario.setId(Integer.parseInt(idText));
                
                // Para actualizaciones, verificar si es una nueva contraseña
                if ("[ENCRIPTADA]".equals(contrasena)) {
                    // El usuario no cambió la contraseña, obtener la actual
                    Usuarios usuarioActual = service.obtenerUsuarioCompleto(nombreUsuario);
                    if (usuarioActual != null) {
                        usuario.setContrasena(usuarioActual.getContrasena());
                    } else {
                        JOptionPane.showMessageDialog(view, "Error: No se pudo obtener la contraseña actual");
                        return;
                    }
                } else {
                    // Nueva contraseña ingresada
                    usuario.setContrasena(contrasena);
                }
            } else {
                // Nuevo usuario
                usuario.setContrasena(contrasena);
            }
            
            usuario.setNombreUsuario(nombreUsuario);
            usuario.setRol(rol);
            
            // Verificar si el usuario ya existe (solo para nuevos usuarios)
            if (idText.isEmpty()) {
                Usuarios usuarioExistente = service.buscarPorNombre(nombreUsuario);
                if (usuarioExistente != null) {
                    JOptionPane.showMessageDialog(view, 
                        "Ya existe un usuario con ese nombre: " + nombreUsuario,
                        "Usuario duplicado",
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }
            
            service.guardarOActualizarUsuario(usuario);
            
            JOptionPane.showMessageDialog(view, 
                idText.isEmpty() ? "Usuario creado con éxito" : "Usuario actualizado con éxito");
            
            view.limpiarCampos();
            cargarUsuarios();
            logger.info("Usuario guardado/actualizado exitosamente");
            
        } catch (Exception e) {
            logger.error("Error al guardar usuario", e);
            JOptionPane.showMessageDialog(view, "Error al guardar: " + e.getMessage());
        }
    }
    
    private void cargarUsuarios() {
        try {
            List<Usuarios> usuarios = service.listarUsuarios();
            view.actualizarTabla(usuarios);
        } catch (Exception e) {
            logger.error("Error al cargar usuarios", e);
            JOptionPane.showMessageDialog(view, "Error al cargar usuarios: " + e.getMessage());
        }
    }
    
    private void eliminarUsuario() {
        int fila = view.getTablaUsuarios().getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(view, "Por favor, seleccione un usuario para eliminar");
            return;
        }

        int id = (Integer) view.getTablaUsuarios().getValueAt(fila, 0);
        String nombreUsuario = (String) view.getTablaUsuarios().getValueAt(fila, 1);

        // Prevenir eliminación del admin principal
        if ("admin".equals(nombreUsuario)) {
            JOptionPane.showMessageDialog(view, 
                "No se puede eliminar el usuario administrador principal",
                "Operación no permitida",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(view,
            "¿Está seguro que desea eliminar al usuario " + nombreUsuario + "?",
            "Confirmar Eliminación",
            JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                service.eliminarUsuario(id);
                JOptionPane.showMessageDialog(view, "Usuario eliminado con éxito");
                cargarUsuarios();
                view.limpiarCampos();
                logger.info("Usuario eliminado exitosamente");
            } catch (Exception e) {
                logger.error("Error al eliminar usuario", e);
                JOptionPane.showMessageDialog(view, "Error al eliminar el usuario: " + e.getMessage());
            }
        }
    }
    
    private void limpiarFormulario() {
        view.limpiarCampos();
    }
    
    private void buscarUsuarios() {
        try {
            String terminoBusqueda = view.getTxtBuscar().getText().trim();
            if (!terminoBusqueda.isEmpty()) {
                Usuarios usuario = service.buscarPorNombre(terminoBusqueda);
                if (usuario != null) {
                    List<Usuarios> resultados = List.of(usuario);
                    view.actualizarTabla(resultados);
                } else {
                    JOptionPane.showMessageDialog(view, 
                        "No se encontró ningún usuario con el nombre: " + terminoBusqueda,
                        "No encontrado",
                        JOptionPane.INFORMATION_MESSAGE);
                    cargarUsuarios();
                }
            } else {
                cargarUsuarios();
            }
        } catch (Exception e) {
            logger.error("Error al buscar usuarios", e);
            JOptionPane.showMessageDialog(view, "Error al realizar la búsqueda: " + e.getMessage());
        }
    }
}
