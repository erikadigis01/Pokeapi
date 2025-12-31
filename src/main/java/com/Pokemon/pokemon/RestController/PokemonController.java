package com.Pokemon.pokemon.RestController;

import com.Pokemon.pokemon.DTO.PokemonDTO;
import com.Pokemon.pokemon.Service.PokemonService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pokemon")
public class PokemonController {
    
    @Autowired
    PokemonService pokemonService;
    
    //traerpokemon por nombre
    @GetMapping("/{name}")
    public PokemonDTO GetPokemonByName(@PathVariable String name){
        return pokemonService.getPokemonByName(name);
    }
    
    @GetMapping("/pokemons")
    public List<PokemonDTO> GetAll(@RequestParam(defaultValue = "20") int limit, @RequestParam(defaultValue = "0") int offset){
        return pokemonService.getPokemons(limit, offset);
    }

}
