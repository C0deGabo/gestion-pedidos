package modelo;

public class Cliente {
    private final String id;
    private String nombre;
    private String telefono;
    private String email;

    // Sobrecarga de constructor: el correo puede omitirse.
    public Cliente(String id, String nombre, String telefono) {
        this(id, nombre, telefono, "");
    }

    public Cliente(String id, String nombre, String telefono, String email) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del cliente es obligatorio.");
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del cliente es obligatorio.");
        }

        this.id = id.trim().toUpperCase();
        this.nombre = nombre.trim();
        this.telefono = telefono == null ? "" : telefono.trim();
        this.email = email == null ? "" : email.trim();
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del cliente es obligatorio.");
        }
        this.nombre = nombre.trim();
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono == null ? "" : telefono.trim();
    }

    public void setEmail(String email) {
        this.email = email == null ? "" : email.trim();
    }

    public void mostrarDatos() {
        System.out.printf("Cliente: %s - %s | Tel: %s | Email: %s%n",
                id, nombre, telefono, email.isEmpty() ? "-" : email);
    }

    @Override
    public String toString() {
        return id + " - " + nombre;
    }
}
