package dao;

import modelo.Producto;
import persistencia.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAOImpl implements ProductoDAO {

    private static final String SQL_INSERTAR =
            "INSERT INTO dbo.productos (codigo, nombre, precio, stock, activo) " +
            "VALUES (?, ?, ?, ?, ?)";

    private static final String SQL_LISTAR =
            "SELECT codigo, nombre, precio, stock, activo " +
            "FROM dbo.productos ORDER BY codigo";

    private static final String SQL_ACTUALIZAR =
            "UPDATE dbo.productos " +
            "SET nombre = ?, precio = ?, stock = ?, activo = ? " +
            "WHERE codigo = ?";

    private static final String SQL_ELIMINAR =
            "DELETE FROM dbo.productos WHERE codigo = ?";

    @Override
    public void insertar(Producto producto) throws SQLException {

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement statement =
                     conexion.prepareStatement(SQL_INSERTAR)) {

            statement.setString(1, producto.getCodigo());
            statement.setString(2, producto.getNombre());
            statement.setDouble(3, producto.getPrecio());
            statement.setInt(4, producto.getStock());
            statement.setBoolean(5, producto.isActivo());

            statement.executeUpdate();
        }
    }

    @Override
    public List<Producto> listar() throws SQLException {

        List<Producto> productos = new ArrayList<>();

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement statement =
                     conexion.prepareStatement(SQL_LISTAR);
             ResultSet resultado = statement.executeQuery()) {

            while (resultado.next()) {

                Producto producto = new Producto(
                        resultado.getString("codigo"),
                        resultado.getString("nombre"),
                        resultado.getDouble("precio"),
                        resultado.getInt("stock"),
                        resultado.getBoolean("activo")
                );

                productos.add(producto);
            }
        }

        return productos;
    }

    @Override
    public void actualizar(Producto producto) throws SQLException {

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement statement =
                     conexion.prepareStatement(SQL_ACTUALIZAR)) {

            statement.setString(1, producto.getNombre());
            statement.setDouble(2, producto.getPrecio());
            statement.setInt(3, producto.getStock());
            statement.setBoolean(4, producto.isActivo());
            statement.setString(5, producto.getCodigo());

            statement.executeUpdate();
        }
    }

    @Override
    public void eliminar(String codigo) throws SQLException {

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement statement =
                     conexion.prepareStatement(SQL_ELIMINAR)) {

            statement.setString(1, codigo);

            statement.executeUpdate();
        }
    }
}