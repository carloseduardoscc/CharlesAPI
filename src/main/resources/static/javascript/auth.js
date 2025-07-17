import { apiCall } from './api.js';


export function login(email, password) {
    localStorage.removeItem("token");
    const payload = {"login": email, "password": password}
    return apiCall("/auth/login","POST" , payload)
        .then(data => {
            if (data.token) {
                saveToken(data.token);
            }
            return data;
        });
}

export function saveToken(token) {
    localStorage.setItem("token", token);
}

export function getToken() {
    return localStorage.getItem("token");
}

export function logout() {
    localStorage.removeItem("token");
    window.location.href = "login.html";
}
