package com.mycompany.avanceproyecto.controller;

import com.mycompany.avanceproyecto.model.Facturas;
import com.mycompany.avanceproyecto.model.Alojamientos;
import com.mycompany.avanceproyecto.model.Clientes;
import com.mycompany.avanceproyecto.service.FacturaService;
import com.mycompany.avanceproyecto.service.AlojamientoService;
import com.mycompany.avanceproyecto.service.ClienteService;
import com.mycompany.avanceproyecto.service.ConsumoServicioService;
import com.mycompany.avanceproyecto.service.HabitacionService;

import com.mycompany.avanceproyecto.view.Factura;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FacturaController {
    private static final Logger logger = LoggerFactory.getLogger(FacturaController.class);
    private final Factura view;
    private final FacturaService service;
    private final AlojamientoService alojamientoService;
    private final ConsumoServicioService consumoService;
    private final ClienteService clienteService;
    
    public FacturaController(Factura view) {
        this.view = view;
        this.service = new FacturaService();
        this.alojamientoService = new AlojamientoService();
        this.consumoService = new ConsumoServicioService();
        this.clienteService = new ClienteService();

        inicializarControlador();
    }
    
    private void inicializarControlador() {
        // Configurar eventos de botones
        view.getBtnNuevo().addActionListener(e -> limpiarFormulario());
        view.getBtnGuardar().addActionListener(e -> generarFactura());
        view.getBtnEliminar().addActionListener(e -> eliminarFactura());
        view.getBtnBuscar().addActionListener(e -> mostrarSelectorClientes()); // Ahora es "Buscar Cliente"
        
        // Agregar listener para la tabla de alojamientos
        view.getTablaFacturas().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = view.getTablaFacturas().getSelectedRow();
                if (row != -1) {
                    cargarConsumosAlojamiento(row);
                }
            }
        });
        
        // Cargar datos iniciales
        try {
            logger.info("Vista de facturación inicializada");
        } catch (Exception e) {
            logger.error("Error al inicializar vista", e);
            JOptionPane.showMessageDialog(view, "Error al inicializar: " + e.getMessage());
        }
    }
    
    private void mostrarSelectorClientes() {
        try {
            String[] columnas = {"ID", "Nombre", "DNI", "Teléfono"};
            DefaultTableModel modelo = new DefaultTableModel(columnas, 0);
            JTable tablaTemp = new JTable(modelo);
            
            List<Clientes> clientes = clienteService.listarClientes();
            for (Clientes c : clientes) {
                modelo.addRow(new Object[]{
                    c.getId(),
                    c.getNombre(),
                    c.getDni(),
                    c.getTelefono()
                });
            }
            
            JScrollPane scroll = new JScrollPane(tablaTemp);
            int result = JOptionPane.showConfirmDialog(view, scroll, 
                "Seleccionar Cliente", JOptionPane.OK_CANCEL_OPTION);
                
            if (result == JOptionPane.OK_OPTION) {
                int fila = tablaTemp.getSelectedRow();
                if (fila != -1) {
                    int idCliente = (Integer) tablaTemp.getValueAt(fila, 0);
                    String nombreCliente = (String) tablaTemp.getValueAt(fila, 1);
                    
                    // Actualizar datos del cliente
                    view.getTxtIdAlojamiento().setText(String.valueOf(idCliente));
                    view.getTxtCliente().setText(nombreCliente);
                    
                    // Cargar alojamientos del cliente
                    cargarAlojamientosDelCliente(idCliente);
                }
            }
        } catch (Exception e) {
            logger.error("Error al mostrar selector de clientes", e);
            JOptionPane.showMessageDialog(view, "Error al cargar clientes");
        }
    }
    
    private void cargarAlojamientosDelCliente(int idCliente) {
        try {
            List<Alojamientos> todosAlojamientos = alojamientoService.listarAlojamientos();
            List<Alojamientos> alojamientosCliente = todosAlojamientos.stream()
                .filter(a -> a.getCliente().getId() == idCliente)
                .collect(Collectors.toList());
            
            view.actualizarTablaAlojamientos(alojamientosCliente);
            
            if (alojamientosCliente.isEmpty()) {
                JOptionPane.showMessageDialog(view, 
                    "El cliente seleccionado no tiene alojamientos registrados",
                    "Sin alojamientos", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            logger.error("Error al cargar alojamientos del cliente", e);
            JOptionPane.showMessageDialog(view, "Error al cargar alojamientos del cliente");
        }
    }
    
    public void cargarConsumosAlojamiento(int filaSeleccionada) {
        try {
            int idAlojamiento = (Integer) view.getTablaFacturas().getValueAt(filaSeleccionada, 0);
            
            // Cargar consumos del alojamiento
            List<com.mycompany.avanceproyecto.model.ConsumoServicio> consumos = 
                consumoService.listarPorAlojamiento(idAlojamiento);
            
            view.actualizarTablaConsumos(consumos);
            
            // Actualizar campos con información del alojamiento seleccionado
            String habitacion = (String) view.getTablaFacturas().getValueAt(filaSeleccionada, 1);
            double precioHabitacion = (Double) view.getTablaFacturas().getValueAt(filaSeleccionada, 4);
            
            view.getTxtHabitacion().setText(habitacion);
            view.getTxtSubtotal().setText(String.valueOf(precioHabitacion));
            
            // Calcular total automáticamente
            double totalConsumos = consumos.stream()
                .mapToDouble(c -> c.getCantidad() * c.getServicio().getPrecio())
                .sum();
            
            double total = precioHabitacion + totalConsumos;
            view.getTxtTotal().setText(String.format("%.2f", total));
            
            logger.info("Consumos cargados para alojamiento ID: {}", idAlojamiento);
        } catch (Exception e) {
            logger.error("Error al cargar consumos del alojamiento", e);
            JOptionPane.showMessageDialog(view, "Error al cargar consumos: " + e.getMessage());
        }
    }
    
    private void generarFactura() {
        try {
            int filaAlojamiento = view.getTablaFacturas().getSelectedRow();
            if (filaAlojamiento == -1) {
                JOptionPane.showMessageDialog(view, 
                    "Por favor, seleccione un alojamiento para facturar",
                    "Seleccionar Alojamiento", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            int idAlojamiento = (Integer) view.getTablaFacturas().getValueAt(filaAlojamiento, 0);
            Alojamientos alojamiento = alojamientoService.obtenerAlojamiento(idAlojamiento);
            
            if (alojamiento != null) {
                service.generarFactura(alojamiento);
                new HabitacionService().actualizarHabitacionOcupada(alojamiento.getHabitacion().getId(), true);
                JOptionPane.showMessageDialog(view, 
                    "Factura generada con éxito\nTotal: S/ " + view.getTxtTotal().getText(),
                    "Factura Generada", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Mantener los datos mostrados para poder imprimir
                // No limpiar automáticamente
            }
        } catch (Exception e) {
            logger.error("Error al generar factura", e);
            JOptionPane.showMessageDialog(view, "Error al generar factura: " + e.getMessage());
        }
    }
    
    private void cargarDetalleAlojamiento(Alojamientos alojamiento) {
        try {
            // Llenar campos con datos del alojamiento
            view.getTxtIdAlojamiento().setText(String.valueOf(alojamiento.getId()));
            view.getTxtCliente().setText(alojamiento.getCliente().getNombre());
            view.getTxtHabitacion().setText(alojamiento.getHabitacion().getNumero());
            view.getTxtSubtotal().setText(String.valueOf(alojamiento.getHabitacion().getPrecio()));
            
            // Cargar consumos del alojamiento
            List<com.mycompany.avanceproyecto.model.ConsumoServicio> consumos = 
                consumoService.listarPorAlojamiento(alojamiento.getId());
            
            DefaultTableModel modeloConsumo = (DefaultTableModel) view.getTablaConsumos().getModel();
            modeloConsumo.setRowCount(0);
            
            double totalServicios = 0.0;
            for (com.mycompany.avanceproyecto.model.ConsumoServicio c : consumos) {
                double totalItem = c.getCantidad() * c.getServicio().getPrecio();
                totalServicios += totalItem;
                
                modeloConsumo.addRow(new Object[]{
                    c.getServicio().getNombre(),
                    c.getCantidad(),
                    c.getServicio().getPrecio(),
                    totalItem
                });
            }
            
            double total = alojamiento.getHabitacion().getPrecio() + totalServicios;
            view.getTxtTotal().setText(String.valueOf(total));
            
        } catch (Exception e) {
            logger.error("Error al cargar detalle del alojamiento", e);
        }
    }
    
    private void cargarFacturas() {
        try {
            List<Facturas> facturas = service.listarFacturas();
            // Comentar esta línea por ahora hasta que se implemente correctamente
            // view.actualizarTablaFacturas(facturas);
            logger.info("Facturas cargadas: {}", facturas.size());
        } catch (Exception e) {
            logger.error("Error al cargar facturas", e);
            JOptionPane.showMessageDialog(view, "Error al cargar facturas: " + e.getMessage());
        }
    }
    
    private void eliminarFactura() {
        int fila = view.getTablaFacturas().getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(view, "Por favor, seleccione una factura");
            return;
        }

        int id = (Integer) view.getTablaFacturas().getValueAt(fila, 0);
        String cliente = (String) view.getTablaFacturas().getValueAt(fila, 2);

        int confirmacion = JOptionPane.showConfirmDialog(view,
            "¿Está seguro que desea eliminar la factura del cliente " + cliente + "?",
            "Confirmar Eliminación",
            JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                service.eliminarFactura(id);
                JOptionPane.showMessageDialog(view, "Factura eliminada con éxito");
                cargarFacturas();
                limpiarFormulario();
            } catch (Exception e) {
                logger.error("Error al eliminar factura", e);
                JOptionPane.showMessageDialog(view, "Error al eliminar: " + e.getMessage());
            }
        }
    }
    
    private void limpiarFormulario() {
        view.limpiarFormulario();
    }
}
