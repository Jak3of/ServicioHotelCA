package com.mycompany.avanceproyecto.controller;

import com.mycompany.avanceproyecto.model.Alojamientos;
import com.mycompany.avanceproyecto.model.Servicios;
import com.mycompany.avanceproyecto.service.ConsumoServicioService;
import com.mycompany.avanceproyecto.service.AlojamientoService;
import com.mycompany.avanceproyecto.service.ServicioService;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsumoServicioController {
    private static final Logger logger = LoggerFactory.getLogger(ConsumoServicioController.class);
    private final com.mycompany.avanceproyecto.view.ConsumoServicio view;
    private final ConsumoServicioService service;
    private final AlojamientoService alojamientoService;
    private final ServicioService servicioService;
    
    public ConsumoServicioController(com.mycompany.avanceproyecto.view.ConsumoServicio view) {
        this.view = view;
        this.service = new ConsumoServicioService();
        this.alojamientoService = new AlojamientoService();
        this.servicioService = new ServicioService();
        inicializarControlador();
    }
    
    private void inicializarControlador() {
        // Configurar la tabla
        String[] columnas = {"ID", "Alojamiento", "Cliente", "Servicio", "Cantidad", "Precio Unit.", "Total"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        view.getTablaConsumo().setModel(modelo);

        // Agregar listeners a los botones
        view.getBtnNuevo().addActionListener(e -> limpiarFormulario());
        view.getBtnGuardar().addActionListener(e -> guardarConsumo());
        view.getBtnEliminar().addActionListener(e -> eliminarConsumo());
        view.getBtnBuscarAlojamiento().addActionListener(e -> mostrarSelectorAlojamientos());
        view.getBtnBuscarServicio().addActionListener(e -> mostrarSelectorServicios());

        // Cargar datos iniciales
        try {
            logger.info("Cargando datos iniciales...");
            cargarConsumos();
            logger.info("Datos iniciales cargados con éxito");
        } catch (Exception e) {
            logger.error("Error al cargar datos iniciales", e);
            JOptionPane.showMessageDialog(view, "Error al cargar datos iniciales: " + e.getMessage());
        }
    }
    
    private void guardarConsumo() {
        try {
            logger.debug("Iniciando proceso de guardado de consumo");
            
            // Validar datos
            if (view.getAlojamientoSeleccionado() == null) {
                throw new Exception("Debe seleccionar un alojamiento");
            }
            if (view.getServicioSeleccionado() == null) {
                throw new Exception("Debe seleccionar un servicio");
            }
            
            com.mycompany.avanceproyecto.model.ConsumoServicio consumo = 
                new com.mycompany.avanceproyecto.model.ConsumoServicio();
            consumo.setAlojamiento(view.getAlojamientoSeleccionado());
            consumo.setServicio(view.getServicioSeleccionado());
            consumo.setCantidad(Integer.parseInt(view.getTxtCantidad().getText()));
            
            service.registrarConsumo(consumo);
            
            JOptionPane.showMessageDialog(view, "Consumo registrado con éxito");
            
            limpiarFormulario();
            cargarConsumos();
            
        } catch (Exception e) {
            logger.error("Error al guardar consumo", e);
            JOptionPane.showMessageDialog(view, "Error al guardar: " + e.getMessage());
        }
    }

    private void cargarConsumos() {
        try {
            List<com.mycompany.avanceproyecto.model.ConsumoServicio> consumos = service.listarTodos();
            actualizarTabla(consumos);
        } catch (Exception e) {
            logger.error("Error al cargar consumos", e);
            JOptionPane.showMessageDialog(view, "Error al cargar consumos: " + e.getMessage());
        }
    }

    private void actualizarTabla(List<com.mycompany.avanceproyecto.model.ConsumoServicio> consumos) {
        DefaultTableModel modelo = (DefaultTableModel) view.getTablaConsumo().getModel();
        modelo.setRowCount(0);
        
        for (com.mycompany.avanceproyecto.model.ConsumoServicio c : consumos) {
            try {
                // Validar que los objetos no sean null antes de acceder a sus propiedades
                if (c.getAlojamiento() != null && c.getServicio() != null && 
                    c.getAlojamiento().getCliente() != null) {
                    
                    double total = c.getCantidad() * c.getServicio().getPrecio();
                    modelo.addRow(new Object[]{
                        c.getId(),
                        "Aloj. " + c.getAlojamiento().getId(),
                        c.getAlojamiento().getCliente().getNombre(),
                        c.getServicio().getNombre(),
                        c.getCantidad(),
                        c.getServicio().getPrecio(),
                        total
                    });
                } else {
                    logger.warn("ConsumoServicio con ID {} tiene datos incompletos", c.getId());
                }
            } catch (Exception e) {
                logger.error("Error al agregar fila para ConsumoServicio ID {}: {}", c.getId(), e.getMessage());
            }
        }
        
        view.actualizarTotalRegistros(modelo.getRowCount());
    }

    private void eliminarConsumo() {
        int fila = view.getTablaConsumo().getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(view, "Por favor, seleccione un consumo");
            return;
        }

        int id = (Integer) view.getTablaConsumo().getValueAt(fila, 0);
        String servicio = (String) view.getTablaConsumo().getValueAt(fila, 3);

        int confirmacion = JOptionPane.showConfirmDialog(view,
            "¿Está seguro que desea eliminar el consumo de " + servicio + "?",
            "Confirmar Eliminación",
            JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                service.eliminarConsumo(id);
                JOptionPane.showMessageDialog(view, "Consumo eliminado con éxito");
                cargarConsumos();
                limpiarFormulario();
            } catch (Exception e) {
                logger.error("Error al eliminar consumo", e);
                JOptionPane.showMessageDialog(view, "Error al eliminar: " + e.getMessage());
            }
        }
    }

    private void limpiarFormulario() {
        view.limpiarFormulario();
    }

    private void mostrarSelectorAlojamientos() {
        try {
            String[] columnas = {"ID", "Cliente", "Habitación", "Fecha Entrada", "Fecha Salida"};
            DefaultTableModel modelo = new DefaultTableModel(columnas, 0);
            JTable tablaTemp = new JTable(modelo);
            
            List<Alojamientos> alojamientos = alojamientoService.listarAlojamientos();
            for (Alojamientos a : alojamientos) {
                modelo.addRow(new Object[]{
                    a.getId(),
                    a.getCliente().getNombre(),
                    a.getHabitacion().getNumero(),
                    a.getFechaEntrada(),
                    a.getFechaSalida()
                });
            }
            
            JScrollPane scroll = new JScrollPane(tablaTemp);
            int result = JOptionPane.showConfirmDialog(view, scroll, 
                "Seleccionar Alojamiento", JOptionPane.OK_CANCEL_OPTION);
                
            if (result == JOptionPane.OK_OPTION) {
                int fila = tablaTemp.getSelectedRow();
                if (fila != -1) {
                    view.getTxtIdAlojamiento().setText(tablaTemp.getValueAt(fila, 0).toString());
                    view.getTxtAlojamiento().setText(tablaTemp.getValueAt(fila, 1).toString());
                }
            }
        } catch (Exception e) {
            logger.error("Error al mostrar selector de alojamientos", e);
            JOptionPane.showMessageDialog(view, "Error al cargar alojamientos");
        }
    }

    private void mostrarSelectorServicios() {
        try {
            String[] columnas = {"ID", "Nombre", "Precio"};
            DefaultTableModel modelo = new DefaultTableModel(columnas, 0);
            JTable tablaTemp = new JTable(modelo);
            
            List<Servicios> servicios = servicioService.listarServicios();
            for (Servicios s : servicios) {
                modelo.addRow(new Object[]{
                    s.getId(),
                    s.getNombre(),
                    s.getPrecio()
                });
            }
            
            JScrollPane scroll = new JScrollPane(tablaTemp);
            int result = JOptionPane.showConfirmDialog(view, scroll, 
                "Seleccionar Servicio", JOptionPane.OK_CANCEL_OPTION);
                
            if (result == JOptionPane.OK_OPTION) {
                int fila = tablaTemp.getSelectedRow();
                if (fila != -1) {
                    view.getTxtIdServicio().setText(tablaTemp.getValueAt(fila, 0).toString());
                    view.getTxtServicio().setText(tablaTemp.getValueAt(fila, 1).toString());
                    view.getTxtPrecio().setText(tablaTemp.getValueAt(fila, 2).toString());
                    view.getTxtPrecio().setEditable(false);
                }
            }
        } catch (Exception e) {
            logger.error("Error al mostrar selector de servicios", e);
            JOptionPane.showMessageDialog(view, "Error al cargar servicios");
        }
    }
}

