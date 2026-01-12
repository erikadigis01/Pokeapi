package com.Pokemon.pokemon.RestController;

import com.Pokemon.pokemon.JPA.Favoritos;
import com.Pokemon.pokemon.Service.FavoritosService;
import com.Pokemon.pokemon.Service.JwtService;
import com.Pokemon.pokemon.Service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/favoritos")
@CrossOrigin(origins = "*") // luego lo ajustas si usas auth real
public class FavoritosRestController {

    @Autowired
    JwtService jwtUtil;

    @Autowired
    UsuarioService usuarioService;

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
            HttpSession session,
            @RequestParam Integer idPokemon
    ) {
        String token = (String) session.getAttribute("token");

        if (token == null) {
            return ResponseEntity.status(401).build();
        }

        String email = jwtUtil.extraerUsername(token);
        Long idUsuario = usuarioService.getUserIdByEmail(email);

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
    @GetMapping
    public ResponseEntity<List<Favoritos>> obtenerFavoritos(HttpSession session) {

        String token = (String) session.getAttribute("token");
        if (token == null) {
            return ResponseEntity.status(401).build();
        }

        String email = jwtUtil.extraerUsername(token);
        Long idUsuario = usuarioService.getUserIdByEmail(email);

        return ResponseEntity.ok(
                favoritosService.obtenerFavoritos(idUsuario)
        );
    }

}
