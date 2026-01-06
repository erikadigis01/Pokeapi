package com.Pokemon.pokemon.RestController;

import com.Pokemon.pokemon.DTO.PageResponse;
import com.Pokemon.pokemon.DTO.PokemonDTO;
import com.Pokemon.pokemon.Service.PokemonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pokemon")
public class PokemonRestController {

    @Autowired
    private PokemonService pokemonService;

    @GetMapping("/{name}")
    public PokemonDTO getPokemonByName(@PathVariable String name) {
        return pokemonService.getPokemonByName(name);
    }

    @GetMapping("/pokemons")
    public PageResponse<PokemonDTO> getPokemons(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return pokemonService.getPokemons(page, size);
    }
}
