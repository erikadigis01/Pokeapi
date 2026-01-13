// ======================
// Type colors
// ======================
const typeColors = {
    normal: '#A8A878',
    fire: '#F08030',
    water: '#6890F0',
    electric: '#F8D030',
    grass: '#78C850',
    ice: '#98D8D8',
    fighting: '#C03028',
    poison: '#A040A0',
    ground: '#E0C068',
    flying: '#A890F0',
    psychic: '#F85888',
    bug: '#A8B820',
    rock: '#B8A038',
    ghost: '#705898',
    dragon: '#7038F8',
    dark: '#705848',
    steel: '#B8B8D0',
    fairy: '#EE99AC'
};
const pokemonTypes = Object.keys(typeColors);

// ======================
// State
// ======================
let favoritosIds = [];
let currentPage = 0; // backend usa 0-based
let totalPages = 0;
let currentTypeFilter = 'all';
let currentSearch = '';
const adminGrid = document.getElementById("adminPokemonGrid");
const pokemonGrid = document.getElementById("pokemonGrid");

const isAdminView = !!adminGrid;
let activeGrid = isAdminView ? adminGrid : pokemonGrid;



const pokemonsPerPage = 20;

const HEART_EMPTY = `<svg width="30" height="30" viewBox="0 0 24 24" fill="none" xmlns="www.w3.org"><path d="M9 2H5v2H3v2H1v6h2v2h2v2h2v2h2v2h2v2h4v-2h2v-2h2v-2h2v-2h2v-2h2V6h-2V4h-2V2h-4v2h-2v2h-2V4H9V2zm0 2v2h2v2h2V6h2V4h2v2h2v6h-2v2h-2v2h-2v2h-2v2h-2v-2H9v-2H7v-2H5v-2H3V6h2V4h4z" fill="currentColor"/></svg>`;
const HEART_FULL = `<svg width="30" height="30" viewBox="0 0 24 24" fill="currentColor" xmlns="www.w3.org"><path d="M2 6v6h2v2h2v2h2v2h2v2h4v-2h2v-2h2v-2h2v-2h2V6h-2V4h-2V2h-4v2h-2v2h-2V4H9V2H5v2H3v2H2z" /></svg>`;


// ======================
// DOM
// ======================
const navSearchInput = document.getElementById('navSearchInput');
const navClearSearchBtn = document.getElementById('navClearSearch');
const typeFiltersContainer = document.getElementById('typeFilters');
const filterResults = document.getElementById('filterResults');
const loading = document.getElementById('loading');
const noResults = document.getElementById('noResults');
const pagination = document.getElementById('pagination');
const prevBtn = document.getElementById('prevBtn');
const nextBtn = document.getElementById('nextBtn');
const pageInfo = document.getElementById('pageInfo');
const modal = document.getElementById('modal');
const modalBody = document.getElementById('modalBody');
const userName = document.getElementById('userName');

// ======================
// Init
// ======================

async function initApp() {
    try {
        initializeTypeFilters();

        if (!isAdminView) {
            // Usuario
            favoritosIds = await cargarFavoritos();
        }

        await fetchList(0);
    } catch (err) {
        console.error('Error inicializando app', err);
    }
}



document.addEventListener('DOMContentLoaded', initApp);


// ======================
// Events
// ======================
if (navSearchInput) {
    navSearchInput.addEventListener('input', handleSearch);
}

if (navClearSearchBtn) {
    navClearSearchBtn.addEventListener('click', clearSearch);
}

if (prevBtn) {
    prevBtn.addEventListener('click', () => changePage(currentPage - 1));
}

if (nextBtn) {
    nextBtn.addEventListener('click', () => changePage(currentPage + 1));
}

if (modal) {
    modal.addEventListener('click', e => {
        if (
                e.target === modal ||
                e.target.classList.contains('modal-overlay') ||
                e.target.classList.contains('modal-close')
                ) {
            closeModal();
        }
    });
}

// ======================
// Type filters
// ======================
function initializeTypeFilters() {
    if (!typeFiltersContainer)
        return;

    typeFiltersContainer.innerHTML = '';

    // Botón ALL
    const allBtn = document.createElement('button');
    allBtn.className = 'type-filter-btn active';
    allBtn.dataset.type = 'all';
    allBtn.textContent = 'all';
    allBtn.addEventListener('click', () => filterByType('all'));
    typeFiltersContainer.appendChild(allBtn);


    pokemonTypes.forEach(type => {
        const btn = document.createElement('button');
        btn.className = `type-filter-btn type-${type}`;
        btn.dataset.type = type;
        btn.textContent = type;
        btn.addEventListener('click', () => filterByType(type));
        typeFiltersContainer.appendChild(btn);
    });
}



