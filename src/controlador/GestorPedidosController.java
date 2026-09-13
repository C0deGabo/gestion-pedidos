package controlador;

import excepciones.ValidacionException;
import modelo.Cliente;
import modelo.EstadoPedido;
import modelo.HistorialEvento;
import modelo.Pedido;
import modelo.Producto;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GestorPedidosController {
    // LinkedHashMap evita códigos duplicados y conserva el orden de registro.
    private final Map<String, Producto> productos = new LinkedHashMap<>();
    private final Map<String, Cliente> clientes = new LinkedHashMap<>();
    private final ArrayList<Pedido> pedidos = new ArrayList<>();
    private final ArrayList<HistorialEvento> historial = new ArrayList<>();

    private int correlativoProducto = 1;
    private int correlativoCliente = 1;
    private int correlativoPedido = 1;

    // Sobrecarga: código automático.
    public Producto registrarProducto(String nombre, double precio) throws ValidacionException {
        String codigo;
        do {
            codigo = String.format("PR%03d", correlativoProducto++);
        } while (productos.containsKey(codigo));
        return registrarProducto(codigo, nombre, precio);
    }

    // Sobrecarga: código definido por el usuario.
    public Producto registrarProducto(String codigo, String nombre, double precio) throws ValidacionException {
        validarTexto(codigo, "El código del producto es obligatorio.");
        validarTexto(nombre, "El nombre del producto es obligatorio.");
        if (precio <= 0) {
            throw new ValidacionException("El precio debe ser mayor que cero.");
        }

        String clave = codigo.trim().toUpperCase();
        if (productos.containsKey(clave)) {
            throw new ValidacionException("Ya existe un producto con el código " + clave + ".");
        }

        Producto producto = new Producto(clave, nombre, precio);
        productos.put(clave, producto);
        registrarEvento("PRODUCTO", "Producto registrado: " + clave + " - " + producto.getNombre());
        return producto;
    }

    // Sobrecarga: cliente sin correo.
    public Cliente registrarCliente(String nombre, String telefono) throws ValidacionException {
        return registrarCliente(nombre, telefono, "");
    }

    // Sobrecarga: cliente con correo.
    public Cliente registrarCliente(String nombre, String telefono, String email) throws ValidacionException {
        validarTexto(nombre, "El nombre del cliente es obligatorio.");
        validarTelefono(telefono);
        validarEmail(email);

        String id = String.format("CL%03d", correlativoCliente++);
        Cliente cliente = new Cliente(id, nombre, telefono, email);
        clientes.put(id, cliente);
        registrarEvento("CLIENTE", "Cliente registrado: " + id + " - " + cliente.getNombre());
        return cliente;
    }

    public Pedido crearPedido(Cliente cliente, List<DetalleTemporal> items) throws ValidacionException {
        if (cliente == null) {
            throw new ValidacionException("Debe seleccionar un cliente.");
        }
        if (items == null || items.isEmpty()) {
            throw new ValidacionException("El pedido debe contener al menos un producto.");
        }

        for (DetalleTemporal item : items) {
            if (item == null || item.getProducto() == null || item.getCantidad() <= 0) {
                throw new ValidacionException("Existe un detalle inválido en el pedido.");
            }
        }

        String id = String.format("PE%04d", correlativoPedido++);
        Pedido pedido = new Pedido(id, cliente);

        for (DetalleTemporal item : items) {
            pedido.agregarProducto(item.getProducto(), item.getCantidad());
        }

        pedidos.add(pedido);
        registrarEvento("PEDIDO", "Pedido " + id + " registrado para " + cliente.getNombre()
                + " por S/ " + String.format("%.2f", pedido.calcularTotal()));
        return pedido;
    }

    public void cancelarPedido(String idPedido) throws ValidacionException {
        Pedido pedido = buscarPedido(idPedido);
        if (pedido == null) {
            throw new ValidacionException("No se encontró el pedido seleccionado.");
        }
        if (pedido.getEstado() == EstadoPedido.CANCELADO) {
            throw new ValidacionException("El pedido ya se encuentra cancelado.");
        }

        pedido.cancelar();
        registrarEvento("PEDIDO", "Pedido " + pedido.getId() + " cancelado.");
    }

    public void eliminarProducto(String codigo) throws ValidacionException {
        Producto eliminado = productos.remove(codigo);
        if (eliminado == null) {
            throw new ValidacionException("No se encontró el producto seleccionado.");
        }
        registrarEvento("PRODUCTO", "Producto retirado del catálogo: " + codigo + " - " + eliminado.getNombre());
    }

    public void eliminarCliente(String id) throws ValidacionException {
        for (Pedido pedido : pedidos) {
            if (pedido.getCliente().getId().equals(id)) {
                throw new ValidacionException("No se puede eliminar el cliente porque tiene pedidos en el historial.");
            }
        }

        Cliente eliminado = clientes.remove(id);
        if (eliminado == null) {
            throw new ValidacionException("No se encontró el cliente seleccionado.");
        }
        registrarEvento("CLIENTE", "Cliente eliminado: " + id + " - " + eliminado.getNombre());
    }

    public List<Producto> getProductos() {
        return new ArrayList<>(productos.values());
    }

    public List<Cliente> getClientes() {
        return new ArrayList<>(clientes.values());
    }

    public List<Pedido> getPedidos() {
        return new ArrayList<>(pedidos);
    }

    public List<HistorialEvento> getHistorial() {
        return new ArrayList<>(historial);
    }

    public int getCantidadPedidosActivos() {
        int cantidad = 0;
        for (Pedido pedido : pedidos) {
            if (pedido.getEstado() == EstadoPedido.REGISTRADO) {
                cantidad++;
            }
        }
        return cantidad;
    }

    public double getTotalVendido() {
        double total = 0;
        for (Pedido pedido : pedidos) {
            if (pedido.getEstado() == EstadoPedido.REGISTRADO) {
                total += pedido.calcularTotal();
            }
        }
        return total;
    }

    private Pedido buscarPedido(String id) {
        if (id == null) {
            return null;
        }
        for (Pedido pedido : pedidos) {
            if (pedido.getId().equalsIgnoreCase(id)) {
                return pedido;
            }
        }
        return null;
    }

    private void registrarEvento(String tipo, String descripcion) {
        historial.add(new HistorialEvento(tipo, descripcion));
    }

    private void validarTexto(String valor, String mensaje) throws ValidacionException {
        if (valor == null || valor.trim().isEmpty()) {
            throw new ValidacionException(mensaje);
        }
    }

    private void validarTelefono(String telefono) throws ValidacionException {
        if (telefono == null || telefono.trim().isEmpty()) {
            throw new ValidacionException("El teléfono es obligatorio.");
        }
        String limpio = telefono.replace(" ", "");
        if (!limpio.matches("[0-9+\\-]{7,15}")) {
            throw new ValidacionException("Ingrese un teléfono válido (7 a 15 caracteres numéricos).");
        }
    }

    private void validarEmail(String email) throws ValidacionException {
        if (email == null || email.trim().isEmpty()) {
            return;
        }
        if (!email.trim().matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            throw new ValidacionException("Ingrese un correo electrónico válido.");
        }
    }

    public static class DetalleTemporal {
        private final Producto producto;
        private final int cantidad;

        public DetalleTemporal(Producto producto, int cantidad) {
            this.producto = producto;
            this.cantidad = cantidad;
        }

        public Producto getProducto() {
            return producto;
        }

        public int getCantidad() {
            return cantidad;
        }
    }
}
