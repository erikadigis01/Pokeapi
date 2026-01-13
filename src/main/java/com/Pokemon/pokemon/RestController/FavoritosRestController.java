package com.Pokemon.pokemon.RestController;

import com.Pokemon.pokemon.JPA.Favoritos;
import com.Pokemon.pokemon.Service.FavoritosService;
import com.Pokemon.pokemon.Service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

@RestController
@RequestMapping("/favoritos")
@CrossOrigin(origins = "*")
public class FavoritosRestController {

    private final FavoritosService favoritosService;
    private final UsuarioService usuarioService;

    public FavoritosRestController(FavoritosService favoritosService, UsuarioService usuarioService) {
        this.favoritosService = favoritosService;
        this.usuarioService = usuarioService;
    }

    @PostMapping("/toggle")
    public ResponseEntity<Map<String, Object>> toggleFavorito(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam Integer idPokemon
    ) {
        if (userDetails == null) {
            return ResponseEntity.status(401).build();
        }

        Long idUsuario = usuarioService.getUserIdByEmail(userDetails.getUsername());
        boolean esFavorito = favoritosService.toggleFavorito(idUsuario, idPokemon);

        return ResponseEntity.ok(
                Map.of(
                        "idPokemon", idPokemon,
                        "favorito", esFavorito
                )
        );
    }

    @GetMapping
    public ResponseEntity<List<Favoritos>> obtenerFavoritos(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(401).build();
        }

        Long idUsuario = usuarioService.getUserIdByEmail(userDetails.getUsername());
        return ResponseEntity.ok(favoritosService.obtenerFavoritos(idUsuario));
    }
}
