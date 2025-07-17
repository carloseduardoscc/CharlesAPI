// javascript/main-Login.js
import { login } from './auth.js';

document.getElementById("login-form").addEventListener("submit", async (event) => {
    event.preventDefault();

    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;

    try {
        const data = await login(email, password);

        if (data.token) {
            window.location.href = "Home.html";
        } else {
        }
    } catch (err) {
    }
});
