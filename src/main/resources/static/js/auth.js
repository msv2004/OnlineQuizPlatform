document.addEventListener('DOMContentLoaded', () => {
    
    // Auto-redirect if already logged in
    fetch('/api/auth/me')
        .then(res => {
            if (res.ok) {
                res.json().then(data => redirectBasedOnRole(data.role));
            }
        })
        .catch(err => console.log('Not logged in'));

    const loginForm = document.getElementById('login-form');
    const registerForm = document.getElementById('register-form');
    
    if (loginForm) {
        loginForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            const username = document.getElementById('login-username').value;
            const password = document.getElementById('login-password').value;
            const errorEl = document.getElementById('login-error');
            errorEl.textContent = '';
            
            try {
                const res = await fetch('/api/auth/login', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ username, password })
                });
                
                if (res.ok) {
                    const data = await res.json();
                    redirectBasedOnRole(data.role);
                } else {
                    const error = await res.json();
                    errorEl.textContent = error.error || 'Login failed';
                }
            } catch (err) {
                errorEl.textContent = 'Network error occurred';
            }
        });
    }

    if (registerForm) {
        registerForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            const username = document.getElementById('reg-username').value;
            const email = document.getElementById('reg-email').value;
            const password = document.getElementById('reg-password').value;
            
            const errorEl = document.getElementById('reg-error');
            const successEl = document.getElementById('reg-success');
            errorEl.textContent = '';
            successEl.textContent = '';
            
            try {
                const res = await fetch('/api/auth/register', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ username, email, password })
                });
                
                if (res.ok) {
                    successEl.textContent = 'Registration successful! Please login.';
                    registerForm.reset();
                } else {
                    const error = await res.json();
                    errorEl.textContent = error.error || 'Registration failed';
                }
            } catch (err) {
                errorEl.textContent = 'Network error occurred';
            }
        });
    }
    
    function redirectBasedOnRole(role) {
        if (role === 'ROLE_ADMIN') {
            window.location.href = '/admin.html';
        } else {
            window.location.href = '/dashboard.html';
        }
    }
});
