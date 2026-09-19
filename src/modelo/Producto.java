package modelo;

public class Producto {
    private final String codigo;
    private String nombre;
    private double precio;
    private int stock;        // Agregado para manejar la cantidad disponible del producto
    private boolean activo;   // agregado para indicar si el producto está activo o inactivo

    // Sobrecargas: mantienen compatibilidad con el código existente.
    public Producto(String nombre, double precio) {
        this("AUTO", nombre, precio, 0, true);
    }

    public Producto(String codigo, String nombre, double precio) {
        this(codigo, nombre, precio, 0, true);
    }

    // Constructor completo para inicializar todos los atributos, incluyendo stock y activo.
    public Producto(String codigo, String nombre, double precio, int stock, boolean activo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("El código del producto es obligatorio.");
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto es obligatorio.");
        }
        if (precio < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo.");
        }
        if (stock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo.");
        }

        this.codigo = codigo.trim().toUpperCase();
        this.nombre = nombre.trim();
        this.precio = precio;
        this.stock = stock;
        this.activo = activo;
    }

    public String getCodigo() { return codigo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto es obligatorio.");
        }
        this.nombre = nombre.trim();
    }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) {
        if (precio < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo.");
        }
        this.precio = precio;
    }

    // Metodo para manejar el stock del producto
    public int getStock() { return stock; }
    public void setStock(int stock) {
        if (stock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo.");
        }
        this.stock = stock;
    }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    /** Estado calculado: prioriza inactivo, luego agotado, luego disponible. */
    public String getEstado() {
        if (!activo) return "INACTIVO";
        if (stock <= 0) return "AGOTADO";
        return "DISPONIBLE";
    }

    public void mostrarDatos() {
        System.out.printf("Producto: %s - %s | Precio: S/ %.2f | Stock: %d | Estado: %s%n",
                codigo, nombre, precio, stock, getEstado());
    }

    @Override
    public String toString() {
        return codigo + " - " + nombre + " (S/ " + String.format("%.2f", precio) + ")";
    }
}