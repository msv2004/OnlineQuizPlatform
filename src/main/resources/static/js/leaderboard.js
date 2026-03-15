document.addEventListener('DOMContentLoaded', async () => {
    const tbody = document.getElementById('leaderboard-body');
    
    try {
        const res = await fetch('/api/leaderboard');
        if (res.ok) {
            const data = await res.json();
            
            if (data.length === 0) {
                tbody.innerHTML = '<tr><td colspan="5">No quiz results found yet.</td></tr>';
            } else {
                tbody.innerHTML = '';
                data.forEach((row, index) => {
                    const tr = document.createElement('tr');
                    tr.innerHTML = `
                        <td>#${index + 1}</td>
                        <td class="user-highlight">${row.username}</td>
                        <td>${row.categoryName}</td>
                        <td>${row.score}</td>
                        <td>${row.timeTakenSeconds}s</td>
                    `;
                    tbody.appendChild(tr);
                });
            }
        } else {
            tbody.innerHTML = '<tr><td colspan="5">Failed to fetch leaderboard.</td></tr>';
        }
    } catch (e) {
        tbody.innerHTML = '<tr><td colspan="5">Network error.</td></tr>';
    }
});
