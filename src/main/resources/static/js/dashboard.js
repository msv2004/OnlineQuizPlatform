document.addEventListener('DOMContentLoaded', async () => {
    
    // Check auth
    try {
        const authRes = await fetch('/api/auth/me');
        if (!authRes.ok) {
            window.location.href = '/index.html';
            return;
        }
        const user = await authRes.json();
        document.getElementById('user-display-name').textContent = user.username;
        if(user.role === 'ROLE_ADMIN') {
            const adminLink = document.createElement('a');
            adminLink.href = '/admin.html';
            adminLink.innerText = 'Admin Panel';
            document.querySelector('.nav-links').prepend(adminLink);
        }
    } catch (e) {
        window.location.href = '/index.html';
    }

    // Load Categories
    const container = document.getElementById('category-container');
    
    try {
        const res = await fetch('/api/public/categories'); // Open endpoint for reading in our simple setup
        if (res.ok) {
            const categories = await res.json();
            if (categories.length === 0) {
                container.innerHTML = '<p>No quiz categories available right now.</p>';
            } else {
                categories.forEach(cat => {
                    const card = document.createElement('div');
                    card.className = 'category-card';
                    card.innerHTML = `
                        <h3>${cat.name}</h3>
                        <p>${cat.description || 'Test your knowledge!'}</p>
                        <button class="btn btn-primary start-quiz-btn" data-id="${cat.id}">Start Quiz</button>
                    `;
                    container.appendChild(card);
                });

                document.querySelectorAll('.start-quiz-btn').forEach(btn => {
                    btn.addEventListener('click', (e) => {
                        const id = e.target.getAttribute('data-id');
                        window.location.href = `/quiz.html?category=${id}`;
                    });
                });
            }
        }
    } catch (err) {
        container.innerHTML = '<p class="error-msg">Failed to load categories.</p>';
    }
});
