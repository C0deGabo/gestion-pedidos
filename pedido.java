import java.util.ArrayList;

public class Pedido {
    private Cliente cliente;
    private ArrayList<Producto> productos;

    public Pedido(Cliente cliente) {
        this.cliente = cliente;
        this.productos = new ArrayList<>();
    }

    public void agregarProducto(Producto p) {
        productos.add(p);
    }

    public double calcularTotal() {
        double total = 0;
        for (Producto p : productos) {
            total += p.getPrecio();
        }
        return total;
    }

    public void mostrarPedido() {
        System.out.println("Cliente: " + cliente.getNombre());
        System.out.println("Productos del pedido:");
        if (productos.isEmpty()) {
            System.out.println("  (sin productos)");
        } else {
            for (Producto p : productos) {
                System.out.println("  - " + p.getNombre() + " ($" + p.getPrecio() + ")");
            }
        }
        System.out.printf("TOTAL: $%.2f%n", calcularTotal());
    }

    public Cliente getCliente() { return cliente; }
    public ArrayList<Producto> getProductos() { return productos; }
}