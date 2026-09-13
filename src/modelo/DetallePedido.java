package modelo;

public class DetallePedido {
    private final String codigoProducto;
    private final String nombreProducto;
    private final double precioUnitario;
    private int cantidad;

    public DetallePedido(Producto producto, int cantidad) {
        if (producto == null) {
            throw new IllegalArgumentException("El producto no puede ser nulo.");
        }
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero.");
        }

        // Se guarda una fotografía del producto para que un pedido histórico
        // conserve nombre y precio aunque el catálogo cambie después.
        this.codigoProducto = producto.getCodigo();
        this.nombreProducto = producto.getNombre();
        this.precioUnitario = producto.getPrecio();
        this.cantidad = cantidad;
    }

    public String getCodigoProducto() {
        return codigoProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void aumentarCantidad(int cantidadAdicional) {
        if (cantidadAdicional <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero.");
        }
        this.cantidad += cantidadAdicional;
    }

    public double getSubtotal() {
        return precioUnitario * cantidad;
    }
}
