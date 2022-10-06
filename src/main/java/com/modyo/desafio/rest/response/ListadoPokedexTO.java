package com.modyo.desafio.rest.response;

import java.util.List;

import com.modyo.desafio.rest.to.PokemonTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListadoPokedexTO {
    private int pagina;
	private List<PokemonTO> listaPokemones;
   
}