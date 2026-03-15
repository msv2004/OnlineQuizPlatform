document.addEventListener('DOMContentLoaded', async () => {
    
    // Auth Check (Must be ADMIN)
    try {
        const authRes = await fetch('/api/auth/me');
        if (!authRes.ok) throw new Error();
        const user = await authRes.json();
        if (user.role !== 'ROLE_ADMIN') {
            window.location.href = '/dashboard.html';
        }
    } catch {
        window.location.href = '/index.html';
    }

    const catForm = document.getElementById('add-category-form');
    const qForm = document.getElementById('add-question-form');
    const catSelect = document.getElementById('q-category');
    
    loadCategories();

    async function loadCategories() {
        try {
            const res = await fetch('/api/admin/categories');
            if (res.ok) {
                const cats = await res.json();
                catSelect.innerHTML = cats.map(c => `<option value="${c.id}">${c.name}</option>`).join('');
            }
        } catch (e) { console.error('Failed to load categories'); }
    }

    catForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const name = document.getElementById('cat-name').value;
        const desc = document.getElementById('cat-desc').value;
        const msg = document.getElementById('cat-msg');
        
        const res = await fetch('/api/admin/categories', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ name, description: desc })
        });
        
        if (res.ok) {
            msg.textContent = 'Category created!';
            msg.className = 'success-msg';
            catForm.reset();
            loadCategories(); // Refresh dropdown
        } else {
            const err = await res.json();
            msg.textContent = err.error || 'Failed to create category';
            msg.className = 'error-msg';
        }
    });

    qForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const catId = catSelect.value;
        const msg = document.getElementById('q-msg');
        
        const payload = {
            text: document.getElementById('q-text').value,
            type: document.getElementById('q-type').value,
            difficulty: document.getElementById('q-diff').value,
            options: document.getElementById('q-opts').value,
            correctAnswer: document.getElementById('q-correct').value
        };

        const res = await fetch(`/api/admin/categories/${catId}/questions`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });

        if (res.ok) {
            msg.textContent = 'Question added!';
            msg.className = 'success-msg';
            qForm.reset();
        } else {
            msg.textContent = 'Failed to add question';
            msg.className = 'error-msg';
        }
    });
});
