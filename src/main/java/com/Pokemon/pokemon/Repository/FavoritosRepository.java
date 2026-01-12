package com.Pokemon.pokemon.Repository;

import com.Pokemon.pokemon.JPA.Favoritos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoritosRepository extends JpaRepository<Favoritos, Integer> {

    Optional<Favoritos> findByUsuarioIdAndIdPokemon(
            Long id,
            Integer idPokemon
    );

    List<Favoritos> findByUsuarioId(Long id);
}
