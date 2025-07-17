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
    
    // Variable para mantener el ID del cliente actual
    private int clienteActualId = -1;
    
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
            // Guardar el ID del cliente actual para futuras actualizaciones
            this.clienteActualId = idCliente;
            
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
    
    // Método para actualizar la tabla de alojamientos después de un pago
    private void actualizarTablaAlojamientos() {
        if (clienteActualId != -1) {
            logger.info("Actualizando tabla de alojamientos para cliente ID: {}", clienteActualId);
            cargarAlojamientosDelCliente(clienteActualId);
        } else {
            logger.warn("No hay cliente seleccionado para actualizar la tabla");
        }
    }
    
    // Método para buscar alojamientos por DNI del cliente
    public void buscarAlojamientosPorDni(int dni) {
        try {
            // Buscar cliente por DNI
            Clientes cliente = clienteService.buscarPorDni(dni);
            
            if (cliente != null) {
                // Cliente encontrado - cargar sus alojamientos
                view.getTxtIdAlojamiento().setText(String.valueOf(cliente.getId()));
                view.getTxtCliente().setText(cliente.getNombre());
                
                cargarAlojamientosDelCliente(cliente.getId());
                
                logger.info("Cliente encontrado: {} (DNI: {})", cliente.getNombre(), dni);
            } else {
                // Cliente no encontrado - mostrar opciones
                String[] opciones = {"Ver lista de clientes", "Cancelar"};
                int respuesta = JOptionPane.showOptionDialog(view, 
                    "Cliente con DNI '" + dni + "' no encontrado.\n¿Qué desea hacer?", 
                    "Cliente no encontrado", 
                    JOptionPane.YES_NO_OPTION, 
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    opciones,
                    opciones[0]);
                
                if (respuesta == 0) { // Ver lista de clientes
                    mostrarSelectorClientes();
                }
                // Si respuesta == 1 (Cancelar), no hace nada
                
                logger.info("Cliente con DNI {} no encontrado", dni);
            }
            
        } catch (Exception e) {
            logger.error("Error al buscar cliente por DNI: {}", dni, e);
            JOptionPane.showMessageDialog(view, 
                "Error al buscar cliente: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
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
            // Primero validar que el alojamiento seleccionado se puede pagar
            if (!view.validarAlojamientoPago()) {
                return; // La validación ya muestra el mensaje de error
            }
            
            int filaAlojamiento = view.getTablaFacturas().getSelectedRow();
            int idAlojamiento = (Integer) view.getTablaFacturas().getValueAt(filaAlojamiento, 0);
            Alojamientos alojamiento = alojamientoService.obtenerAlojamiento(idAlojamiento);
            
            if (alojamiento != null) {
                // Confirmar la facturación
                int confirmacion = JOptionPane.showConfirmDialog(view,
                    "¿Confirma que desea procesar el pago y liberar la habitación?\n\n" +
                    "Cliente: " + alojamiento.getCliente().getNombre() + "\n" +
                    "Habitación: " + alojamiento.getHabitacion().getNumero() + "\n" +
                    "Total: S/ " + view.getTxtTotal().getText(),
                    "Confirmar Pago",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
                    
                if (confirmacion == JOptionPane.YES_OPTION) {
                    try {
                        // 1. Generar la factura
                        service.generarFactura(alojamiento);
                        logger.info("✓ Factura generada para alojamiento ID: {}", idAlojamiento);
                        
                        // 2. Actualizar estado del alojamiento a PAGADO
                        logger.info("Intentando actualizar estado del alojamiento {} a PAGADO...", idAlojamiento);
                        alojamientoService.actualizarEstado(idAlojamiento, Alojamientos.EstadoAlojamiento.PAGADO);
                        logger.info("✓ Estado del alojamiento {} actualizado a PAGADO exitosamente", idAlojamiento);
                        
                        // 3. Verificar que el estado se actualizó correctamente
                        Alojamientos alojamientoActualizado = alojamientoService.obtenerAlojamiento(idAlojamiento);
                        logger.info("Estado verificado en BD: {}", alojamientoActualizado.getEstado().getValor());
                        
                        // 4. Liberar la habitación (marcarla como disponible)
                        HabitacionService habitacionService = new HabitacionService();
                        habitacionService.actualizarHabitacionOcupada(alojamiento.getHabitacion().getId(), true);
                        logger.info("✓ Habitación {} liberada después del pago", alojamiento.getHabitacion().getNumero());
                        
                        // 5. Mostrar confirmación
                        JOptionPane.showMessageDialog(view, 
                            "✅ PAGO PROCESADO CON ÉXITO\n\n" +
                            "• Factura generada\n" +
                            "• Estado cambiado a: " + alojamientoActualizado.getEstado().getValor() + "\n" +
                            "• Habitación " + alojamiento.getHabitacion().getNumero() + " liberada\n" +
                            "• Total pagado: S/ " + view.getTxtTotal().getText() + "\n\n" +
                            "La habitación ya está disponible para nuevos huéspedes.",
                            "Pago Completado", 
                            JOptionPane.INFORMATION_MESSAGE);
                        
                        // 6. Actualizar la tabla para reflejar el nuevo estado
                        actualizarTablaAlojamientos();
                        logger.info("✓ Tabla de alojamientos actualizada en la vista");
                        
                    } catch (Exception errorPago) {
                        logger.error("❌ Error específico en el proceso de pago", errorPago);
                        JOptionPane.showMessageDialog(view, 
                            "❌ ERROR EN EL PROCESO DE PAGO\n\n" +
                            "Error específico: " + errorPago.getMessage() + "\n\n" +
                            "Por favor, verifique los logs para más detalles.",
                            "Error en Procesamiento",
                            JOptionPane.ERROR_MESSAGE);
                    }
                    
                    // Mantener los datos mostrados para poder imprimir
                    // No limpiar automáticamente
                }
            }
        } catch (Exception e) {
            logger.error("Error al generar factura", e);
            JOptionPane.showMessageDialog(view, 
                "❌ ERROR AL PROCESAR PAGO\n\n" + e.getMessage(),
                "Error en Facturación",
                JOptionPane.ERROR_MESSAGE);
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
