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

    let latitud = document.getElementById("latitud");
    let longitud = document.getElementById("longitud");

    if(parseFloat(latitud.value)  && parseFloat(longitud.value) ){
        console.log("HAMBURGUESA" + parseFloat(latitud.value) + parseFloat(longitud.value))
        updateMarker(parseFloat(latitud.value),parseFloat(longitud.value));
    }else{
        console.log("localizando");
        if (navigator.geolocation) {
            console.log("obteniendo localizacion")
            navigator.geolocation.getCurrentPosition((pos) => {
                    const lat = pos.coords.latitude;
                    const lng = pos.coords.longitude;
                    map.setCenter([lng, lat]);
                    map.setZoom(15);
                    console.log( "longitud actual:" + lng)
                    updateMarker(lat, lng);
                }, (_) => {},
                { enableHighAccuracy: true }
            );
        } else {
        }
    }

  map.on("click", ({lngLat}) => updateMarker(lngLat.lat, lngLat.lng));

}

function updateMarker(lat, lng) {
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
}