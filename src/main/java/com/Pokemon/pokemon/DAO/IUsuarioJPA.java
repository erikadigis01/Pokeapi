
package com.Pokemon.pokemon.DAO;

import com.Pokemon.pokemon.JPA.Result;
import com.Pokemon.pokemon.JPA.Usuario;


public interface IUsuarioJPA {
    
    Result GetAll();
    Result Add(Usuario usuario);
    Result GetById(int IdUsuario);
    Result Update(Usuario usuario);
    Result Delete(int IdUsuario);
    
}
