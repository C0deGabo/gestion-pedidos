import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    static ArrayList<Producto> productos = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        int opcion;
        do {
            System.out.println("\n--- Sistema de Gestión de Pedidos ---");
            System.out.println("1. Registrar producto");
            System.out.println("2. Mostrar productos");
            System.out.println("3. Salir");
            System.out.print("Opción: ");
            opcion = Integer.parseInt(sc.nextLine());

            switch (opcion) {
                case 1 -> registrarProducto();
                case 2 -> mostrarProductos();
                case 3 -> System.out.println("¡Hasta luego!");
                default -> System.out.println("Opción inválida.");
            }
        } while (opcion != 3);
    }

    static void registrarProducto() {
        System.out.print("Nombre: ");
        String nombre = sc.nextLine();
        System.out.print("Precio: ");
        double precio = Double.parseDouble(sc.nextLine());
        productos.add(new Producto(nombre, precio));
        System.out.println("✔ Producto registrado.");
    }

    static void mostrarProductos() {
        if (productos.isEmpty()) {
            System.out.println("No hay productos.");
            return;
        }
        for (Producto p : productos) p.mostrarDatos();
    }
}