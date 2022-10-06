package com.modyo.desafio.rest.cliente;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.client.RestTemplate;

import com.modyo.desafio.rest.response.DetallePokemonTO;
import com.modyo.desafio.rest.response.ListadoPokedexTO;
import com.modyo.desafio.rest.to.PokemonTO;

public class ClientePokedex {

    private static RestTemplate restTemplate = new RestTemplate();
    
    public static ListadoPokedexTO getListaPokemones() {
        ListadoPokedexTO resultado = new ListadoPokedexTO();
        List<PokemonTO> listaPokemones = new ArrayList<>();
        
        String url = "https://pokeapi.co/api/v2/pokemon/";
         //Object forObject = restTemplate.getForObject(url, Object.class);
        String resp = restTemplate.getForObject(url, String.class);
        JSONObject respuesta = new JSONObject(resp);
        System.out.println(respuesta);
        if(respuesta.has("results")){
            JSONArray resultados = respuesta.getJSONArray("results");
            for (int i = 0; i < resultados.length(); i++){
                String urlPokemon = resultados.getJSONObject(i).get("url").toString();
                listaPokemones.add(getPokemon(urlPokemon));
            }
        }
        resultado.setListaPokemones(listaPokemones);
        resultado.setPagina(0);
        return resultado;
    }
        
    public static DetallePokemonTO getDetallePokemon(String nombre) {
        String urlPokemon = "https://pokeapi.co/api/v2/pokemon/"+nombre;
        JSONObject pokemonJson = new JSONObject(restTemplate.getForObject(urlPokemon, String.class));
        int id = pokemonJson.getInt("id");
        int peso = pokemonJson.getInt("weight");
        return new DetallePokemonTO(nombre, peso, getTipos(pokemonJson),getHabilidades(pokemonJson), 
            getDescripcion(id), null);
            
    }

    private static PokemonTO getPokemon(String url){
        JSONObject pokemonJson = new JSONObject(restTemplate.getForObject(url, String.class));
        String nombre = pokemonJson.getString("name");
        int peso = pokemonJson.getInt("weight");
        return new PokemonTO(nombre,peso,getTipos(pokemonJson),getHabilidades(pokemonJson));
    }

    private static List<String> getTipos(JSONObject pokemonJson){
        List<String> listaTipos= new ArrayList<>();
        String nombre = "";
        if(pokemonJson.has("types")){
            JSONArray tipos = pokemonJson.getJSONArray("types");
            for (int j = 0; j < tipos.length(); j++){
                if(tipos.getJSONObject(j).has("type")){
                    nombre = tipos.getJSONObject(j).getJSONObject("type").getString("name");
                    listaTipos.add(nombre);
                }
            }
        }
        
        return listaTipos;
    }

    private static List<String> getHabilidades(JSONObject pokemonJson){
        List<String> listaHabilidades = new ArrayList<>();
        String nombre = "";
        if(pokemonJson.has("abilities")){
            JSONArray habilidades = pokemonJson.getJSONArray("abilities");
            for (int j = 0; j < habilidades.length(); j++){
                if(habilidades.getJSONObject(j).has("ability")){
                    nombre = habilidades.getJSONObject(j).getJSONObject("ability").getString("name");
                    listaHabilidades.add(nombre);
                }
            }
        }
        return listaHabilidades;
    }
    private static String getDescripcion(int id){
        String descripcion = "";
        String urlCaracteristicas = "https://pokeapi.co/api/v2/characteristic/"+id+"/";
        JSONObject pokemonCaracteristicas = new JSONObject(restTemplate.getForObject(urlCaracteristicas, String.class));
        if(pokemonCaracteristicas.has("descriptions")){
            JSONArray descripciones = pokemonCaracteristicas.getJSONArray("descriptions");
            for (int j = 0; j < descripciones.length(); j++){
                if(descripciones.getJSONObject(j).has("description")){
                    descripcion = descripciones.getJSONObject(j).getString("description");
                }
            }
        }
        return descripcion;
    }
}
