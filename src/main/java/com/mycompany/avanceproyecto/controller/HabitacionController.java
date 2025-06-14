package com.mycompany.avanceproyecto.controller;

import com.mycompany.avanceproyecto.model.Habitaciones;
import com.mycompany.avanceproyecto.service.HabitacionService;
import com.mycompany.avanceproyecto.view.Habitacion;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HabitacionController {
    private static final Logger logger = LoggerFactory.getLogger(HabitacionController.class);
    private final Habitacion view;
    private final HabitacionService service;
    
    public HabitacionController(Habitacion view) {
        this.view = view;
        this.service = new HabitacionService();
        inicializarControlador();
    }
    
    private void inicializarControlador() {
        view.getBtnNuevo().addActionListener(e -> limpiarFormulario());
        view.getBtnGuardar().addActionListener(e -> guardarHabitacion());
        view.getBtnEliminar().addActionListener(e -> eliminarHabitacion());
        view.getBtnBuscar().addActionListener(e -> buscarHabitaciones());
        cargarHabitaciones();
    }
    
    private void guardarHabitacion() {
        try {
            Habitaciones habitacion = new Habitaciones();
            
            // Si hay ID, es una actualización
            String idText = view.getTxtIdHabita().getText().trim();
            if (!idText.isEmpty()) {
                habitacion.setId(Integer.parseInt(idText));
            }
            
            habitacion.setNumero(view.getTxtNumero().getText());
            habitacion.setTipo(view.getCboTipoHabitacion().getSelectedItem().toString());
            habitacion.setPrecio(Double.parseDouble(view.getTxtPrecio().getText()));
            habitacion.setDisponible(view.getCboEstado().getSelectedItem().toString().equals("DISPONIBLE"));
            
            service.guardarOActualizarHabitacion(habitacion);
            
            JOptionPane.showMessageDialog(view, 
                idText.isEmpty() ? "Habitación guardada con éxito" : "Habitación actualizada con éxito");
            
            view.limpiarCampos();
            cargarHabitaciones();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Error al guardar: " + e.getMessage());
        }
    }
    
    private void cargarHabitaciones() {
        try {
            DefaultTableModel modelo = new DefaultTableModel();
            modelo.addColumn("ID");
            modelo.addColumn("Número");
            modelo.addColumn("Tipo");
            modelo.addColumn("Estado");
            modelo.addColumn("Precio");
            
            List<Habitaciones> habitaciones = service.listarHabitaciones();
            for (Habitaciones h : habitaciones) {
                modelo.addRow(new Object[]{
                    h.getId(),
                    h.getNumero(),
                    h.getTipo(),
                    h.isDisponible() ? "DISPONIBLE" : "OCUPADO",
                    h.getPrecio()
                });
            }
            
            view.getTablaHabitaciones().setModel(modelo);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Error al cargar habitaciones: " + e.getMessage());
        }
    }
    
    private void limpiarFormulario() {
        // Implementar limpieza de campos
        logger.info("Limpiando formulario");
        // Aquí irá el código para limpiar todos los campos del formulario
    }

    private void eliminarHabitacion() {
        int fila = view.getTablaHabitaciones().getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(view, "Por favor, seleccione una habitación para eliminar");
            return;
        }

        int id = (Integer) view.getTablaHabitaciones().getValueAt(fila, 0);
        String numero = (String) view.getTablaHabitaciones().getValueAt(fila, 1);

        int confirmacion = JOptionPane.showConfirmDialog(view,
            "¿Está seguro que desea eliminar la habitación " + numero + "?",
            "Confirmar Eliminación",
            JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                service.eliminarHabitacion(id);
                JOptionPane.showMessageDialog(view, "Habitación eliminada con éxito");
                cargarHabitaciones();
                view.limpiarCampos();
            } catch (Exception e) {
                logger.error("Error al eliminar habitación", e);
                JOptionPane.showMessageDialog(view, "Error al eliminar la habitación: " + e.getMessage());
            }
        }
    }

    private void buscarHabitaciones() {
        try {
            // Implementar lógica de búsqueda
            logger.info("Buscando habitaciones");
            // Aquí irá el código para buscar habitaciones
        } catch (Exception e) {
            logger.error("Error al buscar habitaciones", e);
            JOptionPane.showMessageDialog(view, "Error al buscar habitaciones");
        }
    }
    
    // Implementar los demás métodos necesarios para la gestión de habitaciones
}
