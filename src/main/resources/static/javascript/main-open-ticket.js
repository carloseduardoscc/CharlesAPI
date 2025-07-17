import { getToken } from './auth.js';
import { apiCall } from './api.js';

document.addEventListener("DOMContentLoaded", () => {
    const token = getToken();
    if (!token) {
        window.location.href = "Login.html";
        return;
    }

    const form = document.getElementById("ticket-form");

    form.addEventListener("submit", async (e) => {
        e.preventDefault();

        const description = document.getElementById("ticket-description").value.trim();

        if (!description) {
            alert("Por favor, preencha a descrição do chamado.");
            return;
        }

        // try {
            await apiCall("/serviceorder", "POST", { "description":description });

            window.location.href = "Home.html";
        // } catch (error) {
        //     console.error("Erro ao abrir chamado:", error);
        //     alert("Erro ao abrir o chamado. Tente novamente mais tarde.");
        // }
    });
});
