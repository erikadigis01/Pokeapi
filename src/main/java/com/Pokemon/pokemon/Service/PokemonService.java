package com.Pokemon.pokemon.Service;

import com.Pokemon.pokemon.DTO.PageResponse;
import com.Pokemon.pokemon.DTO.PokemonCardDTO;
import com.Pokemon.pokemon.DTO.PokemonDTO;
import com.Pokemon.pokemon.DTO.PokemonListResponse;
import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PokemonService {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String BASE_URL = "https://pokeapi.co/api/v2/pokemon";

    // 🔹 Lista completa en memoria (ligera)
    private final List<PokemonCardDTO> allPokemons = new ArrayList<>();

    // 🔹 Se ejecuta UNA SOLA VEZ al arrancar la app
    @PostConstruct
    public void loadAllPokemons() {

        String url = BASE_URL + "?limit=2000&offset=0";
        PokemonListResponse response =
                restTemplate.getForObject(url, PokemonListResponse.class);

        for (var result : response.getResults()) {
            PokemonDTO dto =
                    restTemplate.getForObject(result.getUrl(), PokemonDTO.class);

            allPokemons.add(mapToCard(dto));
        }

        System.out.println("Pokémon cargados: " + allPokemons.size());
    }

    // 🔹 Mapper: DTO pesado → DTO ligero (cartas)
    private PokemonCardDTO mapToCard(PokemonDTO dto) {
        PokemonCardDTO card = new PokemonCardDTO();
        card.setId(dto.getId());
        card.setName(dto.getName());
        card.setImage(dto.getSprites().getFrontDefault());
        card.setTypes(
                dto.getTypes().stream()
                        .map(t -> t.getType().getName())
                        .toList()
        );
        return card;
    }

    // 🔹 FILTRO GLOBAL (ANTES de paginar)
    private List<PokemonCardDTO> filterPokemons(
            String name,
            String type,
            Integer number
    ) {
        return allPokemons.stream()
                .filter(p -> name == null ||
                        p.getName().toLowerCase().contains(name.toLowerCase()))
                .filter(p -> type == null ||
                        p.getTypes().contains(type.toLowerCase()))
                .filter(p -> number == null ||
                        p.getId() == number)
                .toList();
    }

    // 🔹 PAGINACIÓN CORRECTA
    private PageResponse<PokemonCardDTO> paginate(
            List<PokemonCardDTO> list,
            int page,
            int size
    ) {
        int start = page * size;
        int end = Math.min(start + size, list.size());

        List<PokemonCardDTO> content =
                start > list.size() ? List.of() : list.subList(start, end);

        return new PageResponse<>(
                content,
                page,
                size,
                list.size(),
                (int) Math.ceil((double) list.size() / size)
        );
    }

    // 🔹 MÉTODO PRINCIPAL PARA LA LISTA (cartas)
    public PageResponse<PokemonCardDTO> getPokemons(
            int page,
            int size,
            String name,
            String type,
            Integer number
    ) {
        List<PokemonCardDTO> filtered =
                filterPokemons(name, type, number);

        return paginate(filtered, page, size);
    }

    // 🔹 Para el MODAL (detalle)
    public PokemonDTO getPokemonByName(String name) {
        String url = BASE_URL + "/" + name;
        return restTemplate.getForObject(url, PokemonDTO.class);
    }

    // 🔹 Tipos disponibles (para filtros)
    public Set<String> getAllTypes() {
        return allPokemons.stream()
                .flatMap(p -> p.getTypes().stream())
                .collect(Collectors.toSet());
    }
}
