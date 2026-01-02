// ====================================
// DATOS DE EJEMPLO - AQUÍ CONECTARÁS TU BASE DE DATOS
// ====================================
let pokemonDatabase = [
    {
        id: 1,
        name: "bulbasaur",
        type: "grass",
        image: "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/1.png",
        stats: { hp: 45, attack: 49, defense: 49, speed: 45 },
        height: 0.7,
        weight: 6.9,
        abilities: ["overgrow", "chlorophyll"],
        description: "A strange seed was planted on its back at birth. The plant sprouts and grows with this Pokémon."
    },
    {
        id: 4,
        name: "charmander",
        type: "fire",
        image: "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/4.png",
        stats: { hp: 39, attack: 52, defense: 43, speed: 65 },
        height: 0.6,
        weight: 8.5,
        abilities: ["blaze", "solar power"],
        description: "Obviously prefers hot places. When it rains, steam is said to spout from the tip of its tail."
    },
    {
        id: 7,
        name: "squirtle",
        type: "water",
        image: "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/7.png",
        stats: { hp: 44, attack: 48, defense: 65, speed: 43 },
        height: 0.5,
        weight: 9.0,
        abilities: ["torrent", "rain dish"],
        description: "After birth, its back swells and hardens into a shell. Powerfully sprays foam from its mouth."
    },
    {
        id: 25,
        name: "pikachu",
        type: "electric",
        image: "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/25.png",
        stats: { hp: 35, attack: 55, defense: 40, speed: 90 },
        height: 0.4,
        weight: 6.0,
        abilities: ["static", "lightning rod"],
        description: "When several of these Pokémon gather, their electricity could build and cause lightning storms."
    },
    {
        id: 6,
        name: "charizard",
        type: "fire",
        image: "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/6.png",
        stats: { hp: 78, attack: 84, defense: 78, speed: 100 },
        height: 1.7,
        weight: 90.5,
        abilities: ["blaze", "solar power"],
        description: "Spits fire that is hot enough to melt boulders. Known to cause forest fires unintentionally."
    },
    {
        id: 9,
        name: "blastoise",
        type: "water",
        image: "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/9.png",
        stats: { hp: 79, attack: 83, defense: 100, speed: 78 },
        height: 1.6,
        weight: 85.5,
        abilities: ["torrent", "rain dish"],
        description: "A brutal Pokémon with pressurized water jets on its shell. They are used for high speed tackles."
    },
    {
        id: 94,
        name: "gengar",
        type: "poison",
        image: "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/94.png",
        stats: { hp: 60, attack: 65, defense: 60, speed: 110 },
        height: 1.5,
        weight: 40.5,
        abilities: ["cursed body", "levitate"],
        description: "Under a full moon, this Pokémon likes to mimic the shadows of people and laugh at their fright."
    },
    {
        id: 143,
        name: "snorlax",
        type: "normal",
        image: "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/143.png",
        stats: { hp: 160, attack: 110, defense: 65, speed: 30 },
        height: 2.1,
        weight: 460.0,
        abilities: ["immunity", "thick fat"],
        description: "Very lazy. Just eats and sleeps. As its rotund bulk builds, it becomes steadily more slothful."
    },
    {
        id: 150,
        name: "mewtwo",
        type: "psychic",
        image: "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/150.png",
        stats: { hp: 106, attack: 110, defense: 90, speed: 130 },
        height: 2.0,
        weight: 122.0,
        abilities: ["pressure", "unnerve"],
        description: "It was created by a scientist after years of horrific gene splicing and DNA engineering experiments."
    },
    {
        id: 3,
        name: "venusaur",
        type: "grass",
        image: "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/3.png",
        stats: { hp: 80, attack: 82, defense: 83, speed: 80 },
        height: 2.0,
        weight: 100.0,
        abilities: ["overgrow", "chlorophyll"],
        description: "The plant blooms when it is absorbing solar energy. It stays on the move to seek sunlight."
    },
    {
        id: 74,
        name: "geodude",
        type: "rock",
        image: "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/74.png",
        stats: { hp: 40, attack: 80, defense: 100, speed: 20 },
        height: 0.4,
        weight: 20.0,
        abilities: ["rock head", "sturdy"],
        description: "Found in fields and mountains. Mistaking them for boulders, people often step or trip on them."
    },
    {
        id: 131,
        name: "lapras",
        type: "water",
        image: "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/131.png",
        stats: { hp: 130, attack: 85, defense: 80, speed: 60 },
        height: 2.5,
        weight: 220.0,
        abilities: ["water absorb", "shell armor"],
        description: "A Pokémon that has been overhunted almost to extinction. It can ferry people across the water."
    }
];

