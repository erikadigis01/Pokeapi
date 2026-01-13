package com.Pokemon.pokemon.RestController;

import com.Pokemon.pokemon.DTO.PageResponse;
import com.Pokemon.pokemon.DTO.PokemonAdminCardDTO;
import com.Pokemon.pokemon.Service.PokemonService;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminRestController {

    private final PokemonService pokemonService;

    public AdminRestController(PokemonService pokemonService) {
        this.pokemonService = pokemonService;
    }

    @GetMapping("/pokemons")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageResponse<PokemonAdminCardDTO>> listarPokemonsAdmin(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Integer number
    ) {
        return ResponseEntity.ok(
                pokemonService.getAdminPokemons(page, size, name, type, number)
        );
    }

}
