package com.mycompany.avanceproyecto.util;

import com.mycompany.avanceproyecto.model.Usuarios;

public class SessionManager {
    private static Usuarios usuarioActual;
    
    public static void setUsuarioActual(Usuarios usuario) {
        usuarioActual = usuario;
    }
    
    public static Usuarios getUsuarioActual() {
        return usuarioActual;
    }
    
    public static boolean esAdmin() {
        return usuarioActual != null && "ADMIN".equals(usuarioActual.getRol());
    }
    
    public static boolean esRecepcionista() {
        return usuarioActual != null && "RECEPCIONISTA".equals(usuarioActual.getRol());
    }
    
    public static void cerrarSesion() {
        usuarioActual = null;
    }
}
