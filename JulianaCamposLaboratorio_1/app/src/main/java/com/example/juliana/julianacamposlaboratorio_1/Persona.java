package com.example.juliana.julianacamposlaboratorio_1;

/**
 * Created by Juliana on 04/04/2018.
 */

public class Persona {
    String nombre;
    String genero;
    String uriFoto;
    String perfil;

    public Persona(String nombre, String genero, String uriFoto, String perfil) {
        this.nombre = nombre;
        this.genero = genero;
        this.uriFoto = uriFoto;
        this.perfil = perfil;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getUriFoto() {
        return uriFoto;
    }

    public void setUriFoto(String uriFoto) {
        this.uriFoto = uriFoto;
    }

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }
}
