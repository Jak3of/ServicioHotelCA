
package com.mycompany.avanceproyecto.view;

import com.mycompany.avanceproyecto.model.Habitaciones;
import com.mycompany.avanceproyecto.service.HabitacionService;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class SeleccionarHabitaciones extends javax.swing.JFrame {
private final JPanel panelGrid;
    private final JButton btnCerrar;
    private final HabitacionService habitacionService;
    private final Alojamiento vistaAlojamiento;
    public SeleccionarHabitaciones(Alojamiento vistaAlojamiento) {
        this.vistaAlojamiento = vistaAlojamiento;
        this.habitacionService = new HabitacionService();

        setTitle("Seleccionar Habitación");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        panelGrid = new JPanel(new GridLayout(3, 4, 10, 10)); // 4 columnas, 3 filas
        btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());

        add(new JScrollPane(panelGrid), BorderLayout.CENTER);
        add(btnCerrar, BorderLayout.SOUTH);

        cargarHabitaciones();
    }
  private void cargarHabitaciones() {
        try {
            List<Habitaciones> habitaciones = habitacionService.listarHabitaciones();

            for (Habitaciones h : habitaciones) {
                
                String texto = "<html>"
        + "<b>N°:</b> " + h.getNumero() + "<br>"
        + "<b>Tipo:</b> " + h.getTipo() + "<br>"
        + "<b>Precio:</b> S/ " + h.getPrecio() + "<br>"
        + "<b>Estado:</b> " + (h.isDisponible() ? "DISPONIBLE" : "OCUPADO")
        + "</html>";
                 JButton btn = new JButton(texto);
                btn.setBackground(h.isDisponible() ? Color.GREEN : Color.RED);
                btn.setForeground(Color.WHITE);
                btn.setOpaque(true);
                btn.setBorderPainted(false);

                btn.addActionListener(e -> {
                    if (!h.isDisponible()) {
                        JOptionPane.showMessageDialog(this, "La habitación está ocupada.");
                        return;
                    }

                    // Asignar la habitación a la vista principal
                    vistaAlojamiento.getTxtIdHabitacion().setText(String.valueOf(h.getId()));
                    vistaAlojamiento.getTxtHabitacion().setText(h.getNumero());
                    vistaAlojamiento.getTxtCosto().setText(String.valueOf(h.getPrecio()));
                    dispose();
                });

                panelGrid.add(btn);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar habitaciones: " + e.getMessage());
        }
        }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 669, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 485, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
  
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

