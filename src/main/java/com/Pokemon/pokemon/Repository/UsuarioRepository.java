
package com.Pokemon.pokemon.Repository;

import com.Pokemon.pokemon.JPA.Usuario;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
    
    Usuario findByEmail(String email);
    
    boolean existsByEmail(String email);
    
    @Query("SELECT u FROM Usuario u WHERE u.nombre = :campo OR u.email = :campo")
    Optional<Usuario> findByIdentifier(@Param("campo") String campo);
    
    @Query("SELECT u FROM Usuario u WHERE u.id = :id")
    Optional<Usuario> findById(@Param("id") Long id);
    
//    @Query("SELECT u FROM UsuarioJPA u WHERE u.Email = :email")
//    Optional<Usuario> findByEmail(@Param("email") String email);

//    @Query("SELECT u FROM Usuario u WHERE u.userName = :username")
//    Optional<Usuario> findByUsername(@Param("username") String username);

    @Query("SELECT u FROM Usuario u WHERE u.VerificationToken = :token")
    Optional<Usuario> findByVerificationToken(@Param("token") String token);
     
}
