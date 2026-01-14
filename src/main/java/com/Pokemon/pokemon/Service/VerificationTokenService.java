package com.Pokemon.pokemon.Service;

import com.Pokemon.pokemon.JPA.Usuario;
import com.Pokemon.pokemon.Repository.UsuarioRepository;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VerificationTokenService {

    @Autowired
    private UsuarioRepository userRepository;

    @Autowired
    private EmailService emailService;

    public void createVerificationToken(Usuario usuario, String token) {
        usuario.setVerificationToken(token);
        usuario.setIsVerified(0);
        userRepository.save(usuario);
    }

    public boolean validateVerificationToken(String token) {

        Usuario usuario = userRepository
                .findByVerificationToken(token)
                .orElse(null);

        if (usuario == null) {
            return false;
        }

        usuario.setIsVerified(1);

        usuario.setVerificationToken(null);

        userRepository.save(usuario);

        return true;
    }

    public boolean resendVerification(String email) {

        Usuario usuario = userRepository
                .findByEmail(email)
                .orElse(null);

        if (usuario == null) {
            return false;
        }

        // Si ya está verificado, no reenviar
        if (usuario.getIsVerified() != null && usuario.getIsVerified() == 1) {
            return false;
        }

        String newToken = UUID.randomUUID().toString();

        usuario.setVerificationToken(newToken);
        userRepository.save(usuario);

        emailService.sendMail(usuario.getEmail(), newToken);

        return true;
    }

}
