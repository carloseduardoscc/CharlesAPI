import { getToken } from './auth.js';
import {apiCall, createModal} from './api.js';

window.addEventListener("DOMContentLoaded", async () => {
    const token = getToken();
    if (!token) {
        window.location.href = "Login.html";
        return;
    }

    try {
        const user = await apiCall("/auth/me");
        document.querySelector(".user-name").textContent = `${user.name} ${user.lastname}`;
        document.querySelector(".welcome-section h2").textContent = `Olá, ${user.name}`;

        const stats = await apiCall("/serviceorder/statistcs", "GET");
        document.querySelector(".calls-count").textContent = stats.open;
        document.querySelector(".orders-count").textContent = stats.all;

        await loadRecentCalls();

        // Filtros
        document.getElementById("filter-date").addEventListener("change", loadRecentCalls);
        document.getElementById("filter-status").addEventListener("change", loadRecentCalls);
    } catch (err) {
        createModal("Erro ao carregar dados da Home", "Ocorreu um erro ao carregar os dados da Home. Tente novamente mais tarde. Caso o erro persista, entre em contato com o suporte.")
        alert("Erro ao carregar o painel. Faça login novamente.");
        window.location.href = "Login.html";
    }
});

async function loadRecentCalls() {
    try {

        const user = await apiCall("/auth/me", "GET");

        // Captura filtros
        const selectedDate = document.getElementById("filter-date").value;
        const selectedStatus = document.getElementById("filter-status").value;

        let query = `/serviceorder?maxDate=${selectedDate}&minDate=${selectedDate}`;
        if (selectedStatus) query += `&state=${selectedStatus}`;

        const calls = await apiCall(query, "GET");

        const list = document.querySelector(".calls-list");
        list.innerHTML = "";

        calls.forEach(call => {
            const li = document.createElement("li");
            li.innerHTML = `
                <span>${call.soCode} - ${call.description}</span>
                <span class="status ${call.currentState.toLowerCase()}">${formatStatus(call.currentState)}</span>
            `;
            list.appendChild(li);
        });
    } catch (error) {
        console.error("Erro ao carregar chamados recentes:", error);
    }
}

function formatStatus(status) {
    const map = {
        OPEN: "Aberto",
        ASSIGNED: "Em Andamento",
        CANCELED: "Cancelado",
        COMPLETED: "Concluído"
    };
    return map[status] || status;
}