// ====================================
// VARIABLES GLOBALES
// ====================================
let currentFilter = 'all';
let currentSearch = '';
let filteredPokemon = [...pokemonDatabase];

// ====================================
// ELEMENTOS DOM
// ====================================
const searchInput = document.getElementById('searchInput');
const filterContainer = document.getElementById('filterContainer');
const pokemonGrid = document.getElementById('pokemonGrid');
const noResults = document.getElementById('noResults');
const pokemonCount = document.getElementById('pokemonCount');
const modalOverlay = document.getElementById('modalOverlay');
const modal = document.getElementById('modal');
const closeModal = document.getElementById('closeModal');
const modalContent = document.getElementById('modalContent');

// ====================================
// FUNCIONES PRINCIPALES
// ====================================

// Renderizar tarjetas de Pokémon
function renderPokemonCards() {
    pokemonGrid.innerHTML = '';
    
    if (filteredPokemon.length === 0) {
        noResults.style.display = 'block';
        pokemonGrid.style.display = 'none';
    } else {
        noResults.style.display = 'none';
        pokemonGrid.style.display = 'grid';
        
        filteredPokemon.forEach(pokemon => {
            const card = createPokemonCard(pokemon);
            pokemonGrid.appendChild(card);
        });
    }
    
    pokemonCount.textContent = filteredPokemon.length;
}

// Crear tarjeta individual de Pokémon
function createPokemonCard(pokemon) {
    const card = document.createElement('div');
    card.className = `pokemon-card ${pokemon.type}`;
    card.onclick = () => openModal(pokemon);
    
    card.innerHTML = `
        <div class="pokemon-badge">
            <span>#${String(pokemon.id).padStart(3, '0')}</span>
        </div>
        
        <div class="pokemon-image-container">
            <div class="pokemon-glow"></div>
            <img src="${pokemon.image}" alt="${pokemon.name}" class="pokemon-image">
        </div>
        
        <h3 class="pokemon-name">${pokemon.name}</h3>
        
        <div class="pokemon-type-container">
            <div class="pokemon-type-badge">
                ${getTypeIcon(pokemon.type)}
                <span>${pokemon.type}</span>
            </div>
        </div>
        
        <div class="pokemon-stats-mini">
            <div class="stat-mini">
                <div class="stat-mini-label">HP</div>
                <div class="stat-mini-value">${pokemon.stats.hp}</div>
            </div>
            <div class="stat-mini">
                <div class="stat-mini-label">ATK</div>
                <div class="stat-mini-value">${pokemon.stats.attack}</div>
            </div>
            <div class="stat-mini">
                <div class="stat-mini-label">DEF</div>
                <div class="stat-mini-value">${pokemon.stats.defense}</div>
            </div>
        </div>
    `;
    
    return card;
}

