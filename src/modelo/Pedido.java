package modelo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Pedido {
    private final String id;
    private final Cliente cliente;
    private final LocalDateTime fechaRegistro;
    private final ArrayList<DetallePedido> detalles;
    private EstadoPedido estado;

    public Pedido(String id, Cliente cliente) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del pedido es obligatorio.");
        }
        if (cliente == null) {
            throw new IllegalArgumentException("El cliente es obligatorio.");
        }

        this.id = id.trim().toUpperCase();
        this.cliente = cliente;
        this.fechaRegistro = LocalDateTime.now();
        this.detalles = new ArrayList<>();
        this.estado = EstadoPedido.REGISTRADO;
    }

    // Sobrecarga: agrega una unidad.
    public void agregarProducto(Producto producto) {
        agregarProducto(producto, 1);
    }

    // Sobrecarga: agrega la cantidad indicada.
    public void agregarProducto(Producto producto, int cantidad) {
        if (producto == null) {
            throw new IllegalArgumentException("Debe seleccionar un producto.");
        }
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero.");
        }

        for (DetallePedido detalle : detalles) {
            if (detalle.getCodigoProducto().equalsIgnoreCase(producto.getCodigo())) {
                detalle.aumentarCantidad(cantidad);
                return;
            }
        }

        detalles.add(new DetallePedido(producto, cantidad));
    }

    public double calcularTotal() {
        double total = 0;
        for (DetallePedido detalle : detalles) {
            total += detalle.getSubtotal();
        }
        return total;
    }

    public int calcularCantidadTotal() {
        int total = 0;
        for (DetallePedido detalle : detalles) {
            total += detalle.getCantidad();
        }
        return total;
    }

    public void cancelar() {
        estado = EstadoPedido.CANCELADO;
    }

    public String getId() {
        return id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public List<DetallePedido> getDetalles() {
        return Collections.unmodifiableList(detalles);
    }

    public void mostrarPedido() {
        System.out.println("Pedido: " + id);
        System.out.println("Cliente: " + cliente.getNombre());
        for (DetallePedido detalle : detalles) {
            System.out.printf("- %s x%d | S/ %.2f%n",
                    detalle.getNombreProducto(), detalle.getCantidad(), detalle.getSubtotal());
        }
        System.out.printf("TOTAL: S/ %.2f%n", calcularTotal());
    }
}
