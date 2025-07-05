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

        // Agregar listener para la selección de la tabla
        view.getTablaConsumo().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = view.getTablaConsumo().getSelectedRow();
                if (selectedRow != -1) {
                    logger.info("Fila seleccionada: {}", selectedRow);
                    // La vista ya maneja esto en su propio listener
                }
            }
        });

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
            
            // Validar campos básicos primero
            String idAlojamientoText = view.getTxtIdAlojamiento().getText().trim();
            String idServicioText = view.getTxtIdServicio().getText().trim();
            String cantidadText = view.getTxtCantidad().getText().trim();
            
            if (idAlojamientoText.isEmpty()) {
                JOptionPane.showMessageDialog(view, "Debe seleccionar un alojamiento usando el botón BUSCAR");
                return;
            }
            
            if (idServicioText.isEmpty()) {
                JOptionPane.showMessageDialog(view, "Debe seleccionar un servicio usando el botón BUSCAR");
                return;
            }
            
            if (cantidadText.isEmpty()) {
                JOptionPane.showMessageDialog(view, "Debe ingresar la cantidad");
                view.getTxtCantidad().requestFocus();
                return;
            }
            
            // Validar que los objetos seleccionados existan
            Alojamientos alojamientoSeleccionado = view.getAlojamientoSeleccionado();
            Servicios servicioSeleccionado = view.getServicioSeleccionado();
            
            if (alojamientoSeleccionado == null) {
                JOptionPane.showMessageDialog(view, 
                    "Error: No se pudo obtener la información del alojamiento.\n" +
                    "Por favor, seleccione nuevamente un alojamiento.",
                    "Error de Alojamiento", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (servicioSeleccionado == null) {
                JOptionPane.showMessageDialog(view, 
                    "Error: No se pudo obtener la información del servicio.\n" +
                    "Por favor, seleccione nuevamente un servicio.",
                    "Error de Servicio", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Validar cantidad
            int cantidad;
            try {
                cantidad = Integer.parseInt(cantidadText);
                if (cantidad <= 0) {
                    JOptionPane.showMessageDialog(view, "La cantidad debe ser mayor a 0");
                    return;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(view, "La cantidad debe ser un número válido");
                return;
            }
            
            // Crear el objeto consumo
            com.mycompany.avanceproyecto.model.ConsumoServicio consumo = 
                new com.mycompany.avanceproyecto.model.ConsumoServicio();
            consumo.setAlojamiento(alojamientoSeleccionado);
            consumo.setServicio(servicioSeleccionado);
            consumo.setCantidad(cantidad);
            
            // Guardar en la base de datos
            service.registrarConsumo(consumo);
            
            JOptionPane.showMessageDialog(view, 
                "Consumo registrado con éxito\n" +
                "Alojamiento: " + alojamientoSeleccionado.getId() + "\n" +
                "Servicio: " + servicioSeleccionado.getNombre() + "\n" +
                "Cantidad: " + cantidad);
            
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
                        c.getId(),                              // Columna 0: ID del consumo
                        "Aloj. " + c.getAlojamiento().getId(),  // Columna 1: ID del alojamiento
                        c.getAlojamiento().getCliente().getNombre(), // Columna 2: Cliente
                        c.getServicio().getNombre(),            // Columna 3: Servicio
                        c.getCantidad(),                        // Columna 4: Cantidad
                        c.getServicio().getPrecio(),            // Columna 5: Precio unitario
                        String.format("%.2f", total)           // Columna 6: Total
                    });
                } else {
                    logger.warn("ConsumoServicio con ID {} tiene datos incompletos", c.getId());
                }
            } catch (Exception e) {
                logger.error("Error al agregar fila para ConsumoServicio ID {}: {}", c.getId(), e.getMessage());
            }
        }
        
        view.actualizarTotalRegistros(modelo.getRowCount());
        
        // Limpiar selección
        view.getTablaConsumo().clearSelection();
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
        view.getTablaConsumo().clearSelection();
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
            scroll.setPreferredSize(new java.awt.Dimension(600, 300));
            
            int result = JOptionPane.showConfirmDialog(view, scroll, 
                "Seleccionar Alojamiento", JOptionPane.OK_CANCEL_OPTION, 
                JOptionPane.PLAIN_MESSAGE);
                
            if (result == JOptionPane.OK_OPTION) {
                int fila = tablaTemp.getSelectedRow();
                if (fila != -1) {
                    int idAlojamiento = (Integer) tablaTemp.getValueAt(fila, 0);
                    String cliente = (String) tablaTemp.getValueAt(fila, 1);
                    String habitacion = (String) tablaTemp.getValueAt(fila, 2);
                    
                    // Actualizar los campos de la vista
                    view.getTxtIdAlojamiento().setText(String.valueOf(idAlojamiento));
                    view.getTxtAlojamiento().setText(cliente + " - Hab. " + habitacion);
                    
                    logger.info("Alojamiento seleccionado: ID {}, Cliente: {}", idAlojamiento, cliente);
                } else {
                    JOptionPane.showMessageDialog(view, "Por favor, seleccione un alojamiento de la lista");
                }
            }
        } catch (Exception e) {
            logger.error("Error al mostrar selector de alojamientos", e);
            JOptionPane.showMessageDialog(view, "Error al cargar alojamientos: " + e.getMessage());
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
                    String.format("S/ %.2f", s.getPrecio())
                });
            }
            
            JScrollPane scroll = new JScrollPane(tablaTemp);
            scroll.setPreferredSize(new java.awt.Dimension(500, 300));
            
            int result = JOptionPane.showConfirmDialog(view, scroll, 
                "Seleccionar Servicio", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);
                
            if (result == JOptionPane.OK_OPTION) {
                int fila = tablaTemp.getSelectedRow();
                if (fila != -1) {
                    int idServicio = (Integer) tablaTemp.getValueAt(fila, 0);
                    String nombreServicio = (String) tablaTemp.getValueAt(fila, 1);
                    double precio = servicios.get(fila).getPrecio(); // Obtener precio real
                    
                    // Actualizar los campos de la vista
                    view.getTxtIdServicio().setText(String.valueOf(idServicio));
                    view.getTxtServicio().setText(nombreServicio);
                    view.getTxtPrecio().setText(String.valueOf(precio));
                    view.getTxtPrecio().setEditable(false);
                    
                    logger.info("Servicio seleccionado: ID {}, Nombre: {}, Precio: {}", 
                               idServicio, nombreServicio, precio);
                } else {
                    JOptionPane.showMessageDialog(view, "Por favor, seleccione un servicio de la lista");
                }
            }
        } catch (Exception e) {
            logger.error("Error al mostrar selector de servicios", e);
            JOptionPane.showMessageDialog(view, "Error al cargar servicios: " + e.getMessage());
        }
    }
}

