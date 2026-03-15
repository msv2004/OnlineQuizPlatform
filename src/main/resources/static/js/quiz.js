document.addEventListener('DOMContentLoaded', async () => {
    const urlParams = new URLSearchParams(window.location.search);
    const categoryId = urlParams.get('category');
    
    if (!categoryId) {
        window.location.href = '/dashboard.html';
        return;
    }

    let timeLeft = 300; // 5 minutes standard time
    const timeDisplay = document.getElementById('time-left');
    let timerId;
    let timeTaken = 0;
    
    const questionsContainer = document.getElementById('questions-container');
    const titleEl = document.getElementById('quiz-title');
    const form = document.getElementById('quiz-form');
    
    // Fetch User Auth
    try {
        const authRes = await fetch('/api/auth/me');
        if (!authRes.ok) throw new Error("Not logged in");
    } catch {
        window.location.href = '/index.html';
        return;
    }

    // Load Questions
    try {
        const res = await fetch(`/api/quiz/${categoryId}?count=10`);
        if (res.ok) {
            const questions = await res.json();
            titleEl.textContent = 'Quiz Session';
            
            if (questions.length === 0) {
                questionsContainer.innerHTML = '<p>No questions found for this category.</p>';
            } else {
                renderQuestions(questions);
                document.getElementById('action-buttons').style.display = 'block';
                startTimer();
            }
        }
    } catch (err) {
        titleEl.textContent = 'Error loading quiz';
    }

    function renderQuestions(questions) {
        questionsContainer.innerHTML = '';
        questions.forEach((q, index) => {
            const qDiv = document.createElement('div');
            qDiv.className = 'question-block';
            let optionsHtml = '';
            
            if (q.type === 'TRUE_FALSE') {
                optionsHtml = `
                    <label><input type="radio" name="q_${q.id}" value="True" required> True</label><br>
                    <label><input type="radio" name="q_${q.id}" value="False" required> False</label>
                `;
            } else if (q.type === 'FILL_BLANK') {
                optionsHtml = `<input type="text" name="q_${q.id}" class="form-control" required placeholder="Type your answer...">`;
            } else {
                // Parse options if JSON string or comma separated for MCQ
                const opts = q.options ? q.options.split(',') : [];
                opts.forEach(opt => {
                    const cleanOpt = opt.trim();
                    optionsHtml += `<label><input type="radio" name="q_${q.id}" value="${cleanOpt}" required> ${cleanOpt}</label><br>`;
                });
            }

            qDiv.innerHTML = `
                <p><strong>${index + 1}. ${q.text}</strong> <span class="difficulty-badge ${q.difficulty.toLowerCase()}">${q.difficulty}</span></p>
                <div class="options-container">
                    ${optionsHtml}
                </div>
            `;
            questionsContainer.appendChild(qDiv);
        });
    }

    function startTimer() {
        timerId = setInterval(() => {
            timeLeft--;
            timeTaken++;
            
            const min = String(Math.floor(timeLeft / 60)).padStart(2, '0');
            const sec = String(timeLeft % 60).padStart(2, '0');
            timeDisplay.textContent = `${min}:${sec}`;
            
            if (timeLeft <= 0) {
                clearInterval(timerId);
                submitQuiz(); // Auto submit
            }
        }, 1000);
    }

    form.addEventListener('submit', (e) => {
        e.preventDefault();
        clearInterval(timerId);
        submitQuiz();
    });

    async function submitQuiz() {
        const formData = new FormData(form);
        const answers = {};
        
        for (let [key, value] of formData.entries()) {
            if (key.startsWith('q_')) {
                const qId = key.replace('q_', '');
                answers[qId] = value;
            }
        }

        const payload = {
            answers: answers,
            timeTakenSeconds: timeTaken
        };

        try {
            const res = await fetch(`/api/quiz/submit/${categoryId}`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(payload)
            });

            if (res.ok) {
                const result = await res.json();
                showResults(result);
            } else {
                alert('Error submitting quiz!');
            }
        } catch (e) {
            console.error(e);
        }
    }

    function showResults(data) {
        document.getElementById('result-overlay').style.display = 'flex';
        document.getElementById('final-score').textContent = data.score;
        document.getElementById('max-score').textContent = data.totalQuestions;
        document.getElementById('final-time').textContent = data.timeTakenSeconds;
    }
});
