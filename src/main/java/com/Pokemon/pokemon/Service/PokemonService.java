package com.Pokemon.pokemon.Service;

import com.Pokemon.pokemon.DTO.PokemonDTO;
import com.Pokemon.pokemon.DTO.PokemonListResponse;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PokemonService {
    private final RestTemplate restTemplate = new RestTemplate(); 
    private final String BASE_URL = "https://pokeapi.co/api/v2/pokemon/"; 
    
    //traer un  pokemon por nombre
    public PokemonDTO getPokemonByName(String name) { 
        String url = BASE_URL + name; 
        return restTemplate.getForObject(url, PokemonDTO.class); 
    }
    //traer getall de pokemones limit = cantidad de pokemones offset = desde donde empezar
    public List<PokemonDTO> getPokemons(int limit, int offset) {
        String url = BASE_URL + "?limit=" + limit + "&offset=" + offset;

        PokemonListResponse response = restTemplate.getForObject(url, PokemonListResponse.class);

        List<PokemonDTO> pokemons = new ArrayList<>();

        if (response != null && response.getResults() != null) {
            for (PokemonListResponse.PokemonResult result : response.getResults()) {
                // Llamada adicional para traer detalles completos
                PokemonDTO dto = restTemplate.getForObject(result.getUrl(), PokemonDTO.class);
                pokemons.add(dto);
            }
        }

        return pokemons;
    }

}
