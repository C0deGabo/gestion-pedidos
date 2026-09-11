import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    static ArrayList<Producto> productos = new ArrayList<>();
    static ArrayList<Cliente> clientes = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        int opcion;
        do {
            System.out.println("\n<<<<< Sistema de Gestión de Pedidos >>>>>");
            System.out.println("1. Registrar producto");
            System.out.println("2. Mostrar productos");
            System.out.println("3. Registrar cliente");
            System.out.println("4. Listar cliente");
            System.out.println("8. Salir");
            System.out.print("Opción: ");
            opcion = Integer.parseInt(sc.nextLine());

            switch (opcion) {
                case 1 -> registrarProducto();
                case 2 -> mostrarProductos();
                case 3 -> registrarCliente();
                case 4 -> listarClientes()
                case 8 -> System.out.println("saliendo");
                default -> System.out.println("Opción inválida.");
            }
        } while (opcion != 8);
    }
    ////////////////Modulo de productos////////
    static void registrarProducto() {
        System.out.print("Nombre: ");
        String nombre = sc.nextLine();
        System.out.print("Precio: ");
        double precio = Double.parseDouble(sc.nextLine());
        productos.add(new Producto(nombre, precio));
        System.out.println(" Producto registrado");
    }

    static void mostrarProductos() {
        if (productos.isEmpty()) {
            System.out.println("No hay productos.");
            return;
        }
        for (Producto p : productos) p.mostrarDatos();
    }

    ///////////Modulo de clientes///////////
    static void registrarCliente() {
        System.out.print("Nombre del cliente: ");
        String nombre = sc.nextLine();
        System.out.print("Celular: ");
        String tel = sc.nextLine();
        System.out.print("Email: ");
        String email = sc.nextLine();
        clientes.add(new Cliente(nombre, tel, email));
        System.out.println(" Cliente registrado.");
    }

    static void listarClientes(){
        if(cliente.isEmpty()){
            system.out.println("No existe ningun cliente registrado")
            return;
        }
        for (Cliente c : clientes) c.mostrarDatos();
    }
}