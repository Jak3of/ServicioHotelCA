package com.mycompany.avanceproyecto.controller;

import com.mycompany.avanceproyecto.model.Alojamientos;
import com.mycompany.avanceproyecto.model.Servicios;
import com.mycompany.avanceproyecto.service.AlojamientoService;
import com.mycompany.avanceproyecto.view.Alojamiento;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import com.mycompany.avanceproyecto.model.Clientes;
import com.mycompany.avanceproyecto.model.Habitaciones;
import com.mycompany.avanceproyecto.service.ClienteService;
import com.mycompany.avanceproyecto.service.HabitacionService;
import com.mycompany.avanceproyecto.view.SeleccionarHabitaciones;

public class AlojamientoController {
    private static final Logger logger = LoggerFactory.getLogger(AlojamientoController.class);
    private final Alojamiento view;
    private final AlojamientoService service;
    private final ClienteService clienteService;
    private final HabitacionService habitacionService = new HabitacionService(); // Asegúrate de tener este servicio
    
    public AlojamientoController(Alojamiento view) {
        this.view = view;
        this.service = new AlojamientoService();
        this.clienteService = new ClienteService();
        inicializarControlador();
    }
    
    private void inicializarControlador() {
        // Configurar la tabla primero
        String[] columnas = {"ID", "Cliente", "Habitación", "Fecha Entrada", "Fecha Salida", "Costo", "Estado"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        view.getTablaAlojamientos().setModel(modelo);

        // Agregar listeners a los botones
        view.getBtnNuevo().addActionListener(e -> limpiarFormulario());
        view.getBtnGuardar().addActionListener(e -> guardarAlojamiento());
        view.getBtnEliminar().addActionListener(e -> eliminarAlojamiento());
        view.getBtnBuscar().addActionListener(e -> buscarAlojamientos());
        view.getBtnBuscarCliente().addActionListener(e -> mostrarSelectorClientes());
        view.getBtnBuscarHabitacion().addActionListener(e -> mostrarSelectorHabitaciones());

        // Cargar datos iniciales
        try {
            logger.info("Cargando datos iniciales...");
            cargarAlojamientos();
            logger.info("Datos iniciales cargados con éxito");
        } catch (Exception e) {
            logger.error("Error al cargar datos iniciales", e);
            JOptionPane.showMessageDialog(view, "Error al cargar datos iniciales: " + e.getMessage());
        }
    }
    
    private void guardarAlojamiento() {
        try {
            logger.debug("Iniciando proceso de guardado de alojamiento");
            Alojamientos alojamiento = new Alojamientos();
            
            // Obtener datos del formulario
            int idAlojamiento = view.getTxtIdAlojamiento().getText().isEmpty() ? 0 : 
                               Integer.parseInt(view.getTxtIdAlojamiento().getText());
            alojamiento.setId(idAlojamiento);
            
            // Convertir java.util.Date a LocalDate
            java.util.Date fechaEntrada = view.getFechaEntrada().getDate();
            java.util.Date fechaSalida = view.getFechaSalida().getDate();
            
            if (fechaEntrada != null && fechaSalida != null) {
                alojamiento.setFechaEntrada(fechaEntrada.toInstant()
                    .atZone(java.time.ZoneId.systemDefault())
                    .toLocalDate());
                    
                alojamiento.setFechaSalida(fechaSalida.toInstant()
                    .atZone(java.time.ZoneId.systemDefault())
                    .toLocalDate());
            } else {
                throw new Exception("Las fechas no pueden estar vacías");
            }
            
            alojamiento.setCliente(view.getClienteSeleccionado());
            alojamiento.setHabitacion(view.getHabitacionSeleccionada());
            
            // MANEJO DE ESTADOS DE HABITACIONES
            boolean esNuevoAlojamiento = (idAlojamiento == 0);
            
            if (esNuevoAlojamiento) {
                // NUEVO ALOJAMIENTO: Marcar habitación como ocupada
                logger.debug("Nuevo alojamiento - marcando habitación {} como ocupada", 
                           alojamiento.getHabitacion().getId());
                service.guardarOActualizarAlojamiento(alojamiento);
                habitacionService.actualizarHabitacionOcupada(alojamiento.getHabitacion().getId(), false); // false = ocupada
                
            } else {
                // ACTUALIZACIÓN: Verificar si cambió la habitación
                Alojamientos alojamientoAnterior = service.obtenerAlojamiento(idAlojamiento);
                int habitacionAnteriorId = alojamientoAnterior.getHabitacion().getId();
                int habitacionNuevaId = alojamiento.getHabitacion().getId();
                
                if (habitacionAnteriorId != habitacionNuevaId) {
                    // Cambió la habitación: liberar la anterior y ocupar la nueva
                    logger.debug("Cambio de habitación: liberando {} y ocupando {}", 
                               habitacionAnteriorId, habitacionNuevaId);
                    
                    // Actualizar el alojamiento primero
                    service.guardarOActualizarAlojamiento(alojamiento);
                    
                    // Liberar habitación anterior (disponible = true)
                    habitacionService.actualizarHabitacionOcupada(habitacionAnteriorId, true);
                    
                    // Ocupar nueva habitación (disponible = false)
                    habitacionService.actualizarHabitacionOcupada(habitacionNuevaId, false);
                    
                } else {
                    // Misma habitación: solo actualizar datos del alojamiento
                    logger.debug("Misma habitación - solo actualizando datos del alojamiento");
                    service.guardarOActualizarAlojamiento(alojamiento);
                }
            }
            
            JOptionPane.showMessageDialog(view, 
                esNuevoAlojamiento ? "Alojamiento guardado con éxito" : 
                                   "Alojamiento actualizado con éxito");
            
            limpiarFormulario();
            cargarAlojamientos();
            
        } catch (Exception e) {
            logger.error("Error al guardar alojamiento", e);
            JOptionPane.showMessageDialog(view, "Error al guardar: " + e.getMessage());
        }
    }

    private void cargarAlojamientos() {
        try {
            // Solo mostrar alojamientos activos en la gestión de alojamientos
            List<Alojamientos> alojamientos = service.listarAlojamientosActivos();
            actualizarTabla(alojamientos);
        } catch (Exception e) {
            logger.error("Error al cargar alojamientos", e);
            JOptionPane.showMessageDialog(view, "Error al cargar alojamientos: " + e.getMessage());
        }
    }
    
    // Método público para recargar la tabla desde otros controladores
    public void recargarTabla() {
        cargarAlojamientos();
    }

    private void actualizarTabla(List<Alojamientos> alojamientos) {
        DefaultTableModel modelo = (DefaultTableModel) view.getTablaAlojamientos().getModel();
        modelo.setRowCount(0);
        
        for (Alojamientos a : alojamientos) {
            // Formatear estado con color visual
            String estadoFormatted = a.getEstado().getValor();
            if (a.isActivo()) {
                estadoFormatted = "🟡 " + estadoFormatted;
            } else if (a.isPagado()) {
                estadoFormatted = "🟢 " + estadoFormatted;
            } else if (a.isFinalizado()) {
                estadoFormatted = "⚪ " + estadoFormatted;
            }
            
            modelo.addRow(new Object[]{
                a.getId(),
                a.getCliente().getNombre() + " (DNI: " + a.getCliente().getDni() + ")", // Mostrar nombre y DNI
                "Hab. " + a.getHabitacion().getNumero() + " - " + a.getHabitacion().getTipo(), // Mostrar número y tipo
                a.getFechaEntrada(),
                a.getFechaSalida(),
                a.getHabitacion().getPrecio(),  // Mostrar el precio de la habitación como costo
                estadoFormatted  // Estado con indicador visual
            });
        }
        
        view.actualizarTotalRegistros(alojamientos.size());
    }

    private void eliminarAlojamiento() {
        int fila = view.getTablaAlojamientos().getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(view, "Por favor, seleccione un alojamiento");
            return;
        }

        int id = (Integer) view.getTablaAlojamientos().getValueAt(fila, 0);
        String cliente = (String) view.getTablaAlojamientos().getValueAt(fila, 1);

        int confirmacion = JOptionPane.showConfirmDialog(view,
            "¿Está seguro que desea eliminar el alojamiento del cliente " + cliente + "?",
            "Confirmar Eliminación",
            JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                // Eliminar alojamiento (incluye eliminación de servicios asociados)
                service.eliminarAlojamiento(id);
                
                JOptionPane.showMessageDialog(view, "Alojamiento eliminado con éxito");
                cargarAlojamientos();
                limpiarFormulario();
            } catch (Exception e) {
                logger.error("Error al eliminar alojamiento", e);
                
                // Mostrar mensaje más específico según el tipo de error
                String mensaje;
                if (e.getMessage().contains("facturas asociadas")) {
                    mensaje = "❌ NO SE PUEDE ELIMINAR\n\n" +
                             "Este alojamiento tiene una FACTURA asociada.\n" +
                             "Para eliminarlo debe:\n\n" +
                             "💡 OPCIONES:\n" +
                             "1. Si aún no ha pagado → Ir a 'Facturas' y procesar el pago\n" +
                             "2. Si ya pagó → El alojamiento se elimina automáticamente\n" +
                             "3. Si hay error en facturación → Eliminar la factura primero";
                } else if (e.getMessage().contains("FOREIGN KEY constraint")) {
                    mensaje = "❌ ERROR DE INTEGRIDAD\n\n" +
                             "No se puede eliminar porque tiene registros relacionados.\n" +
                             "Verifique facturas o servicios asociados.";
                } else {
                    mensaje = "❌ ERROR AL ELIMINAR\n\n" + e.getMessage();
                }
                
                JOptionPane.showMessageDialog(view, mensaje, "Error al Eliminar", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void limpiarFormulario() {
        view.limpiarFormulario();
    }

    private void buscarAlojamientos() {
        // Implementar búsqueda según los criterios necesarios
        
    }

    private void mostrarSelectorClientes() {
        try {
            // Crear tabla temporal para mostrar clientes
            String[] columnas = {"ID", "Nombre", "DNI"};
            DefaultTableModel modelo = new DefaultTableModel(columnas, 0);
            JTable tablaTemp = new JTable(modelo);
            
            // Cargar datos de clientes
            List<Clientes> clientes = clienteService.listarClientes();
            for (Clientes c : clientes) {
                modelo.addRow(new Object[]{c.getId(), c.getNombre(), c.getDni()});
            }
            
            // Mostrar diálogo con tabla
            JScrollPane scroll = new JScrollPane(tablaTemp);
            int result = JOptionPane.showConfirmDialog(view, scroll, 
                "Seleccionar Cliente", JOptionPane.OK_CANCEL_OPTION);
                
            if (result == JOptionPane.OK_OPTION) {
                int fila = tablaTemp.getSelectedRow();
                if (fila != -1) {
                    // Usar el nuevo método para establecer cliente seleccionado
                    int idCliente = (Integer) tablaTemp.getValueAt(fila, 0);
                    String nombreCliente = tablaTemp.getValueAt(fila, 1).toString();
                    int dniCliente = (Integer) tablaTemp.getValueAt(fila, 2);
                    
                    view.setClienteSeleccionado(idCliente, nombreCliente, dniCliente);
                }
            }
        } catch (Exception e) {
            logger.error("Error al mostrar selector de clientes", e);
            JOptionPane.showMessageDialog(view, "Error al cargar clientes");
        }
    }

    private void mostrarSelectorHabitaciones() {
        try {
            // Verificar si estamos editando un alojamiento existente
            int habitacionActualId = 0;
            if (!view.getTxtIdAlojamiento().getText().isEmpty()) {
                // Estamos editando: obtener la habitación actual
                int idAlojamiento = Integer.parseInt(view.getTxtIdAlojamiento().getText());
                Alojamientos alojamientoActual = service.obtenerAlojamiento(idAlojamiento);
                habitacionActualId = alojamientoActual.getHabitacion().getId();
                logger.debug("Editando alojamiento {} - habitación actual: {}", idAlojamiento, habitacionActualId);
            }
            
            SeleccionarHabitaciones selector = new SeleccionarHabitaciones(view, habitacionActualId);
            selector.setVisible(true);
        } catch (Exception e) {
            logger.error("Error al mostrar selector de habitaciones", e);
            // Fallback: mostrar selector sin habitación actual
            SeleccionarHabitaciones selector = new SeleccionarHabitaciones(view);
            selector.setVisible(true);
        }
    }
}