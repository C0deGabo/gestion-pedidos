public class Producto {
    private String nombre;
    private double precio;

    public Producto(String nombre, double precio) {
        this.nombre = nombre;
        setPrecio(precio);   
    }

    public String getNombre() { return nombre; }

    public void setNombre(String nombre) { this.nombre = nombre; }

    public double getPrecio() { return precio; }
    
    public void setPrecio(double precio) {
        if (precio < 0) {
            System.out.println("El precio no puede ser negativo, Se pondra 0.");
            this.precio = 0;
        } else {
            this.precio = precio;
        }
    }

    public void mostrarDatos() {
        System.out.printf("Producto: %s | Precio: $%.2f%n", nombre, precio);
    }
}