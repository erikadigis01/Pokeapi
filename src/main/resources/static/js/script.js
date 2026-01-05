// ====================================
// VARIABLES GLOBALES
// ====================================
let pokemonDatabase = [];
let currentFilter = 'all';
let currentSearch = '';
let filteredPokemon = [];

// ====================================
// HELPERS (ADAPTADOS A TU JSON)
// ====================================

function getMainType(pokemon) {
    return pokemon.types?.[0]?.type?.name || 'unknown';
}

function getImage(pokemon) {
    return pokemon.sprites?.front_default || '';
}

function getStat(pokemon, statName) {
    const stat = pokemon.stats?.find(s => s.stat.name === statName);
    return stat ? stat.base_stat : 0;
}

function getAbilities(pokemon) {
    return pokemon.abilities?.map(a => a.ability.name) || [];
}

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
// RENDER LISTA
// ====================================
function renderPokemonCards() {
    pokemonGrid.innerHTML = '';

    if (filteredPokemon.length === 0) {
        noResults.style.display = 'block';
        pokemonGrid.style.display = 'none';
    } else {
        noResults.style.display = 'none';
        pokemonGrid.style.display = 'grid';

        filteredPokemon.forEach(pokemon => {
            pokemonGrid.appendChild(createPokemonCard(pokemon));
        });
    }

    pokemonCount.textContent = filteredPokemon.length;
}

// ====================================
// TARJETA
// ====================================
function createPokemonCard(pokemon) {
    const types = getPokemonTypes(pokemon);

    const card = document.createElement('div');
    card.classList.add('pokemon-card');

    // aplicar todas las clases de tipo
    types.forEach(t => card.classList.add(t.toLowerCase()));

    card.onclick = () => openModal(pokemon);

    const typesHtml = types.map(type => `
        <div class="pokemon-type-badge ${type}">
            ${getTypeIcon(type)}
            <span>${type}</span>
        </div>
    `).join('');

    card.innerHTML = `
        <div class="pokemon-badge">
            <span>#${String(pokemon.id).padStart(3, '0')}</span>
        </div>

        <div class="pokemon-image-container">
            <div class="pokemon-glow"></div>
            <img src="${getImage(pokemon)}" alt="${pokemon.name}" class="pokemon-image">
        </div>

        <h3 class="pokemon-name">${pokemon.name}</h3>

        <div class="pokemon-type-container">
            ${typesHtml}
        </div>

        <div class="pokemon-stats-mini">
            <div class="stat-mini">
                <div class="stat-mini-label">HP</div>
                <div class="stat-mini-value">${getStat(pokemon, 'hp')}</div>
            </div>
            <div class="stat-mini">
                <div class="stat-mini-label">ATK</div>
                <div class="stat-mini-value">${getStat(pokemon, 'attack')}</div>
            </div>
            <div class="stat-mini">
                <div class="stat-mini-label">DEF</div>
                <div class="stat-mini-value">${getStat(pokemon, 'defense')}</div>
            </div>
        </div>
    `;

    return card;
}


// ====================================
// MODAL
// ====================================
function openModal(pokemon) {
    const types = getPokemonTypes(pokemon);

    modalContent.innerHTML = '';
    modal.className = 'modal';

    // usar el primer tipo como base visual
    modal.classList.add(types[0].toLowerCase());

    const typesBadges = types.map(type => `
        <div class="pokemon-type-badge ${type}">
            ${getTypeIcon(type)}
            <span>${type}</span>
        </div>
    `).join('');

    const content = document.createElement('div');
    content.innerHTML = `
        <div class="modal-header">
            <div class="modal-title-section">
                <h2>${pokemon.name}</h2>
                <div class="modal-type-badges">
                    ${typesBadges}
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
                    <img src="${getImage(pokemon)}" alt="${pokemon.name}" class="modal-image">
                </div>

                <div class="modal-physical-stats">
                    <div class="physical-stat">
                        <div class="physical-stat-label">Altura</div>
                        <div class="physical-stat-value">${pokemon.height}</div>
                    </div>
                    <div class="physical-stat">
                        <div class="physical-stat-label">Peso</div>
                        <div class="physical-stat-value">${pokemon.weight}</div>
                    </div>
                </div>
            </div>

            <div class="modal-right">
                <h3 class="modal-stats-title">Estadísticas Base</h3>

                ${renderStatRow(pokemon, 'hp', 'HP')}
                ${renderStatRow(pokemon, 'attack', 'Ataque')}
                ${renderStatRow(pokemon, 'defense', 'Defensa')}
                ${renderStatRow(pokemon, 'speed', 'Velocidad')}

                <div class="modal-abilities">
                    <h4>Habilidades</h4>
                    <div class="abilities-list">
                        ${pokemon.abilities.map(a => `
                            <div class="ability-badge">
                                <span>${a.ability.name}</span>
                            </div>
                        `).join('')}
                    </div>
                </div>
            </div>
        </div>
    `;

    modalContent.appendChild(content);
    modalOverlay.classList.add('active');
}


