// HelloController.java
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


    // INICIAR
    @FXML
    private void initialize() {

        tabla_Nia.setCellValueFactory(data ->
                new javafx.beans.property.SimpleIntegerProperty(data.getValue().getNia()).asObject());
        tabla_Nombre.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getNombre()));
        tabla_FechaNacimiento.setCellValueFactory(data ->
                new ReadOnlyObjectWrapper<>(data.getValue().getFechaNacimiento()));

        tabla_Estudiantes.setItems(Funcionalidades.consultar(conexion));
        guardar_Button.setDisable(true);
    }

    // INSERTAR ESTUDIANTE
    @FXML
    protected void onInsertarButtonClick(ActionEvent event) {

        if (validarCampos()) return;

        int nia = Integer.parseInt(textField_Nia.getText().trim());
        String nombre = textField_Nombre.getText().trim();
        LocalDate fecha = field_Date.getValue();

        Funcionalidades.insertar(conexion, new Estudiante(nia, nombre, fecha));
        System.out.println("Estudiante insertado con éxito");

        tabla_Estudiantes.setItems(Funcionalidades.consultar(conexion));
        limpiarCampos();
    }

    // EDITAR ESTUDIANTE
    @FXML
    protected void onEditarButtonClick(ActionEvent event) {

        Estudiante estudianteSeleccionado = tabla_Estudiantes.getSelectionModel().getSelectedItem();

        if (estudianteSeleccionado == null) {
            mostrarMensajeError("Selección vacía", "Selecciona un estudiante para editar.");
            return;
        }

        niaAnterior = estudianteSeleccionado.getNia();

        textField_Nia.setText(String.valueOf(estudianteSeleccionado.getNia()));
        textField_Nombre.setText(estudianteSeleccionado.getNombre());
        field_Date.setValue(estudianteSeleccionado.getFechaNacimiento());

        guardar_Button.setDisable(false);
        insertar_Button.setDisable(true);
    }

    // GUARDAR ESTUDIANTE
    @FXML
    protected void onGuardarButtonClick(ActionEvent event) {

        if (validarCampos()) return;

        int nia = Integer.parseInt(textField_Nia.getText().trim());
        String nombre = textField_Nombre.getText().trim();
        LocalDate fecha = field_Date.getValue();

        Funcionalidades.modificar(conexion, new Estudiante(nia, nombre, fecha), niaAnterior);
        System.out.println("Estudiante modificado con éxito");

        tabla_Estudiantes.setItems(Funcionalidades.consultar(conexion));

        limpiarCampos();

        guardar_Button.setDisable(true);
        insertar_Button.setDisable(false);
    }

    // ELIMINAR ESTUDIANTE
    @FXML
    protected void onEliminarButtonClick(ActionEvent event) {

        Estudiante estudianteSeleccionado = tabla_Estudiantes.getSelectionModel().getSelectedItem();

        if (estudianteSeleccionado == null) {
            mostrarMensajeError("Selección vacía", "Seleccione un estudiante para eliminar.");
            return;
        }

        Funcionalidades.borrar(conexion, estudianteSeleccionado);
        System.out.println("Estudiante eliminado con éxito");

        tabla_Estudiantes.setItems(Funcionalidades.consultar(conexion));
    }


    // Validar NIA y NOMBRE, muestra alertas si hay errores
    private boolean validarCampos() {

        String niaText = textField_Nia.getText().trim();
        String nombre = textField_Nombre.getText().trim();

        if (!niaText.matches("\\d{8}")) {
            mostrarMensajeError("NIA inválido", "El NIA debe contener 8 dígitos.");
            return true;
        }
        if (!nombre.matches("[A-Za-zÁÉÍÓÚáéíóúÑñ ]+")) {
            mostrarMensajeError("Nombre inválido", "El nombre debe contener solo letras y espacios.");
            return true;
        }
        if (field_Date.getValue() == null) {
            mostrarMensajeError("Fecha vacía", "Seleccione una fecha de nacimiento.");
            return true;
        }

        return false;
    }

    // Limpia los campos de entrada
    private void limpiarCampos() {
        textField_Nia.clear();
        textField_Nombre.clear();
        field_Date.setValue(null);
    }

    // Muestra un Alerta de error con título y mensaje
    private void mostrarMensajeError(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}