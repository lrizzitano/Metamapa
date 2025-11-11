let map;
let markers = [];

document.addEventListener("DOMContentLoaded", () => {

  initMap();
  const hechosIniciales = document.querySelector(".contenedor-hechos");
  parseHechos(hechosIniciales);
  updateMarkers(hechosIniciales);

  document.body.addEventListener("htmx:afterSwap", (event) => {
    parseHechos(event.target);
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

  const hechosContainer = document.querySelector(".contenedor-hechos");
  parseHechos(hechosContainer);
  updateMarkers(hechosContainer);
}

function parseHechos(container) {
  container.querySelectorAll(".card-hecho").forEach(btn => {
    if (!btn._hechoParsed) {
      btn.hecho = JSON.parse(btn.dataset.hecho);
      btn._hechoParsed = true;
    }
  });
}

function updateMarkers(container) {
  markers.forEach(m => m.remove());
  markers = [];

  container.querySelectorAll(".card-hecho").forEach(btn => {
    const hecho = btn.hecho;
    const { latitud, longitud, titulo } = hecho;

    const puntoMapa = document.createElement('div')
    puntoMapa.className = 'punto-mapa'

    const popup = new maplibregl.Popup({
      offset: 10,
      closeButton: false,
      closeOnClick: false
    }).setText(titulo);

    const marker = new maplibregl.Marker({element: puntoMapa})
      .setLngLat([longitud, latitud])
      .addTo(map);

    const element = marker.getElement();
    element.style.cursor = "pointer";
    element.style.pointerEvents = "auto";

    element.addEventListener("mouseenter", () => popup.addTo(map).setLngLat([longitud, latitud]));
    element.addEventListener("mouseleave", () => popup.remove());

    element.addEventListener("click", () => {
      crearModalDetalleHecho({ dataset: { hecho: JSON.stringify(hecho) } });
    });

    markers.push(marker);
  });
}