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
            event.target.querySelectorAll('.card-hecho').forEach(btn => {
                if (!btn.dataset.markerCreated) {
                    const titulo = btn.querySelector('#titulo');
                    const lon = parseFloat(btn.querySelector('#longitud').textContent);
                    const lat = parseFloat(btn.querySelector('#latitud').textContent);
                    marker = new maplibregl.Marker()
                        .setLngLat([lon, lat])
                        .setPopup(new maplibregl.Popup().setText(titulo.textContent))
                        .addTo(map);
                    markers.push(marker);

                    btn.dataset.markerCreated = "true";
                }
            });
        }
    });
});

