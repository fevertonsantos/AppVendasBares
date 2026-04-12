/* ========================================
   AppVendasBares - Main Application JS
   ======================================== */

// ---------- Sidebar Toggle ----------
function toggleSidebar() {
    const sidebar = document.getElementById('sidebar');
    const overlay = document.getElementById('sidebarOverlay');
    if (sidebar) {
        sidebar.classList.toggle('open');
        if (overlay) overlay.classList.toggle('show');
    }
}

function closeSidebar() {
    const sidebar = document.getElementById('sidebar');
    const overlay = document.getElementById('sidebarOverlay');
    if (sidebar) sidebar.classList.remove('open');
    if (overlay) overlay.classList.remove('show');
}

// ---------- Submenu Toggle ----------
function toggleSubmenu(event) {
    event.preventDefault();
    const link = event.currentTarget;
    const submenu = link.nextElementSibling;
    if (submenu && submenu.classList.contains('submenu')) {
        submenu.classList.toggle('open');
        link.classList.toggle('expanded');
    }
}

// ---------- Logout ----------
function doLogout() {
    fetch('/api/auth/logout', { method: 'POST' })
        .then(() => {
            localStorage.removeItem('usuario');
            window.location.href = '/login';
        })
        .catch(() => {
            localStorage.removeItem('usuario');
            window.location.href = '/login';
        });
}

// ---------- Chamar Garcom ----------
function chamarGarcom() {
    const usuario = JSON.parse(localStorage.getItem('usuario') || '{}');
    const empresaId = usuario.empresaId || 1;

    fetch('/api/mesa/chamar-garcom', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ empresaId: empresaId })
    })
    .then(r => {
        if (r.ok) {
            showToast('Garcom chamado com sucesso!');
        } else {
            showToast('Erro ao chamar garcom', true);
        }
    })
    .catch(() => showToast('Erro de conexao', true));
}

// ---------- Toast Notification ----------
function showToast(message, isError) {
    // Remove existing toast
    const existing = document.querySelector('.toast');
    if (existing) existing.remove();

    const toast = document.createElement('div');
    toast.className = 'toast' + (isError ? ' error' : '');
    toast.textContent = message;
    document.body.appendChild(toast);

    // Trigger animation
    requestAnimationFrame(() => {
        toast.classList.add('show');
    });

    setTimeout(() => {
        toast.classList.remove('show');
        setTimeout(() => toast.remove(), 300);
    }, 3000);
}

// ---------- Clock ----------
function updateClock() {
    const el = document.getElementById('clock');
    if (el) {
        el.textContent = new Date().toLocaleTimeString('pt-BR');
    }
}

// ---------- Init ----------
document.addEventListener('DOMContentLoaded', () => {
    // Start clock
    updateClock();
    setInterval(updateClock, 1000);

    // Close sidebar on overlay click
    const overlay = document.getElementById('sidebarOverlay');
    if (overlay) {
        overlay.addEventListener('click', closeSidebar);
    }

    // Mark active nav link
    const currentPath = window.location.pathname;
    document.querySelectorAll('.sidebar-nav .nav-link').forEach(link => {
        const href = link.getAttribute('href');
        if (href && currentPath.startsWith(href) && href !== '/') {
            link.classList.add('active');
            // Open parent submenu if inside one
            const parentSubmenu = link.closest('.submenu');
            if (parentSubmenu) {
                parentSubmenu.classList.add('open');
                const parentLink = parentSubmenu.previousElementSibling;
                if (parentLink) parentLink.classList.add('expanded');
            }
        }
    });
});
