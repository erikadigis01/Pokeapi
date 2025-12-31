package com.Pokemon.pokemon.DTO;

import java.util.List;

public class PokemonListResponse {

    private int count;
    private String next;
    private String previous;
    private List<PokemonResult> results;

    // Constructor vacío
    public PokemonListResponse() {
    }

    // Constructor con parámetros
    public PokemonListResponse(int count, String next, String previous, List<PokemonResult> results) {
        this.count = count;
        this.next = next;
        this.previous = previous;
        this.results = results;
    }

    // Getters y Setters
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public List<PokemonResult> getResults() {
        return results;
    }

    public void setResults(List<PokemonResult> results) {
        this.results = results;
    }

    // Clase interna para cada resultado
    public static class PokemonResult {

        private String name;
        private String url;

        public PokemonResult() {
        }

        public PokemonResult(String name, String url) {
            this.name = name;
            this.url = url;
        }

        // Getters y Setters
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
