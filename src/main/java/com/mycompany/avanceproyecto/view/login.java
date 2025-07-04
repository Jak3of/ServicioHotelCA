/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.avanceproyecto.view;

import com.google.common.base.Preconditions;
import com.mycompany.avanceproyecto.controller.LoginController;
import javax.swing.*;
import java.awt.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Nicolas
 */
public class login extends javax.swing.JFrame {
    private static final Logger logger = LoggerFactory.getLogger(login.class);
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private LoginController controller;
    
    /**
     * Creates new form login
     */
    public login() {
        initComponents();
        setupLoginForm();
    }
    
    private void setupLoginForm() {
        // Inicializar controlador
        this.controller = new LoginController(this);
        
        // Configurar ventana
        setTitle("Login - Hotel Casa Andina");
        setSize(400, 300);
        setLocationRelativeTo(null);
        
        // Limpiar el contenido existente
        getContentPane().removeAll();
        
        // Crear panel principal con GridBagLayout
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        // Configurar componentes
        txtUsername = new JTextField(20);
        txtPassword = new JPasswordField(20);
        btnLogin = new JButton("Iniciar Sesión");
        
        // Agregar Key Listeners para Enter en ambos campos
        txtUsername.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
                    handleLogin();
                }
            }
        });
        
        txtPassword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
                    handleLogin();
                }
            }
        });
        
        // Agregar componentes al panel
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0; gbc.gridy = 0;
        mainPanel.add(new JLabel("Usuario:"), gbc);
        
        gbc.gridx = 1;
        mainPanel.add(txtUsername, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        mainPanel.add(new JLabel("Contraseña:"), gbc);
        
        gbc.gridx = 1;
        mainPanel.add(txtPassword, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        mainPanel.add(btnLogin, gbc);
        
        // Agregar acción al botón
        btnLogin.addActionListener(e -> handleLogin());
        
        // Agregar panel al frame y actualizar
        setContentPane(mainPanel);
        revalidate();
        repaint();
        
        // Establecer el foco en el campo de usuario al abrir
        SwingUtilities.invokeLater(() -> txtUsername.requestFocusInWindow());
    }
    
    private void handleLogin() {
        try {
            String username = txtUsername.getText();
            String password = new String(txtPassword.getPassword());
            
            // Validar campos usando Guava
            Preconditions.checkArgument(!username.isEmpty(), "El usuario no puede estar vacío");
            Preconditions.checkArgument(!password.isEmpty(), "La contraseña no puede estar vacía");
            
            if(controller.autenticar(username, password)) {
                logger.info("Login exitoso para usuario: {}", username);
                // Eliminar el mensaje de bienvenida
                // JOptionPane.showMessageDialog(this, "Bienvenido al sistema");
                
                // Abrir ventana de inicio y cerrar login directamente
                new INICIO().setVisible(true);
                this.dispose();
            } else {
                logger.warn("Intento de login fallido para usuario: {}", username);
                JOptionPane.showMessageDialog(this, "Credenciales incorrectas",
                    "Error", JOptionPane.ERROR_MESSAGE);
                
                // Limpiar campos y enfocar usuario después de error
                txtPassword.setText("");
                txtUsername.requestFocusInWindow();
            }
        } catch (IllegalArgumentException e) {
            logger.error("Error de validación", e);
            JOptionPane.showMessageDialog(this, e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
            
            // Enfocar el campo apropiado según el error
            if (txtUsername.getText().isEmpty()) {
                txtUsername.requestFocusInWindow();
            } else {
                txtPassword.requestFocusInWindow();
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
