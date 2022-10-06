package com.modyo.desafio.rest.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetallePokemonTO {
    private String nombre;
    private int peso;
    private List<String> tipos;
    private List<String> habilidades;
    private String descripcion;
    private List<String> evoluciones;
}
