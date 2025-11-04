document.addEventListener("DOMContentLoaded", function() {
    const map = new maplibregl.Map({
        container: 'map',
        style: 'https://api.maptiler.com/maps/019a4b56-3295-7045-8a12-1232fc53992d/style.json?key=SxjUjE2217MQ3ccMA7Ee',
        center: [-58.38, -34.60],
        zoom: 3
    });

    // TODO: CAMBIAR ESTO, HIPER RUDIMENTARIO, ES PARA PROBAR LOS MARKERS
    // esta hiperacoplado a todo, la estructura del html, el swap de htmx, falta una vuelta de rosca aca fuerte

    markers = [];

    document.body.addEventListener('htmx:afterSwap', (event) => {
        if (event.target.classList.contains('contenedor-hechos')) {
            markers.forEach(marker => marker.remove());
            event.target.querySelectorAll('.hecho').forEach(btn => {
                if (!btn.dataset.markerCreated) {
                    const h1 = btn.querySelector('h1');
                    const pTags = btn.querySelectorAll('p');
                    const latLong = pTags[1].textContent.split(" : ");
                    const lon = parseFloat(latLong[0])
                    const lat = parseFloat(latLong[1])
                    marker = new maplibregl.Marker()
                        .setLngLat([lon, lat])
                        .setPopup(new maplibregl.Popup().setText(h1.textContent))
                        .addTo(map);
                    markers.push(marker);

                    btn.dataset.markerCreated = "true";
                }
            });
        }
    });
});

