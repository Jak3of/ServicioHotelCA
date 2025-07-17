/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.avanceproyecto.view;

import javax.swing.*;
import java.awt.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import com.mycompany.avanceproyecto.model.Facturas;
import com.mycompany.avanceproyecto.model.Alojamientos;
import com.mycompany.avanceproyecto.controller.FacturaController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Pablo Tello
 */
public class Factura extends JInternalFrame {
    private static final Logger logger = LoggerFactory.getLogger(Factura.class);
    private FacturaController controller;

    /**
     * Creates new form Factura
     */
    public Factura() {
        initComponents();
        setupFrame();
        setupForm();
        this.controller = new FacturaController(this);
    }

    private void setupFrame() {
        setTitle("Gestión de Facturas");
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setSize(1353, 528);
        setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void setupForm() {
        // Configurar tabla de alojamientos del cliente (reemplaza tabla de pagos)
        String[] columnasAloj = {"ID", "Habitación", "Fecha Entrada", "Fecha Salida", "Precio Hab.", "Estado"};
        DefaultTableModel modeloAloj = new DefaultTableModel(columnasAloj, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablalistadopago.setModel(modeloAloj);
        tablalistadopago.getTableHeader().setReorderingAllowed(false);

        // Configurar tabla de consumos
        String[] columnasConsumo = {"Servicio", "Cantidad", "Precio Unit.", "Total"};
        DefaultTableModel modeloConsumo = new DefaultTableModel(columnasConsumo, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablalistadoconsumo.setModel(modeloConsumo);
        tablalistadoconsumo.getTableHeader().setReorderingAllowed(false);

        // Cambiar títulos de paneles
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("ALOJAMIENTOS DEL CLIENTE"));
        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("CONSUMOS DEL ALOJAMIENTO"));

        // Configurar botones
        Color btnBackground = new Color(64, 123, 255);
        Color btnForeground = Color.WHITE;
        
        btnNuevo.setBackground(btnBackground);
        btnguardar.setBackground(btnBackground);
        btncancelar.setBackground(btnBackground);
        btneliminar.setBackground(btnBackground);
        btnsalir.setBackground(btnBackground);
        btsbuscar.setBackground(btnBackground);
        jButton1.setBackground(btnBackground);
        
        btnNuevo.setForeground(btnForeground);
        btnguardar.setForeground(btnForeground);
        btncancelar.setForeground(btnForeground);
        btneliminar.setForeground(btnForeground);
        btnsalir.setForeground(btnForeground);
        btsbuscar.setForeground(btnForeground);
        jButton1.setForeground(btnForeground);

        // Reconfigurar el botón de búsqueda como "Buscar Cliente"
        btsbuscar.setText("Listar por cliente");
        btsbuscar.setPreferredSize(new java.awt.Dimension(140, 25));
        
        // Agregar botón para imprimir (provisional)
        JButton btnImprimir = new JButton("IMPRIMIR");
        btnImprimir.setBackground(new Color(34, 139, 34));
        btnImprimir.setForeground(btnForeground);
        btnImprimir.setPreferredSize(new java.awt.Dimension(100, 25));
        btnImprimir.addActionListener(e -> imprimirFactura());
        
        // Configurar campos como no editables
        txttotalpago.setEditable(false);
        txttotalpago.setBackground(new Color(240, 240, 240));
        txtprecio.setEditable(false);
        txtprecio.setBackground(new Color(240, 240, 240));
        txthabitacion.setEditable(false);
        txthabitacion.setBackground(new Color(240, 240, 240));

        // Configurar eventos
        btnsalir.addActionListener(e -> dispose());
        
        // Listener para tabla de alojamientos - CLIC ÚNICO
        tablalistadopago.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = tablalistadopago.getSelectedRow();
                if (row != -1) {
                    cargarConsumosAlojamiento(row);
                }
            }
        });

        // Mejorar labels
        jLabel2.setText("CLIENTE SELECCIONADO:");
        jLabel7.setText("HABITACIÓN:");
        jLabel12.setText("TOTAL A PAGAR:");
        
        // Configurar fecha actual por defecto
        dcfechaemision.setDate(new java.util.Date());
    }

    private void imprimirFactura() {
        int filaAlojamiento = tablalistadopago.getSelectedRow();
        if (filaAlojamiento == -1) {
            JOptionPane.showMessageDialog(this, 
                "Por favor, seleccione un alojamiento para imprimir la factura",
                "Seleccionar Alojamiento", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Por ahora solo mostrar mensaje
        String cliente = txtreserva.getText();
        String habitacion = txthabitacion.getText();
        String total = txttotalpago.getText();
        
        String mensaje = String.format(
            "FACTURA PROVISIONAL\n\n" +
            "Cliente: %s\n" +
            "Habitación: %s\n" +
            "Total: S/ %s\n\n" +
            "Función de impresión será implementada próximamente",
            cliente, habitacion, total
        );
        
        JOptionPane.showMessageDialog(this, mensaje, 
                                     "Vista Previa - Factura", 
                                     JOptionPane.INFORMATION_MESSAGE);
    }

    private void buscarCliente() {
        // Implementar búsqueda de cliente
    }

    private void cargarConsumosAlojamiento(int filaAlojamiento) {
        try {
            int idAlojamiento = (Integer) tablalistadopago.getValueAt(filaAlojamiento, 0);
            // Delegar al controlador
            controller.cargarConsumosAlojamiento(filaAlojamiento);
        } catch (Exception e) {
            logger.error("Error al cargar consumos del alojamiento", e);
            JOptionPane.showMessageDialog(this, "Error al cargar consumos: " + e.getMessage());
        }
    }
    
    // Método para buscar alojamientos por DNI del cliente
    private void buscarAlojamientosPorDni() {
        try {
            String dniTexto = txtbuscar.getText().trim();
            
            if (dniTexto.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Por favor, ingrese un DNI para buscar.", 
                    "Campo vacío", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Validar que sea un número
            int dni;
            try {
                dni = Integer.parseInt(dniTexto);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, 
                    "El DNI debe ser un número válido.", 
                    "DNI inválido", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Delegar la búsqueda al controlador
            controller.buscarAlojamientosPorDni(dni);
            
        } catch (Exception e) {
            logger.error("Error al buscar alojamientos por DNI", e);
            JOptionPane.showMessageDialog(this, 
                "Error al buscar alojamientos: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Método para verificar si un alojamiento ya está pagado
    public boolean verificarEstadoAlojamiento(int filaAlojamiento) {
        if (filaAlojamiento == -1) {
            JOptionPane.showMessageDialog(this, 
                "Por favor, seleccione un alojamiento para procesar el pago",
                "Seleccionar Alojamiento", 
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        // Obtener el estado del alojamiento desde la tabla (columna 5)
        String estadoTexto = tablalistadopago.getValueAt(filaAlojamiento, 5).toString();
        
        // Verificar si ya está pagado
        if (estadoTexto.contains("PAGADO")) {
            String habitacion = tablalistadopago.getValueAt(filaAlojamiento, 1).toString();
            JOptionPane.showMessageDialog(this, 
                "El alojamiento en la habitación " + habitacion + " ya ha sido pagado.\n" +
                "No se puede procesar un pago duplicado.",
                "Alojamiento Ya Pagado", 
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        // Verificar si está finalizado
        if (estadoTexto.contains("FINALIZADO")) {
            String habitacion = tablalistadopago.getValueAt(filaAlojamiento, 1).toString();
            JOptionPane.showMessageDialog(this, 
                "El alojamiento en la habitación " + habitacion + " ya está finalizado.\n" +
                "No se puede procesar el pago de un alojamiento finalizado.",
                "Alojamiento Finalizado", 
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        return true; // Estado ACTIVO - se puede pagar
    }

    public void limpiarFormulario() {
        txtidServicio.setText("");
        txtidreserva.setText("");
        txtreserva.setText("");
        txthabitacion.setText("");
        txtprecio.setText("");
        txttotalpago.setText("");
        dcfechaemision.setDate(new java.util.Date()); // Restablecer fecha actual
        cbotipocomprobante.setSelectedIndex(0);
        tablalistadopago.clearSelection();
        ((DefaultTableModel) tablalistadoconsumo.getModel()).setRowCount(0);
        ((DefaultTableModel) tablalistadopago.getModel()).setRowCount(0);
    }

    public void actualizarTablaAlojamientos(List<Alojamientos> alojamientos) {
        DefaultTableModel modelo = (DefaultTableModel) tablalistadopago.getModel();
        modelo.setRowCount(0);
        
        for (Alojamientos a : alojamientos) {
            // Determinar el ícono del estado
            String estadoTexto = "";
            if (a.getEstado() != null) {
                switch (a.getEstado()) {
                    case ACTIVO:
                        estadoTexto = "ACTIVO";
                        break;
                    case PAGADO:
                        estadoTexto = "PAGADO";
                        break;
                    case FINALIZADO:
                        estadoTexto = "FINALIZADO";
                        break;
                    default:
                        estadoTexto = "ACTIVO";
                        break;
                }
            } else {
                estadoTexto = "ACTIVO"; // Estado por defecto si es null
            }
            
            modelo.addRow(new Object[]{
                a.getId(),
                a.getHabitacion().getNumero(),
                a.getFechaEntrada(),
                a.getFechaSalida(),
                a.getHabitacion().getPrecio(),
                estadoTexto
            });
        }
        
        lbltotalregistro.setText("Alojamientos: " + alojamientos.size());
    }

    public void actualizarTablaConsumos(List<com.mycompany.avanceproyecto.model.ConsumoServicio> consumos) {
        DefaultTableModel modelo = (DefaultTableModel) tablalistadoconsumo.getModel();
        modelo.setRowCount(0);
        
        double totalConsumos = 0.0;
        for (com.mycompany.avanceproyecto.model.ConsumoServicio c : consumos) {
            double totalItem = c.getCantidad() * c.getServicio().getPrecio();
            totalConsumos += totalItem;
            
            modelo.addRow(new Object[]{
                c.getServicio().getNombre(),
                c.getCantidad(),
                c.getServicio().getPrecio(),
                totalItem
            });
        }
        
        // Actualizar totales
        int filaSeleccionada = tablalistadopago.getSelectedRow();
        if (filaSeleccionada != -1) {
            double precioHabitacion = (Double) tablalistadopago.getValueAt(filaSeleccionada, 4);
            double total = precioHabitacion + totalConsumos;
            
            txtprecio.setText(String.valueOf(precioHabitacion));
            txttotalpago.setText(String.valueOf(total));
        }
        
        lbltotalregistro1.setText("Consumos: " + consumos.size());
    }

    public void actualizarTablaFacturas(List<Facturas> facturas) {
        DefaultTableModel modelo = (DefaultTableModel) tablalistadopago.getModel();
        modelo.setRowCount(0);
        
        for (Facturas f : facturas) {
            modelo.addRow(new Object[]{
                f.getId(),
                f.getFecha(),
                f.getCliente().getNombre(),
                "Aloj. " + f.getAlojamiento().getId(),
                f.getTotal()
            });
        }
        
        lbltotalregistro.setText("Total: " + facturas.size());
    }

    // Getters para el controlador
    public JTextField getTxtIdFactura() { return txtidServicio; }
    public JTextField getTxtIdAlojamiento() { return txtidreserva; }
    public JTextField getTxtCliente() { return txtreserva; }
    public JTextField getTxtHabitacion() { return txthabitacion; }
    public JTextField getTxtSubtotal() { return txtprecio; }
    public JTextField getTxtTotal() { return txttotalpago; }
    public JComboBox<String> getCboTipoComprobante() { return cbotipocomprobante; }
    public com.toedter.calendar.JDateChooser getFechaEmision() { return dcfechaemision; }
    public JTable getTablaFacturas() { return tablalistadopago; }
    public JTable getTablaConsumos() { return tablalistadoconsumo; }
    public JButton getBtnNuevo() { return btnNuevo; }
    public JButton getBtnGuardar() { return btnguardar; }
    public JButton getBtnEliminar() { return btneliminar; }
    public JButton getBtnBuscar() { return btsbuscar; }
    
    // Método público para que el controlador verifique el estado antes de procesar pago
    public boolean validarAlojamientoPago() {
        int filaSeleccionada = tablalistadopago.getSelectedRow();
        return verificarEstadoAlojamiento(filaSeleccionada);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtidreserva = new javax.swing.JTextField();
        cbotipocomprobante = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtprecio = new javax.swing.JTextField();
        btnNuevo = new javax.swing.JButton();
        btnguardar = new javax.swing.JButton();
        btncancelar = new javax.swing.JButton();
        txtidServicio = new javax.swing.JTextField();
        txtreserva = new javax.swing.JTextField();
        txthabitacion = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txttotalpago = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        dcfechaemision = new com.toedter.calendar.JDateChooser();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tablalistadopago = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        txtbuscar = new javax.swing.JTextField();
        btsbuscar = new javax.swing.JButton();
        btneliminar = new javax.swing.JButton();
        btnsalir = new javax.swing.JButton();
        lbltotalregistro = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tablalistadoconsumo = new javax.swing.JTable();
        lbltotalregistro1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("PAGO");

        jPanel3.setBackground(new java.awt.Color(255, 204, 204));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("REGISTRO DE PAGO"));

        jLabel2.setText("CLIENTE:");

        txtidreserva.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtidreservaActionPerformed(evt);
            }
        });

        cbotipocomprobante.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "BOLETA", "FACTURA", "TICKET", "OTROS" }));

        jLabel7.setText("ALOJAMIENTO:");

        jLabel6.setText("TIPO COMPROBANTE:");

        txtprecio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtprecioActionPerformed(evt);
            }
        });

        btnNuevo.setText("NUEVO");

        btnguardar.setText("PAGAR");

        btncancelar.setText("CANCELAR");
        btncancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btncancelarActionPerformed(evt);
            }
        });

        txtidServicio.setEditable(false);
        txtidServicio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtidServicioActionPerformed(evt);
            }
        });

        txtreserva.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtreservaActionPerformed(evt);
            }
        });

        txthabitacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txthabitacionActionPerformed(evt);
            }
        });

        jLabel12.setText("TOTAL PAGO:");

        txttotalpago.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txttotalpagoActionPerformed(evt);
            }
        });

        jLabel13.setText("FECHA EMISION:");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(btnNuevo)
                        .addGap(70, 70, 70)
                        .addComponent(btnguardar)
                        .addGap(78, 78, 78)
                        .addComponent(btncancelar))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(181, 181, 181)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtidServicio, javax.swing.GroupLayout.PREFERRED_SIZE, 302, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(83, 83, 83)
                                .addComponent(txthabitacion, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(42, 42, 42)
                        .addComponent(dcfechaemision, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(34, 34, 34)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txttotalpago, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(txtidreserva, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtreserva, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtprecio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbotipocomprobante, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(36, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(txtidServicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dcfechaemision, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbotipocomprobante, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtidreserva, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtreserva, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(19, 19, 19)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txthabitacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtprecio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txttotalpago, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnNuevo)
                    .addComponent(btnguardar)
                    .addComponent(btncancelar))
                .addContainerGap(23, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("LISTADO DE ALOJAMIENTOS"));

        tablalistadopago.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane3.setViewportView(tablalistadopago);

        jLabel3.setText("BUSCAR");

        btsbuscar.setText("Listar por Cliente");
        btsbuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btsbuscarActionPerformed(evt);
            }
        });

        btneliminar.setText("Eliminar");

        btnsalir.setText("Salir");

        lbltotalregistro.setText("REGISTRO");

        jButton1.setText("Buscar por DNI");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 607, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtbuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btsbuscar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btneliminar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnsalir)))
                .addContainerGap(32, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lbltotalregistro, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(123, 123, 123))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtbuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btsbuscar)
                    .addComponent(btneliminar)
                    .addComponent(btnsalir)
                    .addComponent(jButton1))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbltotalregistro)
                .addContainerGap(50, Short.MAX_VALUE))
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("LISTADO DE CONSUMOS"));

        tablalistadoconsumo.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane4.setViewportView(tablalistadoconsumo);

        lbltotalregistro1.setText("REGISTRO");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap(24, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 607, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addComponent(lbltotalregistro1, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(124, 124, 124))))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbltotalregistro1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(503, 503, 503)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtidreservaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtidreservaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtidreservaActionPerformed

    private void txtprecioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtprecioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtprecioActionPerformed

    private void btncancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btncancelarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btncancelarActionPerformed

    private void txtidServicioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtidServicioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtidServicioActionPerformed

    private void btsbuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btsbuscarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btsbuscarActionPerformed

    private void txtreservaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtreservaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtreservaActionPerformed

    private void txthabitacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txthabitacionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txthabitacionActionPerformed

    private void txttotalpagoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txttotalpagoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txttotalpagoActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // Buscar alojamientos por DNI del cliente
        buscarAlojamientosPorDni();
    }//GEN-LAST:event_jButton1ActionPerformed

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
            java.util.logging.Logger.getLogger(Factura.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Factura.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Factura.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Factura.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Factura().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnNuevo;
    private javax.swing.JButton btncancelar;
    private javax.swing.JButton btneliminar;
    private javax.swing.JButton btnguardar;
    private javax.swing.JButton btnsalir;
    private javax.swing.JButton btsbuscar;
    private javax.swing.JComboBox<String> cbotipocomprobante;
    private com.toedter.calendar.JDateChooser dcfechaemision;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JLabel lbltotalregistro;
    private javax.swing.JLabel lbltotalregistro1;
    private javax.swing.JTable tablalistadoconsumo;
    private javax.swing.JTable tablalistadopago;
    private javax.swing.JTextField txtbuscar;
    private javax.swing.JTextField txthabitacion;
    private javax.swing.JTextField txtidServicio;
    private javax.swing.JTextField txtidreserva;
    private javax.swing.JTextField txtprecio;
    private javax.swing.JTextField txtreserva;
    private javax.swing.JTextField txttotalpago;
    // End of variables declaration//GEN-END:variables
}
