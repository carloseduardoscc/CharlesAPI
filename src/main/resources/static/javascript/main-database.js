import { apiCall } from './api.js';

const tbody = document.getElementById('participantsTableBody');
const refreshBtn = document.getElementById('refreshBtn');
const searchInput = document.getElementById('searchInput');

let participants = [];
let user = null;

// Buscar usuário logado
async function fetchLoggedUser() {
    const data = await apiCall('/auth/me', 'GET');
    user = data;
}

// Buscar participantes
async function loadParticipants() {
    const data = await apiCall('/participants', 'GET');
    participants = data;
    renderParticipants(data);
}

// Renderizar tabela
function renderParticipants(data) {
    tbody.innerHTML = '';

    data.forEach(p => {
        const canToggle =
            (user.role === 'OWNER' && ['ADMIN', 'SUPPORTER', 'COLLABORATOR'].includes(p.role)) ||
            (user.role === 'ADMIN' && ['SUPPORTER', 'COLLABORATOR'].includes(p.role));

        const tr = document.createElement('tr');
        tr.innerHTML = `
      <td>${p.id}</td>
      <td>${p.name}</td>
      <td>${p.email}</td>
      <td><span class="badge ${p.role.toLowerCase()}">${p.role}</span></td>
      <td><span class="status ${p.isActive ? 'active' : 'inactive'}">
        ${p.isActive ? 'Ativo' : 'Inativo'}
      </span></td>
      <td class="actions">
        ${canToggle
            ? `<span class="material-icons toggle-btn"
                    style="cursor: pointer;"
                    data-id="${p.id}"
                    data-active="${p.isActive}">
                ${p.isActive ? 'toggle_off' : 'check_circle'}
              </span>`
            : ''
        }
      </td>
    `;
        tbody.appendChild(tr);
    });

    // Botões de ativar/desativar
    document.querySelectorAll('.toggle-btn').forEach(btn => {
        btn.addEventListener('click', async () => {
            const id = btn.dataset.id;
            const isActive = btn.dataset.active === 'true';
            const endpoint = `/participants/${id}/${isActive ? 'deactivate' : 'activate'}`;

            await apiCall(endpoint, 'POST');
            await loadParticipants();
        });
    });
}

// Filtro de busca
searchInput.addEventListener('input', (e) => {
    const term = e.target.value.toLowerCase();
    const filtered = participants.filter(p =>
        p.name.toLowerCase().includes(term) ||
        p.email.toLowerCase().includes(term)
    );
    renderParticipants(filtered);
});

// Botão de atualizar
refreshBtn.addEventListener('click', async () => {
    await loadParticipants();
});

// Inicializar
(async () => {
    await fetchLoggedUser();
    await loadParticipants();
})();
