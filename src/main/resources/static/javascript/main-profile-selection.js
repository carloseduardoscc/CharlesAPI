// registerUser.js
import { apiCall } from './api.js';

function setupProfileSelection() {
    const profileCards = document.querySelectorAll('.profile-card');
    const selectedProfileInput = document.getElementById('selectedProfile');

    profileCards.forEach(card => {
        const selectBtn = card.querySelector('.select-btn');

        card.addEventListener('click', (e) => {
            if (e.target !== selectBtn) selectBtn.click();
        });

        selectBtn.addEventListener('click', (e) => {
            e.stopPropagation();
            profileCards.forEach(c => c.classList.remove('selected'));
            card.classList.add('selected');
            selectedProfileInput.value = card.getAttribute('data-profile');
            console.log('Profile selected:', card.getAttribute('data-profile'));
        });
    });
}

function setupFormSubmit() {
    const form = document.getElementById('registrationForm');

    form.addEventListener('submit', async (e) => {
        e.preventDefault();

        const password = document.getElementById('password').value;
        const confirmPassword = document.getElementById('confirmPassword').value;
        const selectedProfile = document.getElementById('selectedProfile').value;

        if (password !== confirmPassword) {
            alert('As senhas não coincidem!');
            return;
        }

        if (!selectedProfile) {
            alert('Por favor, selecione um perfil!');
            return;
        }

        const body = {
            name: document.getElementById('firstName').value,
            lastName: document.getElementById('lastName').value,
            email: document.getElementById('email').value,
            password: password,
            role: selectedProfile.toUpperCase()
        };

        try {
            const result = await apiCall('/participants', 'POST', body);
            alert('Usuário cadastrado com sucesso!');
            window.location.href = 'Home.html';
        } catch (error) {
        }
    });
}

// Inicializa tudo
window.addEventListener('DOMContentLoaded', () => {
    setupProfileSelection();
    setupFormSubmit();
});
