package com.mycompany.avanceproyecto.controller;

import com.mycompany.avanceproyecto.model.Clientes;
import com.mycompany.avanceproyecto.service.ClienteService;
import com.mycompany.avanceproyecto.view.Cliente;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClienteController {
    private static final Logger logger = LoggerFactory.getLogger(ClienteController.class);
    private final Cliente view;
    private final ClienteService service;
    
    public ClienteController(Cliente view) {
        this.view = view;
        this.service = new ClienteService();
        inicializarControlador();
    }
    
    private void inicializarControlador() {
        view.getBtnNuevo().addActionListener(e -> limpiarFormulario());
        view.getBtnGuardar().addActionListener(e -> guardarCliente());
        view.getBtnEliminar().addActionListener(e -> eliminarCliente());
        view.getBtnBuscar().addActionListener(e -> buscarClientes());
        cargarClientes();
    }
    
    private void guardarCliente() {
        try {
            logger.debug("Iniciando proceso de guardado de cliente");
            Clientes cliente = new Clientes();
            
            String idText = view.getTxtIdCliente().getText().trim();
            if (!idText.isEmpty()) {
                cliente.setId(Integer.parseInt(idText));
            }
            
            cliente.setNombre(view.getTxtNombre().getText());
            cliente.setDni(Integer.parseInt(view.getTxtDni().getText()));
            cliente.setTelefono(Integer.parseInt(view.getTxtTelefono().getText()));
            cliente.setCorreo(view.getTxtCorreo().getText());
            
            service.guardarOActualizarCliente(cliente);
            
            JOptionPane.showMessageDialog(view, 
                idText.isEmpty() ? "Cliente guardado con éxito" : "Cliente actualizado con éxito");
            
            view.limpiarCampos();
            cargarClientes();
            logger.info("Cliente guardado/actualizado exitosamente");
        } catch (Exception e) {
            logger.error("Error al guardar cliente", e);
            JOptionPane.showMessageDialog(view, "Error al guardar: " + e.getMessage());
        }
    }

    private void cargarClientes() {
        try {
            List<Clientes> clientes = service.listarClientes();
            view.actualizarTabla(clientes);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Error al cargar clientes: " + e.getMessage());
        }
    }

    private void eliminarCliente() {
        int fila = view.getTablaClientes().getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(view, "Por favor, seleccione un cliente para eliminar");
            return;
        }

        int id = (Integer) view.getTablaClientes().getValueAt(fila, 0);
        String nombre = (String) view.getTablaClientes().getValueAt(fila, 1);

        int confirmacion = JOptionPane.showConfirmDialog(view,
            "¿Está seguro que desea eliminar al cliente " + nombre + "?",
            "Confirmar Eliminación",
            JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                logger.debug("Iniciando proceso de eliminación de cliente");
                service.eliminarCliente(id);
                JOptionPane.showMessageDialog(view, "Cliente eliminado con éxito");
                cargarClientes();
                view.limpiarCampos();
                logger.info("Cliente eliminado exitosamente");
            } catch (Exception e) {
                logger.error("Error al eliminar cliente", e);
                JOptionPane.showMessageDialog(view, "Error al eliminar el cliente: " + e.getMessage());
            }
        }
    }

    private void limpiarFormulario() {
        view.limpiarCampos();
    }

    private void buscarClientes() {
        try {
            String dniBusqueda = view.getTxtBuscar().getText().trim();
            if (!dniBusqueda.isEmpty()) {
                try {
                    int dni = Integer.parseInt(dniBusqueda);
                    logger.info("Buscando cliente con DNI: {}", dni);
                    Clientes cliente = service.buscarPorDni(dni);
                    
                    if (cliente != null) {
                        // Mostrar solo el cliente encontrado en la tabla
                        List<Clientes> resultados = List.of(cliente);
                        view.actualizarTabla(resultados);
                    } else {
                        JOptionPane.showMessageDialog(view, 
                            "No se encontró ningún cliente con el DNI: " + dni,
                            "No encontrado",
                            JOptionPane.INFORMATION_MESSAGE);
                        cargarClientes(); // Mostrar todos los clientes
                    }
                } catch (NumberFormatException e) {
                    logger.error("Error al convertir DNI: {}", dniBusqueda);
                    JOptionPane.showMessageDialog(view,
                        "Por favor ingrese un DNI válido (solo números)",
                        "Error de formato",
                        JOptionPane.ERROR_MESSAGE);
                }
            } else {
                logger.info("Mostrando todos los clientes");
                cargarClientes(); // Si no hay término de búsqueda, mostrar todos
            }
        } catch (Exception e) {
            logger.error("Error al buscar clientes", e);
            JOptionPane.showMessageDialog(view, 
                "Error al realizar la búsqueda: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}

