package dao;

import java.sql.SQLException;
import java.util.List;
import modelo.Producto;

public interface ProductoDAO {

    void insertar(Producto producto) throws SQLException;

    List<Producto> listar() throws SQLException;

    void actualizar(Producto producto) throws SQLException;

    void eliminar(String codigo) throws SQLException;
}