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
                Date fechaSQL = resultado.getDate("fechaNacimiento");
                LocalDate fechaNacimiento = (fechaSQL != null) ? fechaSQL.toLocalDate() : null;

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

        StringBuilder query = new StringBuilder();
        query.append("INSERT INTO estudiante (nia, nombre, fechaNacimiento) VALUES ('");

        query.append(estudiante.getNia());
        query.append("','");
        query.append(estudiante.getNombre());
        query.append("','");
        query.append(estudiante.getFechaNacimiento());
        query.append("')");

        Statement statement;

        try {
            statement = conexion.createStatement();
            statement.executeUpdate(query.toString());

            out.println("Fila insertada con éxito...");

        } catch (SQLException e) {
            out.println(e.getMessage());
            throw new RuntimeException(e);
        }

    }

    public static void modificar(Connection conexion, Estudiante estudiante, int niaAnterior){

        StringBuilder query = new StringBuilder();

        query.append("UPDATE estudiante SET ")
                .append("nia = '")
                .append(estudiante.getNia())

                .append("', ")

                .append("nombre = '")
                .append(estudiante.getNombre())

                .append("', ")

                .append("fechaNacimiento = '")
                .append(estudiante.getFechaNacimiento())

                .append("' ")

                .append("WHERE nia = '")
                .append(niaAnterior)

                .append("'");

        out.printf(query.toString());

        Statement statement;

        try {
            statement = conexion.createStatement();
            statement.executeUpdate(query.toString());

            out.println("Fila Modificada con éxito...");

        } catch (SQLException e) {
            out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static void borrar(Connection conexion, Estudiante estudiante){

        StringBuilder query = new StringBuilder();

        query.append("DELETE FROM estudiante WHERE nia = '")
            .append(estudiante.getNia())
            .append("'");

        Statement statement;

        try {
            statement = conexion.createStatement();
            statement.executeUpdate(query.toString());

            out.println("Fila Eliminada con éxito...");

        } catch (SQLException e) {
            out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}