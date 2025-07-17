import {apiCall, downloadPdf} from './api.js';

const fromInput = document.getElementById('date-from');
const toInput = document.getElementById('date-to');
const reportTypeInput = document.getElementById('report-type');
const reportSummary = document.getElementById('report-summary');
const reportPeriod = document.getElementById('report-period');
const generateBtn = document.getElementById('generate-btn');

// 🔎 Utilitário pra formatar data
function formatDate(dateStr) {
    const date = new Date(dateStr);
    return date.toLocaleDateString('pt-BR');
}

// 🔄 Atualiza as estatísticas no painel
async function fetchStatistics() {
    const minDate = fromInput.value;
    const maxDate = toInput.value;

    if (!minDate || !maxDate) return;

    const query = `?minDate=${minDate}&maxDate=${maxDate}`;
    const data = await apiCall(`/serviceorder/statistcs${query}`);

    reportPeriod.textContent = `Período: ${formatDate(minDate)} a ${formatDate(maxDate)}`;

    reportSummary.innerHTML = `
    <div class="summary-item"><h3>Abertos</h3><p>${data.open}</p></div>
    <div class="summary-item"><h3>Em Andamento</h3><p>${data.assigned}</p></div>
    <div class="summary-item"><h3>Cancelados</h3><p>${data.canceled}</p></div>
    <div class="summary-item"><h3>Resolvidos</h3><p>${data.completed}</p></div>
    <div class="summary-item"><h3>Fechados</h3><p>${data.closed}</p></div>
    <div class="summary-item"><h3>Total de chamados</h3><p>${data.all}</p></div>
  `;
}

// 📥 Faz o download do PDF
async function downloadReport() {
    const reportType = reportTypeInput.value;
    const minDate = fromInput.value;
    const maxDate = toInput.value;

    const query = `?reportType=${reportType}&minDate=${minDate}&maxDate=${maxDate}`;
    const token = localStorage.getItem('token');

    const response = await downloadPdf(`/serviceorder/report${query}`)
}

// 🧠 Eventos
fromInput.addEventListener('change', fetchStatistics);
toInput.addEventListener('change', fetchStatistics);
generateBtn.addEventListener('click', downloadReport);

// ⏱️ Inicialização
(async () => {
    const today = new Date().toISOString().slice(0, 10);
    const firstDay = new Date();
    firstDay.setDate(firstDay.getDate() - 7);

    fromInput.value = firstDay.toISOString().slice(0, 10);
    toInput.value = today;

    await fetchStatistics();
})();
