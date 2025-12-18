let map;
let markers = [];

document.addEventListener("DOMContentLoaded", () => {

  initMap();
  const hechosIniciales = document.querySelector(".contenedor-hechos");

  if(hechosIniciales){
    parseHechos(hechosIniciales);
    updateMarkers(hechosIniciales);

  }

  document.body.addEventListener("htmx:afterSwap", (event) => {
    parseHechos(event.target);
    updateMarkers(event.target);
  });

  //Restaurar el mapa al hacer back/forward con htmx
  document.body.addEventListener("htmx:historyRestore", (_) => {
    initMap();
  });

  window.addEventListener("DOMContentLoaded", checkUrlForHecho);
  window.addEventListener("popstate", checkUrlForHecho);
});




function zoomHecho(){

  const stringLatitud =document.getElementById("detalleHecho")
      .querySelector(".latitud");
  const stringLongitud =document.getElementById("detalleHecho")
      .querySelector(".longitud");

  const latitud = parseFloat(stringLatitud.textContent.trim());
  const longitud = parseFloat(stringLongitud.textContent.trim());

  map.flyTo({
    center: [longitud, latitud],
    zoom: 13,
    speed: 1.3,
    curve: 1.5,
    essential: true
  });
}

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

function crearPopupHTML(titulo, imagen) {
  // como son todos los popups iguales decidi meterlo aca (+simple)
  // si a futuro queremos que sean diferentes/es mas complejo, podriamos sacarlo a otro archivo
  return `
    <div style="display:flex;align-items:center;gap:5px;flex-direction:column;padding: 0;
    border-radius:2.5rem;justify-content: flex-start;width: 100%;height: 100%;">
    
      <p style="margin: 0;text-align: center;width: 90%;color: var(--color-texto-negrita);
       font-weight: 400;font-size: 0.95rem;border-bottom: solid 0.05rem var(--color-principal);
       font-family: "Segoe UI", Arial, sans-serif";>${titulo}</p>
       
      ${imagen ? `<img src="${imagen}" style="width:60%;height:auto;border-radius:4px;">` : ''}
    </div>
  `;
}

function updateMarkers(container) {
  markers.forEach(m => m.remove());
  markers = [];

  container.querySelectorAll(".card-hecho").forEach(btn => {
    const hecho = btn.hecho;
    const usuario = btn.dataset.usuario;

    const { latitud, longitud, titulo, imagen } = hecho;
    const puntoMapa = document.createElement('div')
    puntoMapa.className = 'punto-mapa'

    const popup = new maplibregl.Popup({
      offset: 10,
      closeButton: false,
      closeOnClick: false
    }).setHTML(crearPopupHTML(titulo, imagen));

    const marker = new maplibregl.Marker({element: puntoMapa})
      .setLngLat([longitud, latitud])
      .addTo(map);

    const element = marker.getElement();
    element.style.cursor = "pointer";
    element.style.pointerEvents = "auto";

    element.addEventListener("mouseenter", () => popup.addTo(map).setLngLat([longitud, latitud]));
    element.addEventListener("mouseleave", () => popup.remove());

    element.addEventListener("click", () => {
      crearModalDetalleHecho({ dataset: { hecho: JSON.stringify(hecho), usuario: usuario} });
    });

    markers.push(marker);
  });

}

function checkUrlForHecho() {
  const titulo = new URLSearchParams(window.location.search).get("hecho");
  if (!titulo) {
    closeDetalleHecho()
    return;
  }

  const botones = document.querySelectorAll(".card-hecho");

  for (const btn of botones) {
    try {
      const hecho = JSON.parse(btn.dataset.hecho);
      if (hecho.titulo === titulo) {
        crearModalDetalleHecho(btn, true);
        return;
      }
    } catch (_) {}
  }
  closeDetalleHecho();
  const url = new URL(window.location.href);
  url.searchParams.delete("hecho");
  history.replaceState({}, "", url.toString());
}