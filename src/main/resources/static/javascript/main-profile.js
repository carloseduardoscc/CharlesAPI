import { apiCall } from './api.js';
import { logout } from './auth.js';

// Elementos do DOM
const nameEl = document.getElementById('profile-name');
const emailEl = document.getElementById('profile-email');
const fullNameEl = document.getElementById('full-name');
const createdAtEl = document.getElementById('created-at');
const logoutBtn = document.getElementById('logout-btn');

// Carregar dados do perfil
async function loadProfile() {
    const user = await apiCall('/auth/me', 'GET');

    nameEl.textContent = user.name;
    emailEl.textContent = user.email;
    fullNameEl.textContent = `${user.name} ${user.lastname}`;
    createdAtEl.textContent = new Date(user.createdAt || new Date()).toLocaleDateString('pt-BR');
}

// Botão de logout
logoutBtn.addEventListener('click', () => logout());

// Inicializar
loadProfile();
