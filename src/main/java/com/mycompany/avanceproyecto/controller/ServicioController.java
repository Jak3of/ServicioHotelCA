package com.mycompany.avanceproyecto.controller;

import com.mycompany.avanceproyecto.model.Servicios;
import com.mycompany.avanceproyecto.service.ServicioService;
import com.mycompany.avanceproyecto.view.Servicio;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServicioController {
    private static final Logger logger = LoggerFactory.getLogger(ServicioController.class);
    private final Servicio view;
    private final ServicioService service;
    
    public ServicioController(Servicio view) {
        this.view = view;
        this.service = new ServicioService();
        inicializarControlador();
    }
    
    private void inicializarControlador() {
        view.getBtnNuevo().addActionListener(e -> limpiarFormulario());
        view.getBtnGuardar().addActionListener(e -> guardarServicio());
        view.getBtnEliminar().addActionListener(e -> eliminarServicio());
        cargarServicios();
    }
    
    private void guardarServicio() {
        try {
            Servicios servicio = new Servicios();
            
            String idText = view.getTxtIdServicio().getText().trim();
            if (!idText.isEmpty()) {
                servicio.setId(Integer.parseInt(idText));
            }
            
            servicio.setNombre(view.getTxtNombre().getText());
            servicio.setPrecio(Double.parseDouble(view.getTxtPrecio().getText()));
            
            service.guardarOActualizarServicio(servicio);
            
            JOptionPane.showMessageDialog(view, 
                idText.isEmpty() ? "Servicio guardado con éxito" : "Servicio actualizado con éxito");
            
            view.limpiarCampos();
            cargarServicios();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Error al guardar: " + e.getMessage());
        }
    }
    
    private void cargarServicios() {
        try {
            DefaultTableModel modelo = new DefaultTableModel();
            modelo.addColumn("ID");
            modelo.addColumn("Nombre");
            modelo.addColumn("Precio");
            
            List<Servicios> servicios = service.listarServicios();
            for (Servicios s : servicios) {
                modelo.addRow(new Object[]{
                    s.getId(),
                    s.getNombre(),
                    s.getPrecio()
                });
            }
            
            view.getTablaServicios().setModel(modelo);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Error al cargar servicios: " + e.getMessage());
        }
    }
    
    private void eliminarServicio() {
        int fila = view.getTablaServicios().getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(view, "Por favor, seleccione un servicio para eliminar");
            return;
        }

        int id = (Integer) view.getTablaServicios().getValueAt(fila, 0);
        String nombre = (String) view.getTablaServicios().getValueAt(fila, 1);

        int confirmacion = JOptionPane.showConfirmDialog(view,
            "¿Está seguro que desea eliminar el servicio " + nombre + "?",
            "Confirmar Eliminación",
            JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                service.eliminarServicio(id);
                JOptionPane.showMessageDialog(view, "Servicio eliminado con éxito");
                cargarServicios();
                view.limpiarCampos();
            } catch (Exception e) {
                logger.error("Error al eliminar servicio", e);
                JOptionPane.showMessageDialog(view, "Error al eliminar el servicio: " + e.getMessage());
            }
        }
    }

    private void limpiarFormulario() {
        view.limpiarCampos();
    }
}
