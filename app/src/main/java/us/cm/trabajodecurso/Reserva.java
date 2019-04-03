package us.cm.trabajodecurso;

import java.util.Objects;

public class Reserva {

    private String titulo;
    private String descripcion;
    private String horario;
    private String ubicacion;
    private String fecha;

    public Reserva(String titulo, String descripcion, String horario, String ubicacion,
                   String fecha) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.horario = horario;
        this.ubicacion = ubicacion;
        this.fecha = fecha;
    }


    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getHorario() {
        return horario;
    }

    public String getFecha() {
        return fecha;
    }







    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reserva reserva = (Reserva) o;
        return Objects.equals(titulo, reserva.titulo) &&
                Objects.equals(descripcion, reserva.descripcion) &&
                Objects.equals(horario, reserva.horario) &&
                Objects.equals(ubicacion, reserva.ubicacion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(titulo, descripcion, horario, ubicacion);
    }
}