// ======================
// Fetch
// ======================

async function fetchList(page = 0) {
    showLoading(true);

    try {
        const search = navSearchInput?.value?.trim() || '';
        const type =
                currentTypeFilter !== 'all'
                ? currentTypeFilter.toUpperCase()
                : '';

        let url;

        if (isAdminView) {
            url = `/admin/pokemons?page=${page}&size=${pokemonsPerPage}`;
        } else {
            url = `/pokemon/pokemons?page=${page}&size=${pokemonsPerPage}`;
        }

        if (search) {
            if (!isNaN(search))
                url += `&number=${search}`;
            else
                url += `&name=${search}`;
        }

        if (type)
            url += `&type=${type}`;

        const res = await fetch(url);
        const data = await res.json();

        const pokemons = data.content || data.pokemons || data;
        totalPages = data.totalPages || 1;

        if (!Array.isArray(pokemons)) {
            console.error("Formato inesperado del backend:", data);
            return;
        }

        renderPokemons(pokemons, favoritosIds);
        updatePagination();

    } catch (err) {
        console.error(err);
    } finally {
        showLoading(false);
}
}

// ======================
// Filters
// ======================

function updateFilterResults() {
    if (navSearchInput.value || currentTypeFilter !== 'all') {
        filterResults.textContent = `${filteredPokemons.length} Pokémon encontrados`;
        pagination.style.display = 'none';
    } else {
        filterResults.textContent = '';
        pagination.style.display = 'flex';
    }
}

// ======================
// Render cards
// ======================
function renderPokemons(pokemons, favoritosIds) {
    activeGrid.innerHTML = '';

    if (!pokemons.length) {
        noResults.style.display = 'block';
        return;
    }

    noResults.style.display = 'none';

    pokemons.forEach(p => {
        const primaryType = p.types[0];
        const pokemonEsFavorito = !isAdminView && favoritosIds.includes(p.id);

        const card = document.createElement('div');
        card.className = 'pokemon-card';

        const typesHTML = p.types.map(type =>
                `<span class="type-badge type-${type}">
                ${type.toUpperCase()}
            </span>`
        ).join('');

        card.innerHTML = `
            <div class="pokemon-card-header" style="background:${typeColors[primaryType]}">
                <div class="pokemon-id">#${String(p.id).padStart(3, '0')}</div>
                <div class="pokemon-image-container">
                    <img src="${p.image}" class="pokemon-image">
                </div>
            </div>

            <div class="pokemon-card-body">
                <h3 class="pokemon-name-card">${p.name.toUpperCase()}</h3>

                <div class="pokemon-types">
                    ${typesHTML}
                </div>

                ${isAdminView
                ? `
                    <div class="favoritos-container">
                        <div class="favoritos-count">
                            ${HEART_FULL} ${p.favoritosCount ?? p.favoritos ?? 0}
                        </div>
                    </div>
                    `
                : `
                    <button
                        class="btn-favorite ${pokemonEsFavorito ? 'active' : ''}"
                        data-id="${p.id}">
                        ${pokemonEsFavorito ? HEART_FULL : HEART_EMPTY}
                    </button>
`
                }

            </div>
        `;

        // Click card → modal
        card.addEventListener('click', (e) => {
            if (e.target.closest('.btn-favorite'))
                return;
            openModal(p.name);
        });

        // Click favorito → backend
        const btnFavorite = card.querySelector('.btn-favorite');

        if (btnFavorite) {
            btnFavorite.addEventListener('click', async (e) => {
                e.stopPropagation();

                const pokemonId = Number(btnFavorite.dataset.id);
                const esFavorito = await toggleFavorito(pokemonId);

                if (esFavorito) {
                    favoritosIds.push(pokemonId);
                } else {
                    favoritosIds = favoritosIds.filter(id => id !== pokemonId);
                }

                btnFavorite.classList.toggle('active', esFavorito);
                btnFavorite.innerHTML = esFavorito ? HEART_FULL : HEART_EMPTY;
            });
        }


        activeGrid.appendChild(card);
    });
}


// ======================
// Modal
// ======================
let statsChart = null;

let statsChartInstance = null;

