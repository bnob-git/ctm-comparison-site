// InsureCompare - Leaflet Map Integration

document.addEventListener('DOMContentLoaded', function() {
    var resultsMap = document.getElementById('provider-map');

    if (resultsMap && resultsMap.classList.contains('map-container-results')) {
        initResultsMap(resultsMap);
    } else if (resultsMap && resultsMap.classList.contains('map-container-detail')) {
        initDetailMap(resultsMap);
    }
});

function initResultsMap(mapDiv) {
    var userLat = parseFloat(mapDiv.dataset.userLat);
    var userLng = parseFloat(mapDiv.dataset.userLng);

    var map = L.map('provider-map').setView([userLat || 53.0, userLng || -1.5], 6);

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors',
        maxZoom: 18
    }).addTo(map);

    var bounds = [];

    // User marker (red)
    if (!isNaN(userLat) && !isNaN(userLng)) {
        var userIcon = L.icon({
            iconUrl: 'https://raw.githubusercontent.com/pointhi/leaflet-color-markers/master/img/marker-icon-red.png',
            shadowUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.9.4/images/marker-shadow.png',
            iconSize: [25, 41],
            iconAnchor: [12, 41],
            popupAnchor: [1, -34],
            shadowSize: [41, 41]
        });

        L.marker([userLat, userLng], {icon: userIcon})
            .addTo(map)
            .bindPopup('<strong>Your Location</strong>')
            .openPopup();

        bounds.push([userLat, userLng]);
    }

    // Provider markers (blue - default)
    var cards = document.querySelectorAll('.result-card');
    cards.forEach(function(card) {
        var lat = parseFloat(card.dataset.lat);
        var lng = parseFloat(card.dataset.lng);
        var name = card.dataset.providerName;

        if (!isNaN(lat) && !isNaN(lng)) {
            L.marker([lat, lng])
                .addTo(map)
                .bindPopup('<strong>' + name + '</strong>');
            bounds.push([lat, lng]);
        }
    });

    if (bounds.length > 1) {
        map.fitBounds(bounds, {padding: [30, 30]});
    } else if (bounds.length === 1) {
        map.setView(bounds[0], 10);
    }
}

function initDetailMap(mapDiv) {
    var lat = parseFloat(mapDiv.dataset.lat);
    var lng = parseFloat(mapDiv.dataset.lng);
    var name = mapDiv.dataset.name;

    if (isNaN(lat) || isNaN(lng)) return;

    var map = L.map('provider-map').setView([lat, lng], 12);

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors',
        maxZoom: 18
    }).addTo(map);

    L.marker([lat, lng])
        .addTo(map)
        .bindPopup('<strong>' + name + '</strong>')
        .openPopup();
}
