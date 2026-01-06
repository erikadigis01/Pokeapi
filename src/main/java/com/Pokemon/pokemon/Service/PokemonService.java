package com.Pokemon.pokemon.Service;

import com.Pokemon.pokemon.DTO.PageResponse;
import com.Pokemon.pokemon.DTO.PokemonDTO;
import com.Pokemon.pokemon.DTO.PokemonListResponse;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PokemonService {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String BASE_URL = "https://pokeapi.co/api/v2/pokemon";

    // Pokémon por nombre
    public PokemonDTO getPokemonByName(String name) {
        String url = BASE_URL + "/" + name;
        return restTemplate.getForObject(url, PokemonDTO.class);
    }

    // Paginación
    public PageResponse<PokemonDTO> getPokemons(int page, int size) {

        int offset = page * size;
        String url = BASE_URL + "?limit=" + size + "&offset=" + offset;

        PokemonListResponse response
                = restTemplate.getForObject(url, PokemonListResponse.class);

        List<PokemonDTO> pokemons = response.getResults()
                .stream()
                .map(r -> restTemplate.getForObject(r.getUrl(), PokemonDTO.class))
                .toList();

        long total = response.getCount();

        return new PageResponse<>(pokemons, total);
    }
}
