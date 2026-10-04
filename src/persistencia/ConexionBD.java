package persistencia;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class ConexionBD {

    private static final String ARCHIVO_CONFIG = "config.properties";

    private ConexionBD() {
    }

    public static Connection obtenerConexion() throws SQLException {

        Properties propiedades = new Properties();

        try (FileInputStream input =
                     new FileInputStream(ARCHIVO_CONFIG)) {

            propiedades.load(input);

        } catch (IOException e) {
            throw new SQLException(
                    "No se pudo leer el archivo config.properties.",
                    e
            );
        }

        String url = propiedades.getProperty("db.url");

        if (url == null || url.isBlank()) {
            throw new SQLException(
                    "No se encontró db.url en config.properties."
            );
        }

        return DriverManager.getConnection(url);
    }
}