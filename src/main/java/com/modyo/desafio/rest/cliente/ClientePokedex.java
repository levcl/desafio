package com.modyo.desafio.rest.cliente;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import com.modyo.desafio.rest.response.DetallePokemonTO;
import com.modyo.desafio.rest.response.ListadoPokedexTO;
import com.modyo.desafio.rest.to.PokemonTO;

public class ClientePokedex {

    private static RestTemplate restTemplate = new RestTemplate();
    private static HttpHeaders headers = new HttpHeaders();
    
    
    public static ListadoPokedexTO getListaPokemones() {
        ListadoPokedexTO resultado = new ListadoPokedexTO();
        List<PokemonTO> listaPokemones = new ArrayList<>();
        int total = 0;
        String endpoint = "https://pokeapi.co/api/v2/pokemon/";
        headers.add("user-agent", "Application");
		HttpEntity<String> entity = new HttpEntity<>(headers);
        JSONObject respuesta = new JSONObject(restTemplate.exchange(endpoint, HttpMethod.GET, entity,String.class).getBody());
        total = respuesta.getInt("count");
        if(respuesta.has("results")){
            JSONArray resultados = respuesta.getJSONArray("results");
            for (int i = 0; i < resultados.length(); i++){
                String urlPokemon = resultados.getJSONObject(i).get("url").toString();
                listaPokemones.add(getPokemon(urlPokemon));
            }
        }
        //TODO: setear anterior y siguiente con urls propias, usar parametros en servicio
        //String anterior = respuesta.getString("previous");
        //String siguiente= respuesta.getString("next");
        
        resultado.setListaPokemones(listaPokemones);
        resultado.setTotal(total);
        resultado.setAnterior("");
        resultado.setSiguiente("");
        return resultado;
    }
        
    public static DetallePokemonTO getDetallePokemon(String nombre) {
        String endpoint = "https://pokeapi.co/api/v2/pokemon/"+nombre;
        headers.add("user-agent", "Application");
		HttpEntity<String> entity = new HttpEntity<>(headers);
        //TODO: Implementar controlador de errores para llamadas restTemplate
        try{
            JSONObject pokemonJson = new JSONObject(restTemplate.exchange(endpoint, HttpMethod.GET, entity,String.class).getBody());
            int id = pokemonJson.getInt("id");
            int peso = pokemonJson.getInt("weight");
            return new DetallePokemonTO(nombre, peso, getTipos(pokemonJson),getHabilidades(pokemonJson), 
            getDescripcion(id), getEvoluciones(id));
        }catch(Exception e){
            return new DetallePokemonTO("Sin información", 0, null ,null, 
            null, null);
        }
    }

    private static PokemonTO getPokemon(String url){
        JSONObject pokemonJson = new JSONObject(restTemplate.getForObject(url, String.class));
        String nombre = pokemonJson.getString("name");
        int peso = pokemonJson.getInt("weight");
        //TODO: Obtener foto
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
        String lenguaje = "";
        String endpoint = "https://pokeapi.co/api/v2/characteristic/"+id+"/";
        headers.add("user-agent", "Application");
		HttpEntity<String> entity = new HttpEntity<>(headers);
        JSONObject pokemonCaracteristicas = new JSONObject(restTemplate.exchange(endpoint, HttpMethod.GET, entity,String.class).getBody());
        if(pokemonCaracteristicas.has("descriptions")){
            JSONArray descripciones = pokemonCaracteristicas.getJSONArray("descriptions");
            for (int j = 0; j < descripciones.length(); j++){
                if(descripciones.getJSONObject(j).has("description")){
                    lenguaje = descripciones.getJSONObject(j).getJSONObject("language").getString("name");
                    if(lenguaje.equals("es")){
                        descripcion = descripciones.getJSONObject(j).getString("description");
                        break;
                    }
                }
            }
        }
        return descripcion;
    }

    private static List<String> getEvoluciones(int id){
        List<String> listaEvoluciones = new ArrayList<>();
        String nombre = "";
        String endpoint = "https://pokeapi.co/api/v2/evolution-chain/"+id+"/";
        headers.add("user-agent", "Application");
		HttpEntity<String> entity = new HttpEntity<>(headers);
        JSONObject evoluciones = new JSONObject(restTemplate.exchange(endpoint, HttpMethod.GET, entity,String.class).getBody());
        if(evoluciones.has("chain")){
            JSONArray listaEvolucionesJson = evoluciones.getJSONObject("chain").getJSONArray("evolves_to");
            for (int j = 0; j < listaEvolucionesJson.length(); j++){
                nombre = listaEvolucionesJson.getJSONObject(j).getJSONObject("species").getString("name");
                listaEvoluciones.add(nombre);
                if(listaEvolucionesJson.getJSONObject(j).has("evolves_to")){
                    listaEvolucionesJson = listaEvolucionesJson.getJSONObject(j).getJSONArray("evolves_to");
                    //TODO: Implementar proceso recursivo
                    nombre = listaEvolucionesJson.getJSONObject(0).getJSONObject("species").getString("name");
                    listaEvoluciones.add(nombre);   
                }
            }
        }
        return listaEvoluciones;
    }
}
