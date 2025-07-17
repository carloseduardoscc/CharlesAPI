import { getToken } from './auth.js';

let BASE_URL = "http://localhost"; // inicial
const FALLBACK_URL = "https://charlesapi-production.up.railway.app"; // seu backend fixo de produção
let alreadySwitched = false;

async function tryBaseUrl(url) {
  try {
    const res = await fetch(url + '/auth/login', {
      method: 'OPTIONS',
      mode: 'cors'
    });
    return res.ok || res.status === 204; // geralmente retorna 204 No Content
  } catch (e) {
    return false;
  }
}

// Inicializa BASE_URL ao carregar o módulo
(async () => {
  const ok = await tryBaseUrl(BASE_URL);
  if (!ok) {
    console.warn("localhost:80 não está acessível, usando fallback.");
    BASE_URL = FALLBACK_URL;
    alreadySwitched = true;
  }
})();


export function createModal(title, message) {
  const modal = document.createElement('div');
  modal.style.position = 'fixed';
  modal.style.top = '50%';
  modal.style.left = '50%';
  modal.style.transform = 'translate(-50%, -50%)';
  modal.style.backgroundColor = '#242424';
  modal.style.padding = '25px';
  modal.style.borderRadius = '10px';
  modal.style.zIndex = '1000';
  modal.style.boxShadow = '0 5px 15px rgba(0, 0, 0, 0.3)';
  modal.style.maxWidth = '450px';
  modal.style.width = '90%';
  modal.style.textAlign = 'center';
  modal.style.fontFamily = "'Gill Sans', 'Gill Sans MT', Calibri, 'Trebuchet MS', sans-serif";

  const overlay = document.createElement('div');
  overlay.style.position = 'fixed';
  overlay.style.top = '0';
  overlay.style.left = '0';
  overlay.style.right = '0';
  overlay.style.bottom = '0';
  overlay.style.backgroundColor = 'rgba(0, 0, 0, 0.7)';
  overlay.style.backdropFilter = 'blur(5px)';
  overlay.style.zIndex = '999';

  const iconElement = document.createElement('div');
  iconElement.innerHTML = '⚠️';
  iconElement.style.fontSize = '48px';
  iconElement.style.marginBottom = '15px';

  const titleElement = document.createElement('h2');
  titleElement.textContent = title;
  titleElement.style.color = '#8be4ac';
  titleElement.style.margin = '0 0 15px 0';
  titleElement.style.fontSize = '1.8rem';

  const messageElement = document.createElement('p');
  messageElement.textContent = message;
  messageElement.style.color = '#f5f8f7';
  messageElement.style.margin = '0 0 25px 0';
  messageElement.style.fontSize = '1.1rem';

  const button = document.createElement('button');
  button.textContent = 'OK';
  button.style.padding = '12px 25px';
  button.style.backgroundColor = '#8be4ac';
  button.style.color = '#121212';
  button.style.border = 'none';
  button.style.borderRadius = '8px';
  button.style.fontSize = '1rem';
  button.style.fontWeight = 'bold';
  button.style.cursor = 'pointer';
  button.style.transition = 'background-color 0.3s';

  button.onmouseover = () => {
    button.style.backgroundColor = '#6bcb9d';
  };

  button.onmouseout = () => {
    button.style.backgroundColor = '#8be4ac';
  };

  button.onclick = () => {
    document.body.removeChild(modal);
    document.body.removeChild(overlay);
  };

  modal.appendChild(iconElement);
  modal.appendChild(titleElement);
  modal.appendChild(messageElement);
  modal.appendChild(button);

  document.body.appendChild(overlay);
  document.body.appendChild(modal);

  // Adiciona foco ao botão OK
  button.focus();

  // Adiciona evento para fechar o modal ao pressionar Enter
  const handleKeyDown = (event) => {
    if (event.key === 'Enter') {
      button.click();
    }
  };

  document.addEventListener('keydown', handleKeyDown);

  // Remover o event listener quando o modal for fechado
  const originalOnClick = button.onclick;
  button.onclick = () => {
    document.removeEventListener('keydown', handleKeyDown);
    originalOnClick();
  };
}

export async function downloadPdf(url) {
  const headers = {
    "Content-Type": "application/pdf"
  };
  const token = getToken();
  if (token) {
    headers.Authorization = `Bearer ${token}`;
  }
  const res = await fetch(BASE_URL + url, {
    method: "GET",
    headers
  });
  if (!res.ok) {
    createModal('ERRO', 'Erro ao baixar o PDF');
    return null;
  }
  const blob = await res.blob();
  const downloadUrl = URL.createObjectURL(blob);
  const link = document.createElement('a');
  link.href = downloadUrl;
  link.download = 'document.pdf';
  document.body.appendChild(link);
  link.click();
  document.body.removeChild(link);
  URL.revokeObjectURL(downloadUrl);
}

export async function apiCall(url, method = "GET", body) {
  const headers = {
    "Content-Type": "application/json"
  };
  const token = getToken();
  if (token) {
    headers.Authorization = `Bearer ${token}`;
  }
  const res = await fetch(BASE_URL + url, {
    method: method,
    headers,
    body: JSON.stringify(body)
  });
  let data = '{}';
  try {
    data = await res.json();
  } catch (error) {
  }
  if (!res.ok && ![200, 201].includes(res.status)) {
    createModal('ERRO', data.message || 'Algo deu errado, mas não foi especificado.');
    throw new Error(data.message);
  } else {
    if (data.message) {
      createModal('ERRO', data.message);
    }
  }
  return data;
}

