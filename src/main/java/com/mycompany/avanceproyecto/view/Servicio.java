/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.avanceproyecto.view;

import javax.swing.*;
import java.awt.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import com.mycompany.avanceproyecto.model.Servicios;
import com.mycompany.avanceproyecto.controller.ServicioController;

/**
 *
 * @author Pablo Tello
 */
public class Servicio extends JInternalFrame {
    
    /**
     * Creates new form Servicio
     */
    public Servicio() {
        initComponents();
        setupFrame();
        setupForm();
        new ServicioController(this);  // Inicializar el controlador
    }

    private void setupFrame() {
        // Configurar ventana interna
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setSize(1204, 397);
        
        // Remover el EXIT_ON_CLOSE ya que es un JInternalFrame
        setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);

        // Configurar botón salir para que solo cierre esta ventana
        btnsalir.addActionListener(e -> dispose());
    }

    // Agregar estos getters para el controlador
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

    public JTextField getTxtIdServicio() {
        return txtidServicio;
    }

    public JTextField getTxtNombre() {
        return txtNOMBRE;
    }

    public JTextField getTxtPrecio() {
        return txtprecio;
    }

    public JTable getTablaServicios() {
        return tablalistadoservicio;
    }

    public void limpiarCampos() {
        txtidServicio.setText("");
        txtNOMBRE.setText("");
        txtprecio.setText("");
        tablalistadoservicio.clearSelection();
    }

    public void actualizarTabla(List<Servicios> servicios) {
        DefaultTableModel modelo = (DefaultTableModel) tablalistadoservicio.getModel();
        modelo.setRowCount(0);
        
        for (Servicios s : servicios) {
            modelo.addRow(new Object[]{
                s.getId(),
                s.getNombre(),
                s.getPrecio()
            });
        }
        
        lbltotalregistro.setText("Total: " + servicios.size());
    }

    private void setupForm() {
        setTitle("Gestión de Servicios");
        
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

        // Configurar tamaños uniformes para botones
        Dimension buttonSize = new Dimension(100, 30);
        btnNuevo.setPreferredSize(buttonSize);
        btnguardar.setPreferredSize(buttonSize);
        btncancelar.setPreferredSize(buttonSize);
        btneliminar.setPreferredSize(buttonSize);
        btnsalir.setPreferredSize(buttonSize);
        btsbuscar.setPreferredSize(buttonSize);

        // Configurar tabla
        String[] columnas = {"ID", "Nombre", "Precio"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablalistadoservicio.setModel(modelo);
        tablalistadoservicio.getTableHeader().setReorderingAllowed(false);
        
        // Configurar anchos de columnas
        tablalistadoservicio.getColumnModel().getColumn(0).setPreferredWidth(50);   // ID
        tablalistadoservicio.getColumnModel().getColumn(1).setPreferredWidth(250);  // Nombre
        tablalistadoservicio.getColumnModel().getColumn(2).setPreferredWidth(100);  // Precio
        
        // Selección única
        tablalistadoservicio.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Listener para selección en tabla
        tablalistadoservicio.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = tablalistadoservicio.getSelectedRow();
                if (row != -1) {
                    mostrarDatosSeleccionados(row);
                }
            }
        });
    }
    
    private void mostrarDatosSeleccionados(int row) {
        txtidServicio.setText(tablalistadoservicio.getValueAt(row, 0).toString());
        txtNOMBRE.setText(tablalistadoservicio.getValueAt(row, 1).toString());
        txtprecio.setText(tablalistadoservicio.getValueAt(row, 2).toString());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel5 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tablalistado1 = new javax.swing.JTable();
        jLabel8 = new javax.swing.JLabel();
        txtbuscar1 = new javax.swing.JTextField();
        btsbuscar1 = new javax.swing.JButton();
        btneliminar1 = new javax.swing.JButton();
        btnsalir1 = new javax.swing.JButton();
        lbltotalregistro1 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tablalistado2 = new javax.swing.JTable();
        jLabel11 = new javax.swing.JLabel();
        txtbuscar2 = new javax.swing.JTextField();
        btsbuscar2 = new javax.swing.JButton();
        btneliminar2 = new javax.swing.JButton();
        btnsalir2 = new javax.swing.JButton();
        lbltotalregistro2 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        tablalistado3 = new javax.swing.JTable();
        jLabel12 = new javax.swing.JLabel();
        txtbuscar3 = new javax.swing.JTextField();
        btsbuscar3 = new javax.swing.JButton();
        btneliminar3 = new javax.swing.JButton();
        btnsalir3 = new javax.swing.JButton();
        lbltotalregistro3 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtNOMBRE = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtprecio = new javax.swing.JTextField();
        btnNuevo = new javax.swing.JButton();
        btnguardar = new javax.swing.JButton();
        btncancelar = new javax.swing.JButton();
        txtidServicio = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tablalistadoservicio = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        txtbuscar = new javax.swing.JTextField();
        btsbuscar = new javax.swing.JButton();
        btneliminar = new javax.swing.JButton();
        btnsalir = new javax.swing.JButton();
        lbltotalregistro = new javax.swing.JLabel();

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("LISTADO DE HABITACION"));

        tablalistado1.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane4.setViewportView(tablalistado1);

        jLabel8.setText("BUSCAR");

        btsbuscar1.setText("Buscar");
        btsbuscar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btsbuscar1ActionPerformed(evt);
            }
        });

        btneliminar1.setText("Eliminar");

        btnsalir1.setText("Salir");

        lbltotalregistro1.setText("REGISTRO");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 607, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtbuscar1, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(60, 60, 60)
                        .addComponent(btsbuscar1)
                        .addGap(34, 34, 34)
                        .addComponent(btneliminar1)
                        .addGap(44, 44, 44)
                        .addComponent(btnsalir1)))
                .addContainerGap(128, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lbltotalregistro1, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(140, 140, 140))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtbuscar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btsbuscar1)
                    .addComponent(btneliminar1)
                    .addComponent(btnsalir1))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lbltotalregistro1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("LISTADO DE HABITACION"));

        tablalistado2.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane5.setViewportView(tablalistado2);

        jLabel11.setText("BUSCAR");

        btsbuscar2.setText("Buscar");
        btsbuscar2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btsbuscar2ActionPerformed(evt);
            }
        });

        btneliminar2.setText("Eliminar");

        btnsalir2.setText("Salir");

        lbltotalregistro2.setText("REGISTRO");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 607, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtbuscar2, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(60, 60, 60)
                        .addComponent(btsbuscar2)
                        .addGap(34, 34, 34)
                        .addComponent(btneliminar2)
                        .addGap(44, 44, 44)
                        .addComponent(btnsalir2)))
                .addContainerGap(128, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lbltotalregistro2, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(140, 140, 140))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtbuscar2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btsbuscar2)
                    .addComponent(btneliminar2)
                    .addComponent(btnsalir2))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lbltotalregistro2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("LISTADO DE HABITACION"));

        tablalistado3.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane6.setViewportView(tablalistado3);

        jLabel12.setText("BUSCAR");

        btsbuscar3.setText("Buscar");
        btsbuscar3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btsbuscar3ActionPerformed(evt);
            }
        });

        btneliminar3.setText("Eliminar");

        btnsalir3.setText("Salir");

        lbltotalregistro3.setText("REGISTRO");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 607, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtbuscar3, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(60, 60, 60)
                        .addComponent(btsbuscar3)
                        .addGap(34, 34, 34)
                        .addComponent(btneliminar3)
                        .addGap(44, 44, 44)
                        .addComponent(btnsalir3)))
                .addContainerGap(128, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lbltotalregistro3, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(140, 140, 140))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtbuscar3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btsbuscar3)
                    .addComponent(btneliminar3)
                    .addComponent(btnsalir3))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lbltotalregistro3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("SERVICIO");

        jPanel3.setBackground(new java.awt.Color(255, 204, 204));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("REGISTRO DE SERVICIO"));

        jLabel2.setText("NOMBRE:");

        txtNOMBRE.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNOMBREActionPerformed(evt);
            }
        });

        jLabel6.setText("PRECIO:");

        txtprecio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtprecioActionPerformed(evt);
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

        txtidServicio.setEditable(false);
        txtidServicio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtidServicioActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(88, 88, 88)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtNOMBRE, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
                            .addComponent(txtidServicio, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
                            .addComponent(txtprecio)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(btnNuevo)
                        .addGap(67, 67, 67)
                        .addComponent(btnguardar)
                        .addGap(64, 64, 64)
                        .addComponent(btncancelar)))
                .addContainerGap(14, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(txtidServicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtNOMBRE, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtprecio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnNuevo)
                    .addComponent(btnguardar)
                    .addComponent(btncancelar))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("LISTADO DE SERVICIO"));

        tablalistadoservicio.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane3.setViewportView(tablalistadoservicio);

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
                .addGap(32, 32, 32)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 607, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtbuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(51, 51, 51)
                        .addComponent(btsbuscar)
                        .addGap(18, 18, 18)
                        .addComponent(btneliminar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnsalir)))
                .addContainerGap(10, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lbltotalregistro, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(89, 89, 89))
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
                    .addComponent(btnsalir))
                .addGap(36, 36, 36)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lbltotalregistro)
                .addContainerGap(97, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(23, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
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
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 348, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(13, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtNOMBREActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNOMBREActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNOMBREActionPerformed

    private void txtprecioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtprecioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtprecioActionPerformed

    private void btncancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btncancelarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btncancelarActionPerformed

    private void btsbuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btsbuscarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btsbuscarActionPerformed

    private void btsbuscar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btsbuscar1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btsbuscar1ActionPerformed

    private void btsbuscar2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btsbuscar2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btsbuscar2ActionPerformed

    private void btsbuscar3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btsbuscar3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btsbuscar3ActionPerformed

    private void txtidServicioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtidServicioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtidServicioActionPerformed

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
            java.util.logging.Logger.getLogger(Servicio.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Servicio.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Servicio.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Servicio.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Servicio().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnNuevo;
    private javax.swing.JButton btncancelar;
    private javax.swing.JButton btneliminar;
    private javax.swing.JButton btneliminar1;
    private javax.swing.JButton btneliminar2;
    private javax.swing.JButton btneliminar3;
    private javax.swing.JButton btnguardar;
    private javax.swing.JButton btnsalir;
    private javax.swing.JButton btnsalir1;
    private javax.swing.JButton btnsalir2;
    private javax.swing.JButton btnsalir3;
    private javax.swing.JButton btsbuscar;
    private javax.swing.JButton btsbuscar1;
    private javax.swing.JButton btsbuscar2;
    private javax.swing.JButton btsbuscar3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JLabel lbltotalregistro;
    private javax.swing.JLabel lbltotalregistro1;
    private javax.swing.JLabel lbltotalregistro2;
    private javax.swing.JLabel lbltotalregistro3;
    private javax.swing.JTable tablalistado1;
    private javax.swing.JTable tablalistado2;
    private javax.swing.JTable tablalistado3;
    private javax.swing.JTable tablalistadoservicio;
    private javax.swing.JTextField txtNOMBRE;
    private javax.swing.JTextField txtbuscar;
    private javax.swing.JTextField txtbuscar1;
    private javax.swing.JTextField txtbuscar2;
    private javax.swing.JTextField txtbuscar3;
    private javax.swing.JTextField txtidServicio;
    private javax.swing.JTextField txtprecio;
    // End of variables declaration//GEN-END:variables
}
