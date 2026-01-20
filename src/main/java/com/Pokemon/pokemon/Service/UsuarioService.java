package com.Pokemon.pokemon.Service;

import com.Pokemon.pokemon.JPA.Result;
import com.Pokemon.pokemon.JPA.Usuario;
import com.Pokemon.pokemon.Repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EmailService emailService;

    @Transactional
    public List<Usuario> getAll() {
        return usuarioRepository.findAll();
    }

    @Transactional
    public Result add(Usuario usuario) {
        Result result = new Result();
        try {
            usuarioRepository.save(usuario);
            String token = UUID.randomUUID().toString();
            usuario.setVerificationToken(token);
            emailService.sendMail(usuario.getEmail(), token);
            result.correct = true;

        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
            result.status = 500;

        }
        return result;
    }

    @Transactional
    public Usuario update(Long id, Usuario usuarioActualizado) {
        Usuario findUsuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return usuarioRepository.save(usuarioActualizado);
    }

    @Transactional
    public Result UpdateImagen(Long id, String base64) {
        Result result = new Result();

        try {
            Usuario usuarioBD = usuarioRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            usuarioBD.setImagen(base64);
            result.correct = true;
            result.status = 202;

        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
            result.status = 500;
        }

        return result;
    }

    @Transactional
    public Usuario getById(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    @Transactional
    public void delete(Long id) {
        usuarioRepository.deleteById(id);
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email);

        if (usuario == null) {
            throw new UsernameNotFoundException("Usuario no encontrado: " + email);
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        if (usuario.getRoll() != null) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + usuario.getRoll().getNombre()));
        }

        return new User(
                usuario.getEmail(),
                usuario.getPassword(),
                true, true, true, true,
                authorities
        );
    }

    public Long getUserIdByEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email);
        return usuario != null ? usuario.getId() : null;
    }
}
