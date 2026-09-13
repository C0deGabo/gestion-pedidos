package modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HistorialEvento {
    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private final LocalDateTime fecha;
    private final String tipo;
    private final String descripcion;

    public HistorialEvento(String tipo, String descripcion) {
        this.fecha = LocalDateTime.now();
        this.tipo = tipo;
        this.descripcion = descripcion;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public String getFechaFormateada() {
        return fecha.format(FORMATO);
    }

    public String getTipo() {
        return tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
