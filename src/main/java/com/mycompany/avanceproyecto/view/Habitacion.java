/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.avanceproyecto.view;

import javax.swing.*;
import java.awt.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import com.mycompany.avanceproyecto.model.Habitaciones;
import com.mycompany.avanceproyecto.controller.HabitacionController;

/**
 *
 * @author Pablo Tello
 */
public class Habitacion extends JInternalFrame {

    /**
     * Creates new form Habitacion
     */
    public Habitacion() {
        initComponents();
        setupForm();
        new HabitacionController(this);
    }

    private void setupForm() {
        // Configuración básica de la ventana INTERNA
        setTitle("Gestión de Habitaciones");
        setClosable(true);          // Permite cerrar la ventana
        setIconifiable(true);       // Permite minimizar
        setMaximizable(true);       // Permite maximizar
        setResizable(true);         // Permite redimensionar
        setSize(1056, 409);         // Tamaño de la ventana
        
        // Configurar comportamiento al cerrar
        setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
        
        // Hacer visible la ventana
        setVisible(true);
        
        // Configurar tabla con orden fijo de columnas
        String[] columnas = {"ID", "Número", "Estado", "Precio", "Tipo"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hace que ninguna celda sea editable
            }
        };
        tablalistadohabitacion.setModel(modelo);
        
        // Evitar que se reordenen las columnas
        tablalistadohabitacion.getTableHeader().setReorderingAllowed(false);

        // Establecer ancho de columnas
        tablalistadohabitacion.getColumnModel().getColumn(0).setPreferredWidth(50);  // ID
        tablalistadohabitacion.getColumnModel().getColumn(1).setPreferredWidth(100); // Número
        tablalistadohabitacion.getColumnModel().getColumn(2).setPreferredWidth(100); // Estado
        tablalistadohabitacion.getColumnModel().getColumn(3).setPreferredWidth(100); // Precio
        tablalistadohabitacion.getColumnModel().getColumn(4).setPreferredWidth(150); // Tipo

