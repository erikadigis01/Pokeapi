// Type colors (darker for better contrast with white text)
const typeColors = {
    normal: '#8A8A59',
    fire: '#C0601A',
    water: '#4A70C0',
    electric: '#D4B020',
    grass: '#5FA844',
    ice: '#6FB5B5',
    fighting: '#8E2420',
    poison: '#7A2F7A',
    ground: '#B89E4C',
    flying: '#7C6AC8',
    psychic: '#D2456F',
    bug: '#7F8F1E',
    rock: '#8E7A2E',
    ghost: '#5A456F',
    dragon: '#5528C8',
    dark: '#4F3E3E',
    steel: '#8E8EA8',
    fairy: '#C97C92'
};


// State
let allPokemons = [];
let filteredPokemons = [];
let currentPage = 1;
let totalPages = 0;
const pokemonsPerPage = 20;

// DOM Elements
const searchInput = document.getElementById('searchInput');
const clearSearchBtn = document.getElementById('clearSearch');
const searchResults = document.getElementById('searchResults');
const loading = document.getElementById('loading');
const noResults = document.getElementById('noResults');
const pokemonGrid = document.getElementById('pokemonGrid');
const pagination = document.getElementById('pagination');
const prevBtn = document.getElementById('prevBtn');
const nextBtn = document.getElementById('nextBtn');
const pageInfo = document.getElementById('pageInfo');
const modal = document.getElementById('modal');
const modalBody = document.getElementById('modalBody');

// Initialize
fetchPokemons(currentPage);

// Event Listeners
searchInput.addEventListener('input', handleSearch);
clearSearchBtn.addEventListener('click', clearSearch);
prevBtn.addEventListener('click', () => changePage(currentPage - 1));
nextBtn.addEventListener('click', () => changePage(currentPage + 1));
modal.addEventListener('click', (e) => {
    if (e.target === modal || e.target.classList.contains('modal-overlay') || e.target.classList.contains('modal-close')) {
        closeModal();
    }
});

// Functions
async function fetchPokemons(page) {
    showLoading(true);

    try {
        const response = await fetch(
                `/pokemon/pokemons?page=${page - 1}&size=${pokemonsPerPage}`
                );

        if (!response.ok) {
            throw new Error(`HTTP error ${response.status}`);
        }

        const data = await response.json();

        // 🔹 Reset limpio
        allPokemons = [];
        filteredPokemons = [];

        // 🔹 Datos desde backend
        allPokemons = data.content;
        filteredPokemons = allPokemons;

        totalPages = Math.ceil(data.totalElements / pokemonsPerPage);

        renderPokemons();
        updatePagination();

    } catch (error) {
        console.error('Error fetching Pokemon:', error);
    } finally {
        showLoading(false);
    }
}



function renderPokemons() {
    pokemonGrid.innerHTML = '';

    if (filteredPokemons.length === 0) {
        noResults.style.display = 'block';
        pokemonGrid.style.display = 'none';
        return;
    }

    noResults.style.display = 'none';
    pokemonGrid.style.display = 'grid';

    filteredPokemons.forEach(pokemon => {
        const card = createPokemonCard(pokemon);
        pokemonGrid.appendChild(card);
    });
}

function createPokemonCard(pokemon) {
    const card = document.createElement('div');
    card.className = 'pokemon-card';

    const primaryType = pokemon.types[0]?.type.name || 'normal';
    const typeColor = typeColors[primaryType] || '#A8A878';

    const typesHTML = pokemon.types.map(type =>
            `<span class="type-badge type-${type.type.name}">${type.type.name.toUpperCase()}</span>`
    ).join('');

    card.innerHTML = `
        <div class="pokemon-card-header" style="background-color: ${typeColor};">
            <div class="pokemon-id">#${String(pokemon.id).padStart(3, '0')}</div>
            <div class="pokemon-image-container">
                <img src="${pokemon.sprites.front_default}" alt="${pokemon.name}" class="pokemon-image">
            </div>
        </div>
        <div class="pokemon-card-body">
            <h3 class="pokemon-name">${pokemon.name}</h3>
            <div class="pokemon-types">
                ${typesHTML}
            </div>
        </div>
    `;

    card.addEventListener('click', () => openModal(pokemon));

    return card;
}

