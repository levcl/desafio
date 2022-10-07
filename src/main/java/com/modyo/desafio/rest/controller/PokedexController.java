package com.modyo.desafio.rest.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.modyo.desafio.rest.cliente.ClientePokedex;
import com.modyo.desafio.rest.response.DetallePokemonTO;
import com.modyo.desafio.rest.response.ListadoPokedexTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "API desfío MODYO")
public class PokedexController {
    
    //TODO: definir parametros para paginación
    @ApiOperation(value = "Permite obtener el listado paginado de pokemones de la pokedex")
    @GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE }, path ="/pokedex")
    @ResponseStatus(code = HttpStatus.OK)
    public ListadoPokedexTO pokedex() {
        ListadoPokedexTO resultado = ClientePokedex.getListaPokemones();
        return resultado;
    }
    
    @ApiOperation(value = "Permite obtener información de un pokemon")
    @GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE }, path ="/pokemon/{nombre}")
    @ResponseStatus(code = HttpStatus.OK)
    public DetallePokemonTO pokemon(@PathVariable("nombre") String nombre) {
        DetallePokemonTO resultado = ClientePokedex.getDetallePokemon(nombre);
        return resultado;
    }

}
