package com.Pokemon.pokemon.Service;

import com.Pokemon.pokemon.JPA.Favoritos;
import com.Pokemon.pokemon.JPA.Usuario;
import com.Pokemon.pokemon.Repository.FavoritosRepository;
import com.Pokemon.pokemon.Repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoritosService {

    private final FavoritosRepository favoritosRepository;
    private final UsuarioRepository usuarioRepository;

    // Constructor injection (recomendado)
    public FavoritosService(FavoritosRepository favoritosRepository,
            UsuarioRepository usuarioRepository) {
        this.favoritosRepository = favoritosRepository;
        this.usuarioRepository = usuarioRepository;
    }

    // =========================
    // Toggle favorito
    // =========================
    public boolean toggleFavorito(Long id, Integer idPokemon) {

        Favoritos existente = favoritosRepository
                .findByUsuarioIdAndIdPokemon(id, idPokemon)
                .orElse(null);

        if (existente != null) {
            favoritosRepository.delete(existente);
            return false; // eliminado de favoritos
        }

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Favoritos favorito = new Favoritos();
        favorito.setUsuario(usuario);
        favorito.setIdPokemon(idPokemon);

        favoritosRepository.save(favorito);
        return true; // agregado a favoritos
    }

    // =========================
    // Obtener favoritos
    // =========================
    public List<Favoritos> obtenerFavoritos(Long id) {
        return favoritosRepository.findByUsuarioId(id);
    }
}
