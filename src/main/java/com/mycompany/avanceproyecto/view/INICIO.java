/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/MDIApplication.java to edit this template
 */
package com.mycompany.avanceproyecto.view;

import com.mycompany.avanceproyecto.util.SessionManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Pablo Tello
 */
public class INICIO extends javax.swing.JFrame {
    private static final Logger logger = LoggerFactory.getLogger(INICIO.class);
    private JInternalFrame frameDashboard; // Referencia al dashboard para poder reposicionarlo
    private JPanel barraMinimizadas; // Barra moderna para ventanas minimizadas
    
    // Referencias a las ventanas internas para evitar duplicados
    private Alojamiento ventanaAlojamiento;
    private Cliente ventanaCliente;
    private Factura ventanaFactura;
    private ConsumoServicio ventanaConsumo;
    private Habitacion ventanaHabitacion;
    private Servicio ventanaServicio;
    private Usuario ventanaUsuario;

    /**
     * Creates new form INICIO
     */
    public INICIO() {
        initComponents();
        // Configurar tamaño de ventana 720p y centrar
        setSize(1280, 720);
        setLocationRelativeTo(null); // Centrar en pantalla
        
        // Configurar el layout para que el desktopPane ocupe todo el espacio
        getContentPane().setLayout(new BorderLayout());
        
        // Crear barra de tareas moderna para ventanas minimizadas
        crearBarraMinimizadas();
        
        getContentPane().add(desktopPane, BorderLayout.CENTER);
        getContentPane().add(barraMinimizadas, BorderLayout.SOUTH);
        
        // Dar color de fondo al desktopPane (opcional)
        desktopPane.setBackground(new Color(240, 240, 240));
        
        // Configurar permisos según el rol del usuario
        configurarPermisosPorRol();
        
        // Agregar panel de accesos rápidos
        agregarPanelAccesosRapidos();
        
        // Agregar listener para redimensionar el dashboard cuando cambie el tamaño de la ventana
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent evt) {
                reposicionarDashboard();
            }
        });
    }
    
    private void crearBarraMinimizadas() {
        barraMinimizadas = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        barraMinimizadas.setBackground(new Color(45, 45, 48)); // Color moderno oscuro
        barraMinimizadas.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(60, 60, 60)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        barraMinimizadas.setPreferredSize(new Dimension(0, 50));
        barraMinimizadas.setVisible(false); // Inicialmente oculta
    }
    
    private void agregarVentanaMinimizada(JInternalFrame ventana) {
        // Verificar que la ventana no esté ya en la barra
        Component[] componentes = barraMinimizadas.getComponents();
        for (Component comp : componentes) {
            if (comp instanceof JButton) {
                JButton boton = (JButton) comp;
                String tituloBoton = boton.getText().replace("■ ", "");
                if (tituloBoton.equals(ventana.getTitle())) {
                    logger.debug("Ventana {} ya está en la barra de tareas", ventana.getTitle());
                    return; // Ya existe, no agregar duplicado
                }
            }
        }
        
        // Crear botón moderno para la ventana minimizada
        JButton botonVentana = new JButton();
        String titulo = ventana.getTitle().isEmpty() ? "Ventana" : ventana.getTitle();
        botonVentana.setText("■ " + titulo);
        botonVentana.setPreferredSize(new Dimension(180, 35));
        botonVentana.setBackground(new Color(70, 130, 180));
        botonVentana.setForeground(Color.WHITE);
        botonVentana.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        botonVentana.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 149, 237), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        botonVentana.setFocusPainted(false);
        botonVentana.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Efecto hover
        botonVentana.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                botonVentana.setBackground(new Color(100, 149, 237));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                botonVentana.setBackground(new Color(70, 130, 180));
            }
        });
        
        // Acción para restaurar ventana
        botonVentana.addActionListener(e -> {
            try {
                // Restaurar ventana desde nuestro sistema moderno
                ventana.setVisible(true);
                ventana.setSelected(true);
                ventana.toFront();
                barraMinimizadas.remove(botonVentana);
                actualizarBarraMinimizadas();
                logger.info("Ventana restaurada desde barra de tareas: {}", titulo);
            } catch (java.beans.PropertyVetoException ex) {
                logger.warn("Error al restaurar ventana desde barra", ex);
            }
        });
        
        barraMinimizadas.add(botonVentana);
        actualizarBarraMinimizadas();
    }
    
    private void actualizarBarraMinimizadas() {
        boolean hayVentanas = barraMinimizadas.getComponentCount() > 0;
        barraMinimizadas.setVisible(hayVentanas);
        barraMinimizadas.revalidate();
        barraMinimizadas.repaint();
    }
    
    private void removerVentanaMinimizada(JInternalFrame ventana) {
        // Buscar y remover el botón correspondiente a esta ventana
        Component[] componentes = barraMinimizadas.getComponents();
        for (Component comp : componentes) {
            if (comp instanceof JButton) {
                JButton boton = (JButton) comp;
                String tituloBoton = boton.getText().replace("■ ", "");
                if (tituloBoton.equals(ventana.getTitle())) {
                    barraMinimizadas.remove(boton);
                    actualizarBarraMinimizadas();
                    logger.debug("Botón removido de barra de tareas: {}", ventana.getTitle());
                    break;
                }
            }
        }
    }
    
    private void agregarPanelAccesosRapidos() {
        // Esperar a que el desktopPane esté completamente inicializado
        SwingUtilities.invokeLater(() -> {
            // Crear panel principal centrado
            JPanel panelPrincipal = new JPanel(new GridBagLayout());
            panelPrincipal.setBackground(Color.WHITE);
            panelPrincipal.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(40, 40, 40, 40)
            ));
            
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(20, 20, 20, 20);
            
            // Título más profesional
            JLabel titulo = new JLabel("PANEL DE CONTROL");
            titulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
            titulo.setForeground(new Color(60, 60, 60));
            gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
            gbc.anchor = GridBagConstraints.CENTER;
            panelPrincipal.add(titulo, gbc);
            
            // Subtítulo
            JLabel subtitulo = new JLabel("Accesos Rápidos del Sistema");
            subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            subtitulo.setForeground(new Color(120, 120, 120));
            gbc.gridy = 1;
            gbc.insets = new Insets(0, 20, 30, 20);
            panelPrincipal.add(subtitulo, gbc);
            
            // Configurar grid según el rol
            boolean esAdmin = !SessionManager.esRecepcionista();
            
            // Botones principales con colores profesionales
            gbc.gridwidth = 1;
            gbc.fill = GridBagConstraints.BOTH;
            gbc.weightx = 1.0; gbc.weighty = 1.0;
            gbc.insets = new Insets(15, 15, 15, 15);
            
            // Fila 1 - Funciones principales (disponibles para todos)
            JButton btnAlojamiento = crearBotonRapido("🏨 ALOJAMIENTOS", "Alt+1", new Color(70, 130, 180));
            gbc.gridx = 0; gbc.gridy = 2;
            panelPrincipal.add(btnAlojamiento, gbc);
            
            JButton btnClientes = crearBotonRapido("👥 CLIENTES", "Alt+2", new Color(95, 158, 160));
            gbc.gridx = 1; gbc.gridy = 2;
            panelPrincipal.add(btnClientes, gbc);
            
            // Fila 2 - Colores profesionales verde/marrón
            JButton btnPagos = crearBotonRapido("💰 PAGOS", "Alt+3", new Color(72, 133, 117));
            gbc.gridx = 0; gbc.gridy = 3;
            panelPrincipal.add(btnPagos, gbc);
            
            JButton btnConsumos = crearBotonRapido("🍽️ CONSUMOS", "Alt+4", new Color(139, 115, 85));
            gbc.gridx = 1; gbc.gridy = 3;
            panelPrincipal.add(btnConsumos, gbc);
            
            // Eventos de botones
            btnAlojamiento.addActionListener(e -> abrirAlojamientos());
            btnClientes.addActionListener(e -> abrirClientes());
            btnPagos.addActionListener(e -> abrirPagos());
            btnConsumos.addActionListener(e -> abrirConsumos());
            
            // Crear JInternalFrame sin bordes y centrado correctamente
            frameDashboard = new JInternalFrame("", false, false, false, false);
            frameDashboard.setBorder(null);
            frameDashboard.add(panelPrincipal);
            
            // Tamaño responsivo basado en el tamaño del desktopPane
            calcularYAplicarTamanoDashboard();
            
            frameDashboard.setVisible(true);
            desktopPane.add(frameDashboard);
            
            // Configurar atajos de teclado
            configurarAtajosTeclado();
        });
    }
    
    private void calcularYAplicarTamanoDashboard() {
        if (frameDashboard == null) return;
        
        // Tamaño optimizado para 2x2 grid (ahora todos tienen el mismo tamaño)
        int desktopWidth = desktopPane.getWidth();
        int desktopHeight = desktopPane.getHeight();
        
        // Tamaño fijo optimizado para el dashboard 2x2
        double widthFactor = 0.6; // Tamaño estándar para 2x2
        double heightFactor = 0.7;
        
        int dashboardWidth = Math.max(600, Math.min(900, (int)(desktopWidth * widthFactor)));
        int dashboardHeight = Math.max(400, Math.min(600, (int)(desktopHeight * heightFactor)));
        
        frameDashboard.setSize(dashboardWidth, dashboardHeight);
        
        // Centrar en el desktopPane
        int x = Math.max(0, (desktopWidth - dashboardWidth) / 2);
        int y = Math.max(0, (desktopHeight - dashboardHeight) / 2);
        frameDashboard.setLocation(x, y);
    }
    
    private void reposicionarDashboard() {
        SwingUtilities.invokeLater(() -> {
            calcularYAplicarTamanoDashboard();
        });
    }
    
    private JButton crearBotonRapido(String texto, String atajo, Color color) {
        JButton boton = new JButton("<html><center><div style='margin: 10px;'>" + texto + "<br><small style='color: rgba(255,255,255,0.8);'>" + atajo + "</small></div></center></html>");
        boton.setPreferredSize(new Dimension(250, 120));
        boton.setBackground(color);
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        boton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color.darker(), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Efecto hover más suave y profesional
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(color.brighter());
                boton.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(color.brighter().darker(), 2),
                    BorderFactory.createEmptyBorder(14, 14, 14, 14)
                ));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(color);
                boton.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(color.darker(), 1),
                    BorderFactory.createEmptyBorder(15, 15, 15, 15)
                ));
            }
        });
        
        return boton;
    }
    
    private void configurarAtajosTeclado() {
        // Alt+1 = Alojamientos (toggle)
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            KeyStroke.getKeyStroke(KeyEvent.VK_1, KeyEvent.ALT_DOWN_MASK), "abrirAlojamientos");
        getRootPane().getActionMap().put("abrirAlojamientos", new AbstractAction() {
            public void actionPerformed(ActionEvent e) { abrirAlojamientos(); }
        });
        
        // Alt+2 = Clientes (toggle)
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            KeyStroke.getKeyStroke(KeyEvent.VK_2, KeyEvent.ALT_DOWN_MASK), "abrirClientes");
        getRootPane().getActionMap().put("abrirClientes", new AbstractAction() {
            public void actionPerformed(ActionEvent e) { abrirClientes(); }
        });
        
        // Alt+3 = Pagos (toggle)
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            KeyStroke.getKeyStroke(KeyEvent.VK_3, KeyEvent.ALT_DOWN_MASK), "abrirPagos");
        getRootPane().getActionMap().put("abrirPagos", new AbstractAction() {
            public void actionPerformed(ActionEvent e) { abrirPagos(); }
        });
        
        // Alt+4 = Consumos (toggle)
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            KeyStroke.getKeyStroke(KeyEvent.VK_4, KeyEvent.ALT_DOWN_MASK), "abrirConsumos");
        getRootPane().getActionMap().put("abrirConsumos", new AbstractAction() {
            public void actionPerformed(ActionEvent e) { abrirConsumos(); }
        });
        
        // Atajos adicionales solo para administradores
        if (!SessionManager.esRecepcionista()) {
            // Alt+H = Habitaciones (solo admin)
            getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_H, KeyEvent.ALT_DOWN_MASK), "abrirHabitaciones");
            getRootPane().getActionMap().put("abrirHabitaciones", new AbstractAction() {
                public void actionPerformed(ActionEvent e) { abrirHabitaciones(); }
            });
            
            // Alt+S = Servicios (solo admin)
            getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.ALT_DOWN_MASK), "abrirServicios");
            getRootPane().getActionMap().put("abrirServicios", new AbstractAction() {
                public void actionPerformed(ActionEvent e) { abrirServicios(); }
            });
            
            // Alt+U = Usuarios (solo admin)
            getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_U, KeyEvent.ALT_DOWN_MASK), "abrirUsuarios");
            getRootPane().getActionMap().put("abrirUsuarios", new AbstractAction() {
                public void actionPerformed(ActionEvent e) { abrirUsuarios(); }
            });
        }
        
        // ESC = Minimizar ventana activa (disponible para todos)
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "minimizarVentanaActiva");
        getRootPane().getActionMap().put("minimizarVentanaActiva", new AbstractAction() {
            public void actionPerformed(ActionEvent e) { minimizarVentanaActiva(); }
        });
        
        // Agregar también al desktopPane para capturar ESC desde ventanas internas
        desktopPane.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(
            KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "minimizarVentanaActivaDesktop");
        desktopPane.getActionMap().put("minimizarVentanaActivaDesktop", new AbstractAction() {
            public void actionPerformed(ActionEvent e) { minimizarVentanaActiva(); }
        });
        
        // Listener adicional para capturar ESC globalmente
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE && e.getID() == KeyEvent.KEY_PRESSED) {
                    minimizarVentanaActiva();
                    return true; // Consumir el evento
                }
                return false;
            }
        });
    }
    
    private <T extends JInternalFrame> T abrirVentanaUnica(T ventanaExistente, String nombreVentana, java.util.function.Supplier<T> constructor) {
        // Si la ventana ya existe y está visible, la traemos al frente
        if (ventanaExistente != null && !ventanaExistente.isClosed()) {
            try {
                ventanaExistente.setSelected(true);
                ventanaExistente.toFront();
                logger.info("Ventana {} ya estaba abierta, trayendo al frente", nombreVentana);
                return ventanaExistente;
            } catch (java.beans.PropertyVetoException ex) {
                logger.warn("No se pudo seleccionar la ventana {}", nombreVentana, ex);
            }
        } else {
            // Si no existe o está cerrada, crear nueva ventana
            try {
                logger.info("Abriendo nueva ventana {}", nombreVentana);
                T nuevaVentana = constructor.get();
                nuevaVentana.setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
                desktopPane.add(nuevaVentana);
                nuevaVentana.setVisible(true);
                nuevaVentana.setSelected(true);
                return nuevaVentana;
            } catch (Exception ex) {
                logger.error("Error al abrir ventana {}", nombreVentana, ex);
                JOptionPane.showMessageDialog(this, "Error al abrir " + nombreVentana + ": " + ex.getMessage());
                return null;
            }
        }
        return ventanaExistente;
    }
    
    private <T extends JInternalFrame> T toggleVentana(T ventanaExistente, String nombreVentana, java.util.function.Supplier<T> constructor) {
        // Si la ventana ya existe y está visible, la cerramos
        if (ventanaExistente != null && !ventanaExistente.isClosed() && ventanaExistente.isVisible()) {
            try {
                logger.info("Cerrando ventana {} (toggle)", nombreVentana);
                ventanaExistente.dispose();
                return null;
            } catch (Exception ex) {
                logger.warn("Error al cerrar ventana {}", nombreVentana, ex);
            }
        } else if (ventanaExistente != null && !ventanaExistente.isClosed() && !ventanaExistente.isVisible()) {
            // Si la ventana existe pero está minimizada (invisible), la restauramos
            try {
                logger.info("Restaurando ventana minimizada {} (toggle)", nombreVentana);
                ventanaExistente.setVisible(true);
                ventanaExistente.setSelected(true);
                ventanaExistente.toFront();
                // Remover de la barra de minimizadas si está ahí
                removerVentanaMinimizada(ventanaExistente);
                return ventanaExistente;
            } catch (Exception ex) {
                logger.warn("Error al restaurar ventana {}", nombreVentana, ex);
            }
        } else {
            // Si no existe o está cerrada, crear nueva ventana
            try {
                logger.info("Abriendo nueva ventana {} (toggle)", nombreVentana);
                T nuevaVentana = constructor.get();
                nuevaVentana.setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
                
                // Deshabilitar minimizado tradicional - usar solo nuestro sistema moderno
                nuevaVentana.setIconifiable(false);
                
                desktopPane.add(nuevaVentana);
                nuevaVentana.setVisible(true);
                nuevaVentana.setSelected(true);
                return nuevaVentana;
            } catch (Exception ex) {
                logger.error("Error al abrir ventana {}", nombreVentana, ex);
                JOptionPane.showMessageDialog(this, "Error al abrir " + nombreVentana + ": " + ex.getMessage());
                return null;
            }
        }
        return ventanaExistente;
    }
    
    // Métodos para abrir ventanas (actualizados para evitar duplicados y toggle)
    private void abrirAlojamientos() {
        ventanaAlojamiento = toggleVentana(ventanaAlojamiento, "Alojamientos", Alojamiento::new);
    }
    
    private void abrirClientes() {
        ventanaCliente = toggleVentana(ventanaCliente, "Clientes", Cliente::new);
    }
    
    private void abrirPagos() {
        ventanaFactura = toggleVentana(ventanaFactura, "Facturas", Factura::new);
    }
    
    private void abrirConsumos() {
        ventanaConsumo = toggleVentana(ventanaConsumo, "Consumo de Servicios", ConsumoServicio::new);
    }
    
    private void abrirHabitaciones() {
        // Verificar permisos - solo administradores pueden acceder a habitaciones
        if (SessionManager.esRecepcionista()) {
            JOptionPane.showMessageDialog(this, 
                "No tiene permisos para acceder a la gestión de habitaciones",
                "Acceso Denegado",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        ventanaHabitacion = toggleVentana(ventanaHabitacion, "Habitaciones", Habitacion::new);
    }
    
    private void abrirServicios() {
        // Verificar permisos - solo administradores pueden acceder a servicios
        if (SessionManager.esRecepcionista()) {
            JOptionPane.showMessageDialog(this, 
                "No tiene permisos para acceder a la gestión de servicios",
                "Acceso Denegado",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        ventanaServicio = toggleVentana(ventanaServicio, "Servicios", Servicio::new);
    }
    
    private void abrirUsuarios() {
        // Verificar permisos - solo administradores pueden acceder a usuarios
        if (SessionManager.esRecepcionista()) {
            JOptionPane.showMessageDialog(this, 
                "No tiene permisos para acceder a esta sección",
                "Acceso Denegado",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        ventanaUsuario = toggleVentana(ventanaUsuario, "Usuarios", Usuario::new);
    }
    
    private void minimizarVentanaActiva() {
        try {
            JInternalFrame ventanaSeleccionada = desktopPane.getSelectedFrame();
            if (ventanaSeleccionada != null && ventanaSeleccionada.isVisible()) {
                // NO minimizar el dashboard - debe quedarse siempre visible
                if (ventanaSeleccionada == frameDashboard) {
                    logger.debug("ESC presionado en dashboard - ignorando (el dashboard debe permanecer visible)");
                    return;
                }
                
                // Usar nuestro sistema moderno: agregar a barra y ocultar
                agregarVentanaMinimizada(ventanaSeleccionada);
                ventanaSeleccionada.setVisible(false);
                logger.info("Ventana minimizada con ESC: {}", ventanaSeleccionada.getTitle());
            } else {
                logger.debug("No hay ventana activa para minimizar o ya está minimizada");
            }
        } catch (Exception ex) {
            logger.warn("Error al minimizar ventana con ESC", ex);
        }
    }
    
    private void configurarPermisosPorRol() {
        // Si es recepcionista, ocultar menú de configuraciones
        if (SessionManager.esRecepcionista()) {
            jMenu2.setVisible(false); // Ocultar menú "CONFIGURACIONES"
            editMenu.setVisible(false); // Ocultar menú "ARCHIVO"
            logger.info("Menú ARCHIVO Y CONFIGURACIONES oculto para usuario RECEPCIONISTA");
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

        desktopPane = new javax.swing.JDesktopPane();
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        editMenu = new javax.swing.JMenu();
        cutMenuItem = new javax.swing.JMenuItem();
        copyMenuItem = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        contentMenuItem = new javax.swing.JMenuItem();
        alojamientoMenuItem = new javax.swing.JMenuItem(); // Nuevo item para alojamiento
        aboutMenuItem = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        fileMenu.setMnemonic('f');
        fileMenu.setText("SISRESERVA");
        menuBar.add(fileMenu);

        editMenu.setMnemonic('e');
        editMenu.setText("ARCHIVO");

        cutMenuItem.setMnemonic('t');
        cutMenuItem.setText("HABITACIONES");
        editMenu.add(cutMenuItem);

        copyMenuItem.setMnemonic('y');
        copyMenuItem.setText("SERVICIO");
        editMenu.add(copyMenuItem);

        menuBar.add(editMenu);

        helpMenu.setMnemonic('h');
        helpMenu.setText("RESERVA");

        contentMenuItem.setMnemonic('c');
        contentMenuItem.setText("CONSUMO DE SERVICIOS"); // Cambiar nombre
        helpMenu.add(contentMenuItem);

        alojamientoMenuItem.setMnemonic('l');
        alojamientoMenuItem.setText("ALOJAMIENTOS"); // Nuevo item
        helpMenu.add(alojamientoMenuItem);

        aboutMenuItem.setMnemonic('a');
        aboutMenuItem.setText("CLIENTES");
        helpMenu.add(aboutMenuItem);

        jMenuItem1.setText("PAGOS");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        helpMenu.add(jMenuItem1);

        menuBar.add(helpMenu);

        jMenu1.setText("CONSULTAS");
        menuBar.add(jMenu1);

        jMenu2.setText("CONFIGURACIONES");

        jMenuItem2.setText("USUARIOS Y ACCESOS");
        jMenu2.add(jMenuItem2);

        menuBar.add(jMenu2);

        jMenu3.setText("SALIR");
        menuBar.add(jMenu3);

        setJMenuBar(menuBar);

        // Configurar eventos del menú
        // Menú Archivo
        cutMenuItem.addActionListener(e -> {
            abrirHabitaciones();
        });

        copyMenuItem.addActionListener(e -> {
            abrirServicios();
        });

        // Menú Reserva
        contentMenuItem.addActionListener(e -> {
            abrirConsumos();
        });

        alojamientoMenuItem.addActionListener(e -> {
            abrirAlojamientos();
        });

        aboutMenuItem.addActionListener(e -> {
            abrirClientes();
        });

        jMenuItem1.addActionListener(e -> {
            abrirPagos();
        });

        // Menú Configuraciones
        jMenuItem2.addActionListener(e -> {
            abrirUsuarios();
        });

        // Menú Salir
        jMenu3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int option = JOptionPane.showConfirmDialog(null, 
                    "¿Desea salir del sistema?",
                    "Confirmar salida",
                    JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(INICIO.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(INICIO.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(INICIO.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(INICIO.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new INICIO().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem aboutMenuItem;
    private javax.swing.JMenuItem contentMenuItem;
    private javax.swing.JMenuItem alojamientoMenuItem; // Agregar nueva variable
    private javax.swing.JMenuItem copyMenuItem;
    private javax.swing.JMenuItem cutMenuItem;
    private javax.swing.JDesktopPane desktopPane;
    private javax.swing.JMenu editMenu;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuBar menuBar;
    // End of variables declaration//GEN-END:variables

}
