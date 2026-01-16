package com.Pokemon.pokemon.RestController;


import com.Pokemon.pokemon.DTO.LoginRequest;
import com.Pokemon.pokemon.JPA.Result;
import com.Pokemon.pokemon.JPA.Usuario;
import com.Pokemon.pokemon.JPA.VerificationCode;
import com.Pokemon.pokemon.Service.EmailService;
import com.Pokemon.pokemon.Service.JwtService;
import com.Pokemon.pokemon.Service.UsuarioService;
import com.Pokemon.pokemon.Service.VerificationCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import org.springframework.security.crypto.password.PasswordEncoder;

@RestController
@RequestMapping("/auth") // Ruta para la API
public class AuthRestController {

    @Autowired
    private AuthenticationManager authManager;

    @Autowired 
    private JwtService jwtUtil;

    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    PasswordEncoder passwordEncoder;
    
    @Autowired
    private VerificationCodeService verificationCodeService;
    
    @Autowired
    private EmailService emailService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            authManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );

            final UserDetails userDetails = usuarioService.loadUserByUsername(loginRequest.getEmail());
            final String token = jwtUtil.creatToken(userDetails);

            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            response.put("status", "success");
            return ResponseEntity.ok(response);

        } catch (BadCredentialsException e) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Email o contraseña incorrectos");
            return ResponseEntity.ok(response);
        }
    }
    
    @PostMapping ("/addUsuario")
    public ResponseEntity Add(@RequestBody  Usuario usuario) {
        
         Result result = new Result();
         
         try {
            //CIFRAR CONTRASE;A
            String password = passwordEncoder.encode(usuario.getPassword());
            usuario.setPassword(password);
            //CREAR USUARIO
            Result newUsuario = usuarioService.add(usuario);
            result.object = newUsuario;
            result.correct = true;
            result.errorMessage = "Se agrego un nuevo usuario correctamente";
            result.status = 201;
         
         
         } catch (Exception ex) {
         
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
            result.status = 500;
         
         }
         
    
         return ResponseEntity.status(result.status).body(result);
      
    
    }
    
    @PostMapping("/resetPassword")
    public ResponseEntity resetPassword(@RequestBody String email) {
    
        Result result = new Result();
        
        try {
            
            //se busca al usuario con ese email
            Long idUser = usuarioService.getUserIdByEmail(email);
            //se crea una clase verification code con el idusuario
            VerificationCode code = new VerificationCode();
            code.usuario = new Usuario();
            code.usuario.setId(idUser);
            code.setCode(verificationCodeService.generarCodigoSeguro());
            //se guarda
            verificationCodeService.add(code);
            //se manda un correo con ese codigo a esa direccion
            emailService.sendPasswordReset(email, code.getCode());
            result.correct = true;
            result.errorMessage = "Se mando el codigo al correo";
            result.status = 200;
            
        } catch(Exception ex) {
        
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
            result.status = 500;
        }
        
        return ResponseEntity.status(result.status).body(result);
    }
    
    @PostMapping("/resetPassword/newPassword")
    public ResponseEntity addNewPassword(@RequestBody Map<String, String> data) {
    
        Result result = new Result();
        
        try {
            String token = data.get("token");//agreagar un numero de usos al token
            String password = data.get("password");
            
            if(verificationCodeService.verificarCode(token)) {
                
                //encontrar al usuario con ese token
                VerificationCode verificationCode = verificationCodeService.getByCode(token);
                Usuario usuario = usuarioService.getById(verificationCode.usuario.getId());
                //actualizar el password encriptado
                usuario.setPassword(passwordEncoder.encode(password));
                //actualizar el usuario
                usuarioService.update(usuario.getId(), usuario);
                result.correct = true;
                result.errorMessage = "Se actualizo la contraseña";
                result.status = 200;
            } else {
            
                result.correct = false;
                result.errorMessage = "Token no valido";
                result.status = 500;
            }
            
        } catch(Exception ex) {
        
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
            result.status = 500;
        }
        
        return ResponseEntity.status(result.status).body(result);
    }

}
