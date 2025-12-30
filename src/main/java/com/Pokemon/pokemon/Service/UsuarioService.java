package com.Pokemon.pokemon.Service;

import com.Pokemon.pokemon.DAO.IUsuarioJPA;
import com.Pokemon.pokemon.DAO.UsuarioJPADAOImplementation;
import com.Pokemon.pokemon.JPA.Result;
import com.Pokemon.pokemon.JPA.Usuario;
import com.Pokemon.pokemon.Repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional
    public List<Usuario> getAll() {
        return usuarioRepository.findAll();
    }

    @Transactional
    public Usuario add(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    @Transactional
    public Usuario getById(Integer id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    @Transactional
    public void delete(Integer id) {
        usuarioRepository.deleteById(id);
    }
}
