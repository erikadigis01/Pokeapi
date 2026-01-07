package com.Pokemon.pokemon.RestController;

import com.Pokemon.pokemon.DTO.PageResponse;
import com.Pokemon.pokemon.DTO.PokemonCardDTO;
import com.Pokemon.pokemon.DTO.PokemonDTO;
import com.Pokemon.pokemon.Service.PokemonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pokemon")
@CrossOrigin // si consumes desde frontend separado
public class PokemonRestController {

    @Autowired
    private PokemonService pokemonService;

    // 🔹 LISTA DE CARTAS (con filtros + paginación)
    @GetMapping("/pokemons")
    public PageResponse<PokemonCardDTO> getPokemons(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Integer number
    ) {
        return pokemonService.getPokemons(page, size, name, type, number);
    }

    // 🔹 DETALLE (MODAL)
    @GetMapping("/{name}")
    public PokemonDTO getPokemonByName(@PathVariable String name) {
        return pokemonService.getPokemonByName(name);
    }
}
