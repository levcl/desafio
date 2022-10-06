package com.modyo.desafio.rest.to;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PokemonTO {
    private String nombre;
    private int peso;
    private List<String> tipos;
    private List<String> habilidades;
    
    
}
