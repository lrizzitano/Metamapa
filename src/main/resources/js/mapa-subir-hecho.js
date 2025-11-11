const puntoMapa = document.createElement('div')
puntoMapa.className = 'punto-mapa'

let map;
let marker = null;

document.addEventListener("DOMContentLoaded", () => {
    initMap();

    document.body.addEventListener("htmx:afterSwap", (event) => {
        updateMarkers(event.target);
    });

    //Restaurar el mapa al hacer back/forward con htmx
    document.body.addEventListener("htmx:historyRestore", (_) => {
        initMap();
    });

});


function initMap() {
    if (map) map.remove();


    map = new maplibregl.Map({
        container: 'map',
        style: 'https://api.maptiler.com/maps/019a4b56-3295-7045-8a12-1232fc53992d/style.json?key=SxjUjE2217MQ3ccMA7Ee',
        center: [-58.38, -34.60],
        zoom: 3
    });

    map.on("click", (event) => {
        const { lng, lat } = event.lngLat;

        if (marker) marker.remove();

        marker = new maplibregl.Marker({element: puntoMapa})
            .setLngLat([lng, lat])
            .addTo(map);

        const inputLat = document.getElementById("latitud");
        const inputLng = document.getElementById("longitud");
        if (inputLat && inputLng) {
            inputLat.value = lat;
            inputLng.value = lng;
        }

    })

}