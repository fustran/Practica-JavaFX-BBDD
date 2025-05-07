package org.example.practica_javafx_bbdd;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.sql.Connection;
import java.time.LocalDate;

public class HelloController {

    static Connection conexion = Funcionalidades.conectar();
    static int niaAnterior;

    @FXML private TableView<Estudiante> tabla_Estudiantes;

    @FXML private TableColumn<Estudiante, Integer> tabla_Nia;
    @FXML private TableColumn<Estudiante, String> tabla_Nombre;
    @FXML private TableColumn<Estudiante, LocalDate> tabla_FechaNacimiento;

    @FXML private TextField textField_Nia;
    @FXML private TextField textField_Nombre;
    @FXML private DatePicker field_Date;

    @FXML private Button editar_Button;
    @FXML private Button eliminar_Button;
    @FXML private Button insertar_Button;
    @FXML private Button guardar_Button;


    @FXML
    private void initialize(){

        Connection conexion = Funcionalidades.conectar();

        tabla_Nia.setCellValueFactory(data ->
                new javafx.beans.property.SimpleIntegerProperty(data.getValue().getNia()).asObject());
        tabla_Nombre.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getNombre()));
        tabla_FechaNacimiento.setCellValueFactory(data ->
                new ReadOnlyObjectWrapper<>(data.getValue().getFechaNacimiento()));

        // Refrescar la tabla
        tabla_Estudiantes.setItems(Funcionalidades.consultar(conexion));

        guardar_Button.setDisable(true);
    }


    // INSERTAR NUEVO ESTUDIANTE
    @FXML
    protected void onInsertarButtonClick(ActionEvent actionEvent){

        int nia =Integer.parseInt(textField_Nia.getText());
        String nombre = textField_Nombre.getText();
        LocalDate fechaNacimiento = field_Date.getValue();

        Estudiante estudiante = new Estudiante(nia, nombre, fechaNacimiento);

        Funcionalidades.insertar(conexion, estudiante);
        System.out.println("Estudiante insertado con éxito");

        // Refrescar la tabla
        tabla_Estudiantes.setItems(Funcionalidades.consultar(conexion));

        textField_Nia.clear();
        textField_Nombre.clear();
        field_Date.setValue(null);

    }


    // EDITAR NUEVO ESTUDIANTE
    @FXML
    protected void onEditarButtonClick(ActionEvent actionEvent){

        Estudiante estudianteSeleccionado = tabla_Estudiantes.getSelectionModel().getSelectedItem();

        if (estudianteSeleccionado == null) {
            System.out.println("No hay ninguna fila seleccionada...");
            return;
        }

        niaAnterior = estudianteSeleccionado.getNia();

        textField_Nia.setText(String.valueOf(estudianteSeleccionado.getNia()));
        textField_Nombre.setText(estudianteSeleccionado.getNombre());
        field_Date.setValue(estudianteSeleccionado.getFechaNacimiento());

        guardar_Button.setDisable(false);
        insertar_Button.setDisable(true);
    }


    // GUARDAR CAMBIOS (UPDATE)
    @FXML
    protected void onGuardarButtonClick(ActionEvent actionEvent) {

        int nia =Integer.parseInt(textField_Nia.getText());
        String nombre = textField_Nombre.getText();
        LocalDate fechaNacimiento = field_Date.getValue();

        Estudiante estudiante = new Estudiante(nia, nombre, fechaNacimiento);

        Funcionalidades.modificar(conexion, estudiante, niaAnterior);
        System.out.println("Estudiante insertado con éxito");

        // Refrescar la tabla
        tabla_Estudiantes.setItems(Funcionalidades.consultar(conexion));

        textField_Nombre.clear();
        textField_Nia.clear();
        field_Date.setValue(null);

        guardar_Button.setDisable(true);
        insertar_Button.setDisable(false);
    }


    // ELIMINAR ESTUDIANTE SELECCIONADO
    @FXML
    protected void onEliminarButtonClick(ActionEvent actionEvent){

        Estudiante estudianteSeleccionado = tabla_Estudiantes.getSelectionModel().getSelectedItem();

        if (estudianteSeleccionado != null) {
            Funcionalidades.borrar(conexion, estudianteSeleccionado);
        }else {
            System.out.println("No hay ningún estudiante seleccionado...");
        }

        // Refrescar la tabla
        tabla_Estudiantes.setItems(Funcionalidades.consultar(conexion));
    }
}