public class Cliente {
    private String nombre;
    private String telefono;
    private String email;

    public Cliente(String nombre, String telefono, String email) {
        this.nombre = nombre;
        this.telefono = telefono;
        this.email = email;
    }

    public String getNombre() { return nombre; }
    public String getTelefono() { return telefono; }
    public String getEmail() { return email; }

    public void mostrarDatos() {
        System.out.printf("Cliente: %s | Tel: %s | Email: %s%n",
                nombre, telefono, email);
    }
}