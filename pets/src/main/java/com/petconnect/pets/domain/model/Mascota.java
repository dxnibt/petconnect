package com.petconnect.pets.domain.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Mascota {
    private long id_mascota;
    private String nombre;
    private String raza;
    private Integer edad;
    private String sexo;
    private Boolean recomendadoParaNinos;
    private Boolean requiereEspacioAmplio;
    private Boolean esterilizacion;
    private String vacunas;
    private String descripcion;
    private String imagenUrl;
    private String estado;




}
