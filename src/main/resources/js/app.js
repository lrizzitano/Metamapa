function openModal(id) {
  document.getElementById(id).classList.add('active');
}
function closeModal(id) {
  document.getElementById(id).classList.remove('active')
}

function closeDetalleHecho() {
  closeModal("detalleHecho");
  const url = new URL(window.location.href);
  if (!url.searchParams.get("hecho")) {
    return;
  }
  url.searchParams.delete("hecho");
  history.replaceState({}, "", url.toString());
}
function compartirHecho() {
  const titulo = document.querySelector("#detalleHecho .titulo").textContent.trim();

  const url = new URL(window.location.href);
  url.search = new URLSearchParams().toString();
  url.searchParams.set("hecho", titulo);

  console.log(url);
  if (navigator.share) {
    navigator.share({
      title: titulo,
      url: url.toString()
    })
      .catch(_ => alert("Error al compartir el link"));
    return;
  }

  // Fallback: copy to clipboard
  navigator.clipboard.writeText(url.toString())
    .then(() => alert("Link copiado al portapapeles"))
    .catch(() => alert("Error al copiar el link"));
}


function crearModalDetalleHecho(boton, replace = false) {
  openModal('detalleHecho');
  const modal = document.getElementById('detalleHecho');
  const hecho = JSON.parse(boton.dataset.hecho);
  const usuario = (boton.dataset.usuario).toString();

  const slideVideo = document.querySelector('.slide-video');
  // probe ocultando el div de controles pero parece que el display:flex tiene prioridad sobre el hidden
  const controles = document.querySelectorAll('.controles a');
  const iframe = document.querySelector('.multimedia-video');

  if (hecho.video) { // la unica que requiere if pq es opcional
    iframe.src = hecho.video;
    slideVideo.hidden = false;
    controles.forEach(control => control.hidden = false);
  } else {
    slideVideo.hidden = true; // lo escondo por si habia uno puesto de antes
    controles.forEach(control => control.hidden = true);
  }

  modal.querySelector('.titulo').textContent = hecho.titulo;
  modal.querySelector('.descripcion').textContent = hecho.descripcion;
  modal.querySelector('.multimedia-imagen').src = hecho.imagen;
  modal.querySelector('.fechaAcontecimiento').textContent = hecho.fechaAcontecimiento;
  modal.querySelector('.fechaCarga').textContent = hecho.fechaCarga;
  modal.querySelector('.categoria').textContent = hecho.categoria;
  modal.querySelector('.contribuyente').textContent = hecho.contribuyente;
  modal.querySelector('.provincia').textContent = hecho.provincia;
  modal.querySelector('.latitud').textContent = hecho.latitud;
  modal.querySelector('.longitud').textContent = hecho.longitud;

  modal.querySelector('.boton-solicitar-eliminacion')
    .href = "/solicitudesDeEliminacion/nueva?hecho=" + encodeURIComponent(hecho.titulo);

  const contenedorBoton = modal.querySelector('.boton-cambio');
  const pipeCambio = modal.querySelector('.pipe-cambio')
  if(usuario === hecho.contribuyente){
    console.log(usuario + " SON IGUALES " + hecho.contribuyente)
    contenedorBoton.innerHTML='';
    pipeCambio.innerHTML='';
    pipeCambio.innerHTML='|';

    const anchorNuevo = document.createElement('a')
    anchorNuevo.textContent ='Modificar'
    anchorNuevo.className= 'boton-solicitar-eliminacion'
    anchorNuevo.href= '/solicitudesDeCambio/nueva?hecho=' + encodeURIComponent(hecho.titulo);

    contenedorBoton.appendChild(anchorNuevo);
  }else{
    contenedorBoton.innerHTML=' ';
    pipeCambio.innerHTML=' ';
  }


  const url = new URL(window.location.href);
  url.searchParams.set("hecho", hecho.titulo);
  if (replace)
    history.replaceState({}, "", url.toString());
  else
    history.pushState({}, "", url.toString())
}

function actualizarRutaFormFiltros(id) {
  const form = document.querySelector('.form-filtros');
  form.setAttribute('hx-get', `/colecciones/${id}/hechos`);
  document.getElementById('btn-filtro').disabled = false;

  for (const boton of document.getElementsByClassName('boton')){
    boton.classList.remove('activo');
  }
  document.getElementById(`boton-${id}`).classList.add('activo');
}

function actualizarBotonColeccion(nombre) {
  const btn = document.getElementById("spanColeccion");
  btn.textContent = nombre;
}

function eliminarOverlayMapa() {
  const overlay = document.querySelector('#overlay-mapa');
  overlay.style.display = "none";
}

document.addEventListener("DOMContentLoaded", function() {

  const rutaActual = window.location.pathname;

  const links = document.querySelectorAll('.links a');

  links.forEach(link => {

    if ((link.getAttribute('href') === rutaActual) ) {
      const elemento = link.querySelector('.link');
      if (elemento) {
        elemento.classList.add('activada');
      }
    }

    if (estoyEditandoColeccion()){
      const element =link.querySelector('.verColecciones ');
      const verColecciones = document.getElementById('verColecciones')
      if(element && verColecciones){
        element.classList.add('activada')
      }

    }

  });
});

function estoyEditandoColeccion(){
  const estoyModificando = document.getElementById('modificandoColeccion');
  if(
      estoyModificando
  ){
    return true;
  }else{
    return false;
  }

}

document.addEventListener('DOMContentLoaded', () => {
  const fechaDesde = document.getElementById('fecha-desde');
  const fechaHasta = document.getElementById('fecha-hasta');

  if(fechaDesde && fechaHasta){
    fechaDesde.addEventListener('change', function() {
      const valorDesde = this.value;

      if (valorDesde) {

        fechaHasta.min = valorDesde;
        console.log("desde");

        if (fechaHasta.value && fechaHasta.value < valorDesde) {
          fechaHasta.value = "";
        }
      }
    });

    fechaHasta.addEventListener('change', function() {
      const valorHasta = this.value;
      if (valorHasta) {
        console.log("hasta");
        fechaDesde.max = valorHasta;
      }
    });

  }



});


