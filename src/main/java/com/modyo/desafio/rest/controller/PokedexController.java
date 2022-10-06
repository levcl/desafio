package com.modyo.desafio.rest.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.modyo.desafio.rest.cliente.ClientePokedex;
import com.modyo.desafio.rest.response.DetallePokemonTO;
import com.modyo.desafio.rest.response.ListadoPokedexTO;

@RestController
public class PokedexController {
    
    @GetMapping("/pokedex")
    public ListadoPokedexTO pokedex() {
        ListadoPokedexTO resultado = ClientePokedex.getListaPokemones();
        return resultado;
    }

    @GetMapping("/pokemon")
    public DetallePokemonTO pokemon() {
        DetallePokemonTO resultado = ClientePokedex.getDetallePokemon("bulbasaur");
        return resultado;
    }

}
