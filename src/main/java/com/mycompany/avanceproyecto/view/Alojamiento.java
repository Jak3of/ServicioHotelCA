/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.avanceproyecto.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

import com.mycompany.avanceproyecto.controller.AlojamientoController;
import com.mycompany.avanceproyecto.model.Alojamientos;
import com.mycompany.avanceproyecto.model.Clientes;
import com.mycompany.avanceproyecto.model.Habitaciones;
import com.toedter.calendar.JDateChooser;

/**
 *
 * @author Pablo Tello
 */
public class Alojamiento extends JInternalFrame {
    private static final Logger logger = LoggerFactory.getLogger(Alojamiento.class);
    
    // Agregar getters para el controlador
    public JTextField getTxtIdAlojamiento() { return txtIdalojamiento; }
    public JTextField getTxtIdHabitacion() { return txtidhabitacion; }
    public JTextField getTxtHabitacion() { return txthabitacion; }
    public JTextField getTxtIdCliente() { return txtidcliente; }
    public JTextField getTxtCliente() { return txtxliente; }
    public JTextField getTxtCosto() { return txtcosto; }
    public JDateChooser getFechaEntrada() { return dcfechaingreso; }
    public JDateChooser getFechaSalida() { return dcfechasalida; }
    public JTable getTablaAlojamientos() { return tablalistadoalojamiento; }
    public JButton getBtnBuscarHabitacion() { return btnbuscahabitacion; }
    public JButton getBtnBuscarCliente() { return btnbuscacliente; }
    public JButton getBtnNuevo() { return btnNuevo; }
    public JButton getBtnGuardar() { return btnguardar; }
    public JButton getBtnEliminar() { return btneliminar; }
    public JButton getBtnBuscar() { return btsbuscar; }

    // Método para actualizar el contador de registros
    public void actualizarTotalRegistros(int total) {
        lbltotalregistro.setText("Total: " + total);
    }

    // Método para limpiar el formulario
    public void limpiarFormulario() {
        txtIdalojamiento.setText("");
        txtidhabitacion.setText("");
        txthabitacion.setText("");
        txtidcliente.setText("");
        txtxliente.setText("");
        txtcosto.setText("");
        dcfechaingreso.setDate(null);
        dcfechasalida.setDate(null);
        tablalistadoalojamiento.clearSelection();
    }

