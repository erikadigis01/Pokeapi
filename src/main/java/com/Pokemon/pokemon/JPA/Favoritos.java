package com.Pokemon.pokemon.JPA;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name  = "FAVORITOS")
public class Favoritos {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int IdFavorito;
    
    @Column(name = "idpokemon")
    private int IdPokemon;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idusuario", nullable = true)
    public Usuario Usuario;
    
    public Favoritos(){}
    public Favoritos(int IdFavorito, Usuario Usuario, int IdPokemon){
        this.IdFavorito = IdFavorito;
        this.IdPokemon = IdPokemon;
        this.Usuario = Usuario;
    }
    
    public void setIdFavorito(int IdFavorito){
        this.IdFavorito = IdFavorito;
    }
    public int getIdFavorito(){
        return IdFavorito;
    }
    
    public void setIdPokemon(int IdPokemon){
        this.IdPokemon = IdPokemon;
    }
    public int getIdPokemon(){
        return IdPokemon;
    }
    
    public void setUsuario(Usuario Usuario){
        this.Usuario = Usuario;
    }
    public Usuario getUsuario(){
        return Usuario;
    }
}
