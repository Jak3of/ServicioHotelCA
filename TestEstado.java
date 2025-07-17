import com.mycompany.avanceproyecto.config.DatabaseInitializer;
import com.mycompany.avanceproyecto.service.AlojamientoService;
import com.mycompany.avanceproyecto.model.Alojamientos;
import java.util.List;

public class TestEstado {
    public static void main(String[] args) {
        try {
            System.out.println("=== TEST DE ESTADO ===");
            
            // 1. Inicializar base de datos
            System.out.println("1. Inicializando base de datos...");
            DatabaseInitializer.initializeDatabase();
            System.out.println("✓ Base de datos inicializada");
            
            // 2. Crear service
            AlojamientoService service = new AlojamientoService();
            
            // 3. Listar alojamientos existentes
            System.out.println("\n2. Listando alojamientos existentes:");
            List<Alojamientos> alojamientos = service.listarAlojamientos();
            for (Alojamientos a : alojamientos) {
                System.out.println("  ID: " + a.getId() + " - Estado: " + a.getEstado().getValor() + " - Cliente: " + a.getCliente().getNombre());
            }
            
            if (!alojamientos.isEmpty()) {
                // 4. Intentar actualizar el estado del primer alojamiento
                Alojamientos primerAlojamiento = alojamientos.get(0);
                int idTest = primerAlojamiento.getId();
                
                System.out.println("\n3. Probando actualización de estado:");
                System.out.println("  Alojamiento ID: " + idTest);
                System.out.println("  Estado actual: " + primerAlojamiento.getEstado().getValor());
                
                // Cambiar a PAGADO
                System.out.println("  Cambiando estado a PAGADO...");
                service.actualizarEstado(idTest, Alojamientos.EstadoAlojamiento.PAGADO);
                
                // Verificar el cambio
                Alojamientos actualizado = service.obtenerAlojamiento(idTest);
                System.out.println("  Estado después del cambio: " + actualizado.getEstado().getValor());
                
                if (actualizado.getEstado() == Alojamientos.EstadoAlojamiento.PAGADO) {
                    System.out.println("✓ ¡ÉXITO! El estado se actualizó correctamente");
                } else {
                    System.out.println("❌ ERROR: El estado NO se actualizó");
                }
                
                // Volver al estado original
                System.out.println("  Restaurando estado original...");
                service.actualizarEstado(idTest, primerAlojamiento.getEstado());
                
            } else {
                System.out.println("No hay alojamientos para probar");
            }
            
        } catch (Exception e) {
            System.out.println("❌ ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