    // Métodos para obtener el cliente y habitación seleccionados
    public Clientes getClienteSeleccionado() {
        try {
            int idCliente = Integer.parseInt(txtidcliente.getText());
            String nombreCliente = txtxliente.getText();
            return new Clientes(idCliente, 0, nombreCliente, 0, "");
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public Habitaciones getHabitacionSeleccionada() {
        try {
            int idHabitacion = Integer.parseInt(txtidhabitacion.getText());
            String numeroHabitacion = txthabitacion.getText();
            return new Habitaciones(idHabitacion, numeroHabitacion, "", true, 0.0);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    // Método para actualizar la tabla
    public void actualizarTabla(List<Alojamientos> alojamientos) {
        DefaultTableModel modelo = (DefaultTableModel) tablalistadoalojamiento.getModel();
        modelo.setRowCount(0);
        
        for (Alojamientos a : alojamientos) {
            modelo.addRow(new Object[]{
                a.getId(),
                a.getCliente().getNombre(),
                a.getHabitacion().getNumero(),
                a.getFechaEntrada(),
                a.getFechaSalida()
            });
        }
        
        actualizarTotalRegistros(alojamientos.size());
    }

    /**
     * Creates new form Alojamiento
     */
    public Alojamiento() {
        initComponents();
        setupFrame();

        new AlojamientoController(this); // El controlador se inicializa después de configurar la vista
        
    }

    private void setupFrame() {
        setTitle("Gestión de Alojamientos");
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setSize(1163, 438);
        setVisible(true);
        
        // Configurar la tabla
        String[] columnas = {"ID", "Cliente", "Habitación", "Fecha Entrada", "Fecha Salida", "Costo"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0);
        tablalistadoalojamiento.setModel(modelo);
        tablalistadoalojamiento.getTableHeader().setReorderingAllowed(false);
        
        // AGREGAR LISTENER PARA LA SELECCIÓN DE LA TABLA
        tablalistadoalojamiento.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = tablalistadoalojamiento.getSelectedRow();
                if (row != -1) {
                    mostrarDatosSeleccionados(row);
                }
            }
        });
    }

    // AGREGAR MÉTODO PARA MOSTRAR DATOS SELECCIONADOS
    private void mostrarDatosSeleccionados(int row) {
        try {
            // Obtener datos de la tabla según las columnas:
            // {"ID", "Cliente", "Habitación", "Fecha Entrada", "Fecha Salida", "Costo"}
            
            // ID del alojamiento (columna 0)
            txtIdalojamiento.setText(tablalistadoalojamiento.getValueAt(row, 0).toString());
            
            // Cliente (columna 1) - necesitamos buscar el ID del cliente
            String nombreCliente = tablalistadoalojamiento.getValueAt(row, 1).toString();
            txtxliente.setText(nombreCliente);
            
            // Habitación (columna 2) - necesitamos buscar el ID de la habitación
            String numeroHabitacion = tablalistadoalojamiento.getValueAt(row, 2).toString();
            txthabitacion.setText(numeroHabitacion);
            
            // Costo (columna 5)
            String costo = tablalistadoalojamiento.getValueAt(row, 5).toString();
            txtcosto.setText(costo);
            
            // Fechas (columnas 3 y 4) - convertir strings a Date
            try {
                String fechaEntradaStr = tablalistadoalojamiento.getValueAt(row, 3).toString();
                String fechaSalidaStr = tablalistadoalojamiento.getValueAt(row, 4).toString();
                
                // Convertir LocalDate strings a Date para los JDateChooser
                java.time.LocalDate fechaEntrada = java.time.LocalDate.parse(fechaEntradaStr);
                java.time.LocalDate fechaSalida = java.time.LocalDate.parse(fechaSalidaStr);
                
                // Convertir a Date para los JDateChooser
                java.util.Date dateEntrada = java.util.Date.from(fechaEntrada.atStartOfDay(java.time.ZoneId.systemDefault()).toInstant());
                java.util.Date dateSalida = java.util.Date.from(fechaSalida.atStartOfDay(java.time.ZoneId.systemDefault()).toInstant());
                
                dcfechaingreso.setDate(dateEntrada);
                dcfechasalida.setDate(dateSalida);
            } catch (Exception e) {
                logger.warn("Error al convertir fechas: {}", e.getMessage());
                dcfechaingreso.setDate(null);
                dcfechasalida.setDate(null);
            }
            
            // Para obtener los IDs reales, necesitamos buscarlos en el servicio
            // (esto es opcional, solo si necesitas los IDs exactos)
            try {
                // Buscar cliente por nombre para obtener ID
                com.mycompany.avanceproyecto.service.ClienteService clienteService = 
                    new com.mycompany.avanceproyecto.service.ClienteService();
                java.util.List<com.mycompany.avanceproyecto.model.Clientes> clientes = 
                    clienteService.listarClientes();
                
                for (com.mycompany.avanceproyecto.model.Clientes c : clientes) {
                    if (c.getNombre().equals(nombreCliente)) {
                        txtidcliente.setText(String.valueOf(c.getId()));
                        break;
                    }
                }
                
                // Buscar habitación por número para obtener ID
                com.mycompany.avanceproyecto.service.HabitacionService habitacionService = 
                    new com.mycompany.avanceproyecto.service.HabitacionService();
                java.util.List<com.mycompany.avanceproyecto.model.Habitaciones> habitaciones = 
                    habitacionService.listarHabitaciones();
                
                for (com.mycompany.avanceproyecto.model.Habitaciones h : habitaciones) {
                    if (h.getNumero().equals(numeroHabitacion)) {
                        txtidhabitacion.setText(String.valueOf(h.getId()));
                        break;
                    }
                }
            } catch (Exception e) {
                logger.warn("Error al obtener IDs: {}", e.getMessage());
                // Si no se pueden obtener los IDs, dejar los campos vacíos
                txtidcliente.setText("");
                txtidhabitacion.setText("");
            }
            
        } catch (Exception e) {
            logger.error("Error al mostrar datos seleccionados", e);
            javax.swing.JOptionPane.showMessageDialog(this, 
                "Error al cargar los datos seleccionados: " + e.getMessage(),
                "Error",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void setupButtons() {
        // Crear panel específico para botones con FlowLayout
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        
        // Configurar botones con tamaño uniforme
        Dimension buttonSize = new Dimension(100, 30);
        btnNuevo.setPreferredSize(buttonSize);
        btnguardar.setPreferredSize(buttonSize);
        btncancelar.setPreferredSize(buttonSize);
        
        // Agregar botones al panel
        buttonPanel.add(btnNuevo);
        buttonPanel.add(btnguardar);
        buttonPanel.add(btncancelar);
        
        // Agregar el panel de botones al final del panel principal
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(15, 0, 10, 0);
        
        jPanel3.add(buttonPanel, gbc);
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
        txtIdalojamiento = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtidhabitacion = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtcosto = new javax.swing.JTextField();
        btnNuevo = new javax.swing.JButton();
        btnguardar = new javax.swing.JButton();
        btncancelar = new javax.swing.JButton();
        txthabitacion = new javax.swing.JTextField();
        txtidcliente = new javax.swing.JTextField();
        txtxliente = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        dcfechaingreso = new com.toedter.calendar.JDateChooser();
        jLabel12 = new javax.swing.JLabel();
        dcfechasalida = new com.toedter.calendar.JDateChooser();
        btnbuscahabitacion = new javax.swing.JButton();
        btnbuscacliente = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tablalistadoalojamiento = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        txtbuscar = new javax.swing.JTextField();
        btsbuscar = new javax.swing.JButton();
        btneliminar = new javax.swing.JButton();
        btnsalir = new javax.swing.JButton();
        lbltotalregistro = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("ALOJAMIENTO");

        jPanel3.setBackground(new java.awt.Color(255, 204, 204));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("REGISTRO DE ALOJAMIENTO"));

        txtIdalojamiento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIdalojamientoActionPerformed(evt);
            }
        });

        jLabel2.setText("HABITACION:");

        txtidhabitacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtidhabitacionActionPerformed(evt);
            }
        });

        jLabel7.setText("CLIENTE:");

        jLabel6.setText("COSTO:");

        txtcosto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtcostoActionPerformed(evt);
            }
        });

        btnNuevo.setText("NUEVO");

        btnguardar.setText("GUARDAR");

        btncancelar.setText("CANCELAR");
        btncancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btncancelarActionPerformed(evt);
            }
        });

        txthabitacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txthabitacionActionPerformed(evt);
            }
        });

        txtidcliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtidclienteActionPerformed(evt);
            }
        });

        txtxliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtxlienteActionPerformed(evt);
            }
        });

        jLabel11.setText("FECHA DE INGRESO:");

        jLabel12.setText("FECHA DE SALIDA:");

        btnbuscahabitacion.setText("BUSCAR");
        btnbuscahabitacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnbuscahabitacionActionPerformed(evt);
            }
        });

        btnbuscacliente.setText("BUSCAR");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel2))
                                .addGap(28, 28, 28)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                                        .addComponent(txtidcliente, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(txtxliente, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                                        .addComponent(txtidhabitacion, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(txthabitacion, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(txtIdalojamiento, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnbuscahabitacion)
                                    .addComponent(btnbuscacliente)))
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(btnNuevo)
                                .addGap(45, 45, 45)
                                .addComponent(btnguardar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btncancelar)
                                .addGap(46, 46, 46))))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(172, 172, 172)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtcosto, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dcfechaingreso, javax.swing.GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE)
                            .addComponent(dcfechasalida, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(16, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(txtIdalojamiento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtidhabitacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txthabitacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnbuscahabitacion))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtidcliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtxliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnbuscacliente))
                .addGap(9, 9, 9)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dcfechaingreso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dcfechasalida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtcosto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnNuevo)
                    .addComponent(btnguardar)
                    .addComponent(btncancelar))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("LISTADO DE ALOJAMIENTO"));

        tablalistadoalojamiento.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane3.setViewportView(tablalistadoalojamiento);

        jLabel3.setText("BUSCAR");

        btsbuscar.setText("Buscar");
        btsbuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btsbuscarActionPerformed(evt);
            }
        });

        btneliminar.setText("Eliminar");

        btnsalir.setText("Salir");

        lbltotalregistro.setText("REGISTRO");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lbltotalregistro, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(140, 140, 140))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 607, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtbuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(60, 60, 60)
                        .addComponent(btsbuscar)
                        .addGap(34, 34, 34)
                        .addComponent(btneliminar)
                        .addGap(44, 44, 44)
                        .addComponent(btnsalir)))
                .addContainerGap(31, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtbuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btsbuscar)
                    .addComponent(btneliminar)
                    .addComponent(btnsalir))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lbltotalregistro)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(396, 396, 396)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(26, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 393, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 9, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtIdalojamientoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIdalojamientoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIdalojamientoActionPerformed

    private void txtidhabitacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtidhabitacionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtidhabitacionActionPerformed

    private void txtcostoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtcostoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtcostoActionPerformed

    private void btncancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btncancelarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btncancelarActionPerformed

    private void btsbuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btsbuscarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btsbuscarActionPerformed

    private void txthabitacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txthabitacionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txthabitacionActionPerformed

    private void txtidclienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtidclienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtidclienteActionPerformed

    private void txtxlienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtxlienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtxlienteActionPerformed

    private void btnbuscahabitacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnbuscahabitacionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnbuscahabitacionActionPerformed

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
            java.util.logging.Logger.getLogger(Alojamiento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Alojamiento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Alojamiento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Alojamiento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Alojamiento().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnNuevo;
    private javax.swing.JButton btnbuscacliente;
    private javax.swing.JButton btnbuscahabitacion;
    private javax.swing.JButton btncancelar;
    private javax.swing.JButton btneliminar;
    private javax.swing.JButton btnguardar;
    private javax.swing.JButton btnsalir;
    private javax.swing.JButton btsbuscar;
    private com.toedter.calendar.JDateChooser dcfechaingreso;
    private com.toedter.calendar.JDateChooser dcfechasalida;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lbltotalregistro;
    private javax.swing.JTable tablalistadoalojamiento;
    private javax.swing.JTextField txtIdalojamiento;
    private javax.swing.JTextField txtbuscar;
    private javax.swing.JTextField txtcosto;
    private javax.swing.JTextField txthabitacion;
    private javax.swing.JTextField txtidcliente;
    private javax.swing.JTextField txtidhabitacion;
    private javax.swing.JTextField txtxliente;
    // End of variables declaration//GEN-END:variables
}
