package modelo;

public enum EstadoPedido {
    REGISTRADO("Registrado"),
    CANCELADO("Cancelado");

    private final String texto;

    EstadoPedido(String texto) {
        this.texto = texto;
    }

    @Override
    public String toString() {
        return texto;
    }
}
