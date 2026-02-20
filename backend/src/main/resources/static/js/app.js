// InsureCompare MVP - Client-side sorting and interactivity

function sortResults(sortBy) {
    const container = document.getElementById('resultsContainer');
    if (!container) return;

    const cards = Array.from(container.querySelectorAll('.result-card'));
    if (cards.length === 0) return;

    cards.sort(function(a, b) {
        switch (sortBy) {
            case 'price-asc':
                return parseFloat(a.dataset.price) - parseFloat(b.dataset.price);
            case 'price-desc':
                return parseFloat(b.dataset.price) - parseFloat(a.dataset.price);
            case 'rating':
                return parseFloat(b.dataset.rating) - parseFloat(a.dataset.rating);
            case 'score':
            default:
                return parseFloat(b.dataset.score) - parseFloat(a.dataset.score);
        }
    });

    cards.forEach(function(card) {
        container.appendChild(card);
    });
}

// Client-side form validation enhancement
document.addEventListener('DOMContentLoaded', function() {
    var form = document.getElementById('quoteForm');
    if (form) {
        form.addEventListener('submit', function(event) {
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
            }
            form.classList.add('was-validated');
        });
    }
});
