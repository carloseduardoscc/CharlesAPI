// javascript/main-Cadastro.js
import {apiCall, createModal} from './api.js';

const loadingBar = document.createElement('div');
loadingBar.className = 'loading-bar';
loadingBar.style.cssText = 'display: none; width: 100%; height: 3px; background: #f0f0f0; position: fixed; top: 0; left: 0;';
const progress = document.createElement('div');
progress.style.cssText = 'width: 0%; height: 100%; background: #4CAF50; transition: width 0.3s;';
loadingBar.appendChild(progress);
document.body.appendChild(loadingBar);

document.getElementById("register-form").addEventListener("submit", async (event) => {
    event.preventDefault();

    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;
    const confirmPassword = document.getElementById("confirm-password").value;
    const name = document.getElementById("name").value;
    const lastName = document.getElementById("lastName").value;
    const workspaceName = document.getElementById("workspace").value;

    if (password !== confirmPassword) {
        alert("As senhas não coincidem. Por favor, verifique.");
        return;
    }

    try {
        loadingBar.style.display = 'block';
        progress.style.width = '30%';

        const data = await apiCall("/auth/register", "POST", {
            email,
            password,
            name,
            lastName,
            workspaceName
        });

        progress.style.width = '100%';

        if (data && !data.error) {
            setTimeout(() => {
                loadingBar.style.display = 'none';
                createModal("Conta criada!", "Sua nova conta na Charles² foi criada com sucesso, faça seu login para continuar.")
                window.location.href = "Login.html";
            }, 300);
        }
    } catch (err) {
        loadingBar.style.display = 'none';
        console.error("Erro no cadastro:", err);
        alert("Não foi possível criar a conta.");
    }
});
