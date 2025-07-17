import {getToken} from './auth.js';
import {apiCall, downloadPdf} from './api.js';

window.addEventListener("DOMContentLoaded", async () => {
    const token = getToken();
    if (!token) return (window.location.href = "Login.html");

    try {
        const user = await apiCall("/auth/me");
        const orders = await apiCall("/serviceorder", "GET");
        renderOrders(orders, user);
    } catch (error) {
        console.error("Erro ao carregar ordens:", error);
    }
});

function renderOrders(orders, user) {
    const tbody = document.querySelector("tbody");
    tbody.innerHTML = "";

    orders.forEach(order => {
        const isMyOS = [order.supporterEmail, order.collaboratorEmail].includes(user.email);
        const tr = document.createElement("tr");

        tr.innerHTML = `
      <td>${order.soCode}${isMyOS ? ' <span class="material-icons" title="Minha OS">star</span>' : ''}</td>
      <td>${order.collaboratorName || '-'}</td>
      <td>${order.supporterName || '-'}</td>
      <td>${formatStatusBadge(order.currentState)}</td>
      <td class="actions">${renderActions(order, user, isMyOS)}</td>
    `;

        tbody.appendChild(tr);
    });

    document.querySelectorAll('.action-btn').forEach(btn => {
        btn.addEventListener("click", async () => {
            const action = btn.dataset.action;
            const orderId = btn.dataset.id;

            try {
                if (action === "assign") {
                    await apiCall(`/serviceorder/${orderId}/assign`, "POST");
                } else if (action === "cancel") {
                    await apiCall(`/serviceorder/${orderId}/cancel`, "POST");
                }
                if (action === "complete") {
                    // Abrir modal (sem recarregar a página!)
                    window.orderToComplete = orderId;
                    document.getElementById("completeModal").style.display = "flex";
                    return; // <- ESSENCIAL: interrompe o fluxo aqui e evita o reload
                } else if (action === "download") {
                        await downloadPdf(`/serviceorder/${orderId}/report`)
                }


                location.reload();
            } catch (err) {
                console.error(`Erro ao executar ${action}:`, err);
                alert("Erro ao executar ação.");
            }
        });
    });
    document.getElementById("cancelModal").addEventListener("click", () => {
        document.getElementById("diagnosticInput").value = "";
        document.getElementById("completeModal").style.display = "none";
    });

    document.getElementById("confirmComplete").addEventListener("click", async () => {
        const diagnostic = document.getElementById("diagnosticInput").value.trim();

        if (!diagnostic) {
            alert("Por favor, preencha o diagnóstico.");
            return;
        }

        try {
            await apiCall(`/serviceorder/${window.orderToComplete}/complete`, "POST", {
                diagnostic
            });
            document.getElementById("diagnosticInput").value = "";
            document.getElementById("completeModal").style.display = "none";
            location.reload();
        } catch (err) {
            console.error("Erro ao concluir OS:", err);
            alert("Erro ao concluir OS.");
        }
    });

}

function renderActions(order, user, isMyOS) {
    const actions = [];
    const status = order.currentState;
    const role = user.role;

    const id = order.id;

    const addAction = (icon, title, action) => {
        actions.push(`<span class="material-icons action-btn" title="${title}" data-id="${id}" data-action="${action}">${icon}</span>`);
    };

    if (["CANCELED", "COMPLETED"].includes(status)) {
        addAction("download", "Baixar Relatório", "download");
    }

    if (["COLLABORATOR", "OWNER"].includes(role)) {
        if (status === "OPEN") addAction("cancel", "Cancelar OS", "cancel");
    }

    if (["SUPPORTER", "ADMIN"].includes(role)) {
        if (status === "OPEN") {
            addAction("assignment_ind", "Assumir OS", "assign");
        }
        if (status === "ASSIGNED" && isMyOS) {
            addAction("cancel", "Cancelar OS", "cancel");
            addAction("check_circle", "Concluir OS", "complete");
        }
    }


    return actions.join(" ");
}

function formatStatusBadge(status) {
    const map = {
        OPEN: {text: "Aberto", class: "aberto"},
        ASSIGNED: {text: "Em Andamento", class: "andamento"},
        COMPLETED: {text: "Finalizado", class: "finalizado"},
        CANCELED: {text: "Cancelado", class: "cancelado"},
    };

    const s = map[status] || {text: status, class: ""};
    return `<span class="badge status-badge ${s.class}">${s.text}</span>`;
}
