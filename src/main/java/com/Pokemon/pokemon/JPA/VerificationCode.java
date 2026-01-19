package com.Pokemon.pokemon.JPA;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "VERIFICATIONCODE")
public class VerificationCode {
    
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idcode")
    private Long idCode;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idusuario", nullable = false)
    public Usuario usuario;
    
    @Column(name = "code")
    private String code;
    
    @Column(name = "usos")
    private int usos;
    
    public VerificationCode(){}
    
    public VerificationCode(Long idCode, Usuario usuario, String code, int usos){
        this.idCode = idCode;
        this.usuario = usuario;
        this.code = code;
        this.usos = usos;
    
    }
    public void setIdCode(Long idCode) {
        this.idCode = idCode;
    }
    
    public Long getIdCode(){
        return idCode;
    }
    
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    public Usuario getUsuario(){
        return usuario;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public String getCode(){
        return code;
    }
    public void setUsos(int usos) {
        this.usos = usos;
    }
    
    public int getUsos(){
        return usos;
    }
}
