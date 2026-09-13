# Gestión de Pedidos - Java Swing (MVC simple)

Aplicación de escritorio desarrollada únicamente con Java estándar y Swing. No utiliza frameworks ni base de datos. Todos los productos, clientes, pedidos e historial permanecen en memoria mientras la aplicación está abierta.

## Estructura del proyecto

```text
gestion-pedidos-mvc/
├── src/
│   ├── app/
│   │   └── Main.java
│   ├── modelo/
│   │   ├── Cliente.java
│   │   ├── Producto.java
│   │   ├── Pedido.java
│   │   ├── DetallePedido.java
│   │   ├── EstadoPedido.java
│   │   └── HistorialEvento.java
│   ├── controlador/
│   │   └── GestorPedidosController.java
│   ├── vista/
│   │   └── VentanaPrincipal.java
│   └── excepciones/
│       └── ValidacionException.java
├── run.bat
├── run.sh
└── README.md
```

### Responsabilidad de cada carpeta

- **app**: punto de entrada de la aplicación.
- **modelo**: clases que representan los datos y reglas básicas del dominio.
- **controlador**: lógica del sistema, colecciones temporales, validaciones y operaciones sobre productos, clientes y pedidos.
- **vista**: interfaz gráfica Swing. La vista llama al controlador y muestra los resultados.
- **excepciones**: excepción personalizada utilizada para mostrar errores de validación de forma controlada.

La separación es intencionalmente sencilla para que el proyecto siga siendo fácil de estudiar y mantener.

## Mejoras visuales

- Menú lateral completamente plano, sin las sombras o relieves que agregaba el estilo nativo de Windows.
- La opción activa del menú se resalta en azul.
- Botones primarios y secundarios con apariencia consistente.
- Se reorganizó el módulo **Pedidos** para dar más espacio al formulario de **Nuevo pedido**.
- El botón **Agregar al pedido** ya no queda cortado.
- Se redujo la altura reservada para la tabla inferior de pedidos registrados y se aumentó el espacio disponible para el detalle del pedido.
- Se mantuvo el diseño adaptable dentro de una ventana Swing estándar.

## Funcionalidades

- Registro y eliminación de productos.
- Código de producto manual o automático mediante sobrecarga de métodos.
- Registro y eliminación de clientes.
- Cliente con o sin correo mediante sobrecarga.
- Creación de pedidos con uno o varios productos.
- Cálculo automático de subtotales y total.
- Cancelación de pedidos.
- Historial cronológico de operaciones.
- Dashboard con indicadores.
- Colecciones `ArrayList` y `LinkedHashMap`.
- Manejo de excepciones y validaciones.
- Conservación del precio y nombre del producto dentro del detalle histórico del pedido.

## Ejecutar en IntelliJ IDEA

1. Descomprime el proyecto.
2. En IntelliJ IDEA selecciona **File > Open** y abre la carpeta `gestion-pedidos-mvc`.
3. Si IntelliJ no detecta automáticamente `src` como código fuente, haz clic derecho en `src` y selecciona **Mark Directory as > Sources Root**.
4. Configura un JDK 11 o superior.
5. Abre `src/app/Main.java`.
6. Ejecuta el método `main`.

## Ejecutar en Windows sin configurar IntelliJ

Con un JDK instalado, ejecuta:

```text
run.bat
```

El archivo compila las clases en la carpeta `out` y luego ejecuta `app.Main`.

## Importante

No existe persistencia. Al cerrar la aplicación, toda la información registrada se pierde. Esto es parte del objetivo del proyecto: trabajar únicamente con colecciones y datos temporales en memoria.
