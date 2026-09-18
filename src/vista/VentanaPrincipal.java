package vista;

import controlador.ExportadorHistorial;
import controlador.GestorPedidosController;
import excepciones.ValidacionException;
import modelo.Cliente;
import modelo.HistorialEvento;
import modelo.Pedido;
import modelo.Producto;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class VentanaPrincipal extends JFrame {
    private static final Color COLOR_FONDO = new Color(244, 247, 251);
    private static final Color COLOR_PANEL = Color.WHITE;
    private static final Color COLOR_PRIMARIO = new Color(37, 99, 235);
    private static final Color COLOR_PRIMARIO_OSCURO = new Color(29, 78, 216);
    private static final Color COLOR_TEXTO = new Color(30, 41, 59);
    private static final Color COLOR_TEXTO_SUAVE = new Color(100, 116, 139);
    private static final Color COLOR_BORDE = new Color(226, 232, 240);
    private static final Color COLOR_PELIGRO = new Color(220, 38, 38);

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final GestorPedidosController gestor;
    private final Map<String, JButton> botonesMenu = new LinkedHashMap<>();
    private String tarjetaActiva = "dashboard";
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel panelContenido = new JPanel(cardLayout);

    private JLabel lblTotalProductos;
    private JLabel lblTotalClientes;
    private JLabel lblPedidosActivos;
    private JLabel lblTotalVendido;

    private DefaultTableModel modeloProductos;
    private DefaultTableModel modeloClientes;
    private DefaultTableModel modeloPedidos;
    private DefaultTableModel modeloHistorial;
    private DefaultTableModel modeloRecientes;
    private DefaultTableModel modeloCarrito;

    private JTable tablaProductos;
    private JTable tablaClientes;
    private JTable tablaPedidos;
    private JTable tablaCarrito;

    private JTextField txtCodigoProducto;
    private JTextField txtNombreProducto;
    private JTextField txtPrecioProducto;

    private JTextField txtNombreCliente;
    private JTextField txtTelefonoCliente;
    private JTextField txtEmailCliente;

    private JComboBox<Cliente> cmbClientes;
    private JComboBox<Producto> cmbProductos;
    private JSpinner spnCantidad;
    private JLabel lblTotalCarrito;

    private final List<GestorPedidosController.DetalleTemporal> carrito = new ArrayList<>();

    public VentanaPrincipal(GestorPedidosController gestor) {
        this.gestor = gestor;

        setTitle("Gestión de Pedidos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1080, 700));
        setSize(1220, 780);
        setLocationRelativeTo(null);

        configurarUI();
        construirInterfaz();
        refrescarTodo();
    }

    private void configurarUI() {
        UIManager.put("OptionPane.messageFont", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("OptionPane.buttonFont", new Font("Segoe UI", Font.PLAIN, 13));
        UIManager.put("TextField.font", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("ComboBox.font", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("Spinner.font", new Font("Segoe UI", Font.PLAIN, 14));
    }

    private void construirInterfaz() {
        JPanel raiz = new JPanel(new BorderLayout());
        raiz.setBackground(COLOR_FONDO);
        setContentPane(raiz);

        raiz.add(crearMenuLateral(), BorderLayout.WEST);

        panelContenido.setBackground(COLOR_FONDO);
        panelContenido.setBorder(new EmptyBorder(24, 26, 18, 26));
        panelContenido.add(crearPanelDashboard(), "dashboard");
        panelContenido.add(crearPanelProductos(), "productos");
        panelContenido.add(crearPanelClientes(), "clientes");
        panelContenido.add(crearPanelPedidos(), "pedidos");
        panelContenido.add(crearPanelHistorial(), "historial");
        raiz.add(panelContenido, BorderLayout.CENTER);

        raiz.add(crearBarraEstado(), BorderLayout.SOUTH);
    }

    private JPanel crearMenuLateral() {
        JPanel menu = new JPanel();
        menu.setPreferredSize(new Dimension(220, 0));
        menu.setBackground(new Color(15, 23, 42));
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.setBorder(new EmptyBorder(24, 16, 24, 16));

        JLabel titulo = new JLabel("PEDIDOS");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitulo = new JLabel("Gestión temporal");
        subtitulo.setForeground(new Color(148, 163, 184));
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        menu.add(titulo);
        menu.add(Box.createVerticalStrut(3));
        menu.add(subtitulo);
        menu.add(Box.createVerticalStrut(30));

        menu.add(crearBotonMenu("Inicio", "dashboard"));
        menu.add(Box.createVerticalStrut(8));
        menu.add(crearBotonMenu("Productos", "productos"));
        menu.add(Box.createVerticalStrut(8));
        menu.add(crearBotonMenu("Clientes", "clientes"));
        menu.add(Box.createVerticalStrut(8));
        menu.add(crearBotonMenu("Pedidos", "pedidos"));
        menu.add(Box.createVerticalStrut(8));
        menu.add(crearBotonMenu("Historial", "historial"));
        seleccionarBotonMenu("dashboard");
        menu.add(Box.createVerticalGlue());

        JLabel nota = new JLabel("Sin base de datos");
        nota.setForeground(new Color(100, 116, 139));
        nota.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        nota.setAlignmentX(Component.LEFT_ALIGNMENT);
        menu.add(nota);

        return menu;
    }

    private JButton crearBotonMenu(String texto, String tarjeta) {
        JButton boton = new JButton(texto);

        // BasicButtonUI evita el relieve/sombra que aplica el Look & Feel de Windows.
        // Así el menú conserva un aspecto plano y consistente.
        boton.setUI(new BasicButtonUI());
        boton.setOpaque(true);
        boton.setContentAreaFilled(true);
        boton.setBorderPainted(false);
        boton.setRolloverEnabled(false);
        boton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        boton.setMinimumSize(new Dimension(0, 42));
        boton.setPreferredSize(new Dimension(188, 42));
        boton.setHorizontalAlignment(SwingConstants.LEFT);
        boton.setFocusPainted(false);
        boton.setBorder(new EmptyBorder(10, 14, 10, 14));
        boton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        botonesMenu.put(tarjeta, boton);

        boton.addActionListener(e -> {
            refrescarTodo();
            seleccionarBotonMenu(tarjeta);
            cardLayout.show(panelContenido, tarjeta);
        });

        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!tarjeta.equals(tarjetaActiva)) {
                    boton.setBackground(new Color(51, 65, 85));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                actualizarEstiloBotonMenu(tarjeta, boton);
            }
        });

        actualizarEstiloBotonMenu(tarjeta, boton);
        return boton;
    }

    private void seleccionarBotonMenu(String tarjeta) {
        tarjetaActiva = tarjeta;
        for (Map.Entry<String, JButton> entry : botonesMenu.entrySet()) {
            actualizarEstiloBotonMenu(entry.getKey(), entry.getValue());
        }
    }

    private void actualizarEstiloBotonMenu(String tarjeta, JButton boton) {
        boolean activo = tarjeta.equals(tarjetaActiva);
        boton.setBackground(activo ? COLOR_PRIMARIO : new Color(30, 41, 59));
        boton.setForeground(activo ? Color.WHITE : new Color(226, 232, 240));
    }

    private JPanel crearBarraEstado() {
        JPanel barra = new JPanel(new BorderLayout());
        barra.setBackground(Color.WHITE);
        barra.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, COLOR_BORDE),
                new EmptyBorder(8, 18, 8, 18)
        ));
        JLabel texto = new JLabel("Los datos se guardan solo mientras la aplicación esté abierta.");
        texto.setForeground(COLOR_TEXTO_SUAVE);
        texto.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        barra.add(texto, BorderLayout.WEST);
        return barra;
    }

    private JPanel crearPanelDashboard() {
        JPanel panel = crearPanelBase("Resumen general", "Vista rápida del estado actual del sistema.");

        JPanel tarjetas = new JPanel(new GridLayout(1, 4, 14, 14));
        tarjetas.setOpaque(false);
        lblTotalProductos = new JLabel("0");
        lblTotalClientes = new JLabel("0");
        lblPedidosActivos = new JLabel("0");
        lblTotalVendido = new JLabel("S/ 0.00");

        tarjetas.add(crearTarjetaIndicador("Productos", lblTotalProductos));
        tarjetas.add(crearTarjetaIndicador("Clientes", lblTotalClientes));
        tarjetas.add(crearTarjetaIndicador("Pedidos activos", lblPedidosActivos));
        tarjetas.add(crearTarjetaIndicador("Total registrado", lblTotalVendido));

        panel.add(tarjetas, BorderLayout.NORTH);

        JPanel recientes = crearTarjetaBlanca();
        recientes.setLayout(new BorderLayout(0, 12));
        recientes.setBorder(new EmptyBorder(18, 18, 18, 18));
        JLabel titulo = crearTituloSeccion("Últimos pedidos");
        recientes.add(titulo, BorderLayout.NORTH);

        modeloRecientes = modeloNoEditable(new String[]{"Pedido", "Cliente", "Fecha", "Estado", "Total"});
        JTable tabla = crearTabla(modeloRecientes);
        recientes.add(new JScrollPane(tabla), BorderLayout.CENTER);

        JPanel contenedor = new JPanel(new BorderLayout());
        contenedor.setOpaque(false);
        contenedor.setBorder(new EmptyBorder(18, 0, 0, 0));
        contenedor.add(recientes, BorderLayout.CENTER);
        panel.add(contenedor, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearTarjetaIndicador(String titulo, JLabel valor) {
        JPanel tarjeta = crearTarjetaBlanca();
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBorder(new EmptyBorder(18, 18, 18, 18));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setForeground(COLOR_TEXTO_SUAVE);
        lblTitulo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        valor.setForeground(COLOR_TEXTO);
        valor.setFont(new Font("Segoe UI", Font.BOLD, 25));
        valor.setAlignmentX(Component.LEFT_ALIGNMENT);

        tarjeta.add(lblTitulo);
        tarjeta.add(Box.createVerticalStrut(10));
        tarjeta.add(valor);
        return tarjeta;
    }

    private JPanel crearPanelProductos() {
        JPanel panel = crearPanelBase("Productos", "Registra y administra el catálogo temporal de productos.");

        JPanel cuerpo = new JPanel(new BorderLayout(16, 0));
        cuerpo.setOpaque(false);

        JPanel formulario = crearTarjetaBlanca();
        formulario.setPreferredSize(new Dimension(320, 0));
        formulario.setLayout(new BoxLayout(formulario, BoxLayout.Y_AXIS));
        formulario.setBorder(new EmptyBorder(20, 20, 20, 20));
        formulario.add(crearTituloSeccion("Nuevo producto"));
        formulario.add(Box.createVerticalStrut(18));

        txtCodigoProducto = crearCampo(formulario, "Código (opcional)");
        txtNombreProducto = crearCampo(formulario, "Nombre");
        txtPrecioProducto = crearCampo(formulario, "Precio");

        JButton btnRegistrar = crearBotonPrimario("Registrar producto");
        btnRegistrar.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnRegistrar.addActionListener(e -> registrarProductoDesdeUI());
        formulario.add(Box.createVerticalStrut(8));
        formulario.add(btnRegistrar);
        formulario.add(Box.createVerticalGlue());

        JPanel listado = crearTarjetaBlanca();
        listado.setLayout(new BorderLayout(0, 12));
        listado.setBorder(new EmptyBorder(18, 18, 18, 18));

        JPanel cabecera = new JPanel(new BorderLayout());
        cabecera.setOpaque(false);
        cabecera.add(crearTituloSeccion("Catálogo"), BorderLayout.WEST);
        JButton btnEliminar = crearBotonSecundario("Eliminar seleccionado", true);
        btnEliminar.addActionListener(e -> eliminarProductoSeleccionado());
        cabecera.add(btnEliminar, BorderLayout.EAST);
        listado.add(cabecera, BorderLayout.NORTH);

        modeloProductos = modeloNoEditable(new String[]{"Código", "Nombre", "Precio"});
        tablaProductos = crearTabla(modeloProductos);
        listado.add(new JScrollPane(tablaProductos), BorderLayout.CENTER);

        cuerpo.add(formulario, BorderLayout.WEST);
        cuerpo.add(listado, BorderLayout.CENTER);
        panel.add(cuerpo, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelClientes() {
        JPanel panel = crearPanelBase("Clientes", "Mantén una lista simple de clientes durante la sesión.");

        JPanel cuerpo = new JPanel(new BorderLayout(16, 0));
        cuerpo.setOpaque(false);

        JPanel formulario = crearTarjetaBlanca();
        formulario.setPreferredSize(new Dimension(320, 0));
        formulario.setLayout(new BoxLayout(formulario, BoxLayout.Y_AXIS));
        formulario.setBorder(new EmptyBorder(20, 20, 20, 20));
        formulario.add(crearTituloSeccion("Nuevo cliente"));
        formulario.add(Box.createVerticalStrut(18));

        txtNombreCliente = crearCampo(formulario, "Nombre completo");
        txtTelefonoCliente = crearCampo(formulario, "Teléfono");
        txtEmailCliente = crearCampo(formulario, "Correo (opcional)");

        JButton btnRegistrar = crearBotonPrimario("Registrar cliente");
        btnRegistrar.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnRegistrar.addActionListener(e -> registrarClienteDesdeUI());
        formulario.add(Box.createVerticalStrut(8));
        formulario.add(btnRegistrar);
        formulario.add(Box.createVerticalGlue());

        JPanel listado = crearTarjetaBlanca();
        listado.setLayout(new BorderLayout(0, 12));
        listado.setBorder(new EmptyBorder(18, 18, 18, 18));

        JPanel cabecera = new JPanel(new BorderLayout());
        cabecera.setOpaque(false);
        cabecera.add(crearTituloSeccion("Clientes registrados"), BorderLayout.WEST);
        JButton btnEliminar = crearBotonSecundario("Eliminar seleccionado", true);
        btnEliminar.addActionListener(e -> eliminarClienteSeleccionado());
        cabecera.add(btnEliminar, BorderLayout.EAST);
        listado.add(cabecera, BorderLayout.NORTH);

        modeloClientes = modeloNoEditable(new String[]{"ID", "Nombre", "Teléfono", "Correo"});
        tablaClientes = crearTabla(modeloClientes);
        listado.add(new JScrollPane(tablaClientes), BorderLayout.CENTER);

        cuerpo.add(formulario, BorderLayout.WEST);
        cuerpo.add(listado, BorderLayout.CENTER);
        panel.add(cuerpo, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelPedidos() {
        JPanel panel = crearPanelBase("Pedidos", "Arma pedidos con cantidades, total automático y registro histórico.");

        JPanel contenedor = new JPanel(new BorderLayout(0, 14));
        contenedor.setOpaque(false);

        JPanel nuevo = crearTarjetaBlanca();
        nuevo.setLayout(new BorderLayout(20, 0));
        nuevo.setBorder(new EmptyBorder(18, 18, 20, 18));
        nuevo.setMinimumSize(new Dimension(0, 300));

        JPanel controles = new JPanel();
        controles.setOpaque(false);
        controles.setPreferredSize(new Dimension(315, 0));
        controles.setMinimumSize(new Dimension(300, 270));
        controles.setLayout(new BoxLayout(controles, BoxLayout.Y_AXIS));
        controles.add(crearTituloSeccion("Nuevo pedido"));
        controles.add(Box.createVerticalStrut(12));

        controles.add(crearEtiquetaCampo("Cliente"));
        cmbClientes = new JComboBox<>();
        estilizarComponente(cmbClientes);
        controles.add(cmbClientes);
        controles.add(Box.createVerticalStrut(9));

        controles.add(crearEtiquetaCampo("Producto"));
        cmbProductos = new JComboBox<>();
        estilizarComponente(cmbProductos);
        controles.add(cmbProductos);
        controles.add(Box.createVerticalStrut(9));

        controles.add(crearEtiquetaCampo("Cantidad"));
        spnCantidad = new JSpinner(new SpinnerNumberModel(1, 1, 999, 1));
        estilizarComponente(spnCantidad);
        controles.add(spnCantidad);
        controles.add(Box.createVerticalStrut(12));

        JButton btnAgregar = crearBotonPrimario("Agregar al pedido");
        btnAgregar.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnAgregar.addActionListener(e -> agregarProductoAlCarrito());
        controles.add(btnAgregar);
        controles.add(Box.createVerticalStrut(8));

        JPanel detalle = new JPanel(new BorderLayout(0, 12));
        detalle.setOpaque(false);
        detalle.add(crearTituloSeccion("Detalle del pedido"), BorderLayout.NORTH);
        modeloCarrito = modeloNoEditable(new String[]{"Código", "Producto", "Cantidad", "P. Unitario", "Subtotal"});
        tablaCarrito = crearTabla(modeloCarrito);
        detalle.add(new JScrollPane(tablaCarrito), BorderLayout.CENTER);

        JPanel pieCarrito = new JPanel(new BorderLayout());
        pieCarrito.setOpaque(false);
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        botones.setOpaque(false);
        JButton btnQuitar = crearBotonSecundario("Quitar fila", false);
        btnQuitar.addActionListener(e -> quitarItemCarrito());
        JButton btnLimpiar = crearBotonSecundario("Limpiar", false);
        btnLimpiar.addActionListener(e -> limpiarCarrito());
        botones.add(btnQuitar);
        botones.add(btnLimpiar);

        JPanel totalYGuardar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 14, 0));
        totalYGuardar.setOpaque(false);
        lblTotalCarrito = new JLabel("Total: S/ 0.00");
        lblTotalCarrito.setForeground(COLOR_TEXTO);
        lblTotalCarrito.setFont(new Font("Segoe UI", Font.BOLD, 18));
        JButton btnGuardar = crearBotonPrimario("Registrar pedido");
        btnGuardar.addActionListener(e -> registrarPedidoDesdeUI());
        totalYGuardar.add(lblTotalCarrito);
        totalYGuardar.add(btnGuardar);

        pieCarrito.add(botones, BorderLayout.WEST);
        pieCarrito.add(totalYGuardar, BorderLayout.EAST);
        detalle.add(pieCarrito, BorderLayout.SOUTH);

        nuevo.add(controles, BorderLayout.WEST);
        nuevo.add(detalle, BorderLayout.CENTER);

        JPanel historialPedidos = crearTarjetaBlanca();
        historialPedidos.setPreferredSize(new Dimension(0, 205));
        historialPedidos.setMinimumSize(new Dimension(0, 180));
        historialPedidos.setLayout(new BorderLayout(0, 12));
        historialPedidos.setBorder(new EmptyBorder(18, 18, 18, 18));

        JPanel cabecera = new JPanel(new BorderLayout());
        cabecera.setOpaque(false);
        cabecera.add(crearTituloSeccion("Pedidos registrados"), BorderLayout.WEST);
        JButton btnCancelar = crearBotonSecundario("Cancelar pedido", true);
        btnCancelar.addActionListener(e -> cancelarPedidoSeleccionado());
        cabecera.add(btnCancelar, BorderLayout.EAST);
        historialPedidos.add(cabecera, BorderLayout.NORTH);

        modeloPedidos = modeloNoEditable(new String[]{"Pedido", "Cliente", "Fecha", "Productos", "Estado", "Total"});
        tablaPedidos = crearTabla(modeloPedidos);
        historialPedidos.add(new JScrollPane(tablaPedidos), BorderLayout.CENTER);

        contenedor.add(nuevo, BorderLayout.CENTER);
        contenedor.add(historialPedidos, BorderLayout.SOUTH);
        panel.add(contenedor, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelHistorial() {
        JPanel panel = crearPanelBase("Historial", "Bitácora cronológica de las operaciones realizadas en esta sesión.");

        JPanel tarjeta = crearTarjetaBlanca();
        tarjeta.setLayout(new BorderLayout(0, 12));
        tarjeta.setBorder(new EmptyBorder(18, 18, 18, 18));
        
        JPanel cabecera = new JPanel(new BorderLayout());
        cabecera.setOpaque(false);
        cabecera.add(crearTituloSeccion("Actividad del sistema"), BorderLayout.WEST);
        
        JButton btnExportar = crearBotonSecundario("Exportar a Excel", false);
        btnExportar.addActionListener(e -> exportarHistorialCSV());
        cabecera.add(btnExportar, BorderLayout.EAST);
        
        tarjeta.add(cabecera, BorderLayout.NORTH);

        modeloHistorial = modeloNoEditable(new String[]{"Fecha y hora", "Tipo", "Descripción"});
        JTable tabla = crearTabla(modeloHistorial);
        tarjeta.add(new JScrollPane(tabla), BorderLayout.CENTER);

        panel.add(tarjeta, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelBase(String titulo, String subtitulo) {
        JPanel panel = new JPanel(new BorderLayout(0, 18));
        panel.setBackground(COLOR_FONDO);

        JPanel cabecera = new JPanel();
        cabecera.setOpaque(false);
        cabecera.setLayout(new BoxLayout(cabecera, BoxLayout.Y_AXIS));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setForeground(COLOR_TEXTO);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblSubtitulo = new JLabel(subtitulo);
        lblSubtitulo.setForeground(COLOR_TEXTO_SUAVE);
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSubtitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        cabecera.add(lblTitulo);
        cabecera.add(Box.createVerticalStrut(4));
        cabecera.add(lblSubtitulo);
        panel.add(cabecera, BorderLayout.NORTH);
        return panel;
    }

    private JPanel crearTarjetaBlanca() {
        JPanel panel = new JPanel();
        panel.setBackground(COLOR_PANEL);
        panel.setBorder(BorderFactory.createLineBorder(COLOR_BORDE));
        return panel;
    }

    private JLabel crearTituloSeccion(String texto) {
        JLabel titulo = new JLabel(texto);
        titulo.setForeground(COLOR_TEXTO);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        return titulo;
    }

    private JTextField crearCampo(JPanel contenedor, String etiqueta) {
        contenedor.add(crearEtiquetaCampo(etiqueta));
        JTextField campo = new JTextField();
        estilizarComponente(campo);
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        contenedor.add(campo);
        contenedor.add(Box.createVerticalStrut(12));
        return campo;
    }

    private JLabel crearEtiquetaCampo(String texto) {
        JLabel label = new JLabel(texto);
        label.setForeground(COLOR_TEXTO_SUAVE);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private void estilizarComponente(JComponent componente) {
        componente.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        componente.setBackground(Color.WHITE);
        componente.setForeground(COLOR_TEXTO);
        componente.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDE),
                new EmptyBorder(7, 9, 7, 9)
        ));
        componente.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        componente.setPreferredSize(new Dimension(200, 38));
        componente.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    private JButton crearBotonPrimario(String texto) {
        JButton boton = new JButton(texto);
        boton.setUI(new BasicButtonUI());
        boton.setOpaque(true);
        boton.setContentAreaFilled(true);
        boton.setBorderPainted(false);
        boton.setBackground(COLOR_PRIMARIO);
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        boton.setFocusPainted(false);
        boton.setBorder(new EmptyBorder(10, 15, 10, 15));
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                boton.setBackground(COLOR_PRIMARIO_OSCURO);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                boton.setBackground(COLOR_PRIMARIO);
            }
        });
        return boton;
    }

    private JButton crearBotonSecundario(String texto, boolean peligro) {
        JButton boton = new JButton(texto);
        boton.setUI(new BasicButtonUI());
        boton.setOpaque(true);
        boton.setContentAreaFilled(true);
        boton.setBorderPainted(true);
        boton.setBackground(Color.WHITE);
        boton.setForeground(peligro ? COLOR_PELIGRO : COLOR_TEXTO);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(peligro ? new Color(254, 202, 202) : COLOR_BORDE),
                new EmptyBorder(8, 12, 8, 12)
        ));
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return boton;
    }

    private DefaultTableModel modeloNoEditable(String[] columnas) {
        return new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    private JTable crearTabla(DefaultTableModel modelo) {
        JTable tabla = new JTable(modelo);
        tabla.setRowHeight(30);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setShowVerticalLines(false);
        tabla.setGridColor(COLOR_BORDE);
        tabla.setSelectionBackground(new Color(219, 234, 254));
        tabla.setSelectionForeground(COLOR_TEXTO);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabla.setForeground(COLOR_TEXTO);
        tabla.setBackground(Color.WHITE);
        tabla.setIntercellSpacing(new Dimension(0, 1));

        JTableHeader header = tabla.getTableHeader();
        header.setReorderingAllowed(false);
        header.setBackground(new Color(248, 250, 252));
        header.setForeground(COLOR_TEXTO_SUAVE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setPreferredSize(new Dimension(0, 34));

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setBorder(new EmptyBorder(0, 8, 0, 8));
        for (int i = 0; i < tabla.getColumnCount(); i++) {
            tabla.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }
        return tabla;
    }

    private void registrarProductoDesdeUI() {
        try {
            String codigo = txtCodigoProducto.getText().trim();
            String nombre = txtNombreProducto.getText().trim();
            String textoPrecio = txtPrecioProducto.getText().trim().replace(",", ".");
            double precio = Double.parseDouble(textoPrecio);

            if (codigo.isEmpty()) {
                gestor.registrarProducto(nombre, precio);
            } else {
                gestor.registrarProducto(codigo, nombre, precio);
            }

            txtCodigoProducto.setText("");
            txtNombreProducto.setText("");
            txtPrecioProducto.setText("");
            refrescarTodo();
            mostrarExito("Producto registrado correctamente.");
        } catch (NumberFormatException ex) {
            mostrarError("El precio debe ser un número válido.");
        } catch (ValidacionException | IllegalArgumentException ex) {
            mostrarError(ex.getMessage());
        } catch (Exception ex) {
            mostrarError("Ocurrió un error inesperado al registrar el producto.");
        }
    }

    private void registrarClienteDesdeUI() {
        try {
            String nombre = txtNombreCliente.getText().trim();
            String telefono = txtTelefonoCliente.getText().trim();
            String email = txtEmailCliente.getText().trim();

            if (email.isEmpty()) {
                gestor.registrarCliente(nombre, telefono);
            } else {
                gestor.registrarCliente(nombre, telefono, email);
            }

            txtNombreCliente.setText("");
            txtTelefonoCliente.setText("");
            txtEmailCliente.setText("");
            refrescarTodo();
            mostrarExito("Cliente registrado correctamente.");
        } catch (ValidacionException | IllegalArgumentException ex) {
            mostrarError(ex.getMessage());
        } catch (Exception ex) {
            mostrarError("Ocurrió un error inesperado al registrar el cliente.");
        }
    }

    private void agregarProductoAlCarrito() {
        try {
            Producto producto = (Producto) cmbProductos.getSelectedItem();
            if (producto == null) {
                throw new ValidacionException("Primero registre y seleccione un producto.");
            }
            int cantidad = (Integer) spnCantidad.getValue();

            carrito.add(new GestorPedidosController.DetalleTemporal(producto, cantidad));
            spnCantidad.setValue(1);
            refrescarCarrito();
        } catch (ValidacionException ex) {
            mostrarError(ex.getMessage());
        } catch (Exception ex) {
            mostrarError("No fue posible agregar el producto al pedido.");
        }
    }

    private void quitarItemCarrito() {
        int fila = tablaCarrito.getSelectedRow();
        if (fila < 0) {
            mostrarError("Seleccione una fila del detalle para quitarla.");
            return;
        }
        carrito.remove(fila);
        refrescarCarrito();
    }

    private void limpiarCarrito() {
        carrito.clear();
        refrescarCarrito();
    }

    private void registrarPedidoDesdeUI() {
        try {
            Cliente cliente = (Cliente) cmbClientes.getSelectedItem();
            gestor.crearPedido(cliente, carrito);
            carrito.clear();
            refrescarTodo();
            mostrarExito("Pedido registrado correctamente.");
        } catch (ValidacionException | IllegalArgumentException ex) {
            mostrarError(ex.getMessage());
        } catch (Exception ex) {
            mostrarError("Ocurrió un error inesperado al registrar el pedido.");
        }
    }

    private void eliminarProductoSeleccionado() {
        int fila = tablaProductos.getSelectedRow();
        if (fila < 0) {
            mostrarError("Seleccione un producto de la tabla.");
            return;
        }
        String codigo = String.valueOf(modeloProductos.getValueAt(fila, 0));
        if (!confirmar("¿Desea retirar el producto " + codigo + " del catálogo?")) {
            return;
        }
        try {
            gestor.eliminarProducto(codigo);
            refrescarTodo();
        } catch (ValidacionException ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void eliminarClienteSeleccionado() {
        int fila = tablaClientes.getSelectedRow();
        if (fila < 0) {
            mostrarError("Seleccione un cliente de la tabla.");
            return;
        }
        String id = String.valueOf(modeloClientes.getValueAt(fila, 0));
        if (!confirmar("¿Desea eliminar el cliente " + id + "?")) {
            return;
        }
        try {
            gestor.eliminarCliente(id);
            refrescarTodo();
        } catch (ValidacionException ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void cancelarPedidoSeleccionado() {
        int fila = tablaPedidos.getSelectedRow();
        if (fila < 0) {
            mostrarError("Seleccione un pedido de la tabla.");
            return;
        }
        String id = String.valueOf(modeloPedidos.getValueAt(fila, 0));
        if (!confirmar("¿Desea cancelar el pedido " + id + "?")) {
            return;
        }
        try {
            gestor.cancelarPedido(id);
            refrescarTodo();
        } catch (ValidacionException ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void refrescarTodo() {
        refrescarDashboard();
        refrescarProductos();
        refrescarClientes();
        refrescarPedidos();
        refrescarHistorial();
        refrescarCombos();
        refrescarCarrito();
    }

    private void refrescarDashboard() {
        if (lblTotalProductos == null) {
            return;
        }
        lblTotalProductos.setText(String.valueOf(gestor.getProductos().size()));
        lblTotalClientes.setText(String.valueOf(gestor.getClientes().size()));
        lblPedidosActivos.setText(String.valueOf(gestor.getCantidadPedidosActivos()));
        lblTotalVendido.setText(moneda(gestor.getTotalVendido()));

        modeloRecientes.setRowCount(0);
        List<Pedido> pedidos = gestor.getPedidos();
        int inicio = Math.max(0, pedidos.size() - 8);
        for (int i = pedidos.size() - 1; i >= inicio; i--) {
            Pedido pedido = pedidos.get(i);
            modeloRecientes.addRow(new Object[]{
                    pedido.getId(),
                    pedido.getCliente().getNombre(),
                    pedido.getFechaRegistro().format(FORMATO_FECHA),
                    pedido.getEstado(),
                    moneda(pedido.calcularTotal())
            });
        }
    }

    private void refrescarProductos() {
        if (modeloProductos == null) {
            return;
        }
        modeloProductos.setRowCount(0);
        for (Producto producto : gestor.getProductos()) {
            modeloProductos.addRow(new Object[]{
                    producto.getCodigo(), producto.getNombre(), moneda(producto.getPrecio())
            });
        }
    }

    private void refrescarClientes() {
        if (modeloClientes == null) {
            return;
        }
        modeloClientes.setRowCount(0);
        for (Cliente cliente : gestor.getClientes()) {
            modeloClientes.addRow(new Object[]{
                    cliente.getId(), cliente.getNombre(), cliente.getTelefono(),
                    cliente.getEmail().isEmpty() ? "-" : cliente.getEmail()
            });
        }
    }

    private void refrescarPedidos() {
        if (modeloPedidos == null) {
            return;
        }
        modeloPedidos.setRowCount(0);
        for (Pedido pedido : gestor.getPedidos()) {
            modeloPedidos.addRow(new Object[]{
                    pedido.getId(),
                    pedido.getCliente().getNombre(),
                    pedido.getFechaRegistro().format(FORMATO_FECHA),
                    pedido.calcularCantidadTotal(),
                    pedido.getEstado(),
                    moneda(pedido.calcularTotal())
            });
        }
    }

    private void refrescarHistorial() {
        if (modeloHistorial == null) {
            return;
        }
        modeloHistorial.setRowCount(0);
        List<HistorialEvento> eventos = gestor.getHistorial();
        for (int i = eventos.size() - 1; i >= 0; i--) {
            HistorialEvento evento = eventos.get(i);
            modeloHistorial.addRow(new Object[]{
                    evento.getFechaFormateada(), evento.getTipo(), evento.getDescripcion()
            });
        }
    }

    private void refrescarCombos() {
        if (cmbClientes == null || cmbProductos == null) {
            return;
        }

        Object clienteSeleccionado = cmbClientes.getSelectedItem();
        Object productoSeleccionado = cmbProductos.getSelectedItem();

        cmbClientes.removeAllItems();
        for (Cliente cliente : gestor.getClientes()) {
            cmbClientes.addItem(cliente);
        }

        cmbProductos.removeAllItems();
        for (Producto producto : gestor.getProductos()) {
            cmbProductos.addItem(producto);
        }

        if (clienteSeleccionado != null) {
            cmbClientes.setSelectedItem(clienteSeleccionado);
        }
        if (productoSeleccionado != null) {
            cmbProductos.setSelectedItem(productoSeleccionado);
        }
    }

    private void refrescarCarrito() {
        if (modeloCarrito == null) {
            return;
        }
        modeloCarrito.setRowCount(0);
        double total = 0;
        for (GestorPedidosController.DetalleTemporal item : carrito) {
            Producto producto = item.getProducto();
            double subtotal = producto.getPrecio() * item.getCantidad();
            total += subtotal;
            modeloCarrito.addRow(new Object[]{
                    producto.getCodigo(), producto.getNombre(), item.getCantidad(),
                    moneda(producto.getPrecio()), moneda(subtotal)
            });
        }
        lblTotalCarrito.setText("Total: " + moneda(total));
    }

    private String moneda(double valor) {
        return String.format("S/ %.2f", valor);
    }

    private boolean confirmar(String mensaje) {
        return JOptionPane.showConfirmDialog(this, mensaje, "Confirmar",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION;
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Atención", JOptionPane.WARNING_MESSAGE);
    }

    private void mostrarExito(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Correcto", JOptionPane.INFORMATION_MESSAGE);
    }

    private void exportarHistorialCSV() {
        List<HistorialEvento> eventos = gestor.getHistorial();
        if (eventos.isEmpty()) {
            mostrarError("No hay datos en el historial para exportar.");
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar Historial como CSV");
        fileChooser.setSelectedFile(new File("HistorialMovimientos.csv"));

        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            try {
                ExportadorHistorial.exportarCSV(fileChooser.getSelectedFile(), eventos);
                mostrarExito("Historial exportado correctamente.");
            } catch (Exception ex) {
                mostrarError(ex.getMessage());
            }
        }
    }
}
