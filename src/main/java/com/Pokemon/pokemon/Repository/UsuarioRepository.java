
package com.Pokemon.pokemon.Repository;

import com.Pokemon.pokemon.JPA.Usuario;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer>{
    Usuario findByEmail(String email);
    
    boolean existsByEmail(String email);
    
    @Query("SELECT u FROM Usuario u WHERE u.nombre = :campo OR u.email = :campo")
    Optional<Usuario> findByIdentifier(@Param("campo") String campo);
    
    
}
