package org.example.practica_javafx_bbdd;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class Estudiante {

    private int nia;
    private String nombre;
    private LocalDate fechaNacimiento;

    @Override
    public String toString() {
        return "Estudiante{" +
                "nia=" + nia +
                ", nombre='" + nombre + '\'' +
                ", fechaNacimiento=" + fechaNacimiento +
                '}';
    }
}