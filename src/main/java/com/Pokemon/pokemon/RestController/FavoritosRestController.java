package com.Pokemon.pokemon.RestController;

import com.Pokemon.pokemon.JPA.Favoritos;
import com.Pokemon.pokemon.Service.FavoritosService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/favoritos")
@CrossOrigin(origins = "*") // luego lo ajustas si usas auth real
public class FavoritosRestController {

    private final FavoritosService favoritosService;

    // Inyección por constructor (OBLIGATORIA)
    public FavoritosRestController(FavoritosService favoritosService) {
        this.favoritosService = favoritosService;
    }

    // =========================
    // Toggle favorito
    // =========================
    @PostMapping("/toggle")
    public ResponseEntity<Map<String, Object>> toggleFavorito(
            @RequestParam Long idUsuario,
            @RequestParam Integer idPokemon
    ) {

        boolean esFavorito = favoritosService.toggleFavorito(idUsuario, idPokemon);

        return ResponseEntity.ok(
                Map.of(
                        "idPokemon", idPokemon,
                        "favorito", esFavorito
                )
        );
    }

    // =========================
    // Obtener favoritos por usuario
    // =========================
    @GetMapping("/{idUsuario}")
    public ResponseEntity<List<Favoritos>> obtenerFavoritos(
            @PathVariable Long idUsuario
    ) {
        return ResponseEntity.ok(
                favoritosService.obtenerFavoritos(idUsuario)
        );
    }
}
