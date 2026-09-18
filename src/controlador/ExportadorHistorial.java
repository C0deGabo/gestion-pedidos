package controlador;

import modelo.HistorialEvento;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.List;

public class ExportadorHistorial {

    /**
     * Exporta una lista de eventos del historial a un archivo CSV.
     * El archivo generado es compatible con Microsoft Excel.
     */
    public static void exportarCSV(File archivo, List<HistorialEvento> eventos) throws Exception {
        if (eventos == null || eventos.isEmpty()) {
            throw new Exception("No hay datos en el historial para exportar.");
        }

        // Asegurar extensión .csv
        if (!archivo.getName().toLowerCase().endsWith(".csv")) {
            archivo = new File(archivo.getParentFile(), archivo.getName() + ".csv");
        }

        try (FileOutputStream fos = new FileOutputStream(archivo)) {
            // BOM UTF-8 para que Excel reconozca tildes y caracteres especiales
            fos.write(0xEF);
            fos.write(0xBB);
            fos.write(0xBF);

            PrintWriter pw = new PrintWriter(fos);
            // Directiva para que Excel use punto y coma como separador
            pw.println("sep=;");
            // Cabeceras
            pw.println("Fecha y hora;Tipo;Descripcion");

            // Filas de datos
            for (HistorialEvento evento : eventos) {
                String fecha = evento.getFechaFormateada();
                String tipo = evento.getTipo();
                String desc = evento.getDescripcion();
                desc = desc.replace(";", ",").replace("\n", " ");
                pw.println(fecha + ";" + tipo + ";" + desc);
            }

            pw.flush();
        }
    }
}
