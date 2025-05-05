package org.example.practica_javafx_bbdd;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;
import java.time.LocalDate;
import static java.lang.System.out;

public class Funcionalidades {

    // Conectar
    public static Connection conectar() {

        Connection conexion;
        String host = "jdbc:mariadb://localhost:3307/";
        String user = "root";
        String password = "";
        String bd = "instituto";

        try {
            conexion = DriverManager.getConnection(host + bd, user, password);
            out.println("Conexión realizada con éxito!");
        } catch (SQLException e) {
            out.println(e.getMessage());
            throw new RuntimeException(e);
        }

        return conexion;
    }

    // Desconectar
    public static void desconectar(Connection conexion) {

        try {
            conexion.close();
            out.println("Desconexión realizada con éxito");
        } catch (SQLException e) {
            out.println("Error de desconexión");
            throw new RuntimeException(e);
        }
    }

    // Consultar
    public static ObservableList<Estudiante> consultar(Connection conexion){

        String query = "SELECT * FROM estudiante";
        Statement statement;
        ResultSet resultado; // Guardar lo que devuelve

        ObservableList<Estudiante> listaEstudiantes = FXCollections.observableArrayList();

        try {
            statement = conexion.createStatement();
            resultado = statement.executeQuery(query);

            while (resultado.next()){
                int nia = resultado.getInt("Nia");
                String nombre = resultado.getString("Nombre");
                LocalDate fechaNacimiento = resultado.getDate("fechaNacimiento").toLocalDate();

                out.println("NIA: " + nia + ", NOMBRE: " + nombre + ", FECHA NACIMIENTO: " + fechaNacimiento);
                listaEstudiantes.add(new Estudiante(nia, nombre, fechaNacimiento));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return listaEstudiantes;

    }

    // Insertar
    public static void insertar(Connection conexion, Estudiante estudiante) {

        String query = "INSERT INTO estudiante (nia, nombre, fechaNacimiento) VALUES (?, ?, ?)";

        try (PreparedStatement statement = conexion.prepareStatement(query)) {

            statement.setInt(1, estudiante.getNia());
            statement.setString(2, estudiante.getNombre());
            statement.setObject(3, estudiante.getFechaNacimiento());

            statement.executeUpdate();

        } catch (SQLException e) {
            out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}