function renderStatRow(pokemon, statKey, label) {
    const value = getStat(pokemon, statKey);
    const percent = (value / 150) * 100;

    return `
        <div class="stat-row">
            <div class="stat-header">
                <span class="stat-label">${label}</span>
                <span class="stat-value">${value}</span>
            </div>
            <div class="stat-bar-bg">
                <div class="stat-bar-fill" style="width:${percent}%"></div>
            </div>
        </div>
    `;
}

function getPokemonTypes(pokemon) {
    if (!pokemon.types || !pokemon.types.length) {
        return ['normal'];
    }

    return pokemon.types
            .map(t => t?.type?.name)
            .filter(Boolean);
}


// ====================================
// CERRAR MODAL
// ====================================
function closeModalFunc() {
    modalOverlay.classList.remove('active');
}

closeModal.addEventListener('click', closeModalFunc);
modalOverlay.addEventListener('click', e => {
    if (e.target === modalOverlay)
        closeModalFunc();
});

// ====================================
// FILTRO POKEMONES TIPO 
// ====================================
function filterPokemon() {
    filteredPokemon = pokemonDatabase.filter(pokemon => {
        const matchesSearch = pokemon.name
                .toLowerCase()
                .includes(currentSearch.toLowerCase());

        const types = getPokemonTypes(pokemon);

        const matchesType =
                currentFilter === 'all' || types.includes(currentFilter);

        return matchesSearch && matchesType;
    });

    renderPokemonCards();
}


// ====================================
// EVENTOS
// ====================================
searchInput.addEventListener('input', e => {
    currentSearch = e.target.value;
    filterPokemon();
});

filterContainer.addEventListener('click', e => {
    if (e.target.classList.contains('filter-btn')) {
        document.querySelectorAll('.filter-btn').forEach(btn => btn.classList.remove('active'));
        e.target.classList.add('active');
        currentFilter = e.target.dataset.type;
        filterPokemon();
    }
});

// ====================================
// CARGA DE DATOS
// ====================================

async function loadPokemonFromDatabase() {
    try {
        const response = await fetch("/pokemon/pokemons"); // sin Authorization
        if (!response.ok) {
            throw new Error("Error en la petición: " + response.status);
        }

        const data = await response.json();
        console.log("DATA BACKEND:", data);

        pokemonDatabase = data;
        filteredPokemon = [...pokemonDatabase];
        renderPokemonCards();
    } catch (error) {
        console.error("Error al cargar datos:", error);
    }
}

loadPokemonFromDatabase();

// ====================================
// ICONOS DE TIPO
// ====================================
function getTypeIcon(type) {
    const icons = {
        fire: '<svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M8.5 14.5A2.5 2.5 0 0 0 11 12c0-1.38-.5-2-1-3-1.072-2.143-.224-4.054 2-6 .5 2.5 2 4.9 4 6.5 2 1.6 3 3.5 3 5.5a7 7 0 1 1-14 0c0-1.153.433-2.294 1-3a2.5 2.5 0 0 0 2.5 2.5z"/></svg>',
        water: '<svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M12 2.69l5.66 5.66a8 8 0 1 1-11.31 0z"/></svg>',
        grass: '<svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M11 20A7 7 0 0 1 9.8 6.1C15.5 5 17 4.48 19 2c1 2 2 4.18 2 8 0 5.5-4.78 10-10 10Z"/><path d="M2 21c0-3 1.85-5.36 5.08-6C9.5 14.52 12 13 13 12"/></svg>',
        electric: '<svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><polygon points="13 2 3 14 12 14 11 22 21 10 12 10 13 2"/></svg>',
        psychic: '<svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M12 2L15.09 8.26L22 9.27L17 14.14L18.18 21.02L12 17.77L5.82 21.02L7 14.14L2 9.27L8.91 8.26L12 2Z"/></svg>',
        rock: '<svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="m8 3 4 8 5-5 5 15H2L8 3z"/></svg>'
    };

    return icons[type] || '';
}
