
package com.Pokemon.pokemon.Repository;

import com.Pokemon.pokemon.JPA.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UsuarioRepository extends JpaRepository<Usuario, Integer>{
    
}