// Abrir modal con detalles del Pokémon
function openModal(pokemon) {
    modalContent.innerHTML = '';
    modal.className = `modal ${pokemon.type}`;
    
    const content = document.createElement('div');
    content.innerHTML = `
        <div class="modal-header">
            <div class="modal-title-section">
                <h2>${pokemon.name}</h2>
                <div class="modal-type-badges">
                    <div class="pokemon-type-badge">
                        ${getTypeIcon(pokemon.type)}
                        <span>${pokemon.type}</span>
                    </div>
                </div>
            </div>
            <div class="modal-number">
                <span>#${String(pokemon.id).padStart(3, '0')}</span>
            </div>
        </div>
        
        <div class="modal-grid">
            <div class="modal-left">
                <div class="modal-image-container">
                    <div class="modal-image-glow"></div>
                    <img src="${pokemon.image}" alt="${pokemon.name}" class="modal-image">
                </div>
                
                <div class="modal-physical-stats">
                    <div class="physical-stat">
                        <div class="physical-stat-label">Altura</div>
                        <div class="physical-stat-value">${pokemon.height}m</div>
                    </div>
                    <div class="physical-stat">
                        <div class="physical-stat-label">Peso</div>
                        <div class="physical-stat-value">${pokemon.weight}kg</div>
                    </div>
                </div>
            </div>
            
            <div class="modal-right">
                <h3 class="modal-stats-title">Estadísticas Base</h3>
                
                <div class="stat-row">
                    <div class="stat-header">
                        <div class="stat-label-container">
                            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                <path d="M20.84 4.61a5.5 5.5 0 0 0-7.78 0L12 5.67l-1.06-1.06a5.5 5.5 0 0 0-7.78 7.78l1.06 1.06L12 21.23l7.78-7.78 1.06-1.06a5.5 5.5 0 0 0 0-7.78z"></path>
                            </svg>
                            <span class="stat-label">HP</span>
                        </div>
                        <span class="stat-value">${pokemon.stats.hp}</span>
                    </div>
                    <div class="stat-bar-bg">
                        <div class="stat-bar-fill" style="width: ${(pokemon.stats.hp / 150) * 100}%"></div>
                    </div>
                </div>
                
                <div class="stat-row">
                    <div class="stat-header">
                        <div class="stat-label-container">
                            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                <polyline points="16 16 12 12 8 12"></polyline>
                                <line x1="12" y1="12" x2="12" y2="21"></line>
                                <path d="M20.39 18.39A5 5 0 0 0 18 9h-1.26A8 8 0 1 0 3 16.3"></path>
                                <polyline points="16 16 12 12 8 16"></polyline>
                            </svg>
                            <span class="stat-label">Ataque</span>
                        </div>
                        <span class="stat-value">${pokemon.stats.attack}</span>
                    </div>
                    <div class="stat-bar-bg">
                        <div class="stat-bar-fill" style="width: ${(pokemon.stats.attack / 150) * 100}%"></div>
                    </div>
                </div>
                
                <div class="stat-row">
                    <div class="stat-header">
                        <div class="stat-label-container">
                            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                <path d="M11 2H9a2 2 0 0 0-2 2v5c0 1.1.9 2 2 2h2a2 2 0 0 0 2-2V4a2 2 0 0 0-2-2zM7 4a2 2 0 0 1 2-2h2a2 2 0 0 1 2 2v5a2 2 0 0 1-2 2H9a2 2 0 0 1-2-2V4z"></path>
                                <path d="M15 2h2a2 2 0 0 1 2 2v5a2 2 0 0 1-2 2h-2a2 2 0 0 1-2-2V4a2 2 0 0 1 2-2zM13 4a2 2 0 0 0-2 2v5a2 2 0 0 0 2 2h2a2 2 0 0 0 2-2V6a2 2 0 0 0-2-2h-2z"></path>
                            </svg>
                            <span class="stat-label">Defensa</span>
                        </div>
                        <span class="stat-value">${pokemon.stats.defense}</span>
                    </div>
                    <div class="stat-bar-bg">
                        <div class="stat-bar-fill" style="width: ${(pokemon.stats.defense / 150) * 100}%"></div>
                    </div>
                </div>
                
                <div class="stat-row">
                    <div class="stat-header">
                        <div class="stat-label-container">
                            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                <path d="m12 14 4-4"></path>
                                <path d="M3.34 19a10 10 0 1 1 17.32 0"></path>
                            </svg>
                            <span class="stat-label">Velocidad</span>
                        </div>
                        <span class="stat-value">${pokemon.stats.speed}</span>
                    </div>
                    <div class="stat-bar-bg">
                        <div class="stat-bar-fill" style="width: ${(pokemon.stats.speed / 150) * 100}%"></div>
                    </div>
                </div>
                
                <div class="modal-abilities">
                    <h4>Habilidades</h4>
                    <div class="abilities-list">
                        ${pokemon.abilities.map(ability => `
                            <div class="ability-badge">
                                <span>${ability}</span>
                            </div>
                        `).join('')}
                    </div>
                </div>
            </div>
        </div>
        
        <div class="modal-description">
            <p>${pokemon.description}</p>
        </div>
    `;
    
    modalContent.appendChild(content);
    modalOverlay.classList.add('active');
}

// Cerrar modal
function closeModalFunc() {
    modalOverlay.classList.remove('active');
}

