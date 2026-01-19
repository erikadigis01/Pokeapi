
package com.Pokemon.pokemon.JPA;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;

public class Result <T>{
    
    public boolean correct;
    public String errorMessage;
    public T object;
    public List<T> objects;
    @JsonIgnore
    public Exception ex;
    
    @JsonIgnore
    public int status;
    
}
