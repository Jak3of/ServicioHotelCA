/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.avanceproyecto.view;

import com.mycompany.avanceproyecto.controller.ConsumoServicioController;
import com.mycompany.avanceproyecto.model.Alojamientos;
import com.mycompany.avanceproyecto.model.Servicios;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.Color;

/**
 *
 * @author Pablo Tello
 */
public class ConsumoServicio extends JInternalFrame {

    /**
     * Creates new form ConsumoServicio
     */
    public ConsumoServicio() {
        initComponents();
        setupFrame();
        setupForm();
        new ConsumoServicioController(this);
    }

    private void setupFrame() {
        setTitle("Gestión de Consumo de Servicios");
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setSize(1164, 410);
        setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void setupForm() {
        // Configurar la tabla
        String[] columnas = {"ID", "Alojamiento", "Cliente", "Servicio", "Cantidad", "Precio Unit.", "Total"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0);
        tablalistadoconsumo.setModel(modelo);
        tablalistadoconsumo.getTableHeader().setReorderingAllowed(false);
        
        // Hacer el precio no editable
        txtprecio.setEditable(false);
        txtprecio.setBackground(new Color(240, 240, 240));
        
        // Listener para la tabla
        tablalistadoconsumo.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = tablalistadoconsumo.getSelectedRow();
                if (row != -1) {
                    mostrarDatosSeleccionados(row);
                }
            }
        });
    }

    private void mostrarDatosSeleccionados(int row) {
        try {
            // Obtener datos de la tabla según las columnas definidas en el controlador:
            // {"ID", "Alojamiento", "Cliente", "Servicio", "Cantidad", "Precio Unit.", "Total"}
            
            // ID del consumo (columna 0) - lo ponemos en el campo ID oculto
            txtIdalojamiento.setText(tablalistadoconsumo.getValueAt(row, 0).toString());
            
            // Información del alojamiento (columna 1) - extraer ID del formato "Aloj. X"
            String alojamientoInfo = tablalistadoconsumo.getValueAt(row, 1).toString();
            String idAlojamiento = alojamientoInfo.replace("Aloj. ", "");
            txtidalojamiento.setText(idAlojamiento);
            
            // Cliente (columna 2) - mostrar en el campo de alojamiento
            String cliente = tablalistadoconsumo.getValueAt(row, 2).toString();
            txtalojamiento.setText(cliente);
            
            // Servicio (columna 3)
            String servicio = tablalistadoconsumo.getValueAt(row, 3).toString();
            txtservicio.setText(servicio);
            
            // Cantidad (columna 4)
            String cantidad = tablalistadoconsumo.getValueAt(row, 4).toString();
            txtcantidad.setText(cantidad);
            
            // Precio unitario (columna 5)
            String precioUnit = tablalistadoconsumo.getValueAt(row, 5).toString();
            txtprecio.setText(precioUnit);
            
            // Para obtener el ID del servicio, necesitamos buscarlo
            // Intentar obtener el ID del servicio basado en el nombre
            try {
                com.mycompany.avanceproyecto.service.ServicioService servicioService = 
                    new com.mycompany.avanceproyecto.service.ServicioService();
                java.util.List<com.mycompany.avanceproyecto.model.Servicios> servicios = 
                    servicioService.listarServicios();
                
                for (com.mycompany.avanceproyecto.model.Servicios s : servicios) {
                    if (s.getNombre().equals(servicio)) {
                        txtidservicio.setText(String.valueOf(s.getId()));
                        break;
                    }
                }
            } catch (Exception e) {
                // Si no se puede obtener el ID del servicio, dejarlo vacío
                txtidservicio.setText("");
            }
            
        } catch (Exception e) {
            // Si hay algún error, limpiar los campos
            limpiarFormulario();
            javax.swing.JOptionPane.showMessageDialog(this, 
                "Error al cargar los datos seleccionados: " + e.getMessage(),
                "Error",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    public void limpiarFormulario() {
        txtIdalojamiento.setText("");
        txtidalojamiento.setText("");
        txtalojamiento.setText("");
        txtidservicio.setText("");
        txtservicio.setText("");
        txtcantidad.setText("");
        txtprecio.setText("");
        tablalistadoconsumo.clearSelection();
    }

    public void actualizarTotalRegistros(int total) {
        lbltotalregistro.setText("Total: " + total);
    }

    // Getters para el controlador
    public JTextField getTxtIdAlojamiento() { return txtidalojamiento; }
    public JTextField getTxtAlojamiento() { return txtalojamiento; }
    public JTextField getTxtIdServicio() { return txtidservicio; }
    public JTextField getTxtServicio() { return txtservicio; }
    public JTextField getTxtCantidad() { return txtcantidad; }
    public JTextField getTxtPrecio() { return txtprecio; }
    public JTable getTablaConsumo() { return tablalistadoconsumo; }
    public JButton getBtnNuevo() { return btnNuevo; }
    public JButton getBtnGuardar() { return btnguardar; }
    public JButton getBtnEliminar() { return btneliminar; }
    public JButton getBtnBuscarAlojamiento() { return btnbuscahabitacion; }
    public JButton getBtnBuscarServicio() { return btnbuscacliente; }

    // Métodos para obtener objetos seleccionados
    public Alojamientos getAlojamientoSeleccionado() {
        try {
            String idAlojamientoText = txtidalojamiento.getText().trim();
            
            if (idAlojamientoText.isEmpty()) {
                return null;
            }
            
            int idAlojamiento = Integer.parseInt(idAlojamientoText);
            
            // Obtener el alojamiento completo del servicio
            try {
                com.mycompany.avanceproyecto.service.AlojamientoService alojamientoService = 
                    new com.mycompany.avanceproyecto.service.AlojamientoService();
                return alojamientoService.obtenerAlojamiento(idAlojamiento);
            } catch (Exception e) {
                return null;
            }
            
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public Servicios getServicioSeleccionado() {
        try {
            String idServicioText = txtidservicio.getText().trim();
            String nombreServicio = txtservicio.getText().trim();
            String precioText = txtprecio.getText().trim();
            
            if (idServicioText.isEmpty() || nombreServicio.isEmpty() || precioText.isEmpty()) {
                return null;
            }
            
            int idServicio = Integer.parseInt(idServicioText);
            double precio = Double.parseDouble(precioText);
            
            return new com.mycompany.avanceproyecto.model.Servicios(idServicio, nombreServicio, precio);
        } catch (NumberFormatException e) {
            return null;
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

        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        txtIdalojamiento = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtidalojamiento = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtcantidad = new javax.swing.JTextField();
        btnNuevo = new javax.swing.JButton();
        btnguardar = new javax.swing.JButton();
        btncancelar = new javax.swing.JButton();
        txtalojamiento = new javax.swing.JTextField();
        txtidservicio = new javax.swing.JTextField();
        txtservicio = new javax.swing.JTextField();
        btnbuscahabitacion = new javax.swing.JButton();
        btnbuscacliente = new javax.swing.JButton();
        txtprecio = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tablalistadoconsumo = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        txtbuscar = new javax.swing.JTextField();
        btsbuscar = new javax.swing.JButton();
        btneliminar = new javax.swing.JButton();
        btnsalir = new javax.swing.JButton();
        lbltotalregistro = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("CONSUMO DE SERVICIO");

        jPanel3.setBackground(new java.awt.Color(255, 204, 204));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("REGISTRO DE CONSUMO DE SERVICIO"));

        txtIdalojamiento.setEditable(false);
        txtIdalojamiento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIdalojamientoActionPerformed(evt);
            }
        });

        jLabel2.setText("ALOJAMIENTO:");

        txtidalojamiento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtidalojamientoActionPerformed(evt);
            }
        });

        jLabel7.setText("SERVICIO:");

        jLabel6.setText("CANTIDAD:");

        txtcantidad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtcantidadActionPerformed(evt);
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

        txtalojamiento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtalojamientoActionPerformed(evt);
            }
        });

        txtidservicio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtidservicioActionPerformed(evt);
            }
        });

        txtservicio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtservicioActionPerformed(evt);
            }
        });

        btnbuscahabitacion.setText("BUSCAR");

        btnbuscacliente.setText("BUSCAR");

        txtprecio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtprecioActionPerformed(evt);
            }
        });

        jLabel11.setText("COSTO:");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(txtidservicio, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(txtservicio, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(txtidalojamiento, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(txtalojamiento, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(50, 50, 50)
                                .addComponent(txtIdalojamiento, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnbuscahabitacion)
                            .addComponent(btnbuscacliente)))
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txtprecio, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtcantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(btnNuevo)
                        .addGap(51, 51, 51)
                        .addComponent(btnguardar)
                        .addGap(45, 45, 45)
                        .addComponent(btncancelar)))
                .addContainerGap(16, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(txtIdalojamiento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtidalojamiento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtalojamiento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnbuscahabitacion))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtidservicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtservicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnbuscacliente))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtcantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtprecio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnNuevo)
                    .addComponent(btnguardar)
                    .addComponent(btncancelar))
                .addGap(0, 10, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("LISTADO DE CONSUMO DE SERVICIO"));

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
        jScrollPane3.setViewportView(tablalistadoconsumo);

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
                .addContainerGap(19, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lbltotalregistro, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(139, 139, 139))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtbuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btsbuscar)
                    .addComponent(btneliminar)
                    .addComponent(btnsalir))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbltotalregistro)
                .addContainerGap(112, Short.MAX_VALUE))
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
                        .addGap(18, 18, 18)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(277, 277, 277)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(26, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 374, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btsbuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btsbuscarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btsbuscarActionPerformed

    private void txtservicioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtservicioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtservicioActionPerformed

    private void txtidservicioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtidservicioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtidservicioActionPerformed

    private void txtalojamientoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtalojamientoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtalojamientoActionPerformed

    private void btncancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btncancelarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btncancelarActionPerformed

    private void txtcantidadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtcantidadActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtcantidadActionPerformed

    private void txtidalojamientoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtidalojamientoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtidalojamientoActionPerformed

    private void txtIdalojamientoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIdalojamientoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIdalojamientoActionPerformed

    private void txtprecioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtprecioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtprecioActionPerformed

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
            java.util.logging.Logger.getLogger(ConsumoServicio.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ConsumoServicio.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ConsumoServicio.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ConsumoServicio.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ConsumoServicio().setVisible(true);
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
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lbltotalregistro;
    private javax.swing.JTable tablalistadoconsumo;
    private javax.swing.JTextField txtIdalojamiento;
    private javax.swing.JTextField txtalojamiento;
    private javax.swing.JTextField txtbuscar;
    private javax.swing.JTextField txtcantidad;
    private javax.swing.JTextField txtidalojamiento;
    private javax.swing.JTextField txtidservicio;
    private javax.swing.JTextField txtprecio;
    private javax.swing.JTextField txtservicio;
    // End of variables declaration//GEN-END:variables
}