async function openModal(pokemonName) {
    try {
        const res = await fetch(`/pokemon/${pokemonName}`);
        if (!res.ok)
            throw new Error('Error cargando Pokémon');

        const pokemon = await res.json();

        const mainType = pokemon.types[0].type.name;
        const color = typeColors[mainType];

        // GIF animado (fallback seguro)
        const animatedGif =
                pokemon.sprites?.versions?.["generation-v"]?.["black-white"]?.animated?.front_default
                || pokemon.sprites.front_default;

        // Limpiar chart previo
        if (statsChartInstance) {
            statsChartInstance.destroy();
            statsChartInstance = null;
        }

        modalBody.innerHTML = `
            <!-- HEADER -->
            <div class="modal-header" style="background:${color}">
                <div class="modal-pokemon-id">#${pokemon.id}</div>
                <h2 class="modal-pokemon-name">${pokemon.name}</h2>

                <img class="modal-main-gif" src="${getPokemonSprite(pokemon, {gif: true})}" alt="${pokemon.name}">

                <div class="modal-types">
                    ${pokemon.types.map(t => `
                        <span class="type-badge modal-type">
                            ${t.type.name.toUpperCase()}
                        </span>
                    `).join('')}
                </div>
            </div>

            <!-- BODY -->
            <div class="modal-body-content">

                <!-- Altura / Peso -->
                <div class="stats-grid">
                    <div class="stat-box">
                        <div class="stat-label">Altura</div>
                        <div class="stat-value">${pokemon.height / 10} m</div>
                    </div>
                    <div class="stat-box">
                        <div class="stat-label">Peso</div>
                        <div class="stat-value">${pokemon.weight / 10} kg</div>
                    </div>
                </div>

                <!-- Habilidades -->
                <h3 class="section-title">Habilidades</h3>
                <div class="abilities-list">
                    ${pokemon.abilities.map(a => `
                        <span class="ability-badge">
                            ${a.ability.name}
                            ${a.is_hidden ? ' (Oculta)' : ''}
                        </span>
                    `).join('')}
                </div>

                <!-- Estadísticas -->
                <h3 class="section-title">Estadísticas</h3>
                <canvas id="statsChart"></canvas>

                <!-- Evoluciones -->
                <h3 class="section-title">Evoluciones</h3>
                <div id="evolutions" class="evolutions">
                    <p>Cargando evoluciones...</p>
                </div>

                <!-- Shiny -->
                ${
                pokemon.sprites.front_shiny
                ? `
                        <div class="shiny-section">
                            <h3 class="section-title">Shiny</h3>
                            <img src="${pokemon.sprites.front_shiny}" alt="Shiny ${pokemon.name}">
                        </div>
                        `
                : ''
                }
            </div>
        `;

        modal.style.display = 'block';

        requestAnimationFrame(() => {
            renderStatsChart(pokemon, color);
        });

        loadEvolutions(pokemon.species.url);

    } catch (err) {
        console.error(err);
    }
}


function closeModal() {
    modal.style.display = 'none';
    if (statsChartInstance) {
        statsChartInstance.destroy();
        statsChartInstance = null;
    }
}

// ======================
// Chart
// ======================

function renderStatsChart(pokemon, color) {
    const ctx = document.getElementById('statsChart');

    if (!ctx)
        return;

    const labels = pokemon.stats.map(s =>
        s.stat.name
                .replace('-', ' ')
                .toUpperCase()
    );

    const data = pokemon.stats.map(s => s.base_stat);

    statsChart = new Chart(ctx, {
        type: 'radar',
        data: {
            labels,
            datasets: [{
                    label: 'Stats',
                    data,
                    backgroundColor: color + '55',
                    borderColor: color,
                    borderWidth: 2,
                    pointBackgroundColor: color
                }]
        },
        options: {
            responsive: true,
            scales: {
                r: {
                    beginAtZero: true,
                    ticks: {
                        stepSize: 20
                    }
                }
            },
            plugins: {
                legend: {
                    display: false
                }
            }
        }
    });
}

// ======================
// Helpers
// ======================

