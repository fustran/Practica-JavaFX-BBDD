package org.example.practica_javafx_bbdd;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.sql.Connection;
import java.time.LocalDate;

public class HelloController {

    static Connection conexion = Funcionalidades.conectar();

    @FXML
    private TableView<Estudiante> tabla_Estudiantes;

    @FXML
    private TableColumn<Estudiante, Integer> tabla_Nia;

    @FXML
    private TableColumn<Estudiante, String> tabla_Nombre;

    @FXML
    private TableColumn<Estudiante, LocalDate> tabla_FechaNacimiento;

    @FXML
    private TextField textField_Nia;

    @FXML
    private TextField textField_Nombre;

    @FXML
    private DatePicker field_Date;

    //private ObservableList<Estudiante> listaEstudiantes = FXCollections.observableArrayList();

    @FXML
    private void initialize(){

        Connection conexion = Funcionalidades.conectar();

        tabla_Nia.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getNia()).asObject());
        tabla_Nombre.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNombre()));
        tabla_FechaNacimiento.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getFechaNacimiento()));

        tabla_Estudiantes.setItems(Funcionalidades.consultar(conexion));

    }

    @FXML
    protected void onGuardarButtonClick() {

        int nia =Integer.parseInt(textField_Nia.getText());
        String nombre = textField_Nombre.getText();
        LocalDate fechaNacimiento = field_Date.getValue();

        Estudiante estudiante = new Estudiante(nia, nombre, fechaNacimiento);

        Funcionalidades.insertar(conexion, estudiante);
        System.out.println("Estudiante insertado con éxito");

    }
}