        // Habilitar selección única en la tabla
        tablalistadohabitacion.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Eventos de tabla
        tablalistadohabitacion.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = tablalistadohabitacion.getSelectedRow();
                if (row != -1) {
                    mostrarDatosSeleccionados(row);
                }
            }
        });

        // Eventos de botones
        btnNuevo.addActionListener(e -> limpiarCampos());
        btncancelar.addActionListener(e -> limpiarCampos());
        btnsalir.addActionListener(e -> dispose());
        
        // Configurar botones con tamaño uniforme
        Dimension buttonSize = new Dimension(100, 30);
        btnNuevo.setPreferredSize(buttonSize);
        btnguardar.setPreferredSize(buttonSize);
        btncancelar.setPreferredSize(buttonSize);
        btneliminar.setPreferredSize(buttonSize);
        btnsalir.setPreferredSize(buttonSize);
        btsbuscar.setPreferredSize(buttonSize);

        // Mejorar apariencia de los campos
        Font labelFont = new Font("Segoe UI", Font.BOLD, 12);
        jLabel2.setFont(labelFont);
        jLabel6.setFont(labelFont);
        jLabel9.setFont(labelFont);
        jLabel10.setFont(labelFont);

        // Ajustar tamaños de componentes
        txtNumero.setPreferredSize(new Dimension(150, 25));
        txtNumero1.setPreferredSize(new Dimension(150, 25));
        cbopiso1.setPreferredSize(new Dimension(150, 25));
        cbopiso2.setPreferredSize(new Dimension(150, 25));
        
        // Configurar colores de botones
        Color btnBackground = new Color(64, 123, 255);
        Color btnForeground = Color.WHITE;
        
        btnNuevo.setBackground(btnBackground);
        btnguardar.setBackground(btnBackground);
        btncancelar.setBackground(btnBackground);
        btneliminar.setBackground(btnBackground);
        btnsalir.setBackground(btnBackground);
        btsbuscar.setBackground(btnBackground);
        
        btnNuevo.setForeground(btnForeground);
        btnguardar.setForeground(btnForeground);
        btncancelar.setForeground(btnForeground);
        btneliminar.setForeground(btnForeground);
        btnsalir.setForeground(btnForeground);
        btsbuscar.setForeground(btnForeground);
    }

    private void mostrarDatosSeleccionados(int row) {
        // Ajustar el orden de lectura de las columnas
        txtIdHabita.setText(tablalistadohabitacion.getValueAt(row, 0).toString());  // ID
        txtNumero.setText(tablalistadohabitacion.getValueAt(row, 1).toString());    // Número
        cbopiso1.setSelectedItem(tablalistadohabitacion.getValueAt(row, 3));        // Estado
        txtNumero1.setText(tablalistadohabitacion.getValueAt(row, 4).toString());   // Precio
        cbopiso2.setSelectedItem(tablalistadohabitacion.getValueAt(row, 2));        // Tipo
    }

    public void limpiarCampos() {
        txtIdHabita.setText("");
        txtNumero.setText("");
        txtNumero1.setText("");
        cbopiso1.setSelectedIndex(0);
        cbopiso2.setSelectedIndex(0);
        tablalistadohabitacion.clearSelection();
    }

    public void actualizarTabla(List<Habitaciones> habitaciones) {
        DefaultTableModel modelo = (DefaultTableModel) tablalistadohabitacion.getModel();
        modelo.setRowCount(0);
        
        for (Habitaciones h : habitaciones) {
            modelo.addRow(new Object[]{
                h.getId(),             // Columna 0: ID
                h.getNumero(),         // Columna 1: Número
                h.isDisponible() ? "DISPONIBLE" : "OCUPADO",  // Columna 2: Estado
                h.getPrecio(),         // Columna 3: Precio
                h.getTipo()            // Columna 4: Tipo
            });
        }
        
        lbltotalregistro.setText("Total: " + habitaciones.size());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        txtIdHabita = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtNumero = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtNumero1 = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        cbopiso1 = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        cbopiso2 = new javax.swing.JComboBox<>();
        btnNuevo = new javax.swing.JButton();
        btnguardar = new javax.swing.JButton();
        btncancelar = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tablalistadohabitacion = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        txtbuscar = new javax.swing.JTextField();
        btsbuscar = new javax.swing.JButton();
        btneliminar = new javax.swing.JButton();
        btnsalir = new javax.swing.JButton();
        lbltotalregistro = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("HABITACION");

        jPanel3.setBackground(new java.awt.Color(255, 204, 204));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("REGISTRO DE HABITACION"));

        txtIdHabita.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIdHabitaActionPerformed(evt);
            }
        });

        jLabel2.setText("NUMERO:");

        txtNumero.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNumeroActionPerformed(evt);
            }
        });

        jLabel6.setText("PRECIO DIARIO:");

        txtNumero1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNumero1ActionPerformed(evt);
            }
        });

        jLabel9.setText("ESTADO");

        cbopiso1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "DISPONIBLE", "OCUPADO", "MANMTENJMIENTO" }));

        jLabel10.setText("TIPO DE HABITACION");

        cbopiso2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "INDIVUDUAL", "MATRIMONIAL", "FAMILIAR", "PRESIDENCIAL" }));

        btnNuevo.setText("NUEVO");

        btnguardar.setText("GUARDAR");

        btncancelar.setText("CANCELAR");
        btncancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btncancelarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(157, 157, 157)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtIdHabita, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtNumero, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cbopiso1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtNumero1, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18))))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cbopiso2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(btnNuevo)
                                .addGap(54, 54, 54)
                                .addComponent(btnguardar)
                                .addGap(41, 41, 41)
                                .addComponent(btncancelar)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(txtIdHabita, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtNumero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbopiso1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNumero1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbopiso2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnNuevo)
                    .addComponent(btnguardar)
                    .addComponent(btncancelar))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("LISTADO DE HABITACION"));

        tablalistadohabitacion.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane3.setViewportView(tablalistadohabitacion);

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
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 551, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtbuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btsbuscar)
                    .addComponent(btneliminar)
                    .addComponent(btnsalir))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37)
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
                        .addGap(18, 18, 18)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(394, 394, 394)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 21, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtIdHabitaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIdHabitaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIdHabitaActionPerformed

    private void txtNumeroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNumeroActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumeroActionPerformed

    private void txtNumero1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNumero1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumero1ActionPerformed

    private void btncancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btncancelarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btncancelarActionPerformed

    private void btsbuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btsbuscarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btsbuscarActionPerformed

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
            java.util.logging.Logger.getLogger(Habitacion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Habitacion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Habitacion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Habitacion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Habitacion().setVisible(true);
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
    private javax.swing.JComboBox<String> cbopiso1;
    private javax.swing.JComboBox<String> cbopiso2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lbltotalregistro;
    private javax.swing.JTable tablalistadohabitacion;
    private javax.swing.JTextField txtIdHabita;
    private javax.swing.JTextField txtNumero;
    private javax.swing.JTextField txtNumero1;
    private javax.swing.JTextField txtbuscar;
    // End of variables declaration//GEN-END:variables

    // Agregar getters
    public JButton getBtnNuevo() {
        return btnNuevo;
    }

    public JButton getBtnGuardar() {
        return btnguardar;
    }

    public JButton getBtnEliminar() {
        return btneliminar;
    }

    public JButton getBtnBuscar() {
        return btsbuscar;
    }

    // Agregar estos getters adicionales
    public JTextField getTxtNumero() {
        return txtNumero;
    }

    public JTextField getTxtPrecio() {
        return txtNumero1;  // Renombrar este campo sería mejor
    }

    public JComboBox<String> getCboEstado() {
        return cbopiso1;
    }

    public JComboBox<String> getCboTipoHabitacion() {
        return cbopiso2;
    }

    public JTable getTablaHabitaciones() {
        return tablalistadohabitacion;
    }

    // Agregar getter para el campo ID
    public JTextField getTxtIdHabita() {
        return txtIdHabita;
    }

    
}

