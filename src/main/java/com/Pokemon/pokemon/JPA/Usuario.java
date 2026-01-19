package com.Pokemon.pokemon.JPA;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "USUARIO")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
//    VALIDACIONES 
    @NotNull(message = "El campo no puede ser nulo")
    @NotBlank(message = "El campo debe contener datos")
    @Size(min = 2, max = 17, message = "Limite de letras excedido, entre 2 y 20")
    @Pattern(regexp = "^[a-zA-Z]+(?: [a-zA-Z]+)?$", 
            message = "El nombre solo debe contener letras")
    @Column(name = "nombre")
    private String nombre;

    @NotNull(message = "El campo no puede ser nulo")
    @NotBlank(message = "El campo debe contener datos")
    @Size(min = 2, max = 20, message = "Limite de letras excedido, entre 2 y 20")
    @Pattern(regexp = "^[a-zA-ZñÑ]+?$", message = "Solo se permiten letras")
    @Column(name = "apellidopaterno")
    private String apellidoPaterno;
    
    @NotNull(message = "El campo no puede ser nulo")
    @NotBlank(message = "El campo debe contener datos")
    @Size(min = 2, max = 20, message = "Limite de letras excedido, entre 2 y 20")
    @Pattern(regexp = "^[a-zA-ZñÑ]+?$", message = "Solo se permiten letras")
    @Column(name = "apellidomaterno")
    private String apellidoMaterno;

    @NotNull(message = "El campo no puede ser nulo")
    @NotBlank(message = "El campo debe contener datos")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", 
            message = "Direccion de correo invalida")
    @Column(name = "email")
    private String email;
    
    @NotNull(message = "El campo no puede ser nulo")
    @NotBlank(message = "El campo debe contener datos")
    @Pattern(
    regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,20}$",
    message = "El password debe ser de 8 a 20 caracteres, una minúscula, "
            + "una mayúscula, un número y un caracter especial",  groups = OnRegistro.class )
    @Column(name = "password")
    private String password;

    
    @Lob
    @Column(name = "imagen")
    private String Imagen;

    @Column(name = "isverified")
    private Integer IsVerified;

    @Column(name = "verificationtoken")
    private String VerificationToken;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idroll", nullable = false)
    public Roll roll;

    @OneToMany(
            mappedBy = "usuario",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Favoritos> favoritos = new ArrayList<>();

    public Usuario() {}

    // ===== Getters & Setters =====

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImagen() {
        return Imagen;
    }

    public void setImagen(String Imagen) {
        this.Imagen = Imagen;
    }

    public Integer getIsVerified() {
        return IsVerified;
    }

    public void setIsVerified(Integer IsVerified) {
        this.IsVerified = IsVerified;
    }

    public String getVerificationToken() {
        return VerificationToken;
    }

    public void setVerificationToken(String VerificationToken) {
        this.VerificationToken = VerificationToken;
    }
    
    

    public Roll getRoll() {
        return roll;
    }

    public void setRoll(Roll roll) {
        this.roll = roll;
    }

    public List<Favoritos> getFavoritos() {
        return favoritos;
    }

    public void setFavoritos(List<Favoritos> favoritos) {
        this.favoritos = favoritos;
    }

    public Usuario orElse(Object object) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
