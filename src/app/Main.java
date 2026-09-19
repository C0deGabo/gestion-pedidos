package app;

import controlador.GestorPedidosController;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import vista.VentanaPrincipal;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
                // Si el estilo del sistema no está disponible, Swing usa su estilo por defecto.
            }

            GestorPedidosController gestor = new GestorPedidosController();
            VentanaPrincipal ventana = new VentanaPrincipal(gestor);
            ventana.setVisible(true);
        });
    }
}