// Obtener icono de tipo
function getTypeIcon(type) {
    const icons = {
        fire: '<svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M8.5 14.5A2.5 2.5 0 0 0 11 12c0-1.38-.5-2-1-3-1.072-2.143-.224-4.054 2-6 .5 2.5 2 4.9 4 6.5 2 1.6 3 3.5 3 5.5a7 7 0 1 1-14 0c0-1.153.433-2.294 1-3a2.5 2.5 0 0 0 2.5 2.5z"/></svg>',
        water: '<svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M12 2.69l5.66 5.66a8 8 0 1 1-11.31 0z"/></svg>',
        grass: '<svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M11 20A7 7 0 0 1 9.8 6.1C15.5 5 17 4.48 19 2c1 2 2 4.18 2 8 0 5.5-4.78 10-10 10Z"/><path d="M2 21c0-3 1.85-5.36 5.08-6C9.5 14.52 12 13 13 12"/></svg>',
        electric: '<svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><polygon points="13 2 3 14 12 14 11 22 21 10 12 10 13 2"/></svg>',
        psychic: '<svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M12 2L15.09 8.26L22 9.27L17 14.14L18.18 21.02L12 17.77L5.82 21.02L7 14.14L2 9.27L8.91 8.26L12 2Z"/></svg>',
        rock: '<svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="m8 3 4 8 5-5 5 15H2L8 3z"/></svg>',
    };
    return icons[type] || '';
}

// Filtrar Pokémon
function filterPokemon() {
    filteredPokemon = pokemonDatabase.filter(pokemon => {
        const matchesSearch = pokemon.name.toLowerCase().includes(currentSearch.toLowerCase());
        const matchesType = currentFilter === 'all' || pokemon.type === currentFilter;
        return matchesSearch && matchesType;
    });
    renderPokemonCards();
}

// ====================================
// EVENT LISTENERS
// ====================================

// Búsqueda
searchInput.addEventListener('input', (e) => {
    currentSearch = e.target.value;
    filterPokemon();
});

// Filtros de tipo
filterContainer.addEventListener('click', (e) => {
    if (e.target.classList.contains('filter-btn')) {
        // Remover active de todos los botones
        document.querySelectorAll('.filter-btn').forEach(btn => {
            btn.classList.remove('active');
        });
        
        // Agregar active al botón clickeado
        e.target.classList.add('active');
        
        // Actualizar filtro
        currentFilter = e.target.dataset.type;
        filterPokemon();
    }
});

// Cerrar modal
closeModal.addEventListener('click', closeModalFunc);
modalOverlay.addEventListener('click', (e) => {
    if (e.target === modalOverlay) {
        closeModalFunc();
    }
});

// Cerrar modal con tecla ESC
document.addEventListener('keydown', (e) => {
    if (e.key === 'Escape' && modalOverlay.classList.contains('active')) {
        closeModalFunc();
    }
});

// ====================================
// INICIALIZACIÓN
// ====================================
renderPokemonCards();

// ====================================
// FUNCIÓN PARA CARGAR DATOS DESDE BASE DE DATOS
// ====================================

/*
// EJEMPLO DE CÓMO CARGAR DATOS DESDE UNA API O BASE DE DATOS
async function loadPokemonFromDatabase() {
    try {
        // Reemplaza esta URL con tu endpoint de API
        const response = await fetch('TU_URL_DE_API_AQUI');
        const data = await response.json();
        
        // Asegúrate de que los datos tengan el formato correcto
        pokemonDatabase = data;
        filteredPokemon = [...pokemonDatabase];
        renderPokemonCards();
    } catch (error) {
        console.error('Error al cargar datos:', error);
    }
}

// Descomentar para usar
// loadPokemonFromDatabase();
*/

/*
// EJEMPLO DE CÓMO AGREGAR UN NUEVO POKÉMON
function addPokemon(newPokemon) {
    pokemonDatabase.push(newPokemon);
    filterPokemon();
}

// Ejemplo de uso:
// addPokemon({
//     id: 13,
//     name: "nuevo pokemon",
//     type: "fire",
//     image: "url_de_imagen",
//     stats: { hp: 50, attack: 60, defense: 55, speed: 70 },
//     height: 1.2,
//     weight: 15.5,
//     abilities: ["habilidad1", "habilidad2"],
//     description: "Descripción del Pokémon"
// });
*/
