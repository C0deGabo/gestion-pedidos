package modelo;

public class Producto {
    private final String codigo;
    private String nombre;
    private double precio;

    // Sobrecarga de constructor: genera un código simple cuando no se envía uno.
    public Producto(String nombre, double precio) {
        this("AUTO", nombre, precio);
    }

    public Producto(String codigo, String nombre, double precio) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("El código del producto es obligatorio.");
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto es obligatorio.");
        }
        if (precio < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo.");
        }

        this.codigo = codigo.trim().toUpperCase();
        this.nombre = nombre.trim();
        this.precio = precio;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto es obligatorio.");
        }
        this.nombre = nombre.trim();
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        if (precio < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo.");
        }
        this.precio = precio;
    }

    public void mostrarDatos() {
        System.out.printf("Producto: %s - %s | Precio: S/ %.2f%n", codigo, nombre, precio);
    }

    @Override
    public String toString() {
        return codigo + " - " + nombre + " (S/ " + String.format("%.2f", precio) + ")";
    }
}
