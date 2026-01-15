// ======================
// State (igual que script.js)
// ======================
let favoritosIds = [];

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

const HEART_EMPTY = `<svg width="30" height="30" viewBox="0 0 24 24" fill="none" xmlns="www.w3.org"><path d="M9 2H5v2H3v2H1v6h2v2h2v2h2v2h2v2h2v2h4v-2h2v-2h2v-2h2v-2h2v-2h2V6h-2V4h-2V2h-4v2h-2v2h-2V4H9V2zm0 2v2h2v2h2V6h2V4h2v2h2v6h-2v2h-2v2h-2v2h-2v2h-2v-2H9v-2H7v-2H5v-2H3V6h2V4h4z" fill="currentColor"/></svg>`;
const HEART_FULL = `<svg width="30" height="30" viewBox="0 0 24 24" fill="currentColor" xmlns="www.w3.org"><path d="M2 6v6h2v2h2v2h2v2h2v2h4v-2h2v-2h2v-2h2v-2h2V6h-2V4h-2V2h-4v2h-2v2h-2V4H9V2H5v2H3v2H2z" /></svg>`;

// ======================
// DOM
// ======================
const pokemonGrid = document.getElementById('pokemonGrid');
const noResults = document.getElementById('noResults');

// Modal editar perfil
const modalEditar = document.getElementById("modal-editar");
const btnEditar = document.getElementById("btnEditarPerfil");
const btnCerrar = document.querySelector(".close");


// ======================
// Init
// ======================
document.addEventListener("DOMContentLoaded", async () => {
    initModalPerfil();

    try {
        favoritosIds = await cargarFavoritos();
        await cargarPokemonsFavoritos();
    } catch (e) {
        console.error("Error cargando perfil", e);
    }
});


// ======================
// Modal editar perfil
// ======================
function initModalPerfil() {
    if (!modalEditar || !btnEditar || !btnCerrar) return;

    btnEditar.addEventListener("click", () => {
        modalEditar.style.display = "block";
    });

    btnCerrar.addEventListener("click", () => {
        modalEditar.style.display = "none";
    });

    window.addEventListener("click", e => {
        if (e.target === modalEditar) {
            modalEditar.style.display = "none";
        }
    });
}


// ======================
// Backend Favoritos
// ======================
async function cargarFavoritos() {
    const res = await fetch(`/favoritos`);

    if (!res.ok) {
        throw new Error("No autorizado");
    }

    const favoritos = await res.json();
    return favoritos.map(f => f.idPokemon);
}


// ======================
// Cargar pokémon favoritos
// ======================
async function cargarPokemonsFavoritos() {
    pokemonGrid.innerHTML = '';

    if (favoritosIds.length === 0) {
        noResults.style.display = 'block';
        return;
    }

    noResults.style.display = 'none';

    for (const id of favoritosIds) {
        const res = await fetch(`/pokemon/${id}`);
        const p = await res.json();

        renderPokemonCard({
            id: p.id,
            name: p.name,
            image: p.sprites.front_default,
            types: p.types.map(t => t.type.name)
        });
    }
}


// ======================
// Render Card (misma lógica que script.js)
// ======================
function renderPokemonCard(p) {
    const primaryType = p.types[0];
    const esFavorito = favoritosIds.includes(p.id);

    const card = document.createElement('div');
    card.className = 'pokemon-card';

    const typesHTML = p.types.map(type => `
        <span class="type-badge type-${type}">
            ${type.toUpperCase()}
        </span>
    `).join('');

    card.innerHTML = `
        <div class="pokemon-card-header" style="background:${typeColors[primaryType]}">
            <div class="pokemon-id">#${String(p.id).padStart(3, '0')}</div>
            <img src="${p.image}" class="pokemon-image">
        </div>

        <div class="pokemon-card-body">
            <h3 class="pokemon-name-card">${p.name.toUpperCase()}</h3>
            <div class="pokemon-types">${typesHTML}</div>

            <button class="btn-favorite ${esFavorito ? 'active' : ''}" data-id="${p.id}">
                ${esFavorito ? HEART_FULL : HEART_EMPTY}
            </button>
        </div>
    `;

    // Favoritos
    const btnFav = card.querySelector(".btn-favorite");
    btnFav.addEventListener("click", async e => {
        e.stopPropagation();

        const pokemonId = Number(btnFav.dataset.id);
        const esFavorito = await toggleFavorito(pokemonId);

        if (!esFavorito) {
            favoritosIds = favoritosIds.filter(id => id !== pokemonId);
            card.remove();

            if (favoritosIds.length === 0) {
                noResults.style.display = 'block';
            }
        }
    });

    pokemonGrid.appendChild(card);
}


// ======================
// Toggle favorito
// ======================
async function toggleFavorito(pokemonId) {
    const res = await fetch(`/favoritos/toggle?idPokemon=${pokemonId}`, {
        method: 'POST'
    });

    const data = await res.json();
    return data.favorito;
}

//============================================
//FUNCION PARA CONFIRMAR LA ELIMINACION DE UN USUARIO
//============================================

function confirmarEliminacionUsuario(id){
    Swal.fire({
    title: "Estas seguro de eliminar este usuario?",
            text: "No puedes revertir esta accion!",
            icon: "warning",
            showCancelButton: true,
            confirmButtonColor: "#3085d6",
            cancelButtonColor: "#d33",
            confirmButtonText: "Si, eliminalo"
    }).then((result) => {
    if (result.isConfirmed) {
    window.location.href = "/administrador/users/delete/" + id;
    }
  });
}