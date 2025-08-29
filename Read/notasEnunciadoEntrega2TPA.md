# Fuentes dinámicas
Guarda los hechos de manera local en una BD (Patrón repository para esta entrega)

## Contribuyentes
- Subir hechos de forma **anónima** (no puede modificar)
- Subir hechos de forma **registrada** (puede modificar por 1 semana)

## Administradores
- Aceptar los hechos
- Aceptar los hechos con sugerencia de cambios
- Rechazar los hechos

# Fuentes Proxy
Se conectan a un servicio externo y nos exponene sus hechos a través de nuestra API.

Actualiza la información **cada 1 hora**.

## Fuente DEMO
Se conceta con un sistema externo ficticio en forma de API o biblioteca.

Interfaz `Conexión` para abstraer la conecxión con el sistema externo.

## Fuente MetaMapa
Se conceta con otra instancia de MetaMapa mediante una API REST
(Es la misma que usamos para nuestras fuentes?)

# Colecciones
Se mantiene la implementación pero se abstrae la fuente polimórficamente.

Deberá poder exponerse a través de la API REST de conexión de fuentes PROXY

# Detector de Spam
Componente que detecta y rechaza automáticamente los hechos spam

---
---
# Reunión 26/5

## Preguntas
  - Timer de fuente Demo con hilo con sleep cada 1 hora vs biblioteca de scheduler
  - Contenidos multimedia guardados en la DB o simplemente un PATH


## Fuente Dinámica

- 1 sola fuente dinámica que guarda hechos y solicitudes de cambio en un singleton propio
- Los administradores pueden eliminar por sí mismos hechos subidos a una fuente dinámica
- Al aprobarse una solicitud de modificación un hecho se elimina el hecho original y se sube uno nuevo con la información actualizada
  - Una solicitud de cambio tiene el hecho a reemplazar y el hecho nuevo
- El agregador solo guarda solicitudes de eliminación
- El hecho tiene como atributo un Usuario
  
## Fuente Proxy

- Cada tipo de fuente proxy existe como una clase en nuestro dominio que implementa la interfaz `Fuente` 
  - La Fuente MetaMapa le pega a la API cada vez que se la llama para pedir hechos
    - Biblioteca para gestionar las url
  - La fuente Demo actualiza cada 1 hora la información guardada en memoria


## Colecciones
- Identificar cada coleccion con un atributo Handle (UUID)

## Detector de Spam
- Filtrar spam suponiendo la interfaz implementada

# Reunion 2/6

## Fuente Dinámica

- Los hechos se suben automaticamente sin necesidad de ser aprobados por un admin
- **TODO:** Distinguir los hechos ya revisados de los pendientes de revision (flag en hecho o listas diferentes)
  - Subtipo HechoDinamico? (Depende de cuantos campos extra, además del flag de revision, tenga un hecho solo por ser de fuente dinamica)
- **TODO:** La eliminacion un hecho de fuente dinamica deberia eliminarse de la DB de la fuente (cada hecho conoce su fuente y permite llamar a un metodo de eliminar?)
- **TODO:** Se separa en la fuente que implementa la interfaz `Fuente` y se comunica con el repository que persiste los hechos y las solicitudes de cambio

## Fuente Proxy Demo

- Clase abstracta para las fuentes proxy calendarizadas
  - **TODO:** Uso de eventos para calendarizar las fuentes asincronicas 

## Fuente proxy metamapa

- Para pasar un filtro por la url se componen uniendo con "&" y se meten como query param
- Para hace el post de solicitud se le pasa la instancia de solicitud y se pasa como DTO
  - **TODO:** Para eso hay que sacar el llamado al metodo `nuevaSolicitud()` del constructor de las solicitudes
- **TODO:** Usar biblioteca para los request http


## Filtros

- Muere el enum de filtros, ahora cada filtro es una instancia de la clase correspondiente con los datos especificos

## Solicitudes

- **TODO:** Revisar inyeccion de detector de spam a Solicitudes