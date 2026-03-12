// InsureCompare MVP - Client-side sorting and interactivity

function haversineDistance(lat1, lon1, lat2, lon2) {
    var R = 6371;
    var dLat = (lat2 - lat1) * Math.PI / 180;
    var dLon = (lon2 - lon1) * Math.PI / 180;
    var a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
        + Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180)
        * Math.sin(dLon / 2) * Math.sin(dLon / 2);
    var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    return R * c;
}

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
            case 'distance':
                var mapDiv = document.getElementById('provider-map');
                if (!mapDiv) return 0;
                var userLat = parseFloat(mapDiv.dataset.userLat);
                var userLng = parseFloat(mapDiv.dataset.userLng);
                if (isNaN(userLat) || isNaN(userLng)) return 0;
                var distA = haversineDistance(userLat, userLng, parseFloat(a.dataset.lat), parseFloat(a.dataset.lng));
                var distB = haversineDistance(userLat, userLng, parseFloat(b.dataset.lat), parseFloat(b.dataset.lng));
                return distA - distB;
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