function openModal(pokemon) {
    const primaryType = pokemon.types[0]?.type.name || 'normal';
    const typeColor = typeColors[primaryType] || '#A8A878';

    const typesHTML = pokemon.types.map(type =>
            `<span class="type-badge type-${type.type.name}">${type.type.name.toUpperCase()}</span>`
    ).join('');

    const abilitiesHTML = pokemon.abilities.map(ability =>
            `<span class="ability-badge">${ability.ability.name.replace('-', ' ')}</span>`
    ).join('');

    const statsHTML = pokemon.stats.map(stat => {
        const percentage = Math.min((stat.base_stat / 255) * 100, 100);
        return `
            <div class="stat-item">
                <div class="stat-header">
                    <span class="stat-name">${stat.stat.name.replace('-', ' ')}</span>
                    <span class="stat-number">${stat.base_stat}</span>
                </div>
                <div class="stat-bar-container">
                    <div class="stat-bar" style="width: ${percentage}%; background-color: ${typeColor};"></div>
                </div>
            </div>
        `;
    }).join('');

    const backSprite = pokemon.sprites.back_default
            ? `<img src="${pokemon.sprites.back_default}" alt="${pokemon.name} back">`
            : '';

    modalBody.innerHTML = `
        <div class="modal-header" style="background-color: ${typeColor};">
            <div class="modal-pokemon-id">#${String(pokemon.id).padStart(3, '0')}</div>
            <h2 class="modal-pokemon-name">${pokemon.name}</h2>
            <div class="modal-images">
                <img src="${pokemon.sprites.front_default}" alt="${pokemon.name} front">
                ${backSprite}
            </div>
            <div class="modal-types">
                ${typesHTML}
            </div>
        </div>
        <div class="modal-body-content">
            <div class="stats-grid">
                <div class="stat-box">
                    <p class="stat-label">Altura</p>
                    <p class="stat-value">${(pokemon.height / 10).toFixed(1)} m</p>
                </div>
                <div class="stat-box">
                    <p class="stat-label">Peso</p>
                    <p class="stat-value">${(pokemon.weight / 10).toFixed(1)} kg</p>
                </div>
            </div>
            <div>
                <h3 class="section-title">Habilidades</h3>
                <div class="abilities-list">
                    ${abilitiesHTML}
                </div>
            </div>
            <div>
                <h3 class="section-title">Estadísticas</h3>
                <div class="stats-list">
                    ${statsHTML}
                </div>
            </div>
        </div>
    `;

    modal.style.display = 'block';
}

function closeModal() {
    modal.style.display = 'none';
}

function handleSearch(e) {
    const searchTerm = e.target.value.toLowerCase().trim();

    if (searchTerm === '') {
        clearSearchBtn.style.display = 'none';
        searchResults.textContent = '';
        filteredPokemons = allPokemons;
        pagination.style.display = 'flex';
    } else {
        clearSearchBtn.style.display = 'flex';
        filteredPokemons = allPokemons.filter(pokemon => {
            const matchesName = pokemon.name.toLowerCase().includes(searchTerm);
            const matchesId = pokemon.id.toString().includes(searchTerm);
            return matchesName || matchesId;
        });

        const count = filteredPokemons.length;
        searchResults.textContent = `${count} resultado${count !== 1 ? 's' : ''} encontrado${count !== 1 ? 's' : ''}`;
        pagination.style.display = 'none';
    }

    renderPokemons();
}

function clearSearch() {
    searchInput.value = '';
    clearSearchBtn.style.display = 'none';
    searchResults.textContent = '';
    filteredPokemons = allPokemons;
    pagination.style.display = 'flex';
    renderPokemons();
}

function changePage(newPage) {
    if (newPage < 1 || newPage > totalPages)
        return;

    currentPage = newPage;
    searchInput.value = '';
    clearSearchBtn.style.display = 'none';
    searchResults.textContent = '';

    fetchPokemons(currentPage);
    window.scrollTo({top: 0, behavior: 'smooth'});
}

function updatePagination() {
    pageInfo.textContent = `Página ${currentPage} de ${totalPages}`;
    prevBtn.disabled = currentPage === 1;
    nextBtn.disabled = currentPage === totalPages;
}

function showLoading(show) {
    if (show) {
        loading.style.display = 'flex';
        pokemonGrid.style.display = 'none';
        pagination.style.display = 'none';
        noResults.style.display = 'none';
    } else {
        loading.style.display = 'none';
        pokemonGrid.style.display = 'grid';
        pagination.style.display = 'flex';
    }
}

