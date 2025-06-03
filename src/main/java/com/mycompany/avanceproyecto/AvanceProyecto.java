/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.avanceproyecto;

import com.mycompany.avanceproyecto.config.DatabaseInitializer;
import com.mycompany.avanceproyecto.view.login;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author LAB-USR-LSUR
 */
public class AvanceProyecto {
    private static final Logger logger = LoggerFactory.getLogger(AvanceProyecto.class);

    public static void main(String[] args) {
        logger.info("Iniciando aplicación...");
        
        // Inicializar base de datos
        DatabaseInitializer.initializeDatabase();
        
        // Iniciar interfaz gráfica
        java.awt.EventQueue.invokeLater(() -> {
            new login().setVisible(true);
        });
    }
}
