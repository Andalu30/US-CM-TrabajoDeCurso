package us.cm.trabajodecurso;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class Reserva implements Serializable {

    private String titulo;
    private String descripcion;
    private String horario;
    private String ubicacion;
    private Calendar fecha;

    private String centro;
    private String disponibilidad;
    private String tipo;

    public Reserva() {
    }

    public Reserva(String titulo, String descripcion, String horario, String ubicacion, String fecha,
                   String centro, String disponibilidad, String tipo) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.horario = horario;
        this.ubicacion = ubicacion;


        String aParsear = fecha +" "+horario;

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        try {
            cal.setTime(sdf.parse(aParsear));
        } catch (ParseException e) {
            e.printStackTrace();
        }


        this.fecha = cal;

        this.centro = centro;
        this.disponibilidad = disponibilidad;
        this.tipo = tipo;
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

    public String getUbicacion() {
        return ubicacion;
    }

    public Calendar getFecha() {
        return fecha;
    }

    public String getCentro() {
        return centro;
    }

    public String getDisponibilidad() {
        return disponibilidad;
    }

    public String getTipo() {
        return tipo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reserva reserva = (Reserva) o;
        return Objects.equals(getTitulo(), reserva.getTitulo()) &&
                Objects.equals(getDescripcion(), reserva.getDescripcion()) &&
                Objects.equals(getHorario(), reserva.getHorario()) &&
                Objects.equals(getUbicacion(), reserva.getUbicacion()) &&
                Objects.equals(getFecha(), reserva.getFecha()) &&
                Objects.equals(getCentro(), reserva.getCentro()) &&
                Objects.equals(getDisponibilidad(), reserva.getDisponibilidad()) &&
                Objects.equals(getTipo(), reserva.getTipo());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTitulo(), getDescripcion(), getHorario(), getUbicacion(), getFecha(), getCentro(), getDisponibilidad(), getTipo());
    }


    @Override
    public String toString() {
        return "Reserva{" +
                "titulo='" + titulo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", horario='" + horario + '\'' +
                ", ubicacion='" + ubicacion + '\'' +
                ", fecha='" + fecha + '\'' +
                ", centro='" + centro + '\'' +
                ", disponibilidad='" + disponibilidad + '\'' +
                ", tipo='" + tipo + '\'' +
                '}';
    }
}