async function loadEvolutions(speciesUrl) {
    try {
        // 1️⃣ Obtener species
        const speciesRes = await fetch(speciesUrl);
        const speciesData = await speciesRes.json();

        // 2️⃣ Obtener evolution chain
        const evoRes = await fetch(speciesData.evolution_chain.url);
        const evoData = await evoRes.json();

        const evolutionsContainer = document.getElementById('evolutions');
        evolutionsContainer.innerHTML = '';

        // 3️⃣ Recorrer la cadena
        const evolutions = [];
        let current = evoData.chain;

        do {
            evolutions.push(current.species.name);
            current = current.evolves_to[0];
        } while (current);

        // 4️⃣ Pintar evoluciones
        for (const name of evolutions) {
            const res = await fetch(`/pokemon/${name}`);
            const pokemon = await res.json();

            evolutionsContainer.innerHTML += `
                <div class="evolution-card" onclick="openModal('${pokemon.name}')">
                    <img src="${pokemon.sprites.front_default}">
                    <span>${pokemon.name}</span>
                </div>
            `;
        }

    } catch (err) {
        console.error('Error cargando evoluciones', err);
    }
}

function getPokemonSprite(pokemon, { gif = false } = {}) {
    if (gif) {
        return (
                pokemon.sprites?.other?.showdown?.front_default ||
                pokemon.sprites.front_default
                );
    }
    return pokemon.sprites.front_default;
}

function hexToRgba(hex, alpha) {
    const r = parseInt(hex.slice(1, 3), 16);
    const g = parseInt(hex.slice(3, 5), 16);
    const b = parseInt(hex.slice(5, 7), 16);
    return `rgba(${r},${g},${b},${alpha})`;
}

function handleSearch() {
    navClearSearchBtn.style.display = navSearchInput.value ? 'flex' : 'none';
    currentPage = 0;
    fetchList(0);
}

function clearSearch() {
    navSearchInput.value = '';
    navClearSearchBtn.style.display = 'none';
    applyFilters();
}

function filterByType(type) {
    currentTypeFilter = type;
    currentPage = 0;

    document
            .querySelectorAll('.type-filter-btn')
            .forEach(b => b.classList.remove('active'));

    document
            .querySelector(`[data-type="${type}"]`)
            .classList.add('active');

    fetchList(0);
}

function changePage(page) {
    if (page < 0 || page >= totalPages)
        return;
    currentPage = page;
    fetchList(page);
}


function updatePagination() {
    pageInfo.textContent = `Página ${currentPage + 1} de ${totalPages}`;
}

function showLoading(show) {
    loading.style.display = show ? 'flex' : 'none';
}

// ======================
// User name
// ======================
async function loadUserName() {
    const res = await fetch('/users/me');
    const user = await res.json();
    userName.textContent = user.name;
}

// ======================
// Favoritos
// ======================

async function toggleFavorito(pokemonId) {

    const res = await fetch(
            `/favoritos/toggle?idPokemon=${pokemonId}`,
            {method: 'POST'}
    );

    const data = await res.json();
    return data.favorito;
}


async function cargarFavoritos() {
    const res = await fetch(`/favoritos`);

    const text = await res.text();
    console.log("Respuesta favoritos:", text);

    if (!res.ok) {
        throw new Error("No autorizado");
    }

    const favoritos = JSON.parse(text);
    return favoritos.map(f => f.idPokemon);
}

async function cargarPokemonsFavoritos() {
    activeGrid.innerHTML = '';

    for (const id of favoritosIds) {
        const res = await fetch(`/pokemon/${id}`);
        const pokemon = await res.json();

        renderPokemons([{
                id: pokemon.id,
                name: pokemon.name,
                image: pokemon.sprites.front_default,
                types: pokemon.types.map(t => t.type.name)
            }], favoritosIds);
    }
}

// ======================
// Funcion del admin 
// ======================

function renderPokemonsAdmin(pokemons) {
    const grid = document.getElementById('adminPokemonGrid');
    grid.innerHTML = '';

    pokemons.forEach(p => {
        const card = document.createElement('div');
        card.className = 'pokemon-card';

        card.innerHTML = `
            <div class="pokemon-card-header" style="background:${typeColors[p.types[0]]}">
                <div class="pokemon-id">#${String(p.id).padStart(3, '0')}</div>
                <div class="pokemon-image-container">
                    <img src="${p.image}" class="pokemon-image">
                </div>
            </div>

            <div class="pokemon-card-body">
                <h3 class="pokemon-name-card">${p.name.toUpperCase()}</h3>
                <div class="pokemon-types">
                    ${p.types.map(type => `<span class="type-badge type-${type}">${type.toUpperCase()}</span>`).join('')}
                </div>
                <div class="favoritos-container">
                    <div class="favoritos-count">
                        ${HEART_FULL} ${p.favoritosCount}
                    </div>
                </div>
            </div>
        `;
        card.addEventListener('click', () => {
            openModal(p.name);
        });
        grid.appendChild(card);
    });
}
