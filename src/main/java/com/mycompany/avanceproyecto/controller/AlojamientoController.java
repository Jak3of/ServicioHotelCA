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
        String[] columnas = {"ID", "Cliente", "Habitación", "Fecha Entrada", "Fecha Salida", "Costo"};
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
            alojamiento.setId(view.getTxtIdAlojamiento().getText().isEmpty() ? 0 : 
                            Integer.parseInt(view.getTxtIdAlojamiento().getText()));
            
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
            
            service.guardarOActualizarAlojamiento(alojamiento);
            habitacionService.actualizarHabitacionOcupada(alojamiento.getHabitacion().getId(), false);
            JOptionPane.showMessageDialog(view, 
                alojamiento.getId() == 0 ? "Alojamiento guardado con éxito" : 
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
            List<Alojamientos> alojamientos = service.listarAlojamientos();
            actualizarTabla(alojamientos);
        } catch (Exception e) {
            logger.error("Error al cargar alojamientos", e);
            JOptionPane.showMessageDialog(view, "Error al cargar alojamientos: " + e.getMessage());
        }
    }

    private void actualizarTabla(List<Alojamientos> alojamientos) {
        DefaultTableModel modelo = (DefaultTableModel) view.getTablaAlojamientos().getModel();
        modelo.setRowCount(0);
        
        for (Alojamientos a : alojamientos) {
            modelo.addRow(new Object[]{
                a.getId(),
                a.getCliente().getNombre(),
                a.getHabitacion().getNumero(),
                a.getFechaEntrada(),
                a.getFechaSalida(),
                a.getHabitacion().getPrecio()  // Mostrar el precio de la habitación como costo
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
                service.eliminarAlojamiento(id);
                JOptionPane.showMessageDialog(view, "Alojamiento eliminado con éxito");
                cargarAlojamientos();
                limpiarFormulario();
            } catch (Exception e) {
                logger.error("Error al eliminar alojamiento", e);
                JOptionPane.showMessageDialog(view, "Error al eliminar: " + e.getMessage());
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
                    // Actualizar campos del cliente en la vista
                    view.getTxtIdCliente().setText(tablaTemp.getValueAt(fila, 0).toString());
                    view.getTxtCliente().setText(tablaTemp.getValueAt(fila, 1).toString());
                }
            }
        } catch (Exception e) {
            logger.error("Error al mostrar selector de clientes", e);
            JOptionPane.showMessageDialog(view, "Error al cargar clientes");
        }
    }

    private void mostrarSelectorHabitaciones() {
  
    SeleccionarHabitaciones selector = new SeleccionarHabitaciones(view);
    selector.setVisible(true);
}
         
        }
    


